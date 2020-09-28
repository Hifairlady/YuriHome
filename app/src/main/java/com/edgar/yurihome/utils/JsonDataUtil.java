package com.edgar.yurihome.utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonDataUtil<T> {
    private static final String TAG = "================" + JsonDataListUtil.class.getSimpleName();

    private T data = null;
    private Type type;
    private int jsonType = 0;

    public JsonDataUtil(Type type) {
        this.type = type;
        this.jsonType = Config.FETCH_JSON_DATA_TYPE_NORMAL;
    }

    public JsonDataUtil(int jsonType) {
        this.jsonType = jsonType;
    }

    public JsonDataUtil(Type type, int jsonType) {
        this.type = type;
        this.jsonType = jsonType;
    }

    //fetch main list json data
    public void fetchJsonData(final Handler handler, String urlString) {
        HttpUtil.sendRequestWithOkhttp(urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(HttpUtil.REQUEST_JSON_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int resultCode = HttpUtil.PARSE_JSON_DATA_ERROR;
                if (!response.isSuccessful()) {
                    response.close();
                    handler.sendEmptyMessage(resultCode);
                    return;
                }

                try {
                    String jsonString = response.body().string();

                    if (jsonType == Config.FETCH_JSON_DATA_TYPE_COMMENT) {
                        jsonString = jsonString.replace("\"comments\":{", "\"comments\":[");
                        jsonString = jsonString.replace("},\"total\"", "],\"total\"");
                        jsonString = jsonString.replaceAll("\"[0-9]*\":", "");

                    } else if (jsonType == Config.FETCH_JSON_DATA_TYPE_TRANSLATOR) {
                        Message message = Message.obtain();
                        int startPos = jsonString.indexOf("\"translator\":") + 14;
                        int endPos = jsonString.indexOf("\",\"link\":");
                        String translatorName = jsonString.substring(startPos, endPos);
                        translatorName = UnicodeUtil.unicode2String(translatorName);
                        if (translatorName.length() == 0) {
                            translatorName = "NONE";
                        }
                        message.what = HttpUtil.REQUEST_JSON_SUCCESS;
                        message.obj = translatorName;
                        handler.sendMessage(message);
                        return;
                    }
                    Gson gson = new Gson();
                    data = gson.fromJson(jsonString, type);
                    resultCode = HttpUtil.REQUEST_JSON_SUCCESS;
                } catch (IOException | JsonSyntaxException | NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    if (response.body() != null) {
                        response.body().close();
                    }
                }
                handler.sendEmptyMessage(resultCode);

            }
        });
    }

    public T getData() {
        return data;
    }

    public void setType(Type type) {
        this.type = type;
    }

}

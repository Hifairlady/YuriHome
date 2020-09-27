package com.edgar.yurihome.utils;

import android.os.Handler;
import android.os.Message;

import com.edgar.yurihome.beans.JsonResponseItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonDataUtil2<T> {
    private static final String TAG = "================" + JsonDataListUtil.class.getSimpleName();

    private T data = null;
    private Type type;

    public JsonDataUtil2(Type type) {
        this.type = type;
    }

    //fetch main list json data
    public void fetchJsonData(final Handler handler, String urlString) {
        HttpUtil.sendRequestWithOkhttp(urlString, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = HttpUtil.REQUEST_JSON_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = Message.obtain();
                final JsonResponseItem responseItem = new JsonResponseItem();
                parseResponseData(response, responseItem);
                message.what = responseItem.getResponseCode();
                handler.sendMessage(message);
            }
        });
    }

    private void parseResponseData(Response response, JsonResponseItem responseItem) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) {
                responseItem.setResponseCode(result);
                response.close();
                return;
            }
            String jsonString = response.body().string();
            jsonString = jsonString.replace("\"comments\":{", "\"comments\":[");
            jsonString = jsonString.replace("},\"total\"", "],\"total\"");
            jsonString = jsonString.replaceAll("\"[0-9]*\":", "");
            responseItem.setJsonString(jsonString);
            Gson gson = new Gson();
            data = gson.fromJson(jsonString, type);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (response.body() != null) {
                response.body().close();
            }
        }
        responseItem.setResponseCode(result);
    }

    public T getData() {
        return data;
    }

    public void setType(Type type) {
        this.type = type;
    }

}

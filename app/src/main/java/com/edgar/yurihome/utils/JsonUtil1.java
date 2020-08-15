package com.edgar.yurihome.utils;

import android.os.Handler;
import android.os.Message;

import com.edgar.yurihome.beans.JsonResponseItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonUtil1 {


    public static void fetchJsonData(final Handler handler, String urlString, final JsonResponseItem<?> responseItem) {
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
                parseJsonString(response, responseItem);
                message.what = responseItem.getResponseCode();
                handler.sendMessage(message);
            }
        });
    }

    private static void parseJsonString(Response response, final JsonResponseItem<?> responseItem) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) {
                responseItem.setResponseCode(result);
                return;
            }
            String jsonString = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<?>>() {
            }.getType();
            ArrayList<?> items = gson.fromJson(jsonString, type);
            responseItem.setDataItems(items);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        responseItem.setResponseCode(result);
    }


}

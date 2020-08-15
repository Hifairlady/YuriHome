package com.edgar.yurihome.utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonUtil1 {

    //fetch main list json data
    public static void fetchComicsData(final Handler handler, String urlString, final JsonResponseItem responseItem) {
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
                parseComicJsonString(response, responseItem);
                message.what = responseItem.getResponseCode();
                handler.sendMessage(message);
            }
        });
    }

    private static void parseComicJsonString(Response response, JsonResponseItem responseItem) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) {
                responseItem.setResponseCode(result);
                return;
            }
            String jsonString = response.body().string();
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<ComicItem>>() {
//            }.getType();
//            ArrayList<ComicItem> items = gson.fromJson(jsonString, type);
//            comicResponse.setDataItems(items);
            responseItem.setJsonString(jsonString);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        responseItem.setResponseCode(result);
    }

}

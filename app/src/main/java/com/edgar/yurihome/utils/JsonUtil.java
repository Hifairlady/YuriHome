package com.edgar.yurihome.utils;

import android.os.Handler;
import android.os.Message;

import com.edgar.yurihome.beans.ClassifyFilterBean;
import com.edgar.yurihome.beans.ComicItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class JsonUtil {

    private static ArrayList<ClassifyFilterBean> classifyFilterBeans = new ArrayList<>();
    private static ArrayList<ComicItem> comicItems = new ArrayList<>();

    public static void fetchClassifyData(final Handler handler) {
        HttpUtil.sendRequestWithOkhttp(Config.BASE_URL_CLASSIFY, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = HttpUtil.REQUEST_JSON_FAILED;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = Message.obtain();
                message.what = parseClassifyJsonString(response);
                handler.sendMessage(message);
            }
        });
    }

    private static int parseClassifyJsonString(Response response) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) return result;
            String jsonString = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ClassifyFilterBean>>() {
            }.getType();
            ArrayList<ClassifyFilterBean> items = gson.fromJson(jsonString, type);
            classifyFilterBeans = new ArrayList<>(items);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ClassifyFilterBean> getClassifyFilterBeans() {
        return classifyFilterBeans;
    }


    public static void fetchComicsData(final Handler handler, String urlString) {
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
                message.what = parseComicJsonString(response);
                handler.sendMessage(message);
            }
        });
    }

    private static int parseComicJsonString(Response response) {
        int result = HttpUtil.PARSE_JSON_DATA_ERROR;
        try {
            if (response.body() == null) return result;
            String jsonString = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ComicItem>>() {
            }.getType();
            ArrayList<ComicItem> items = gson.fromJson(jsonString, type);
            comicItems = new ArrayList<>(items);
            result = HttpUtil.REQUEST_JSON_SUCCESS;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<ComicItem> getComicItems() {
        return comicItems;
    }
}

package com.edgar.yurihome;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static void sendRequestWithOkhttp(String urlString, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        client.newCall(request).enqueue(callback);
    }

}

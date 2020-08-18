package com.edgar.yurihome.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    public static final int REQUEST_JSON_SUCCESS = 100;
    public static final int REQUEST_JSON_FAILED = 101;
    public static final int PARSE_JSON_DATA_ERROR = 102;
    public static final String MESSAGE_NETWORK_ERROR = "Network Error!";
    public static final String MESSAGE_JSON_ERROR = "Json Data Error!";
    public static final String MESSAGE_UNKNOWN_ERROR = "Unknown Error!";


    public static void sendRequestWithOkhttp(String urlString, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        client.newCall(request).enqueue(callback);
    }

}

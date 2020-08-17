package com.edgar.yurihome.beans;

import com.edgar.yurihome.utils.HttpUtil;

public class JsonResponseItem {
    private int responseCode = HttpUtil.REQUEST_JSON_FAILED;
    private String jsonString;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}

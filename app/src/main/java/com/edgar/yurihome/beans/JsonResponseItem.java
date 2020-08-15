package com.edgar.yurihome.beans;

import com.edgar.yurihome.utils.HttpUtil;

import java.util.ArrayList;

public class JsonResponseItem<T> {
    private int responseCode = HttpUtil.REQUEST_JSON_FAILED;
    private ArrayList<?> dataItems = new ArrayList<T>();

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<?> getDataItems() {
        return dataItems;
    }

    public void setDataItems(ArrayList<?> dataItems) {
        this.dataItems = dataItems;
    }
}

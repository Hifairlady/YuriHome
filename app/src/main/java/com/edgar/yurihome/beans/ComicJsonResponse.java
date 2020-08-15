package com.edgar.yurihome.beans;

import com.edgar.yurihome.utils.HttpUtil;

import java.util.ArrayList;

public class ComicJsonResponse {
    private int responseCode = HttpUtil.REQUEST_JSON_FAILED;
    private ArrayList<ComicItem> dataItems = new ArrayList<>();

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public ArrayList<ComicItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(ArrayList<ComicItem> dataItems) {
        this.dataItems = dataItems;
    }
}

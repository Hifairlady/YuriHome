package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

public class ViewPointBean {

    /**
     * id : 50546020
     * uid : 55384
     * content : WRNG
     * num : 1
     * page : 24
     */

    @SerializedName("id")
    private int viewPointId;
    private int uid;
    private String content;
    private int num;
    private int page;

    public int getViewPointId() {
        return viewPointId;
    }

    public void setViewPointId(int viewPointId) {
        this.viewPointId = viewPointId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

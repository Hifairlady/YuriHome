package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

public class ComicItem {

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_FOOTER = 1;
    /**
     * id : 44026
     * title : Anima Yell!
     * authors : 卯花つかさ
     * status : 连载中
     * cover : https://images.dmzj.com/webpic/3/000AnimaYellhya51.jpg
     * types : ゆり
     * last_updatetime : 1597058849
     * num : 1941273
     */

    private int id;
    private String title;
    private String authors;
    private String status;
    private String cover;
    private String types;
    @SerializedName("last_updatetime")
    private long lastUpdateTime;
    private int num;
    private int itemType = 0;

    public ComicItem(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}

package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

public class SearchResultBean {

    /**
     * _biz : comicacg_comics
     * addtime : 0
     * alias_name : BanG Dream
     * authors : らい公
     * copyright : 0
     * cover : https://images.dmzj.com/webpic/9/bangdream0521.jpg
     * device_show : 7
     * grade : 0
     * hidden : 0
     * hot_hits : 1
     * last_name : 短篇
     * quality : 1
     * status : 0
     * title : BanG Dream！Sister Festival
     * types : ゆり/欢乐向
     * id : 48548
     */

    private String _biz;
    @SerializedName("addtime")
    private int addTime;
    @SerializedName("alias_name")
    private String aliasName;
    private String authors;
    private int copyright;
    private String cover;
    @SerializedName("device_show")
    private int deviceShow;
    private int grade;
    private int hidden;
    @SerializedName("hot_hits")
    private int hotHits;
    @SerializedName("last_name")
    private String latestChapterName;
    private int quality;
    private int status;
    private String title;
    private String types;
    private int id;

    public String get_biz() {
        return _biz;
    }

    public void set_biz(String _biz) {
        this._biz = _biz;
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getDeviceShow() {
        return deviceShow;
    }

    public void setDeviceShow(int deviceShow) {
        this.deviceShow = deviceShow;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getHidden() {
        return hidden;
    }

    public void setHidden(int hidden) {
        this.hidden = hidden;
    }

    public int getHotHits() {
        return hotHits;
    }

    public void setHotHits(int hotHits) {
        this.hotHits = hotHits;
    }

    public String getLatestChapterName() {
        return latestChapterName;
    }

    public void setLatestChapterName(String latestChapterName) {
        this.latestChapterName = latestChapterName;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

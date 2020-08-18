package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReaderImagesItem {

    /**
     * chapter_id : 94348
     * comic_id : 19081
     * title : 第67话
     * chapter_order : 810
     * direction : 1
     * page_url : ["http://imgsmall.dmzj.com/j/19081/94348/0.jpg","http://imgsmall.dmzj.com/j/19081/94348/1.jpg","http://imgsmall.dmzj.com/j/19081/94348/2.jpg","http://imgsmall.dmzj.com/j/19081/94348/3.jpg","http://imgsmall.dmzj.com/j/19081/94348/4.jpg","http://imgsmall.dmzj.com/j/19081/94348/5.jpg","http://imgsmall.dmzj.com/j/19081/94348/6.jpg","http://imgsmall.dmzj.com/j/19081/94348/7.jpg","http://imgsmall.dmzj.com/j/19081/94348/8.jpg","http://imgsmall.dmzj.com/j/19081/94348/9.jpg","http://imgsmall.dmzj.com/j/19081/94348/10.jpg","http://imgsmall.dmzj.com/j/19081/94348/11.jpg","http://imgsmall.dmzj.com/j/19081/94348/12.jpg","http://imgsmall.dmzj.com/j/19081/94348/13.jpg"]
     * picnum : 14
     * comment_count : 73
     */

    @SerializedName("chapter_id")
    private int chapterId;
    @SerializedName("comic_id")
    private int comicId;
    private String title;
    @SerializedName("chapter_order")
    private int chapterOrder;
    private int direction;
    private int picnum;
    @SerializedName("comment_count")
    private int commentCount;
    @SerializedName("page_url")
    private List<String> pageUrls;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getComicId() {
        return comicId;
    }

    public void setComicId(int comicId) {
        this.comicId = comicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapterOrder() {
        return chapterOrder;
    }

    public void setChapterOrder(int chapterOrder) {
        this.chapterOrder = chapterOrder;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPicnum() {
        return picnum;
    }

    public void setPicnum(int picnum) {
        this.picnum = picnum;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getPageUrls() {
        return pageUrls;
    }

    public void setPageUrls(List<String> pageUrls) {
        this.pageUrls = pageUrls;
    }
}

package com.edgar.yurihome.beans;

import java.util.Objects;

public class BrowseHistoryBean {

    private String coverUrl, detailsUrl, title;

    public BrowseHistoryBean(String coverUrl, String detailsUrl, String title) {
        this.coverUrl = coverUrl;
        this.detailsUrl = detailsUrl;
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrowseHistoryBean that = (BrowseHistoryBean) o;
        return coverUrl.equals(that.coverUrl) &&
                detailsUrl.equals(that.detailsUrl) &&
                title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coverUrl, detailsUrl, title);
    }
}

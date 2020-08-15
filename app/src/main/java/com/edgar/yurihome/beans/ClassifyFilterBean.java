package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ClassifyFilterBean {

    /**
     * title : 题材
     * items : [{"tag_id":0,"tag_name":"全部"},{"tag_id":4,"tag_name":"冒险"},{"tag_id":5,"tag_name":"欢乐向"},{"tag_id":6,"tag_name":"格斗"},{"tag_id":7,"tag_name":"科幻"},{"tag_id":8,"tag_name":"爱情"},{"tag_id":9,"tag_name":"侦探"},{"tag_id":10,"tag_name":"竞技"},{"tag_id":11,"tag_name":"魔法"},{"tag_id":12,"tag_name":"神鬼"},{"tag_id":13,"tag_name":"校园"},{"tag_id":14,"tag_name":"惊悚"},{"tag_id":16,"tag_name":"其他"},{"tag_id":17,"tag_name":"四格"},{"tag_id":3242,"tag_name":"生活"},{"tag_id":3243,"tag_name":"百合"},{"tag_id":3244,"tag_name":"秀吉"},{"tag_id":3245,"tag_name":"悬疑"},{"tag_id":3246,"tag_name":"纯爱"},{"tag_id":3248,"tag_name":"热血"},{"tag_id":3249,"tag_name":"后宫"},{"tag_id":3250,"tag_name":"历史"},{"tag_id":3251,"tag_name":"战争"},{"tag_id":3252,"tag_name":"萌系"},{"tag_id":3253,"tag_name":"宅系"},{"tag_id":3254,"tag_name":"治愈"},{"tag_id":3255,"tag_name":"励志"},{"tag_id":3324,"tag_name":"武侠"},{"tag_id":3325,"tag_name":"机战"},{"tag_id":3326,"tag_name":"音乐舞蹈"},{"tag_id":3327,"tag_name":"美食"},{"tag_id":3328,"tag_name":"职场"},{"tag_id":3365,"tag_name":"西方魔幻"},{"tag_id":4459,"tag_name":"高清单行"},{"tag_id":4518,"tag_name":"性转换"},{"tag_id":5077,"tag_name":"东方"},{"tag_id":5345,"tag_name":"扶她"},{"tag_id":5806,"tag_name":"魔幻"},{"tag_id":5848,"tag_name":"奇幻"},{"tag_id":6219,"tag_name":"节操"},{"tag_id":6316,"tag_name":"轻小说"},{"tag_id":6437,"tag_name":"颜艺"},{"tag_id":7568,"tag_name":"搞笑"},{"tag_id":7900,"tag_name":"仙侠"},{"tag_id":13627,"tag_name":"舰娘"},{"tag_id":17192,"tag_name":"动画"},{"tag_id":18522,"tag_name":"AA"}]
     */

    private String title;
    @SerializedName("items")
    private List<FilterItem> filterItems;

    public static FilterItem findFilterItemById(ArrayList<FilterItem> filterItems, int tagId) {
        for (FilterItem filterItem : filterItems) {
            if (filterItem.getTagId() == tagId) {
                return filterItem;
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FilterItem> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<FilterItem> filterItems) {
        this.filterItems = filterItems;
    }

    public static class FilterItem {
        /**
         * tag_id : 0
         * tag_name : 全部
         */

        @SerializedName("tag_id")
        private int tagId;
        @SerializedName("tag_name")
        private String tagName;

        public int getTagId() {
            return tagId;
        }

        public void setTagId(int tagId) {
            this.tagId = tagId;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

    }
}

package com.edgar.yurihome.beans;

import java.util.List;

public class AuthorComicsBean {

    /**
     * nickname : なもり
     * description :
     * cover : http://img.178.com/acg1/201204/129153842704/129176578787.jpg
     * data : [{"id":1832,"name":"神灯女仆","cover":"https://images.dmzj.com/webpic/15/sdeng.jpg","status":"已完结"},{"id":7020,"name":"摇曳百合","cover":"https://images.dmzj.com/webpic/13/baihe20191031.jpg","status":"连载中"},{"id":7678,"name":"Reset!","cover":"https://images.dmzj.com/webpic/14/reset20110823.jpg","status":"连载中"},{"id":9049,"name":"大室家 摇曳百合外传","cover":"https://images.dmzj.com/webpic/17/191009dsjyybh.jpg","status":"连载中"},{"id":12461,"name":"缤纷百合","cover":"https://images.dmzj.com/webpic/0/binfenbaihe.jpg","status":"连载中"},{"id":15669,"name":"百合姬封面故事","cover":"https://images.dmzj.com/webpic/7/bhjfmgs4846.jpg","status":"已完结"},{"id":50323,"name":"缤纷百合2","cover":"https://images.dmzj.com/webpic/12/191102bfbh.jpg","status":"连载中"},{"id":50580,"name":"月影特工料理对决","cover":"https://images.dmzj.com/webpic/10/duijue20191010.jpg","status":"已完结"},{"id":55808,"name":"东西南北！","cover":"https://images.dmzj.com/webpic/10/200730dxnb.jpg","status":"连载中"}]
     */

    private String nickname;
    private String description;
    private String cover;
    private List<AuthorComicData> data;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<AuthorComicData> getData() {
        return data;
    }

    public void setData(List<AuthorComicData> data) {
        this.data = data;
    }

    public static class AuthorComicData {
        /**
         * id : 1832
         * name : 神灯女仆
         * cover : https://images.dmzj.com/webpic/15/sdeng.jpg
         * status : 已完结
         */

        private int id;
        private String name;
        private String cover;
        private String status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

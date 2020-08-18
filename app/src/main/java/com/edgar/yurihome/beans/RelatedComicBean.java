package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RelatedComicBean {

//    {
//        "author_comics": [
//        {
//            "author_name": "なもり",
//                "author_id": 1170,
//                "data": [
//            {
//                "id": 1832,
//                    "name": "神灯女仆",
//                    "cover": "https://images.dmzj.com/webpic/15/sdeng.jpg",
//                    "status": "已完结"
//            },
//            {
//                "id": 7020,
//                    "name": "摇曳百合",
//                    "cover": "https://images.dmzj.com/webpic/13/baihe20191031.jpg",
//                    "status": "连载中"
//            },
//            {
//                "id": 7678,
//                    "name": "Reset!",
//                    "cover": "https://images.dmzj.com/webpic/14/reset20110823.jpg",
//                    "status": "连载中"
//            }
//            ]
//        }
//    ],
//        "theme_comics": [
//        {
//            "id": 198,
//                "name": "女生爱女生",
//                "cover": "https://images.dmzj.com/webpic/18/glove.jpg",
//                "status": "已完结"
//        },
//        {
//            "id": 424,
//                "name": "惊爆草莓",
//                "cover": "https://images.dmzj.com/webpic/4/jingbaocaomei20180810.jpg",
//                "status": "连载中"
//        },
//        {
//            "id": 425,
//                "name": "西蒙",
//                "cover": "https://images.dmzj.com/webpic/5/simoun.jpg",
//                "status": "已完结"
//        }
//    ],
//        "novels": []
//    }

    @SerializedName("author_comics")
    private List<AuthorComicsBean> authorComics;
    @SerializedName("theme_comics")
    private List<ThemeComicsBean> themeComics;

    public List<AuthorComicsBean> getAuthorComics() {
        return authorComics;
    }

    public void setAuthorComics(List<AuthorComicsBean> authorComics) {
        this.authorComics = authorComics;
    }

    public List<ThemeComicsBean> getThemeComics() {
        return themeComics;
    }

    public void setThemeComics(List<ThemeComicsBean> themeComics) {
        this.themeComics = themeComics;
    }


    public static class AuthorComicsBean {
        /**
         * author_name : なもり
         * author_id : 1170
         * data : [{"id":1832,"name":"神灯女仆","cover":"https://images.dmzj.com/webpic/15/sdeng.jpg","status":"已完结"},{"id":7020,"name":"摇曳百合","cover":"https://images.dmzj.com/webpic/13/baihe20191031.jpg","status":"连载中"},{"id":7678,"name":"Reset!","cover":"https://images.dmzj.com/webpic/14/reset20110823.jpg","status":"连载中"}]
         */

        @SerializedName("author_name")
        private String authorName;
        @SerializedName("author_id")
        private int authorId;
        private List<AuthorComicDataBean> data;

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
        }

        public List<AuthorComicDataBean> getData() {
            return data;
        }

        public void setData(List<AuthorComicDataBean> data) {
            this.data = data;
        }

        public static class AuthorComicDataBean {
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

    public static class ThemeComicsBean {
        /**
         * id : 198
         * name : 女生爱女生
         * cover : https://images.dmzj.com/webpic/18/glove.jpg
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

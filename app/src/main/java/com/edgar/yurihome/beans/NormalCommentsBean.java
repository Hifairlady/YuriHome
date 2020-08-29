package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NormalCommentsBean {

    /**
     * commentIds : ["29973392","29973344","29973288","29973277","29973223","29973186","29973107,29972596","29973028,29972728","29972840","29972839","29972781","29972728","29972596","29972575","29972564","29972484","29972475","29972457","29972391","29972195"]
     * comments : [{"obj_id":"56325","content":"害怕，为什么猫会说话？","sender_uid":"102405440","sender_ip":"182.51.86.252","create_time":"1598502051","is_goods":"0","like_amount":"1","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973392","avatar_url":"https://avatar.dmzj.com/46/44/464426121c2786a3dbb624b512be1bd8.png","nickname":"S总奇遇记","sex":"1"},{"obj_id":"56325","content":"可爱","sender_uid":"100817759","sender_ip":"222.169.177.78","create_time":"1598501814","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973344","avatar_url":"https://avatar.dmzj.com/70/40/70404282c41e3e6c82a7219912efb3a0.png","nickname":"肆风","sex":"2"},{"obj_id":"56325","content":"这个妹妹性格好酷","sender_uid":"104673385","sender_ip":"117.176.140.107","create_time":"1598501579","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973288","avatar_url":"https://avatar.dmzj.com/70/33/70331a5071313642727910783d473a98.png","nickname":"sumu","sex":"1"},{"obj_id":"56325","content":"好","sender_uid":"105337252","sender_ip":"101.130.212.227","create_time":"1598501541","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973277","avatar_url":"https://avatar.dmzj.com/96/46/9646a90258e935807d268583da068ec8.png","nickname":"youak","sex":"1"},{"obj_id":"56325","content":"好可爱","sender_uid":"106305535","sender_ip":"42.234.28.89","create_time":"1598501255","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973223","avatar_url":"https://avatar.dmzj.com/31/4b/314b054b93655225e977d2dfdeef2a52.png","nickname":"犬曰今天天气不错","sex":"1"},{"obj_id":"56325","content":"不错","sender_uid":"104843609","sender_ip":"117.136.89.87","create_time":"1598501119","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973186","avatar_url":"https://avatar.dmzj.com/78/03/78032793616ed79194b54563a8ecccfd.png","nickname":"tianzhilongxiang","sex":"1"},{"obj_id":"56325","content":"这个老师不会的，应该偏萌系日常一点","sender_uid":"103418655","sender_ip":"117.175.129.47","create_time":"1598500710","is_goods":"0","like_amount":"0","upload_images":"","sender_terminal":"1","origin_comment_id":"0","id":"29973107","avatar_url":"https://avatar.dmzj.com/31/50/31503ba0bf8d85dd572a1c42e8be35dc.png","nickname":"夜已无月，人已不在","sex":"1"},{"id":"29972596","obj_id":"56325","content":"这个总不会再发刀子吧...？应该吧？？？？","sender_uid":"101836131","is_goods":"0","upload_images":"","like_amount":"0","create_time":"1598498412","origin_comment_id":"0","avatar_url":"https://avatar.dmzj.com/16/82/1682324d97bbbc5659e24d7cc8bae7b4.png","nickname":"阿夸天下第一可爱","sex":"1"}]
     * total : 6616
     */

    private int total;
    private List<String> commentIds;
    private List<CommentsBean> comments;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * obj_id : 56325
         * content : 害怕，为什么猫会说话？
         * sender_uid : 102405440
         * sender_ip : 182.51.86.252
         * create_time : 1598502051
         * is_goods : 0
         * like_amount : 1
         * upload_images :
         * sender_terminal : 1
         * origin_comment_id : 0
         * id : 29973392
         * avatar_url : https://avatar.dmzj.com/46/44/464426121c2786a3dbb624b512be1bd8.png
         * nickname : S总奇遇记
         * sex : 1
         */

        @SerializedName("obj_id")
        private int objId;
        private String content;
        @SerializedName("sender_uid")
        private String senderUid;
        @SerializedName("sender_ip")
        private String senderIp;
        @SerializedName("create_time")
        private long createTime;
        @SerializedName("is_goods")
        private String isGoods;
        @SerializedName("like_amount")
        private int likeAmount;
        @SerializedName("upload_images")
        private String uploadImages;
        @SerializedName("sender_terminal")
        private String senderTerminal;
        @SerializedName("origin_comment_id")
        private int originCommentId;
        private int id;
        @SerializedName("avatar_url")
        private String avatarUrl;
        private String nickname;
        private int sex;

        public int getObjId() {
            return objId;
        }

        public void setObjId(int objId) {
            this.objId = objId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSenderUid() {
            return senderUid;
        }

        public void setSenderUid(String senderUid) {
            this.senderUid = senderUid;
        }

        public String getSenderIp() {
            return senderIp;
        }

        public void setSenderIp(String senderIp) {
            this.senderIp = senderIp;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getIsGoods() {
            return isGoods;
        }

        public void setIsGoods(String isGoods) {
            this.isGoods = isGoods;
        }

        public int getLikeAmount() {
            return likeAmount;
        }

        public void setLikeAmount(int likeAmount) {
            this.likeAmount = likeAmount;
        }

        public String getUploadImages() {
            return uploadImages;
        }

        public void setUploadImages(String uploadImages) {
            this.uploadImages = uploadImages;
        }

        public String getSenderTerminal() {
            return senderTerminal;
        }

        public void setSenderTerminal(String senderTerminal) {
            this.senderTerminal = senderTerminal;
        }

        public int getOriginCommentId() {
            return originCommentId;
        }

        public void setOriginCommentId(int originCommentId) {
            this.originCommentId = originCommentId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }
    }
}

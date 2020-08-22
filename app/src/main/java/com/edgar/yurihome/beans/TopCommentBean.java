package com.edgar.yurihome.beans;

import com.google.gson.annotations.SerializedName;

public class TopCommentBean {

    /**
     * id : 27484053
     * obj_id : 7020
     * content : 大家好，这里是嵌字，为了更好的观看体验，往后的漫画本篇中尽量不加入制作者信息，如果有问题想反馈的话可以站内私信我。另外这里还要画两个饼，第一个是水母老师目前还有两个画集，“なもり画集 ゆるなもり”和“なもり画集2 なもり展”，由于汉化内容比较少，所以过段时间会把这两个发上来；第二个饼是“ゆるゆり10周年記念本　ゆるゆりX (百合姫コミックス) ”，由于是类似设定集的原因，内容比较多，有好心人能汉化的话也很欢迎，如果还是没人汉的话，那就只能等我们这边慢慢摸出来了（估计是以季为单位）
     * PS.最近发的十周年其实是指2018年……今年是漫画连载第十二个年头了
     * to_uid : 0
     * to_comment_id : 0
     * origin_comment_id : 0
     * sender_uid : 101367119
     * sender_zone : ""
     * sender_terminal : 1
     * sender_ip : 100.67.74.13
     * is_passed : 1
     * like_amount : 0
     * create_time : 1589808218
     * reply_amount : 0
     * check_uid : 8
     * top_status : 2
     * is_goods : 0
     * upload_images :
     * sensitive : 0
     * cover :
     * nickname : 三未丶
     * hot_comment_amount : 0
     * sex : 1
     * avatar_url : https://avatar.dmzj.com/71/71/717185754c372b8a275dfb934adca104.png
     * masterCommentNum : 0
     */

    private int id;
    @SerializedName("obj_id")
    private int objId;
    private String content;
    @SerializedName("to_uid")
    private int toUid;
    @SerializedName("to_comment_id")
    private int toCommentId;
    @SerializedName("origin_comment_id")
    private int originCommentId;
    @SerializedName("sender_uid")
    private int senderUid;
    @SerializedName("sender_zone")
    private String senderZone;
    @SerializedName("sender_terminal")
    private int senderTerminal;
    @SerializedName("sender_ip")
    private String senderIp;
    @SerializedName("is_passed")
    private int isPassed;
    @SerializedName("like_amount")
    private int likeAmount;
    @SerializedName("create_time")
    private long createTime;
    @SerializedName("reply_amount")
    private int replyAmount;
    @SerializedName("check_uid")
    private int checkUid;
    @SerializedName("top_status")
    private int topStatus;
    @SerializedName("is_goods")
    private int isGoods;
    @SerializedName("upload_images")
    private String uploadImages;
    private int sensitive;
    private String cover;
    private String nickname;
    @SerializedName("hot_comment_amount")
    private int hotCommentAmount;
    private int sex;
    @SerializedName("avatar_url")
    private String avatarUrl;
    private int masterCommentNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public int getToUid() {
        return toUid;
    }

    public void setToUid(int toUid) {
        this.toUid = toUid;
    }

    public int getToCommentId() {
        return toCommentId;
    }

    public void setToCommentId(int toCommentId) {
        this.toCommentId = toCommentId;
    }

    public int getOriginCommentId() {
        return originCommentId;
    }

    public void setOriginCommentId(int originCommentId) {
        this.originCommentId = originCommentId;
    }

    public int getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(int senderUid) {
        this.senderUid = senderUid;
    }

    public String getSenderZone() {
        return senderZone;
    }

    public void setSenderZone(String senderZone) {
        this.senderZone = senderZone;
    }

    public int getSenderTerminal() {
        return senderTerminal;
    }

    public void setSenderTerminal(int senderTerminal) {
        this.senderTerminal = senderTerminal;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public int getIsPassed() {
        return isPassed;
    }

    public void setIsPassed(int isPassed) {
        this.isPassed = isPassed;
    }

    public int getLikeAmount() {
        return likeAmount;
    }

    public void setLikeAmount(int likeAmount) {
        this.likeAmount = likeAmount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getReplyAmount() {
        return replyAmount;
    }

    public void setReplyAmount(int replyAmount) {
        this.replyAmount = replyAmount;
    }

    public int getCheckUid() {
        return checkUid;
    }

    public void setCheckUid(int checkUid) {
        this.checkUid = checkUid;
    }

    public int getTopStatus() {
        return topStatus;
    }

    public void setTopStatus(int topStatus) {
        this.topStatus = topStatus;
    }

    public int getIsGoods() {
        return isGoods;
    }

    public void setIsGoods(int isGoods) {
        this.isGoods = isGoods;
    }

    public String getUploadImages() {
        return uploadImages;
    }

    public void setUploadImages(String uploadImages) {
        this.uploadImages = uploadImages;
    }

    public int getSensitive() {
        return sensitive;
    }

    public void setSensitive(int sensitive) {
        this.sensitive = sensitive;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getHotCommentAmount() {
        return hotCommentAmount;
    }

    public void setHotCommentAmount(int hotCommentAmount) {
        this.hotCommentAmount = hotCommentAmount;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getMasterCommentNum() {
        return masterCommentNum;
    }

    public void setMasterCommentNum(int masterCommentNum) {
        this.masterCommentNum = masterCommentNum;
    }
}

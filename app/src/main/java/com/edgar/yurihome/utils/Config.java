package com.edgar.yurihome.utils;

public class Config {

    //https://v3api.dmzj.com/classify/3243-2304-26310-3263/1/0.json
    public static final String BASE_URL_CLASSIFY = "https://v3api.dmzj.com/classify/";

    //http://v3api.dmzj.com/0/category.json
    public static final String BASE_URL_CATEGORY = "http://v3api.dmzj.com/0/category.json";

    //http://v3api.dmzj.com/chapter/19081/94348.json
    public static final String BASE_URL_CHAPTER = "http://v3api.dmzj.com/chapter/";

    //http://v3api.dmzj.com/v3/comic/related/7020.json
    public static final String BASE_URL_RELATED_COMICS = "http://v3api.dmzj.com/v3/comic/related/";

    //http://v3api.dmzj.com/UCenter/author/1170.json
    public static final String BASE_URL_AUTHOR_COMICS = "http://v3api.dmzj.com/UCenter/author/";

    //http://v3api.dmzj.com/comic/comic_7020.json
    public static final String BASE_URL_COMIC_DETAILS = "http://v3api.dmzj.com/comic/comic_";

    //http://v3comment.dmzj.com/v1/4/latest/7020?page_index=1&limit=30
    public static final String BASE_URL_LATEST_COMMENTS = "http://v3comment.dmzj.com/v1/4/latest/";

    //http://v3api.dmzj.com/comment2/getTopComment/4/4/7020.json
    public static final String BASE_URL_TOP_COMMENT_BOARD = "http://v3api.dmzj.com/comment2/getTopComment/4/4/";

    //http://v3api.dmzj.com/comment2/getTopComment/4/2/7020.json
    public static final String BASE_URL_TOP_COMMENT = "http://v3api.dmzj.com/comment2/getTopComment/4/2/";

    //http://interface.dmzj.com/api/NewComment2/list?type=4&obj_id=7020&hot=1&page_index=0
    public static final String BASE_URL_HOT_COMMENTS = "http://interface.dmzj.com/api/NewComment2/list?type=4&obj_id=";

    //http://images.dmzj.com/commentImg/13/20200519142333_301_small.jpg
    public static final String BASE_URL_COMMENT_IMAGE = "http://images.dmzj.com/commentImg/";

    //http://v3api.dmzj.com/hotView/7020/104175.json
    public static final String BASE_URL_HOT_VIEWS = "http://v3api.dmzj.com/hotView/";

    //http://v3api.dmzj.com/viewPoint/0/55975/106404.json
    public static final String BASE_URL_ALL_VIEWS = "http://v3api.dmzj.com/viewPoint/0/";

    //http://v3api.dmzj.com/search/show/0/bang%2520dream/0.json
    public static final String BASE_URL_SEARCH = "http://v3api.dmzj.com/search/show/0/";

    //https://m.dmzj.com/view/42571/106671.html
    public static final String BASE_URL_TRANSLATOR = "https://m.dmzj.com/view/";

    public static String getComicsUrlByFilter(int typeCode, int regionCode, int groupCode, int statusCode, int sortCode, int page) {
        String result = BASE_URL_CLASSIFY + typeCode + "-" + regionCode + "-" + groupCode + "-" + statusCode + "/" + sortCode + "/" + page + ".json";
        return result;
    }

    public static String getChapterImagesUrl(int comicId, int chapterId) {
        String result = BASE_URL_CHAPTER + comicId + "/" + chapterId + ".json";
        return result;
    }

    public static String getRelatedComicsUrl(int comicId) {
        String result = BASE_URL_RELATED_COMICS + comicId + ".json";
        return result;
    }

    public static String getAuthorComicsUrl(int authorId) {
        String result = BASE_URL_AUTHOR_COMICS + authorId + ".json";
        return result;
    }

    public static String getComicDetailsUrl(int comicId) {
        String result = BASE_URL_COMIC_DETAILS + comicId + ".json";
        return result;
    }

    public static String getLatestCommentsUrl(int comicId, int page, int limit) {
        String result = BASE_URL_LATEST_COMMENTS + comicId + "?page_index=" + page + "&limit=" + limit;
        return result;
    }

    public static String getTopCommentBoardUrl(int comicId) {
        String result = BASE_URL_TOP_COMMENT_BOARD + comicId + ".json";
        return result;
    }

    public static String getTopCommentUrl(int comicId) {
        String result = BASE_URL_TOP_COMMENT + comicId + ".json";
        return result;
    }

    public static String getHotCommentUrl(int comicId, int page) {
        String result = BASE_URL_HOT_COMMENTS + comicId + "&hot=1&page_index=" + page;
        return result;
    }

    public static String getCommentSmallImageUrl(int objId, String imageName) {
        String prefix = imageName.substring(0, imageName.lastIndexOf("."));
        String suffix = imageName.substring(imageName.lastIndexOf("."));
        String result = BASE_URL_COMMENT_IMAGE + prefix + "_small" + suffix;
        return result;
    }

}

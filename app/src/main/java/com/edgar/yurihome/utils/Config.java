package com.edgar.yurihome.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Config {

    private static final String TAG = "=======================" + Config.class.getSimpleName();

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

    public static String getClassifyFiltersUrl() {
        String result = BASE_URL_CLASSIFY + "filter.json";
        Log.d(TAG, "getClassifyFiltersUrl: " + result);
        return result;
    }

    public static String getComicsUrlByFilter(int typeCode, int regionCode, int groupCode, int statusCode, int sortCode, int page) {

        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("");

        if (typeCode != 0) {
            resultBuilder.append(typeCode);
        }

        if (regionCode != 0) {
            if (resultBuilder.length() != 0) {
                resultBuilder.append("-");
            }
            resultBuilder.append(regionCode);
        }

        if (groupCode != 0) {
            if (resultBuilder.length() != 0) {
                resultBuilder.append("-");
            }
            resultBuilder.append(groupCode);
        }

        if (statusCode != 0) {
            if (resultBuilder.length() != 0) {
                resultBuilder.append("-");
            }
            resultBuilder.append(statusCode);
        }

        if (resultBuilder.length() == 0) {
            resultBuilder.append(3243);
        }

        String result = BASE_URL_CLASSIFY + resultBuilder.toString() + "/" + sortCode + "/" + page + ".json";
        Log.d(TAG, "getComicsUrlByFilter: " + result);
        return result;
    }

    public static String getChapterImagesUrl(int comicId, int chapterId) {
        String result = BASE_URL_CHAPTER + comicId + "/" + chapterId + ".json";
        Log.d(TAG, "getChapterImagesUrl: " + result);
        return result;
    }

    public static String getRelatedComicsUrl(int comicId) {
        String result = BASE_URL_RELATED_COMICS + comicId + ".json";
        Log.d(TAG, "getRelatedComicsUrl: " + result);
        return result;
    }

    public static String getAuthorComicsUrl(int authorId) {
        String result = BASE_URL_AUTHOR_COMICS + authorId + ".json";
        Log.d(TAG, "getAuthorComicsUrl: " + result);
        return result;
    }

    public static String getComicDetailsUrl(int comicId) {
        String result = BASE_URL_COMIC_DETAILS + comicId + ".json";
        Log.d(TAG, "getComicDetailsUrl: " + result);
        return result;
    }

    public static String getLatestCommentsUrl(int comicId, int page, int limit) {
        String result = BASE_URL_LATEST_COMMENTS + comicId + "?page_index=" + page + "&limit=" + limit;
        Log.d(TAG, "getLatestCommentsUrl: " + result);
        return result;
    }

    public static String getTopCommentBoardUrl(int comicId) {
        String result = BASE_URL_TOP_COMMENT_BOARD + comicId + ".json";
        Log.d(TAG, "getTopCommentBoardUrl: " + result);
        return result;
    }

    public static String getTopCommentUrl(int comicId) {
        String result = BASE_URL_TOP_COMMENT + comicId + ".json";
        Log.d(TAG, "getTopCommentUrl: " + result);
        return result;
    }

    public static String getHotCommentUrl(int comicId, int page) {
        String result = BASE_URL_HOT_COMMENTS + comicId + "&hot=1&page_index=" + page;
        Log.d(TAG, "getHotCommentUrl: " + result);
        return result;
    }

    public static String getCommentSmallImageUrl(int objId, String imageName) {
        String prefix = imageName.substring(0, imageName.lastIndexOf("."));
        String suffix = imageName.substring(imageName.lastIndexOf("."));
        String result = BASE_URL_COMMENT_IMAGE + prefix + "_small" + suffix;
        Log.d(TAG, "getCommentSmallImageUrl: " + result);
        return result;
    }

    public static String getCommentBigImageUrl(int objId, String imageName) {
        String result = BASE_URL_COMMENT_IMAGE + objId + "/" + imageName;
        Log.d(TAG, "getCommentBigImageUrl: " + result);
        return result;
    }

    public static String getHotViewsUrl(int comicId, int chapterId) {
        String result = BASE_URL_HOT_VIEWS + comicId + "/" + chapterId + ".json";
        Log.d(TAG, "getHotViewsUrl: " + result);
        return result;
    }

    public static String getAllViewsUrl(int comicId, int chapterId) {
        String result = BASE_URL_ALL_VIEWS + comicId + "/" + chapterId + ".json";
        Log.d(TAG, "getAllViewsUrl: " + result);
        return result;
    }

    public static String getTranslatorUrl(int comicId, int chapterId) {
        String result = BASE_URL_TRANSLATOR + comicId + "/" + chapterId + ".html";
        Log.d(TAG, "getTranslatorUrl: " + result);
        return result;
    }

    public static String getSearchQueryUrl(String queryContent, int page) {
        String result = "";
        try {
            queryContent = URLEncoder.encode(queryContent, "UTF-8");
            queryContent = queryContent.replaceAll("\\+", "%20");
            result = BASE_URL_SEARCH + queryContent + "/" + page + ".json";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getSearchQueryUrl: " + result);
        return result;
    }

}

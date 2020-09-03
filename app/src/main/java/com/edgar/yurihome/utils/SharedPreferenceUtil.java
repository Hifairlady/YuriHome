package com.edgar.yurihome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.edgar.yurihome.beans.ClassifyFilterBean;

import java.util.ArrayList;

public class SharedPreferenceUtil {

    private static final String TAG = "===================" + SharedPreferenceUtil.class.getSimpleName();

    private static final String SP_CLASSIFY_FILTERS = "SP_CLASSIFY_FILTERS";

    private static final String SP_FILTERS_TYPE_TAG_ID = "SP_FILTERS_TYPE_TAG_ID";
    private static final String SP_FILTERS_TYPE_TAG_NAME = "SP_FILTERS_TYPE_TAG_NAME";
    private static final String SP_FILTERS_STATUS_TAG_ID = "SP_FILTERS_STATUS_TAG_ID";
    private static final String SP_FILTERS_STATUS_TAG_NAME = "SP_FILTERS_STATUS_TAG_NAME";
    private static final String SP_FILTERS_REGION_TAG_ID = "SP_FILTERS_REGION_TAG_ID";
    private static final String SP_FILTERS_REGION_TAG_NAME = "SP_FILTERS_REGION_TAG_NAME";

    public static void storeFiltersFromNetwork(Context context, ArrayList<ClassifyFilterBean> filterBeans) {
        ClassifyFilterBean typeFilterBean = filterBeans.get(0);
        ClassifyFilterBean statusFilterBean = filterBeans.get(2);
        ClassifyFilterBean regionFilterBean = filterBeans.get(3);
        ArrayList<ClassifyFilterBean.FilterItem> typeFilterItems = new ArrayList<>(typeFilterBean.getFilterItems());
        ArrayList<ClassifyFilterBean.FilterItem> statusFilterItems = new ArrayList<>(statusFilterBean.getFilterItems());
        ArrayList<ClassifyFilterBean.FilterItem> regionFilterItems = new ArrayList<>(regionFilterBean.getFilterItems());
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String typeTagIdString = "", typeTagNameString = "";
        for (ClassifyFilterBean.FilterItem typeItem : typeFilterItems) {
            typeTagIdString = typeTagIdString + typeItem.getTagId() + "_";
            typeTagNameString = typeTagNameString + typeItem.getTagName() + "_";
        }
        typeTagIdString = typeTagIdString.substring(0, typeTagIdString.length() - 1);
        typeTagNameString = typeTagNameString.substring(0, typeTagNameString.length() - 1);

        editor.putString(SP_FILTERS_TYPE_TAG_ID, typeTagIdString);
        editor.putString(SP_FILTERS_TYPE_TAG_NAME, typeTagNameString);

        String statTagIdString = "", statTagNameString = "";
        for (ClassifyFilterBean.FilterItem statItem : statusFilterItems) {
            statTagIdString = statTagIdString + statItem.getTagId() + "_";
            statTagNameString = statTagNameString + statItem.getTagName() + "_";
        }
        statTagIdString = statTagIdString.substring(0, statTagIdString.length() - 1);
        statTagNameString = statTagNameString.substring(0, statTagNameString.length() - 1);

        editor.putString(SP_FILTERS_STATUS_TAG_ID, statTagIdString);
        editor.putString(SP_FILTERS_STATUS_TAG_NAME, statTagNameString);

        String regionTagIdString = "", regionTagNameString = "";
        for (ClassifyFilterBean.FilterItem regionItem : regionFilterItems) {
            regionTagIdString = regionTagIdString + regionItem.getTagId() + "_";
            regionTagNameString = regionTagNameString + regionItem.getTagName() + "_";
        }
        regionTagIdString = regionTagIdString.substring(0, regionTagIdString.length() - 1);
        regionTagNameString = regionTagNameString.substring(0, regionTagNameString.length() - 1);

        editor.putString(SP_FILTERS_REGION_TAG_ID, regionTagIdString);
        editor.putString(SP_FILTERS_REGION_TAG_NAME, regionTagNameString);

        editor.apply();
    }

    public static int[] getAllFilterTypeTagId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String typeTagIdString = sharedPreferences.getString(SP_FILTERS_TYPE_TAG_ID, "");
        assert typeTagIdString != null;
        String[] temp = typeTagIdString.split("_");
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("")) {
                result[i] = 0;
                continue;
            }
            result[i] = Integer.parseInt(temp[i]);
        }
        return result;
    }

    public static String[] getAllFilterTypeTagName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String typeTagIdString = sharedPreferences.getString(SP_FILTERS_TYPE_TAG_NAME, "");
        assert typeTagIdString != null;
        return typeTagIdString.split("_");
    }

    public static int[] getAllFilterStatTagId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String statTagIdString = sharedPreferences.getString(SP_FILTERS_STATUS_TAG_ID, "");
        assert statTagIdString != null;
        String[] temp = statTagIdString.split("_");
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("")) {
                result[i] = 0;
                continue;
            }
            result[i] = Integer.parseInt(temp[i]);
        }
        return result;
    }

    public static String[] getAllFilterStatTagName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String statTagIdString = sharedPreferences.getString(SP_FILTERS_STATUS_TAG_NAME, "");
        assert statTagIdString != null;
        return statTagIdString.split("_");
    }

    public static int[] getAllFilterRegionTagId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String regionTagIdString = sharedPreferences.getString(SP_FILTERS_REGION_TAG_ID, "");
        assert regionTagIdString != null;
        String[] temp = regionTagIdString.split("_");
        int[] result = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].equals("")) {
                result[i] = 0;
                continue;
            }
            result[i] = Integer.parseInt(temp[i]);
        }
        return result;
    }

    public static String[] getAllFilterRegionTagName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        String regionTagIdString = sharedPreferences.getString(SP_FILTERS_REGION_TAG_NAME, "");
        assert regionTagIdString != null;
        return regionTagIdString.split("_");
    }

    public static void storeUserFilterSetting(Context context, int typeStoreIndex, int statStoreIndex, int regionStoreIndex, int sortStoreIndex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("FILTER_USER_SETTING_TYPE_INDEX", typeStoreIndex);
        editor.putInt("FILTER_USER_SETTING_STATUS_INDEX", statStoreIndex);
        editor.putInt("FILTER_USER_SETTING_REGION_INDEX", regionStoreIndex);
        editor.putInt("FILTER_USER_SETTING_SORT_INDEX", sortStoreIndex);
        editor.apply();
    }

    public static int getUserFilterTypeIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("FILTER_USER_SETTING_TYPE_INDEX", 0);
    }

    public static int getUserFilterStatIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("FILTER_USER_SETTING_STATUS_INDEX", 0);
    }

    public static int getUserFilterRegionIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("FILTER_USER_SETTING_REGION_INDEX", 0);
    }

    public static int getUserFilterSortIndex(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("FILTER_USER_SETTING_SORT_INDEX", 1);
    }

    public static void storeSearchHistories(Context context, ArrayList<String> histories) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP_SEARCH_HISTORY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder builder = new StringBuilder();
        int index = 0;
        for (String history : histories) {
            builder.append(history);
            if (index != histories.size() - 1) {
                builder.append("_");
            }
            index++;
        }
        editor.putString("ALL_SEARCH_HISTORY_STRING", builder.toString());
        editor.apply();
    }

    public static String[] getSearchHistories(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP_SEARCH_HISTORY", Context.MODE_PRIVATE);
        String allSearchHistoryString = sharedPreferences.getString("ALL_SEARCH_HISTORY_STRING", "");
        if (allSearchHistoryString != null && allSearchHistoryString.length() != 0) {
            return allSearchHistoryString.split("_");
        }
        return null;
    }

    public static String getBrowseHistoryJson(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP_BROWSE_HISTORY", Context.MODE_PRIVATE);
        return sharedPreferences.getString("BROWSE_HISTORY_JSON", "");
    }

    public static void storeBrowseHistoryJson(Context context, String jsonString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SP_BROWSE_HISTORY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("BROWSE_HISTORY_JSON", jsonString);
        editor.apply();
    }

}

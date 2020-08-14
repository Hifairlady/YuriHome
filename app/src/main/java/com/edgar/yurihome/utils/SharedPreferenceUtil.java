package com.edgar.yurihome.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static final String SP_CLASSIFY_FILTERS = "SP_CLASSIFY_FILTERS";
    private static final String SP_FILTER_TYPE_CODE = "SP_FILTER_TYPE_CODE";
    private static final String SP_FILTER_REGION_CODE = "SP_FILTER_REGION_CODE";
    private static final String SP_FILTER_GROUP_CODE = "SP_FILTER_GROUP_CODE";
    private static final String SP_FILTER_STATUS_CODE = "SP_FILTER_STATUS_CODE";
    private static final String SP_FILTER_SORT_CODE = "SP_FILTER_SORT_CODE";

    private static final String[] filterKeys = {SP_FILTER_TYPE_CODE, SP_FILTER_REGION_CODE, SP_FILTER_GROUP_CODE, SP_FILTER_STATUS_CODE, SP_FILTER_SORT_CODE};

    public static int saveClassifyFilters(Context context, int[] filters) {
        if (filters.length != 5) return -1;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //0: typeCode, 1: regionCode, 2: groupCode, 3: statusCode, 4: sortCode
        for (int i = 0; i < filters.length; i++) {
            editor.putInt(filterKeys[i], filters[i]);
        }
        editor.apply();
        return 0;
    }

    public static int[] getClassifyFilters(Context context) {
        int[] result = new int[5];
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_CLASSIFY_FILTERS, Context.MODE_PRIVATE);
        for (int i = 0; i < filterKeys.length; i++) {
            int defVal = (i == 0 ? 3243 : 0);
//            int defVal = 0;
            result[i] = sharedPreferences.getInt(filterKeys[i], defVal);
        }
        return result;
    }

}

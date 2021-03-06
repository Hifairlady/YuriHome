package com.edgar.yurihome.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class ScreenUtil {
    public static void setNoTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}

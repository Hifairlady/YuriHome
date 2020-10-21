package com.edgar.yurihome.scenarios;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ClassifyFilterBean;
import com.edgar.yurihome.fragments.MainListFragment;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonDataListUtil;
import com.edgar.yurihome.utils.SharedPreferenceUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "======================" + MainActivity.class.getSimpleName();

    private Handler mHandler;
    private ConstraintLayout mainRootView;
    private Type type = new TypeToken<ArrayList<ClassifyFilterBean>>() {
    }.getType();
    private JsonDataListUtil<ClassifyFilterBean> filterJsonDataListUtil = new JsonDataListUtil<>(type);

    private boolean canExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
        fetchFilterData();

    }

    private void initView() {
        mainRootView = findViewById(R.id.main_root_view);
        MainListFragment mainListFragment = MainListFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, mainListFragment);
        transaction.commit();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        ArrayList<ClassifyFilterBean> filterBeans = filterJsonDataListUtil.getDataList();
                        SharedPreferenceUtil.storeFiltersFromNetwork(MainActivity.this, filterBeans);
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(mainRootView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(mainRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        Snackbar.make(mainRootView, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    private void fetchFilterData() {
        String urlString = Config.getClassifyFiltersUrl();
        filterJsonDataListUtil.fetchJsonData(mHandler, urlString);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!canExit) {
                Snackbar.make(mainRootView, R.string.string_one_click_to_exit_text, Snackbar.LENGTH_SHORT).show();
                canExit = true;
                mainRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canExit = false;
                    }
                }, 2000);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
package com.edgar.yurihome;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edgar.yurihome.beans.ClassifyFilterBean;
import com.edgar.yurihome.fragments.MainListFragment;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonResponseItem;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SharedPreferenceUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "======================" + MainActivity.class.getSimpleName();

    private JsonResponseItem responseItem = new JsonResponseItem();
    private Handler mHandler;
    private ConstraintLayout mainRootView;

    private ArrayList<ClassifyFilterBean> filterBeans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

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
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<ClassifyFilterBean>>() {
                            }.getType();
                            filterBeans = gson.fromJson(jsonString, type);
                            SharedPreferenceUtil.storeFiltersFromNetwork(MainActivity.this, filterBeans);

                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            Snackbar.make(mainRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(mainRootView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(mainRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void initData() {
        String urlString = Config.getClassifyFiltersUrl();
        JsonUtil.fetchJsonData(mHandler, urlString);
    }
}
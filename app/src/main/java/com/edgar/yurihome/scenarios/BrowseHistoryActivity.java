package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.BrowseHistoryListAdapter;
import com.edgar.yurihome.beans.BrowseHistoryBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.SharedPreferenceUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BrowseHistoryActivity extends AppCompatActivity {

    private static final String TAG = "================" + BrowseHistoryActivity.class.getSimpleName();

    private RecyclerView rvHistoryList;
    private MaterialToolbar toolbar;
    private BrowseHistoryListAdapter listAdapter;

    private OnListItemClickListener mOnListItemClickListener = new OnListItemClickListener() {
        @Override
        public void onItemClick(int position) {
            BrowseHistoryBean historyBean = listAdapter.getItemAt(position);
            if (historyBean == null) return;
            Intent intent = new Intent(BrowseHistoryActivity.this, ComicDetailsActivity.class);
            intent.putExtra("COMIC_DETAILS_URL", historyBean.getDetailsUrl());
            intent.putExtra("COMIC_COVER_URL", historyBean.getCoverUrl());
            intent.putExtra("COMIC_TITLE", historyBean.getTitle());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_history);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();

    }

    private void initView() {

        toolbar = findViewById(R.id.history_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvHistoryList = findViewById(R.id.rv_browse_history_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvHistoryList.setLayoutManager(layoutManager);
        listAdapter = new BrowseHistoryListAdapter(this);
        listAdapter.setOnItemClickListener(mOnListItemClickListener);
        rvHistoryList.setAdapter(listAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.browse_history_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.browse_history_clear_list) {
            listAdapter.setDataList(null);
            SharedPreferenceUtil.storeBrowseHistoryJson(this, "");
            toolbar.setTitle(getString(R.string.string_browse_history_activity_appbar_title_text, 0));
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList() {

        String jsonString = SharedPreferenceUtil.getBrowseHistoryJson(this);
        ArrayList<BrowseHistoryBean> list = new ArrayList<>();
        if (!jsonString.isEmpty()) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<BrowseHistoryBean>>() {
                }.getType();
                list = gson.fromJson(jsonString, type);
                listAdapter.setDataList(list);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        toolbar.setTitle(getString(R.string.string_browse_history_activity_appbar_title_text, list.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        updateList();
    }
}
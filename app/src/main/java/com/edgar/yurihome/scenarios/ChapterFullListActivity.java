package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.ChapterListAdapter;
import com.edgar.yurihome.beans.ComicDetailsBean;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChapterFullListActivity extends AppCompatActivity {

    private ArrayList<ComicDetailsBean.ChaptersBean.DataBean> dataBeans = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChapterListAdapter adapter;
    private int lastChapterId;
    private int comicId;
    private String comicName, chapterPartTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_full_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        MaterialToolbar toolbar = findViewById(R.id.full_chapter_list_toolbar);
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
        initData();
        recyclerView = findViewById(R.id.rv_full_chapter_list);
        adapter = new ChapterListAdapter(ChapterFullListActivity.this, dataBeans, lastChapterId, true,
                comicId, comicName, chapterPartTitle);
        GridLayoutManager layoutManager = new GridLayoutManager(ChapterFullListActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {

//        intent.putExtra("COMIC_ID", comicId);
//        intent.putExtra("COMIC_NAME", comicName);
//        intent.putExtra("CHAPTER_ID", dataBean.getChapterId());
//        intent.putExtra("CHAPTER_UPDATE_TIME", chapterUpdateTime);
//        intent.putExtra("CHAPTER_LONG_TITLE", chapterLongTitle);
//        intent.putExtra("CHAPTER_PART_TITLE", chapterPartTitle);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        lastChapterId = bundle.getInt("LAST_CHAPTER_ID", 0);
        comicId = bundle.getInt("COMIC_ID", 0);
        comicName = bundle.getString("COMIC_NAME", "COMIC_NAME");
        chapterPartTitle = bundle.getString("CHAPTER_PART_TITLE", "CHAPTER_PART_TITLE");
        String jsonString = bundle.getString("FULL_DATA_LIST_JSON", "");

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ComicDetailsBean.ChaptersBean.DataBean>>() {
            }.getType();
            ArrayList<ComicDetailsBean.ChaptersBean.DataBean> temp = gson.fromJson(jsonString, type);
            dataBeans = new ArrayList<>(temp);
        } catch (JsonSyntaxException | NullPointerException e) {
            e.printStackTrace();
        }

    }
}
package com.edgar.yurihome.scenarios;

import android.content.Intent;
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

public class FullChapterListActivity extends AppCompatActivity {

    private ArrayList<ComicDetailsBean.ChaptersBean.DataBean> dataBeans = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChapterListAdapter adapter;
    private int lastChapterId;
    private int comicId;
    private boolean isCurOrderAsc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_full_chapter_list);

        MaterialToolbar toolbar = findViewById(R.id.full_chapter_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        recyclerView = findViewById(R.id.rv_full_chapter_list);
        adapter = new ChapterListAdapter(FullChapterListActivity.this, dataBeans, lastChapterId, true, comicId);
        GridLayoutManager layoutManager = new GridLayoutManager(FullChapterListActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        lastChapterId = bundle.getInt("LAST_CHAPTER_ID", 0);
        comicId = bundle.getInt("COMIC_ID", 0);
        String jsonString = bundle.getString("FULL_DATA_LIST_JSON", "");

        try {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ComicDetailsBean.ChaptersBean.DataBean>>() {
            }.getType();
            ArrayList<ComicDetailsBean.ChaptersBean.DataBean> temp = gson.fromJson(jsonString, type);
            dataBeans = new ArrayList<>(temp);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

    }
}
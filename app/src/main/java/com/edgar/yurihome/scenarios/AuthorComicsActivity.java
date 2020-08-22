package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.AuthorComicsListAdapter;
import com.edgar.yurihome.beans.AuthorComicsBean;
import com.edgar.yurihome.interfaces.OnComicListItemClickListener;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

public class AuthorComicsActivity extends AppCompatActivity {

    private RecyclerView rvAuthorComics;
    private MaterialToolbar mToolbar;

    private AuthorComicsListAdapter listAdapter;
    private ArrayList<AuthorComicsBean.AuthorComicData> dataList = new ArrayList<>();
    private Handler mHandler;

    private int authorId;
    private String urlString, authorName;
    private OnComicListItemClickListener mOnComicListItemClickListener = new OnComicListItemClickListener() {
        @Override
        public void onItemClick(int position) {
//            comicDetailsUrl = bundle.getString("COMIC_DETAILS_URL", "");
//            coverUrl = bundle.getString("COMIC_COVER_URL", "");
//            comicTitle = bundle.getString("COMIC_TITLE", "");
            AuthorComicsBean.AuthorComicData comicData = dataList.get(position);
            String comicDetailsUrl = Config.getComicDetailsUrl(comicData.getId());
            String coverUrl = comicData.getCover();
            String comicTitle = comicData.getName();

            Intent detailsIntent = new Intent(AuthorComicsActivity.this, ComicDetailsActivity.class);
            detailsIntent.putExtra("COMIC_DETAILS_URL", comicDetailsUrl);
            detailsIntent.putExtra("COMIC_COVER_URL", coverUrl);
            detailsIntent.putExtra("COMIC_TITLE", comicTitle);
            startActivity(detailsIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_comics);

        initData();
        initView();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            setIntent(intent);
            initData();
            initView();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            authorId = bundle.getInt("AUTHOR_ID", 0);
            authorName = bundle.getString("AUTHOR_NAME", "AUTHOR_NAME");
        }
        urlString = Config.getAuthorComicsUrl(authorId);
    }

    private void initView() {

        mToolbar = findViewById(R.id.author_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbar.setTitle(authorName);

        rvAuthorComics = findViewById(R.id.rv_author_comics);
        listAdapter = new AuthorComicsListAdapter(this, dataList);
        listAdapter.setItemClickListener(mOnComicListItemClickListener);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvAuthorComics.setLayoutManager(layoutManager);
        rvAuthorComics.setAdapter(listAdapter);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            AuthorComicsBean authorComicsBean = gson.fromJson(jsonString, AuthorComicsBean.class);
                            dataList = new ArrayList<>(authorComicsBean.getData());
                            listAdapter.setDataList(dataList);
                            authorName = authorComicsBean.getNickname();
                            mToolbar.setTitle(authorName);
                        } catch (JsonSyntaxException | NullPointerException e) {
                            e.printStackTrace();
                            Snackbar.make(rvAuthorComics, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(rvAuthorComics, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(rvAuthorComics, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        Snackbar.make(rvAuthorComics, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        JsonUtil.fetchJsonData(mHandler, urlString);
    }

}
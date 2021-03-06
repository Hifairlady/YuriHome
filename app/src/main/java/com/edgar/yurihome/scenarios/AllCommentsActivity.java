package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.AllCommentsListAdapter;
import com.edgar.yurihome.beans.NormalCommentsBean;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonDataUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AllCommentsActivity extends AppCompatActivity {

    private static final String TAG = "=================" + AllCommentsActivity.class.getSimpleName();
    private static final int MAX_LIMIT = 30;
    private int comicId;
    private String latestCommentsUrl = "";
    private int curPage = 0;
    private RecyclerView rvAllComments;
    private AllCommentsListAdapter listAdapter;

    private Handler mHandler;
    private JsonDataUtil<NormalCommentsBean> latestJsonDataUtil = new JsonDataUtil<>(NormalCommentsBean.class, Config.FETCH_JSON_DATA_TYPE_COMMENT);

    private MaterialToolbar toolbar;
    private FloatingActionButton fabTop;
    private SwipeRefreshLayout srlAllComments;
    private int scrollDistance = 0;

    private boolean isLoading = false, isFinalPage = false;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab_all_comments_top:
                    if (rvAllComments != null) {
                        scrollDistance = 0;
                        rvAllComments.scrollToPosition(0);
                        fabTop.hide();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initView();
        initData();
    }

    private void initView() {


        toolbar = findViewById(R.id.all_comments_toolbar);
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

        srlAllComments = findViewById(R.id.srl_all_comments);
        srlAllComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    curPage = 0;
                    isFinalPage = false;
                    isLoading = true;
                    latestCommentsUrl = Config.getLatestCommentsUrl(comicId, 0, MAX_LIMIT);
                    latestJsonDataUtil.fetchJsonData(mHandler, latestCommentsUrl);
                }
            }
        });

        fabTop = findViewById(R.id.fab_all_comments_top);
        fabTop.hide();
        fabTop.setOnClickListener(mOnClickListener);

        rvAllComments = findViewById(R.id.rv_all_comments_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new AllCommentsListAdapter(this);
        rvAllComments.setLayoutManager(layoutManager);
        rvAllComments.setAdapter(listAdapter);
        rvAllComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //load next page
                    if (isSlideToBottom(recyclerView) && !isLoading && !isFinalPage && listAdapter.getItemCount() > 0) {
                        loadNextPage();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDistance += dy;
                if (!fabTop.isShown() && scrollDistance >= 1500) {
                    fabTop.show();
                }
                if (fabTop.isShown() && scrollDistance < 1200) {
                    fabTop.hide();
                }
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                listAdapter.removeFooter();
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        NormalCommentsBean latestCommentsBean = latestJsonDataUtil.getData();
                        if (curPage == 0) {
                            initLatestComments(latestCommentsBean);
                        } else {
                            if (latestCommentsBean.getCommentIds().isEmpty()) {
                                if (curPage > 0) curPage -= 1;
                                isFinalPage = true;
                                Snackbar.make(srlAllComments, getString(R.string.string_no_more_data), Snackbar.LENGTH_SHORT).show();
                                break;
                            }
                            if (latestCommentsBean.getComments() == null || latestCommentsBean.getComments().isEmpty())
                                break;
                            ArrayList<NormalCommentsBean.CommentsBean> latestCommentsList = new ArrayList<>(latestCommentsBean.getComments());
                            ArrayList<String> commentIdList = new ArrayList<>(latestCommentsBean.getCommentIds());
                            listAdapter.appendItems(commentIdList, latestCommentsList);
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        if (curPage > 0) curPage -= 1;
                        Snackbar.make(srlAllComments, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        if (curPage > 0) curPage -= 1;
                        Snackbar.make(srlAllComments, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        if (curPage > 0) curPage -= 1;
                        Snackbar.make(srlAllComments, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
                isLoading = false;
                srlAllComments.setRefreshing(false);
            }
        };
    }

    private void initData() {
//        allCommentsIntent.putExtra("COMIC_ID", comicId);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            comicId = bundle.getInt("COMIC_ID", 0);
            latestCommentsUrl = Config.getLatestCommentsUrl(comicId, 0, MAX_LIMIT);
        }
        latestJsonDataUtil.fetchJsonData(mHandler, latestCommentsUrl);
    }

    private void loadNextPage() {
        curPage++;
        isLoading = true;
        listAdapter.addFooter();
        latestCommentsUrl = Config.getLatestCommentsUrl(comicId, curPage, MAX_LIMIT);
        latestJsonDataUtil.fetchJsonData(mHandler, latestCommentsUrl);
    }

    private void initLatestComments(NormalCommentsBean latestCommentsBean) {
        if (latestCommentsBean == null || latestCommentsBean.getComments() == null
                || latestCommentsBean.getComments().isEmpty()) return;
        ArrayList<NormalCommentsBean.CommentsBean> latestCommentsList = new ArrayList<>(latestCommentsBean.getComments());
        ArrayList<String> commentIdList = new ArrayList<>(latestCommentsBean.getCommentIds());
        listAdapter.setItems(commentIdList, latestCommentsList);
        toolbar.setTitle(getString(R.string.string_all_comments_total_title_text, latestCommentsBean.getTotal()));
    }

    private boolean isSlideToBottom(RecyclerView rvArg) {
        return rvArg != null &&
                rvArg.computeVerticalScrollExtent() + rvArg.computeVerticalScrollOffset() >=
                        rvArg.computeVerticalScrollRange();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
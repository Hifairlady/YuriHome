package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
import com.edgar.yurihome.utils.JsonUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
    private MaterialToolbar toolbar;
    private FloatingActionButton fabTop;
    private SwipeRefreshLayout srlAllComments;

    private boolean isLoading = false, isFinalPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_comments);

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

//        allCommentsIntent.putExtra("COMIC_ID", comicId);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            comicId = bundle.getInt("COMIC_ID", 0);
            latestCommentsUrl = Config.getLatestCommentsUrl(comicId, 0, MAX_LIMIT);
        }

        srlAllComments = findViewById(R.id.srl_all_comments);
        srlAllComments.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 0;
                isFinalPage = false;
                isLoading = true;
                latestCommentsUrl = Config.getLatestCommentsUrl(comicId, 0, MAX_LIMIT);
                JsonUtil.fetchJsonData(mHandler, latestCommentsUrl);
            }
        });

        fabTop = findViewById(R.id.fab_all_comments_top);
        fabTop.hide();
        fabTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvAllComments != null) {
                    rvAllComments.scrollToPosition(0);
                    fabTop.hide();
                }
            }
        });

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
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager1 = (LinearLayoutManager) rvAllComments.getLayoutManager();
                if (!fabTop.isShown() && layoutManager1 != null && layoutManager1.findLastCompletelyVisibleItemPosition() >= 5) {
                    fabTop.show();
                }
                if (fabTop.isShown() && layoutManager1 != null && layoutManager1.findFirstCompletelyVisibleItemPosition() <= 1) {
                    fabTop.hide();
                }

                if (isSlideToBottom(recyclerView) && !isLoading && !isFinalPage) {
                    loadNextPage();
                }
            }
        });

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                listAdapter.removeFooter();
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            jsonString = jsonString.replace("\"comments\":{", "\"comments\":[");
                            jsonString = jsonString.replace("},\"total\"", "],\"total\"");
                            jsonString = jsonString.replaceAll("\"[0-9]*\":", "");
                            Log.d(TAG, "handleMessage: " + jsonString);
                            Gson gson = new Gson();
                            NormalCommentsBean latestCommentsBean = gson.fromJson(jsonString, NormalCommentsBean.class);
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
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            if (curPage > 0) curPage -= 1;
                            Snackbar.make(srlAllComments, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
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

        JsonUtil.fetchJsonData(mHandler, latestCommentsUrl);


    }

    private void loadNextPage() {
        curPage++;
        isLoading = true;
        listAdapter.addFooter();
        latestCommentsUrl = Config.getLatestCommentsUrl(comicId, curPage, MAX_LIMIT);
        JsonUtil.fetchJsonData(mHandler, latestCommentsUrl);
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


}
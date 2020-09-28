package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.SearchHistoryListAdapter;
import com.edgar.yurihome.adapters.SearchResultListAdapter;
import com.edgar.yurihome.beans.SearchResultBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonDataListUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private SearchView svSearch;
    private RecyclerView rvResultList;
    private SearchResultListAdapter listAdapter;
    private ArrayList<SearchResultBean> resultBeans = new ArrayList<>();
    private Handler mHandler;
    private Type type = new TypeToken<ArrayList<SearchResultBean>>() {
    }.getType();
    private JsonDataListUtil<SearchResultBean> searchResultJsonDataListUtil = new JsonDataListUtil<>(type);

    private int curPage = 0;
    private String urlString, queryContent;
    private boolean isLoading = false;
    private boolean isFinalPage = false;

    private RecyclerView rvHistoryList;
    private SearchHistoryListAdapter historyListAdapter;

    private ConstraintLayout clRootLayout;
    private OnListItemClickListener mOnHistoryItemClickListener = new OnListItemClickListener() {
        @Override
        public void onItemClick(int position) {
            String queryContent = historyListAdapter.getHistoryAt(position);
            if (svSearch != null && queryContent != null) {
                svSearch.setQuery(queryContent, true);
            }
        }
    };

    //    comicDetailsUrl = bundle.getString("COMIC_DETAILS_URL", "");
//    coverUrl = bundle.getString("COMIC_COVER_URL", "");
//    comicTitle = bundle.getString("COMIC_TITLE", "");
    private OnListItemClickListener mOnListItemClickListener = new OnListItemClickListener() {
        @Override
        public void onItemClick(int position) {
            SearchResultBean resultBean = listAdapter.getItemAt(position);
            if (resultBean != null) {
                Intent detailsIntent = new Intent(SearchActivity.this, ComicDetailsActivity.class);
                detailsIntent.putExtra("COMIC_DETAILS_URL", Config.getComicDetailsUrl(resultBean.getId()));
                detailsIntent.putExtra("COMIC_COVER_URL", resultBean.getCover());
                detailsIntent.putExtra("COMIC_TITLE", resultBean.getTitle());
                startActivity(detailsIntent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initView();
        initData();
        initSearchHistoryView();
    }

    private void initView() {

        btnBack = findViewById(R.id.btn_search_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clRootLayout = findViewById(R.id.search_root_layout);
        svSearch = findViewById(R.id.sv_search);
        svSearch.setSubmitButtonEnabled(true);
        rvResultList = findViewById(R.id.rv_search_result_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new SearchResultListAdapter(this);
        listAdapter.setOnComicListItemClickListener(mOnListItemClickListener);
        rvResultList.setLayoutManager(layoutManager);
        rvResultList.setAdapter(listAdapter);
    }

    private void initData() {

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        ArrayList<SearchResultBean> resultBeans = searchResultJsonDataListUtil.getDataList();
                        if (resultBeans.size() == 0) {
                            isFinalPage = true;
                            if (curPage > 0) {
                                curPage--;
                            }
                            Snackbar.make(clRootLayout, getString(R.string.string_no_more_data), Snackbar.LENGTH_SHORT).show();
                            break;
                        }

                        if (curPage == 0) {
                            listAdapter.setItems(resultBeans);
                            rvResultList.scrollToPosition(0);
                        } else {
                            listAdapter.appendItems(resultBeans);
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        if (curPage > 0) {
                            curPage--;
                        }
                        Snackbar.make(clRootLayout, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        if (curPage > 0) {
                            curPage--;
                        }
                        Snackbar.make(clRootLayout, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        if (curPage > 0) {
                            curPage--;
                        }
                        Snackbar.make(clRootLayout, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
                isLoading = false;
            }
        };

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                isLoading = true;
                isFinalPage = false;
                queryContent = s;
                curPage = 0;
                urlString = Config.getSearchQueryUrl(queryContent, curPage);
                searchResultJsonDataListUtil.fetchJsonData(mHandler, urlString);
                historyListAdapter.appendHistory(queryContent);
                svSearch.clearFocus();
                hideHistoryView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                historyListAdapter.filterMatchedList(s);
                showHistoryView();
                return false;
            }
        });

        rvResultList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView) && !isLoading && !isFinalPage) {
                    isLoading = true;
                    loadNextPage();
                }
            }
        });



        svSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showHistoryView();
                }
            }
        });



    }

    private boolean isSlideToBottom(RecyclerView rvArg) {
        return rvArg != null &&
                rvArg.computeVerticalScrollExtent() + rvArg.computeVerticalScrollOffset() >=
                        rvArg.computeVerticalScrollRange();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rvHistoryList != null && rvHistoryList.getVisibility() != View.GONE) {
                hideHistoryView();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loadNextPage() {
        curPage++;
        urlString = Config.getSearchQueryUrl(queryContent, curPage);
        searchResultJsonDataListUtil.fetchJsonData(mHandler, urlString);
    }

    private void initSearchHistoryView() {

        rvHistoryList = findViewById(R.id.rv_search_history_list2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        historyListAdapter = new SearchHistoryListAdapter(this);
        rvHistoryList.setLayoutManager(layoutManager);
        rvHistoryList.setItemAnimator(null);
        rvHistoryList.setAdapter(historyListAdapter);
        historyListAdapter.setOnHistoryItemClickListener(mOnHistoryItemClickListener);

    }

    private void showHistoryView() {

        if (rvHistoryList != null && rvHistoryList.getVisibility() == View.GONE) {
            rvHistoryList.setVisibility(View.VISIBLE);
        }

    }

    private void hideHistoryView() {

        if (rvHistoryList != null && rvHistoryList.getVisibility() != View.GONE) {
            rvHistoryList.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
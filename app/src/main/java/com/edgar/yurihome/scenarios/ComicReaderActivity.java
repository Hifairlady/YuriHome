package com.edgar.yurihome.scenarios;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.ReaderListAdapter;
import com.edgar.yurihome.adapters.ViewPointListAdapter;
import com.edgar.yurihome.beans.ReaderImagesItem;
import com.edgar.yurihome.beans.ViewPointBean;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.NetworkUtil;
import com.edgar.yurihome.utils.ScreenUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ComicReaderActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "=================" + ComicReaderActivity.class.getSimpleName();

    private ImageButton btnBack;
    private SeekBar sbReader;
    private TextView tvCurPage, tvMaxPage, tvTitle;
    private Button btnMore;
    private FrameLayout flRootView;
    private ConstraintLayout clActionsLayout;

    private TextView tvBottomChapter, tvBottomPage, tvBottomNetwork, tvBottomTime;
    private TextView tvDrawerComicTitle, tvDrawerChapterTitle, tvDrawerUpdateTime;

    private DrawerLayout drawerLayout;

    private RecyclerView rvReaderList, rvViewPointsList;
    private ReaderListAdapter listAdapter;
    private ViewPointListAdapter viewPointListAdapter;
    private ReaderImagesItem readerImagesItem;

    private ArrayList<ViewPointBean> viewPointsList = new ArrayList<>();

    private String urlString;
    private int comicId, chapterId;
    private boolean isFullScreen = true;
    private int curPage = 0;
    private int totalPageNum = 0;
    private String chapterName;
    private String comicName, chapterLongTitle, chapterUpdateString;
    private long chapterUpdateTime;

    private Handler mHandler, mViewPointHandler;

    private int screenWidth, screenHeight;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ib_reader_back:
                    finish();
                    break;

                case R.id.btn_reader_more_actions:
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    break;

                case R.id.reader_root_view:
                    break;

                default:
                    break;
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            curPage = i;
            tvCurPage.setText(String.valueOf(i + 1));
//            Log.d(TAG, "onProgressChanged: max: " + seekBar.getMax() + " cur: " + i);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            rvReaderList.scrollToPosition(curPage);
        }
    };
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (layoutManager != null) {
                curPage = layoutManager.findFirstVisibleItemPosition();
                sbReader.setProgress(curPage);
                if (isSlideToBottom(recyclerView)) {
                    curPage = sbReader.getMax();
                    sbReader.setProgress(curPage);
                }
                tvCurPage.setText(String.valueOf(curPage + 1));
                if (!isFullScreen) {
                    isFullScreen = true;
                    setActionsLayoutVisible(true);
                }
                setBottomInfos();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.setNoTitleBar(ComicReaderActivity.this);
        setContentView(R.layout.activity_comic_reader);

        initData();
        initView();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        flRootView = findViewById(R.id.reader_root_view);
        clActionsLayout = findViewById(R.id.reader_actions_layout);
        btnBack = findViewById(R.id.ib_reader_back);
        sbReader = findViewById(R.id.sb_reader_progress);
        tvCurPage = findViewById(R.id.tv_seekbar_cur_page);
        tvMaxPage = findViewById(R.id.tv_seekbar_max_page);
        tvTitle = findViewById(R.id.tv_reader_title);
        btnMore = findViewById(R.id.btn_reader_more_actions);
        rvReaderList = findViewById(R.id.rv_reader_list);

        tvBottomChapter = findViewById(R.id.tv_reader_bottom_chapter);
        tvBottomPage = findViewById(R.id.tv_reader_bottom_page);
        tvBottomNetwork = findViewById(R.id.tv_reader_bottom_network);
        tvBottomTime = findViewById(R.id.tv_reader_bottom_time);

        tvDrawerComicTitle = findViewById(R.id.tv_drawer_comic_title);
        tvDrawerChapterTitle = findViewById(R.id.tv_drawer_chapter_title);
        tvDrawerUpdateTime = findViewById(R.id.tv_drawer_update_time);

        tvDrawerComicTitle.setText(comicName);
        tvDrawerChapterTitle.setText(chapterLongTitle);
        tvDrawerUpdateTime.setText(chapterUpdateString);

        drawerLayout = findViewById(R.id.reader_drawer_layout);

        btnBack.setOnClickListener(mOnClickListener);
        btnMore.setOnClickListener(mOnClickListener);
        sbReader.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        rvReaderList.setOnTouchListener(this);

        setActionsLayoutVisible(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

//        drawerLayout.openDrawer(Gravity.END);

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
//            readerIntent.putExtra("COMIC_ID", comicId);
//            readerIntent.putExtra("COMIC_NAME", comicName);
//            readerIntent.putExtra("CHAPTER_ID", dataBean.getChapterId());
//            readerIntent.putExtra("CHAPTER_UPDATE_TIME", chapterUpdateTime);
//            readerIntent.putExtra("CHAPTER_LONG_TITLE", chapterLongTitle);
            comicId = bundle.getInt("COMIC_ID", 0);
            comicName = bundle.getString("COMIC_NAME", "COMIC_NAME");
            chapterId = bundle.getInt("CHAPTER_ID", 0);
            chapterUpdateTime = bundle.getLong("CHAPTER_UPDATE_TIME", 0);
            chapterLongTitle = bundle.getString("CHAPTER_LONG_TITLE", "CHAPTER_LONG_TITLE");
            chapterUpdateString = DateUtil.getTimeString(chapterUpdateTime);

        }
        urlString = Config.getChapterImagesUrl(comicId, chapterId);
        Log.d(TAG, "initData: " + urlString);

        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(outSize);
        screenWidth = outSize.x;
        screenHeight = outSize.y;

//        Log.d(TAG, "initData: " + screenWidth + " " + screenHeight);

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            readerImagesItem = gson.fromJson(jsonString, ReaderImagesItem.class);
                            totalPageNum = readerImagesItem.getPicnum();
                            curPage = 0;
                            chapterName = readerImagesItem.getTitle();
                            tvCurPage.setText(String.valueOf(curPage + 1));
                            tvMaxPage.setText(String.valueOf(totalPageNum));
                            sbReader.setMax(totalPageNum - 1);
                            tvTitle.setText(chapterName);
                            Log.d(TAG, "handleMessage: " + chapterName);
                            setBottomInfos();
                            initRecyclerView();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            Snackbar.make(flRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        JsonUtil.fetchJsonData(mHandler, urlString);

        String viewPointUrl = Config.getAllViewsUrl(comicId, chapterId);
        mViewPointHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String viewPointJson = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<ViewPointBean>>() {
                            }.getType();
                            ArrayList<ViewPointBean> dataList = gson.fromJson(viewPointJson, type);
                            rvViewPointsList = findViewById(R.id.rv_view_points_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(ComicReaderActivity.this);
                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                            ViewPointListAdapter adapter = new ViewPointListAdapter(ComicReaderActivity.this, dataList);
                            rvViewPointsList.setLayoutManager(layoutManager);
                            rvViewPointsList.setAdapter(adapter);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            Snackbar.make(flRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        Snackbar.make(flRootView, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        JsonUtil.fetchJsonData(mViewPointHandler, viewPointUrl);

        fetchTranslatorName();
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new ReaderListAdapter(this, readerImagesItem);
        rvReaderList.setLayoutManager(layoutManager);
        rvReaderList.setAdapter(listAdapter);
        rvReaderList.addOnScrollListener(mOnScrollListener);
        if (totalPageNum == 0) {
            Snackbar.make(rvReaderList, "This chapter is empty!", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setBottomInfos() {
        tvBottomChapter.setText(chapterName);
        tvBottomPage.setText(getString(R.string.string_reader_bottom_page_text, curPage + 1, totalPageNum));
        tvBottomNetwork.setText(NetworkUtil.getNetworkType(this));
        tvBottomTime.setText(DateUtil.getCurrentTimeString());
    }

    private boolean isSlideToBottom(RecyclerView rvArg) {
        return rvArg != null &&
                rvArg.computeVerticalScrollExtent() + rvArg.computeVerticalScrollOffset() >=
                        rvArg.computeVerticalScrollRange();
    }

    private void setActionsLayoutVisible(boolean hide) {
        if (hide) {
            clActionsLayout.setVisibility(View.GONE);
            ScreenUtil.setFullScreen(this);
        } else {
            ScreenUtil.cancelFullScreen(this);
            clActionsLayout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;
                if (((x > centerX - 150.0f) && (x < centerX + 150.0f)) && ((y > centerY - 400.0f) && (y < centerY + 0.0f))) {
                    if (isFullScreen) {
                        isFullScreen = false;
                        setActionsLayoutVisible(false);
                    } else {
                        isFullScreen = true;
                        setActionsLayoutVisible(true);
                    }
                    return true;
                }
                break;

            default:
                break;
        }
        return false;
    }

    private void fetchTranslatorName() {
        final TextView tvTranslator = findViewById(R.id.tv_drawer_translator);
        tvTranslator.setText(R.string.string_translator_loading_text);
        String translatorUrl = Config.getTranslatorUrl(comicId, chapterId);
//        Log.d(TAG, "fetchTranslatorName: " + translatorUrl);
        Handler translatorHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String translatorHtmlString = (String) msg.obj;
//                Log.d(TAG, "handleMessage: " + translatorHtmlString);
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        int startPos = translatorHtmlString.indexOf("\"translator\":") + 14;
                        int endPos = translatorHtmlString.indexOf("\",\"link\":");
                        String translatorName = translatorHtmlString.substring(startPos, endPos);
//                        Log.d(TAG, "handleMessage: translatorName: " + translatorName);
                        tvTranslator.setText(translatorName);
                        break;

                    default:
                        break;
                }
            }
        };
        JsonUtil.fetchJsonData(translatorHandler, translatorUrl);
    }
}
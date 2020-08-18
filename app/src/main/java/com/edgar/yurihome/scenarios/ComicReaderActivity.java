package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.ReaderListAdapter;
import com.edgar.yurihome.beans.ReaderImagesItem;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.ScreenUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ComicReaderActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "=================" + ComicReaderActivity.class.getSimpleName();

    private ImageButton btnBack;
    private SeekBar sbReader;
    private TextView tvCurPage, tvMaxPage, tvTitle;
    private Button btnMore;
    private FrameLayout flRootView;
    private ConstraintLayout clActionsLayout;

    private RecyclerView rvReaderList;
    private ReaderListAdapter listAdapter;
    private ReaderImagesItem readerImagesItem;

    private String urlString;
    private int comicId, chapterId;
    private boolean isFullScreen = true;
    private int curPage = 0;

    private Handler mHandler;

    private int screenWidth, screenHeight;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ib_reader_back:
                    finish();
                    break;

                case R.id.btn_reader_more_actions:
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
            Log.d(TAG, "onProgressChanged: max: " + seekBar.getMax() + " cur: " + i);
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
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtil.setNoTitleBar(ComicReaderActivity.this);
        setContentView(R.layout.activity_comic_reader);

        initView();
        initData();

    }

    private void initView() {
        flRootView = findViewById(R.id.reader_root_view);
        clActionsLayout = findViewById(R.id.reader_actions_layout);
        btnBack = findViewById(R.id.ib_reader_back);
        sbReader = findViewById(R.id.sb_reader_progress);
        tvCurPage = findViewById(R.id.tv_seekbar_cur_page);
        tvMaxPage = findViewById(R.id.tv_seekbar_max_page);
        tvTitle = findViewById(R.id.tv_reader_title);
        btnMore = findViewById(R.id.btn_reader_more_actions);
        btnBack.setOnClickListener(mOnClickListener);
        btnMore.setOnClickListener(mOnClickListener);
        sbReader.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        rvReaderList = findViewById(R.id.rv_reader_list);
        rvReaderList.setOnTouchListener(this);

        setActionsLayoutVisible(true);

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
                            tvCurPage.setText(String.valueOf(curPage + 1));
                            tvMaxPage.setText(String.valueOf(readerImagesItem.getPicnum()));
                            sbReader.setMax(readerImagesItem.getPicnum() - 1);
                            tvTitle.setText(readerImagesItem.getTitle());
                            Log.d(TAG, "handleMessage: " + readerImagesItem.getTitle());
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

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        comicId = bundle.getInt("COMIC_ID", 0);
        chapterId = bundle.getInt("CHAPTER_ID", 0);
        urlString = Config.getChapterImagesUrl(comicId, chapterId);

        Point outSize = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(outSize);
        screenWidth = outSize.x;
        screenHeight = outSize.y;

        Log.d(TAG, "initData: " + screenWidth + " " + screenHeight);

        JsonUtil.fetchJsonData(mHandler, urlString);
    }

    private void initRecyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        listAdapter = new ReaderListAdapter(this, readerImagesItem);
        rvReaderList.setLayoutManager(layoutManager);
        rvReaderList.setAdapter(listAdapter);
        rvReaderList.addOnScrollListener(mOnScrollListener);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int centerX = screenWidth / 2;
                int centerY = screenHeight / 2;
                if (((x > centerX - 150.0f) && (x < centerX + 150.0f)) && ((y > centerY - 150.0f) && (y < centerY + 150.0f))) {
                    String temp = "X: " + x + " Y: " + y + " centerX: " + centerX + " centerY: " + centerY;
                    if (isFullScreen) {
                        isFullScreen = false;
                        setActionsLayoutVisible(true);
                    } else {
                        isFullScreen = true;
                        setActionsLayoutVisible(false);
                    }
                    return true;
                }
                break;

            default:
                break;
        }
        return false;
    }
}
package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.edgar.yurihome.GlideApp;
import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.DetailsViewPagerAdapter;
import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.GlideUtil;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SpannableStringUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ComicDetailsActivity extends AppCompatActivity {

    private static final String TAG = "======================" + ComicDetailsActivity.class.getSimpleName();

    private String comicDetailsUrl, coverUrl, comicTitle, comicAuthors, comicStatus, comicTypes;
    private long lastUpdateTime;
    private String[] tabTitles;

    private TextView tvComicTags, tvLastUpdateChapter, tvComicAuthors, tvComicStatus, tvLastUpdateTime;
    private ImageView ivComicCover;
    private MaterialToolbar mToolbar;
    private CoordinatorLayout detailsRootView;
    private ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private DetailsViewPagerAdapter mPagerAdapter;

    private Handler fetchDetailsHandler;

    private ComicDetailsBean comicDetailsBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        initView();
        fetchComicDetails();

    }

    private void fetchComicDetails() {
        fetchDetailsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            comicDetailsBean = gson.fromJson(jsonString, ComicDetailsBean.class);
//                            mPagerAdapter.setData(comicDetailsBean);
                            mPagerAdapter = new DetailsViewPagerAdapter(ComicDetailsActivity.this, comicDetailsBean);
                            initViewPager(mPagerAdapter);
                            String lastUpdateChapterName = comicDetailsBean.getLastUpdateChapterName();
                            tvLastUpdateChapter.setText(getString(R.string.string_comic_details_last_chapter_text, lastUpdateChapterName));
                            SpannableStringUtil.setupSpannableString(getString(R.string.string_comic_details_authors_text, comicAuthors), comicDetailsBean, tvComicAuthors, 0);
                            SpannableStringUtil.setupSpannableString(getString(R.string.string_comic_details_tags_text, comicTypes), comicDetailsBean, tvComicTags, 1);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            Snackbar.make(detailsRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(detailsRootView, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(detailsRootView, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        break;
                }

            }
        };

        JsonUtil.fetchJsonData(fetchDetailsHandler, comicDetailsUrl);

    }


    private void initView() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        comicDetailsUrl = bundle.getString("COMIC_DETAILS_URL", "");
        coverUrl = bundle.getString("COMIC_COVER_URL", "");
        comicTitle = bundle.getString("COMIC_TITLE", "");
        comicAuthors = bundle.getString("COMIC_AUTHORS", "");
        comicStatus = bundle.getString("COMIC_STATUS", "");
        comicTypes = bundle.getString("COMIC_TYPES", "");
        lastUpdateTime = bundle.getLong("COMIC_LAST_UPDATE_TIME", 0);
        Log.d(TAG, "onCreate: " + lastUpdateTime);
        Log.d(TAG, "initView: " + comicTitle);

        detailsRootView = findViewById(R.id.details_root_view);
        tvComicTags = findViewById(R.id.tv_comic_details_tags);
        tvLastUpdateChapter = findViewById(R.id.tv_details_last_chapter);
        tvComicStatus = findViewById(R.id.tv_comic_details_status);
        tvLastUpdateTime = findViewById(R.id.tv_details_last_update_time);
        tvComicAuthors = findViewById(R.id.tv_comic_details_authors);
        ivComicCover = findViewById(R.id.iv_comic_details_cover);
        mToolbar = findViewById(R.id.comic_details_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(comicTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager = findViewById(R.id.details_view_pager);
        mTabLayout = findViewById(R.id.details_tab_layout);

        tvComicTags.setText(getString(R.string.string_comic_details_tags_text, comicTypes));
        tvComicStatus.setText(getString(R.string.string_comic_details_status_text, comicStatus));
//        tvComicAuthors.setText(getString(R.string.string_comic_details_authors_text, comicAuthors));
        tvLastUpdateTime.setText(DateUtil.getTimeString(lastUpdateTime));

        GlideApp.with(ivComicCover)
                .load(GlideUtil.getGlideUrl(coverUrl))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(ivComicCover);

//        mPagerAdapter = new DetailsViewPagerAdapter(ComicDetailsActivity.this);
//        initViewPager(mPagerAdapter);


    }

    private void initViewPager(DetailsViewPagerAdapter pagerAdapter) {
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(mTabLayout, mViewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(R.string.string_related_comics_text);
                        break;

                    case 1:
                        tab.setText(R.string.string_chapter_infos_text);
                        break;

                    default:
                        tab.setText(R.string.string_comic_comments_text);
                        break;
                }
            }
        }).attach();
        mViewPager.setCurrentItem(1, false);
    }

}
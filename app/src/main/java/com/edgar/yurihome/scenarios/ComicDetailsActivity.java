package com.edgar.yurihome.scenarios;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.DetailsViewPagerAdapter;
import com.edgar.yurihome.beans.BrowseHistoryBean;
import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.GlideUtil;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SharedPreferenceUtil;
import com.edgar.yurihome.utils.SpannableStringUtil;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ComicDetailsActivity extends AppCompatActivity {

    private static final String TAG = "======================" + ComicDetailsActivity.class.getSimpleName();

    private String comicDetailsUrl, coverUrl, comicTitle;

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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initView();
        fetchComicDetails();

    }

//    @Override
////    protected void onNewIntent(Intent intent) {
////        super.onNewIntent(intent);
////        if (intent != null) {
////            setIntent(intent);
////            initView();
////            fetchComicDetails();
////        }
////    }

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
                            mPagerAdapter = new DetailsViewPagerAdapter(ComicDetailsActivity.this, comicDetailsBean);
                            initViewPager(mPagerAdapter);
                            initTextViews();

                        } catch (JsonSyntaxException | NullPointerException e) {
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
                        Snackbar.make(detailsRootView, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;
                }

            }
        };

        JsonUtil.fetchJsonData(fetchDetailsHandler, comicDetailsUrl);

    }


    //comic url, cover url, comic title
    private void initView() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            comicDetailsUrl = bundle.getString("COMIC_DETAILS_URL", "");
            coverUrl = bundle.getString("COMIC_COVER_URL", "");
            comicTitle = bundle.getString("COMIC_TITLE", "");

            BrowseHistoryBean historyBean = new BrowseHistoryBean(coverUrl, comicDetailsUrl, comicTitle);
            String historyJson = SharedPreferenceUtil.getBrowseHistoryJson(this);
            String storeJson = "";
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<BrowseHistoryBean>>() {
            }.getType();

            if (historyJson.isEmpty()) {
                ArrayList<BrowseHistoryBean> historyList = new ArrayList<>();
                historyList.add(historyBean);
                storeJson = gson.toJson(historyList, type);
            } else {
                try {
                    ArrayList<BrowseHistoryBean> storeList = gson.fromJson(historyJson, type);
                    if (!storeList.remove(historyBean)) {
                        if (storeList.size() >= 15) {
                            storeList.remove(14);
                        }
                    }
                    storeList.add(0, historyBean);
                    storeJson = gson.toJson(storeList, type);
                } catch (JsonSyntaxException e) {
                    ArrayList<BrowseHistoryBean> historyList = new ArrayList<>();
                    historyList.add(historyBean);
                    storeJson = gson.toJson(historyList, type);
                    e.printStackTrace();
                }
            }
            SharedPreferenceUtil.storeBrowseHistoryJson(this, storeJson);


        }
//        comicAuthors = bundle.getString("COMIC_AUTHORS", "");
//        comicStatus = bundle.getString("COMIC_STATUS", "");
//        comicTypes = bundle.getString("COMIC_TYPES", "");
//        lastUpdateTime = bundle.getLong("COMIC_LAST_UPDATE_TIME", 0);

        detailsRootView = findViewById(R.id.details_root_view);
        tvComicTags = findViewById(R.id.tv_comic_details_tags);
        tvLastUpdateChapter = findViewById(R.id.tv_details_last_chapter);
        tvComicStatus = findViewById(R.id.tv_comic_details_status);
        tvLastUpdateTime = findViewById(R.id.tv_details_last_update_time);
        tvComicAuthors = findViewById(R.id.tv_comic_details_authors);
        ivComicCover = findViewById(R.id.iv_comic_details_cover);
        mToolbar = findViewById(R.id.comic_details_toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(comicTitle);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager = findViewById(R.id.details_view_pager);
        mTabLayout = findViewById(R.id.details_tab_layout);

        tvComicTags.setText(getString(R.string.string_comic_details_tags_text, "Loading..."));
        tvLastUpdateChapter.setText(getString(R.string.string_comic_details_last_chapter_text, "Loading..."));
        tvComicStatus.setText(getString(R.string.string_comic_details_status_text, "Loading..."));
        tvLastUpdateTime.setText(getString(R.string.string_comic_details_last_update_time_text, "Loading..."));
        tvComicAuthors.setText(getString(R.string.string_comic_details_authors_text, "Loading..."));
//        tvLastUpdateTime.setText(DateUtil.getTimeString(lastUpdateTime));
        GlideUtil.loadImageWithUrl(ivComicCover, coverUrl);

    }

    private void initTextViews() {
        StringBuilder tagTypeStringBuilder = new StringBuilder();
        ArrayList<ComicDetailsBean.TypesBean> typesBeans = new ArrayList<>(comicDetailsBean.getTypes());
        for (ComicDetailsBean.TypesBean typesBean : typesBeans) {
            tagTypeStringBuilder.append(typesBean.getTagName());
            tagTypeStringBuilder.append("/");
        }
        String tagTypeText = tagTypeStringBuilder.toString();
        tagTypeText = tagTypeText.substring(0, tagTypeText.length() - 1);
        tvComicTags.setText(getString(R.string.string_comic_details_tags_text, tagTypeText));

        SpannableStringUtil.setLastChapterSpannable(tvLastUpdateChapter, comicDetailsBean);

        String comicStatusText = comicDetailsBean.getStatus().get(0).getTagName();
        tvComicStatus.setText(getString(R.string.string_comic_details_status_text, comicStatusText));

        long lastUpdateTime = comicDetailsBean.getLastUpdateTime();
        tvLastUpdateTime.setText(DateUtil.getTimeString(lastUpdateTime));

        StringBuilder authorsStringBuilder = new StringBuilder();
        ArrayList<ComicDetailsBean.AuthorsBean> authorsBeans = new ArrayList<>(comicDetailsBean.getAuthors());
        for (ComicDetailsBean.AuthorsBean authorsBean : authorsBeans) {
            authorsStringBuilder.append(authorsBean.getTagName());
            authorsStringBuilder.append("/");
        }
        String authorsText = authorsStringBuilder.toString();
        authorsText = authorsText.substring(0, authorsText.length() - 1);
        tvComicAuthors.setText(getString(R.string.string_comic_details_tags_text, authorsText));

        SpannableStringUtil.setAuthorSpanString(this, getString(R.string.string_comic_details_authors_text, authorsText), authorsBeans, tvComicAuthors);
        SpannableStringUtil.setTagSpanString(getString(R.string.string_comic_details_tags_text, tagTypeText), typesBeans, tvComicTags);

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
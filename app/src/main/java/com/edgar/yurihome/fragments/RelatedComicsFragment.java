package com.edgar.yurihome.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.RelatedComicListAdapter;
import com.edgar.yurihome.beans.RelatedComicBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.scenarios.ComicDetailsActivity;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonDataUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class RelatedComicsFragment extends Fragment {

    private static final String TAG = "=================" + RelatedComicsFragment.class.getSimpleName();

    private static final String ARG_COMIC_ID = "ARG_COMIC_ID";

    private int comicId;

    private LinearLayout llContainer;
    private Handler mHandler;
    private JsonDataUtil<RelatedComicBean> relatedComicJsonDataUtil = new JsonDataUtil<>(RelatedComicBean.class);

    private Context mContext;

    public RelatedComicsFragment() {
        // Required empty public constructor
    }

    public static RelatedComicsFragment newInstance(int comicId) {
        RelatedComicsFragment fragment = new RelatedComicsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMIC_ID, comicId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comicId = getArguments().getInt(ARG_COMIC_ID, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_related_comics, container, false);
        mContext = getContext();
        llContainer = view.findViewById(R.id.ll_related_comics_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (!isAdded() || isDetached()) {
                    return;
                }
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        RelatedComicBean relatedComicBean = relatedComicJsonDataUtil.getData();
                        ArrayList<RelatedComicBean.AuthorComicsBean> authorComicsBeans = new ArrayList<>(relatedComicBean.getAuthorComics());
                        for (RelatedComicBean.AuthorComicsBean authorComicsBean : authorComicsBeans) {
                            final ArrayList<RelatedComicBean.AuthorComicsBean.AuthorComicDataBean> authorComicList = new ArrayList<>(authorComicsBean.getData());
                            View authorView = LayoutInflater.from(mContext).inflate(R.layout.layout_related_author_comics, null, false);
                            llContainer.addView(authorView);
                            TextView authorTitle = authorView.findViewById(R.id.tv_related_comic_author_title);
                            authorTitle.setText(getString(R.string.string_related_comic_author_title_text, authorComicsBean.getAuthorName()));
                            RelatedComicListAdapter adapter = new RelatedComicListAdapter(mContext, authorComicList, authorComicsBean.getAuthorName());
                            RecyclerView recyclerView = authorView.findViewById(R.id.rv_related_theme_comic_list);
                            GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter.setOnComicItemClickListener(new OnListItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    RelatedComicBean.AuthorComicsBean.AuthorComicDataBean authorComicData = authorComicList.get(position);
                                    if (authorComicData == null || authorComicData.getId() == comicId)
                                        return;
                                    Intent intent = new Intent(getActivity(), ComicDetailsActivity.class);
                                    intent.putExtra("COMIC_DETAILS_URL", Config.getComicDetailsUrl(authorComicData.getId()));
                                    intent.putExtra("COMIC_COVER_URL", authorComicData.getCover());
                                    intent.putExtra("COMIC_TITLE", authorComicData.getName());
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        }

                        final ArrayList<RelatedComicBean.ThemeComicsBean> themeComicList = new ArrayList<>(relatedComicBean.getThemeComics());
                        View themeView = LayoutInflater.from(mContext).inflate(R.layout.layout_related_theme_comics, null, false);
                        llContainer.addView(themeView);
                        RecyclerView recyclerView = themeView.findViewById(R.id.rv_related_theme_comic_list);
                        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 3);
                        RelatedComicListAdapter adapter = new RelatedComicListAdapter(mContext, themeComicList);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter.setOnComicItemClickListener(new OnListItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                RelatedComicBean.ThemeComicsBean themeComicsBean = themeComicList.get(position);
                                if (themeComicsBean == null || themeComicsBean.getId() == comicId)
                                    return;
                                Intent intent = new Intent(getActivity(), ComicDetailsActivity.class);
                                intent.putExtra("COMIC_DETAILS_URL", Config.getComicDetailsUrl(themeComicsBean.getId()));
                                intent.putExtra("COMIC_COVER_URL", themeComicsBean.getCover());
                                intent.putExtra("COMIC_TITLE", themeComicsBean.getName());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(adapter);

                        break;

                    case HttpUtil.REQUEST_JSON_FAILED:
                        Snackbar.make(llContainer, HttpUtil.MESSAGE_NETWORK_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    case HttpUtil.PARSE_JSON_DATA_ERROR:
                        Snackbar.make(llContainer, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                    default:
                        Snackbar.make(llContainer, HttpUtil.MESSAGE_UNKNOWN_ERROR, Snackbar.LENGTH_SHORT).show();
                        break;

                }


            }
        };

        String urlString = Config.getRelatedComicsUrl(comicId);
        relatedComicJsonDataUtil.fetchJsonData(mHandler, urlString);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
    }
}
package com.edgar.yurihome.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.CommentImagesListAdapter;
import com.edgar.yurihome.beans.TopCommentBean;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.GlideUtil;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SpannableStringUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ComicCommentsFragment extends Fragment {

    private static final String ARG_COMIC_ID = "ARG_COMIC_ID";

    private ImageView ivTopCommentAvatar, ivTopCommentGender;
    private TextView tvTopCommentUserName, tvTopCommentContent, tvTopCommentCreateTime;
    private RecyclerView rvTopCommentImagesList;
    private FrameLayout flRootLayout;
    private MaterialCardView topCommentLayout;
    private CommentImagesListAdapter topListAdapter;

    private TopCommentBean topCommentBean;
    private String topCommentUrl;
    private Handler topCommentHandler;

    // TODO: Rename and change types of parameters
    private int comicId;

    public ComicCommentsFragment() {
        // Required empty public constructor
    }

    public static ComicCommentsFragment newInstance(int param1) {
        ComicCommentsFragment fragment = new ComicCommentsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COMIC_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            comicId = getArguments().getInt(ARG_COMIC_ID);
            topCommentUrl = Config.getTopCommentUrl(comicId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comic_comments, container, false);

        ivTopCommentAvatar = view.findViewById(R.id.iv_top_comment_user_avatar);
        ivTopCommentGender = view.findViewById(R.id.iv_top_comment_user_gender);
        tvTopCommentContent = view.findViewById(R.id.tv_top_comment_content);
        tvTopCommentCreateTime = view.findViewById(R.id.tv_top_comment_create_time);
        tvTopCommentUserName = view.findViewById(R.id.tv_top_comment_user_nickname);
        rvTopCommentImagesList = view.findViewById(R.id.rv_top_comment_images_list);
        flRootLayout = view.findViewById(R.id.fl_comment_frag_root_layout);
        topCommentLayout = view.findViewById(R.id.top_comic_comment_layout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topCommentHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            Gson gson = new Gson();
                            topCommentBean = gson.fromJson(jsonString, TopCommentBean.class);
                            topListAdapter = new CommentImagesListAdapter(getContext(), topCommentBean);
                            initTopComment();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            topCommentLayout.setVisibility(View.GONE);
//                            Snackbar.make(flRootLayout, HttpUtil.MESSAGE_JSON_ERROR, Snackbar.LENGTH_SHORT).show();
                        }
                        break;

                    default:
                        topCommentLayout.setVisibility(View.GONE);
                        break;
                }
            }
        };

        JsonUtil.fetchJsonData(topCommentHandler, topCommentUrl);

    }

    private void initTopComment() {
        if (topCommentBean == null) return;
        topCommentLayout.setVisibility(View.VISIBLE);
        GlideUtil.loadCircularImageWithUrl(ivTopCommentAvatar, topCommentBean.getAvatarUrl());
        tvTopCommentUserName.setText(topCommentBean.getNickname());
        ivTopCommentGender.setImageResource((topCommentBean.getSex() == 1) ? R.drawable.ic_gender_male_100 : R.drawable.ic_gender_female_100);
        SpannableStringUtil.addReadMore(topCommentBean.getContent(), tvTopCommentContent);
        tvTopCommentCreateTime.setText(DateUtil.getTimeString(topCommentBean.getCreateTime()));

        if (topCommentBean.getUploadImages().length() == 0) {
            rvTopCommentImagesList.setVisibility(View.GONE);
            return;
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        rvTopCommentImagesList.setLayoutManager(layoutManager);
        rvTopCommentImagesList.setAdapter(topListAdapter);
    }
}
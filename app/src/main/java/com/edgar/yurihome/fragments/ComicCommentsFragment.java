package com.edgar.yurihome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.CommentImagesListAdapter;
import com.edgar.yurihome.beans.NormalCommentsBean;
import com.edgar.yurihome.beans.TopCommentBean;
import com.edgar.yurihome.scenarios.AllCommentsActivity;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.GlideUtil;
import com.edgar.yurihome.utils.HttpUtil;
import com.edgar.yurihome.utils.JsonUtil;
import com.edgar.yurihome.utils.SpannableStringUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

public class ComicCommentsFragment extends Fragment {

    private static final String TAG = "===================" + ComicCommentsFragment.class.getSimpleName();

    private static final String ARG_COMIC_ID = "ARG_COMIC_ID";
    private static final int MAX_LIMIT = 10;

    private ImageView ivTopCommentAvatar, ivTopCommentGender;
    private TextView tvTopCommentUserName, tvTopCommentContent, tvTopCommentCreateTime;
    private RecyclerView rvTopCommentImagesList;
    private LinearLayout llRootLayout;
    private MaterialCardView topCommentLayout;
    private CommentImagesListAdapter topListAdapter;

    private TopCommentBean topCommentBean;
    private String topCommentUrl, latestCommentsUrl;
    private Handler topCommentHandler, latestCommentsHandler;
    private ArrayList<NormalCommentsBean.CommentsBean> latestCommentsList = new ArrayList<>();
    private NormalCommentsBean latestCommentsBean;

    private Button btnViewAll;

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
            latestCommentsUrl = Config.getLatestCommentsUrl(comicId, 0, MAX_LIMIT);
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
        llRootLayout = view.findViewById(R.id.ll_comment_frag_root_layout);
        topCommentLayout = view.findViewById(R.id.top_comic_comment_layout);
        btnViewAll = view.findViewById(R.id.btn_view_all_comments);
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allCommentsIntent = new Intent(getContext(), AllCommentsActivity.class);
                allCommentsIntent.putExtra("COMIC_ID", comicId);
                startActivity(allCommentsIntent);
            }
        });

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
                        }
                        break;

                    default:
                        topCommentLayout.setVisibility(View.GONE);
                        break;
                }
            }
        };

        latestCommentsHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String jsonString = (String) msg.obj;
                switch (msg.what) {
                    case HttpUtil.REQUEST_JSON_SUCCESS:
                        try {
                            jsonString = jsonString.replace("\"comments\":{", "\"comments\":[");
                            jsonString = jsonString.replace("},\"total\"", "],\"total\"");
                            jsonString = jsonString.replaceAll("\"[0-9]*\":", "");
                            Log.d(TAG, "handleMessage: " + jsonString);
                            Gson gson = new Gson();
                            latestCommentsBean = gson.fromJson(jsonString, NormalCommentsBean.class);
                            initLatestComments();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        break;

                    default:
                        break;
                }
            }
        };

        JsonUtil.fetchJsonData(topCommentHandler, topCommentUrl);
        JsonUtil.fetchJsonData(latestCommentsHandler, latestCommentsUrl);

    }

    private void initLatestComments() {
        if (latestCommentsBean == null || latestCommentsBean.getComments() == null
                || latestCommentsBean.getComments().isEmpty() || llRootLayout == null) return;
        latestCommentsList = new ArrayList<>(latestCommentsBean.getComments());
        ArrayList<String> commentIdList = new ArrayList<>(latestCommentsBean.getCommentIds());

        int idIndex = 0;
        for (String commentIdString : commentIdList) {
            if (idIndex >= MAX_LIMIT) break;
            String[] ids = commentIdString.split(",");
            NormalCommentsBean.CommentsBean commentsBean = findCommentById(ids[0], latestCommentsList);
            if (commentsBean == null) continue;

            View commentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_comment_normal_list_item, null, false);
            ImageView ivAvatar = commentView.findViewById(R.id.iv_normal_comment_avatar);
            ImageView ivGender = commentView.findViewById(R.id.iv_normal_comment_gender);
            TextView tvNickname = commentView.findViewById(R.id.tv_normal_comment_nickname);
            TextView tvContent = commentView.findViewById(R.id.tv_normal_comment_content);
            TextView tvCreateTime = commentView.findViewById(R.id.tv_normal_comment_create_time);
            TextView tvLikeAmount = commentView.findViewById(R.id.tv_normal_comment_like_amount);

            GlideUtil.loadImageWithUrl(ivAvatar, commentsBean.getAvatarUrl());
            ivGender.setImageResource(commentsBean.getSex() == 1 ? R.drawable.ic_gender_male_100 : R.drawable.ic_gender_female_100);
            tvNickname.setText(commentsBean.getNickname());
            tvContent.setText(commentsBean.getContent());
//            SpannableStringUtil.addReadMore(commentsBean.getContent(), tvContent);
            tvCreateTime.setText(DateUtil.getTimeString(commentsBean.getCreateTime()));
            tvLikeAmount.setText(String.valueOf(commentsBean.getLikeAmount()));

            LinearLayout llChildCommentsContainer = commentView.findViewById(R.id.ll_normal_comment_child_container);
            llChildCommentsContainer.removeAllViews();
            if (ids.length >= 2) {
                int startPos = Math.min(ids.length, 4) - 1;
                for (int i = startPos; i > 0; i--) {
                    NormalCommentsBean.CommentsBean childComment = findCommentById(ids[i], latestCommentsList);
                    if (childComment == null) continue;

                    View childCommentView = LayoutInflater.from(getContext()).inflate(R.layout.layout_comment_child_list_item, null, false);
                    ImageView ivChildGender = childCommentView.findViewById(R.id.iv_child_comment_gender);
                    TextView tvChildNickname = childCommentView.findViewById(R.id.tv_child_comment_nickname);
                    TextView tvChildFloor = childCommentView.findViewById(R.id.tv_child_comment_floor);
                    TextView tvChildContent = childCommentView.findViewById(R.id.tv_child_comment_content);
                    ivChildGender.setImageResource(childComment.getSex() == 1 ? R.drawable.ic_gender_male_100 : R.drawable.ic_gender_female_100);
                    tvChildNickname.setText(childComment.getNickname());
                    tvChildFloor.setText(getString(R.string.string_comment_child_item_floor_text, startPos - i + 1));
//                    tvChildContent.setText(childComment.getContent());
                    SpannableStringUtil.addReadMore(childComment.getContent(), tvChildContent);
                    llChildCommentsContainer.addView(childCommentView);
                }
            }

            llRootLayout.addView(commentView);
            idIndex++;
        }

    }

    private void initTopComment() {
        if (topCommentBean == null) return;
        topCommentLayout.setVisibility(View.VISIBLE);
        GlideUtil.loadImageWithUrl(ivTopCommentAvatar, topCommentBean.getAvatarUrl());
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

    private NormalCommentsBean.CommentsBean findCommentById(String idString, ArrayList<NormalCommentsBean.CommentsBean> list) {
        for (NormalCommentsBean.CommentsBean commentsBean : list) {
            if (String.valueOf(commentsBean.getId()).equals(idString)) {
                return commentsBean;
            }
        }
        return null;
    }

}
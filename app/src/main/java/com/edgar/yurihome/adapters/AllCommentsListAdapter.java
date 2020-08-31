package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.NormalCommentsBean;
import com.edgar.yurihome.utils.DateUtil;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class AllCommentsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "===============" + AllCommentsListAdapter.class.getSimpleName();

    private static final int NORMAL_VIEW_TYPE = 0;
    private static final int FOOTER_VIEW_TYPE = 1;

    private Context mContext;
    private ArrayList<NormalCommentsBean.CommentsBean> normalCommentsList = new ArrayList<>();
    private ArrayList<String> idsList = new ArrayList<>();

    public AllCommentsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == FOOTER_VIEW_TYPE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_item, parent, false);
            return new FooterHolder(view);
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_normal_list_item, parent, false);
        return new AllCommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterHolder) return;

        AllCommentsHolder mHolder = (AllCommentsHolder) holder;
        mHolder.llChildCommentsContainer.removeAllViews();
        String commentId = idsList.get(position);
        String[] ids = commentId.split(",");
        final NormalCommentsBean.CommentsBean commentsBean = findCommentById(ids[0], normalCommentsList);
        if (commentsBean == null) return;

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_from_bottom);
        mHolder.itemView.startAnimation(animation);
        GlideUtil.loadImageWithUrl(mHolder.ivAvatar, commentsBean.getAvatarUrl());
//        mHolder.ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                GlideUtil.loadImageWithUrlNoCache((ImageView) view, commentsBean.getAvatarUrl());
//                return false;
//            }
//        });
        mHolder.ivGender.setImageResource(commentsBean.getSex() == 1 ? R.drawable.ic_gender_male_100 : R.drawable.ic_gender_female_100);
        mHolder.tvNickname.setText(commentsBean.getNickname());
        mHolder.tvContent.setText(commentsBean.getContent());
        mHolder.tvCreateTime.setText(DateUtil.getTimeString(commentsBean.getCreateTime()));
        mHolder.tvLikeAmount.setText(String.valueOf(commentsBean.getLikeAmount()));

        if (ids.length >= 2) {
            final int startPos = Math.min(ids.length, 4) - 1;
            for (int i = startPos; i > 0; i--) {
                NormalCommentsBean.CommentsBean childComment = findCommentById(ids[i], normalCommentsList);
                if (childComment == null) continue;

                View childCommentView = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_child_list_item, null, false);
                ImageView ivChildGender = childCommentView.findViewById(R.id.iv_child_comment_gender);
                TextView tvChildNickname = childCommentView.findViewById(R.id.tv_child_comment_nickname);
                TextView tvChildFloor = childCommentView.findViewById(R.id.tv_child_comment_floor);
                TextView tvChildContent = childCommentView.findViewById(R.id.tv_child_comment_content);
                ivChildGender.setImageResource(childComment.getSex() == 1 ? R.drawable.ic_gender_male_100 : R.drawable.ic_gender_female_100);
                tvChildNickname.setText(childComment.getNickname());
                tvChildFloor.setText(mContext.getString(R.string.string_comment_child_item_floor_text, startPos - i + 1));
                tvChildContent.setText(childComment.getContent());
                mHolder.llChildCommentsContainer.addView(childCommentView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (idsList == null ? 0 : idsList.size());
    }

    @Override
    public int getItemViewType(int position) {
        return (idsList.get(position).equals("FOOTER_VIEW") ? FOOTER_VIEW_TYPE : NORMAL_VIEW_TYPE);
    }

    public void appendItems(ArrayList<String> stringList, ArrayList<NormalCommentsBean.CommentsBean> commentList) {
        int startPos = idsList.size();
        idsList.addAll(stringList);
        for (NormalCommentsBean.CommentsBean item : commentList) {
            if (!normalCommentsList.contains(item)) {
                normalCommentsList.add(item);
            }
        }
        notifyItemRangeInserted(startPos, stringList.size());
    }

    public void setItems(ArrayList<String> stringList, ArrayList<NormalCommentsBean.CommentsBean> commentList) {
        idsList = new ArrayList<>(stringList);
        normalCommentsList = new ArrayList<>(commentList);
        notifyDataSetChanged();
    }

    public void addFooter() {
        idsList.add("FOOTER_VIEW");
        notifyItemInserted(idsList.size() - 1);
    }

    public void removeFooter() {
        if (idsList.isEmpty()) return;
        if (idsList.get(idsList.size() - 1).equals("FOOTER_VIEW")) {
            idsList.remove(idsList.size() - 1);
            notifyItemRemoved(idsList.size());
        }
    }

    private NormalCommentsBean.CommentsBean findCommentById(String idString, ArrayList<NormalCommentsBean.CommentsBean> list) {
        for (NormalCommentsBean.CommentsBean commentsBean : list) {
            if (String.valueOf(commentsBean.getId()).equals(idString)) {
                return commentsBean;
            }
        }
        return null;
    }

    private class AllCommentsHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatar, ivGender;
        TextView tvNickname, tvContent, tvCreateTime, tvLikeAmount;
        LinearLayout llChildCommentsContainer;

        public AllCommentsHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_normal_comment_avatar);
            ivGender = itemView.findViewById(R.id.iv_normal_comment_gender);
            tvNickname = itemView.findViewById(R.id.tv_normal_comment_nickname);
            tvContent = itemView.findViewById(R.id.tv_normal_comment_content);
            tvCreateTime = itemView.findViewById(R.id.tv_normal_comment_create_time);
            tvLikeAmount = itemView.findViewById(R.id.tv_normal_comment_like_amount);
            llChildCommentsContainer = itemView.findViewById(R.id.ll_normal_comment_child_container);
        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}

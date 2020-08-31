package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.TopCommentBean;
import com.edgar.yurihome.utils.Config;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class CommentImagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "====================" + CommentImagesListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private TopCommentBean topCommentBean;

    public CommentImagesListAdapter(Context mContext, TopCommentBean topCommentBean) {
        this.mContext = mContext;
        this.topCommentBean = topCommentBean;
        if (topCommentBean != null) {
            String imageNamesString = topCommentBean.getUploadImages();
            String[] imageNames = imageNamesString.split(",");
            for (String imageName : imageNames) {
                this.imageUrls.add(Config.getCommentBigImageUrl(topCommentBean.getObjId(), imageName));
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_images_list_item, parent, false);
        return new CommentImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommentImageHolder mHolder = (CommentImageHolder) holder;
        String imageUrl = imageUrls.get(position);
        GlideUtil.loadImageWithUrl(mHolder.ivCommentImage, imageUrl);
    }

    @Override
    public int getItemCount() {
        return (imageUrls == null ? 0 : imageUrls.size());
    }

    private class CommentImageHolder extends RecyclerView.ViewHolder {
        private ImageView ivCommentImage;

        public CommentImageHolder(@NonNull View itemView) {
            super(itemView);
            ivCommentImage = itemView.findViewById(R.id.iv_comment_image);
        }
    }
}

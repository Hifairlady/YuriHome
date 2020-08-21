package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.AuthorComicsBean;
import com.edgar.yurihome.interfaces.OnComicListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class AuthorComicsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<AuthorComicsBean.AuthorComicData> dataList = new ArrayList<>();
    private OnComicListItemClickListener itemClickListener;

    public AuthorComicsListAdapter(Context mContext, ArrayList<AuthorComicsBean.AuthorComicData> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_related_comic_list_item, parent, false);
        return new AuthorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AuthorHolder mHolder = (AuthorHolder) holder;
        AuthorComicsBean.AuthorComicData data = dataList.get(position);
        mHolder.tvFinished.setVisibility(data.getStatus().equals("已完结") ? View.VISIBLE : View.GONE);
        mHolder.tvTitle.setText(data.getName());
        GlideUtil.loadImageWithUrl(mHolder.ivCover, data.getCover());
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList == null ? 0 : dataList.size());
    }

    public void setDataList(ArrayList<AuthorComicsBean.AuthorComicData> data) {
        this.dataList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnComicListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class AuthorHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvFinished;
        private ImageView ivCover;

        public AuthorHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_related_comic_item_title);
            tvFinished = itemView.findViewById(R.id.tv_related_comic_item_finished);
            ivCover = itemView.findViewById(R.id.iv_related_comic_item_cover);
        }
    }

}

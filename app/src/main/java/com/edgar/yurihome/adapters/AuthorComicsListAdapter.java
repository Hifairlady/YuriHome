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
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class AuthorComicsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<AuthorComicsBean.AuthorComicData> dataList = new ArrayList<>();
    private OnListItemClickListener itemClickListener;

    public AuthorComicsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_author_comics_list_item, parent, false);
        return new AuthorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final AuthorHolder mHolder = (AuthorHolder) holder;
        AuthorComicsBean.AuthorComicData data = dataList.get(position);
        mHolder.tvFinished.setVisibility(data.getStatus().equals("已完结") ? View.VISIBLE : View.GONE);
        mHolder.tvTitle.setText(data.getName());
        GlideUtil.loadImageWithUrl(mHolder.ivCover, data.getCover());
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(mHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList == null ? 0 : dataList.size());
    }

    public AuthorComicsBean.AuthorComicData getItemAt(int position) {
        if (dataList.isEmpty() || position >= dataList.size() || position < 0) {
            return null;
        }
        return dataList.get(position);
    }

    public void setDataList(ArrayList<AuthorComicsBean.AuthorComicData> data) {
        this.dataList = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class AuthorHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvFinished;
        private ImageView ivCover;

        public AuthorHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_author_comic_item_title);
            tvFinished = itemView.findViewById(R.id.tv_author_comic_item_finished);
            ivCover = itemView.findViewById(R.id.iv_author_comic_item_cover);
        }
    }

}

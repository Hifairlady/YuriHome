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
import com.edgar.yurihome.beans.BrowseHistoryBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class BrowseHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = BrowseHistoryListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<BrowseHistoryBean> dataList = new ArrayList<>();
    private OnListItemClickListener itemClickListener;

    public BrowseHistoryListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_browse_history, parent, false);
        return new BrowseHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final BrowseHistoryHolder mHolder = (BrowseHistoryHolder) holder;
        BrowseHistoryBean historyBean = dataList.get(position);
        mHolder.tvTitle.setText(historyBean.getTitle());
        GlideUtil.loadImageWithUrl(mHolder.ivCover, historyBean.getCoverUrl());
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

    public BrowseHistoryBean getItemAt(int position) {
        if (dataList == null || position < 0 || position >= dataList.size()) return null;
        return dataList.get(position);
    }

    public void setDataList(ArrayList<BrowseHistoryBean> list) {
        if (list == null) {
            this.dataList = new ArrayList<>();
        } else {
            this.dataList = new ArrayList<>(list);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class BrowseHistoryHolder extends RecyclerView.ViewHolder {

        private ImageView ivCover;
        private TextView tvTitle;

        public BrowseHistoryHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_history_item_cover);
            tvTitle = itemView.findViewById(R.id.tv_history_item_title);
        }
    }

}

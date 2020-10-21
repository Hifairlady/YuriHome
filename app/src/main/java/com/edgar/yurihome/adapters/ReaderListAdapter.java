package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ReaderImagesItem;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class ReaderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> pageUrls = new ArrayList<>();
    private int[] loadStatCodes;

    public ReaderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_reader_list, parent, false);
        return new PageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final PageHolder mHolder = (PageHolder) holder;
        GlideUtil.loadReaderImage(mHolder.ivImage, pageUrls.get(position), position, loadStatCodes);
    }

    @Override
    public int getItemCount() {
        return (pageUrls == null ? 0 : pageUrls.size());
    }

    public void setData(ReaderImagesItem readerImagesItem) {
        this.pageUrls = new ArrayList<>(readerImagesItem.getPageUrls());
        this.loadStatCodes = new int[pageUrls.size()];
        notifyDataSetChanged();
    }

    private class PageHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public PageHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_reader_item);
        }
    }

    public int getLoadStatCodeAt(int position) {
        if (pageUrls.isEmpty() || position >= pageUrls.size() || position < 0) {
            return 0;
        }
        return loadStatCodes[position];
    }

    public void setLoadStatCodeAt(int position, int value) {
        if (pageUrls.isEmpty() || position >= pageUrls.size() || position < 0) {
            return;
        }
        loadStatCodes[position] = value;
    }
}

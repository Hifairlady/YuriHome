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

    public ReaderListAdapter(Context mContext, ReaderImagesItem readerImagesItem) {
        this.mContext = mContext;
        this.pageUrls = new ArrayList<>(readerImagesItem.getPageUrls());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reader_list_item, parent, false);
        return new PageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final PageHolder mHolder = (PageHolder) holder;
        GlideUtil.loadImageWithUrlNoCache(mHolder.ivImage, pageUrls.get(position));
        mHolder.ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                GlideUtil.loadImageWithUrlNoCache((ImageView) view, pageUrls.get(mHolder.getAdapterPosition()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (pageUrls == null ? 0 : pageUrls.size());
    }

    private class PageHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public PageHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_reader_item);
        }
    }
}

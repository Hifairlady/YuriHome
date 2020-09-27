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
    private int[] loadCodes;

    public ReaderListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_reader_list_item, parent, false);
        return new PageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final PageHolder mHolder = (PageHolder) holder;
        GlideUtil.loadReaderImage(mHolder.ivImage, pageUrls.get(position), position, loadCodes);
//        if (loadCodes[position] == -1) {
//            GlideUtil.loadImageWithUrlNoCache(mHolder.ivImage, pageUrls.get(position));
////            loadCodes[position] = 0;
//        } else {
//            String url = pageUrls.get(position);
////            if (position % 5 == 1) {
////                url = "test";
////            }
//            GlideApp.with(mHolder.ivImage)
//                    .load(GlideUtil.getGlideUrl(url))
//                    .addListener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            loadCodes[position] = -1;
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            return false;
//                        }
//                    })
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.image_loading)
//                    .error(R.drawable.image_error)
//                    .into(mHolder.ivImage);
//        }
    }

    @Override
    public int getItemCount() {
        return (pageUrls == null ? 0 : pageUrls.size());
    }

    public void setData(ReaderImagesItem readerImagesItem) {
        this.pageUrls = new ArrayList<>(readerImagesItem.getPageUrls());
        this.loadCodes = new int[pageUrls.size()];
        notifyDataSetChanged();
    }

    private class PageHolder extends RecyclerView.ViewHolder {

        private ImageView ivImage;

        public PageHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_reader_item);
        }
    }
}

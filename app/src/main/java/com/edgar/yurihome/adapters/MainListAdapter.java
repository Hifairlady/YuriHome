package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ComicItem;
import com.edgar.yurihome.interfaces.OnComicListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ComicItem> comicItems = new ArrayList<>();
    private OnComicListItemClickListener clickListener;

    public MainListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == ComicItem.ITEM_TYPE_FOOTER) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_footer_item, parent, false);
            return new FooterHolder(itemView);
        }
        itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_main_list_item, parent, false);
        return new NormalHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FooterHolder) return;
        ComicItem item = comicItems.get(position);
        final NormalHolder normalHolder = (NormalHolder) holder;
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_slide_right);
        normalHolder.itemView.startAnimation(animation);
        normalHolder.tvAuthor.setText(item.getAuthors());
        normalHolder.tvTitle.setText(item.getTitle());
        if (item.getStatus().equals(mContext.getString(R.string.const_string_status_finished))) {
            normalHolder.tvFinished.setVisibility(View.VISIBLE);
        } else {
            normalHolder.tvFinished.setVisibility(View.GONE);
        }
        normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(normalHolder.getAdapterPosition());
                }
            }
        });
        GlideUtil.loadImageWithUrl(normalHolder.ivCover, item.getCover());
    }

    @Override
    public int getItemCount() {
        return (comicItems == null ? 0 : comicItems.size());
    }

    @Override
    public long getItemId(int position) {
        return (comicItems.isEmpty() ? 0 : comicItems.get(position).getId());
    }

    @Override
    public int getItemViewType(int position) {
        return (comicItems.isEmpty() ? 0 : comicItems.get(position).getItemType());
    }

    public ComicItem getComicItemAt(int position) {
        if (comicItems.isEmpty() || position >= comicItems.size() || position < 0) {
            return null;
        }
        return comicItems.get(position);
    }

    public void appendComicItem(ComicItem item) {
        comicItems.add(item);
        notifyItemInserted(comicItems.size() - 1);
    }

    public void appendComicItems(ArrayList<ComicItem> items) {
        int startPos = comicItems.size();
        comicItems.addAll(items);
        notifyItemRangeInserted(startPos, items.size());
    }

    public void setComicItems(ArrayList<ComicItem> items) {
        comicItems = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void removeFooterItem() {
        if (!comicItems.isEmpty() && comicItems.get(comicItems.size() - 1).getItemType() == ComicItem.ITEM_TYPE_FOOTER) {
            comicItems.remove(comicItems.size() - 1);
            notifyItemRemoved(comicItems.size());
        }
    }

    public void addFooterItem() {
        ComicItem item = new ComicItem(ComicItem.ITEM_TYPE_FOOTER);
        appendComicItem(item);
    }


    private class NormalHolder extends RecyclerView.ViewHolder {

        private ImageView ivCover;
        private TextView tvAuthor, tvTitle, tvFinished;

        public NormalHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvAuthor = itemView.findViewById(R.id.tv_comic_author);
            tvTitle = itemView.findViewById(R.id.tv_comic_title);
            tvFinished = itemView.findViewById(R.id.tv_finished);

        }
    }

    private class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setOnMainListItemClickListener(OnComicListItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}

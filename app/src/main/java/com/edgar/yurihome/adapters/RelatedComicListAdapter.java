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
import com.edgar.yurihome.beans.RelatedComicBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;
import java.util.List;

public class RelatedComicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "=======================" + RelatedComicListAdapter.class.getSimpleName();

    private static final int FLAG_RELATED_AUTHOR_COMICS = 0;
    private static final int FLAG_RELATED_THEME_COMICS = 1;

    private Context mContext;
    private ArrayList<RelatedComicBean.AuthorComicsBean.AuthorComicDataBean> authorComicList = new ArrayList<>();
    private ArrayList<RelatedComicBean.ThemeComicsBean> themeComicList = new ArrayList<>();
    private int flag = 0;

    private OnListItemClickListener itemClickListener;

    public RelatedComicListAdapter(Context mContext, ArrayList<RelatedComicBean.ThemeComicsBean> themeComicList) {
        this.mContext = mContext;
        this.themeComicList = new ArrayList<>(themeComicList);
        this.flag = FLAG_RELATED_THEME_COMICS;
    }

    public RelatedComicListAdapter(Context mContext, ArrayList<RelatedComicBean.AuthorComicsBean.AuthorComicDataBean> authorComicList, String authorName) {
        this.mContext = mContext;
        this.authorComicList = new ArrayList<>(authorComicList);
        this.flag = FLAG_RELATED_AUTHOR_COMICS;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_related_comic_list_item, parent, false);
        return new RelatedComicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RelatedComicHolder mHolder = (RelatedComicHolder) holder;
        if (flag == FLAG_RELATED_AUTHOR_COMICS) {
            final RelatedComicBean.AuthorComicsBean.AuthorComicDataBean authorComicData = authorComicList.get(position);
            mHolder.tvTitle.setText(authorComicData.getName());
            mHolder.tvFinished.setVisibility(authorComicData.getStatus().equals("已完结") ? View.VISIBLE : View.GONE);
            GlideUtil.loadImageWithUrl(mHolder.ivCover, authorComicData.getCover());

            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(mHolder.getAdapterPosition());
                    }
                }
            });
        }

        if (flag == FLAG_RELATED_THEME_COMICS) {
            RelatedComicBean.ThemeComicsBean themeComicsBean = themeComicList.get(position);
            mHolder.tvTitle.setText(themeComicsBean.getName());
            mHolder.tvFinished.setVisibility(themeComicsBean.getStatus().equals("已完结") ? View.VISIBLE : View.GONE);
            GlideUtil.loadImageWithUrl(mHolder.ivCover, themeComicsBean.getCover());

            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(position);
                    }
                }
            });
        }

    }

    public Object getComicItemAt(int position) {
        if (this.flag == FLAG_RELATED_AUTHOR_COMICS) {
            if (authorComicList.isEmpty() || position >= authorComicList.size() || position < 0)
                return null;
            return authorComicList.get(position);
        }
        if (this.flag == FLAG_RELATED_THEME_COMICS) {
            if (themeComicList.isEmpty() || position >= themeComicList.size() || position < 0)
                return null;
            return themeComicList.get(position);
        }
        return null;
    }

    public void setAuthorComicItems(List<RelatedComicBean.AuthorComicsBean.AuthorComicDataBean> dataBeans) {
        authorComicList = new ArrayList<>(dataBeans);
        notifyDataSetChanged();
    }

    public void setThemeComicItems(List<RelatedComicBean.ThemeComicsBean> comicItems) {
        themeComicList = new ArrayList<>(comicItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (flag == FLAG_RELATED_AUTHOR_COMICS ? authorComicList.size() : themeComicList.size());
    }

    public void setOnComicItemClickListener(OnListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class RelatedComicHolder extends RecyclerView.ViewHolder {

        private ImageView ivCover;
        private TextView tvTitle, tvFinished;

        public RelatedComicHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_author_comic_item_cover);
            tvTitle = itemView.findViewById(R.id.tv_author_comic_item_title);
            tvFinished = itemView.findViewById(R.id.tv_author_comic_item_finished);
        }
    }
}

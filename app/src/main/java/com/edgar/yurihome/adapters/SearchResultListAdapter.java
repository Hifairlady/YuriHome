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
import com.edgar.yurihome.beans.SearchResultBean;
import com.edgar.yurihome.interfaces.OnListItemClickListener;
import com.edgar.yurihome.utils.GlideUtil;

import java.util.ArrayList;

public class SearchResultListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<SearchResultBean> resultList = new ArrayList<>();
    private OnListItemClickListener clickListener;

    public SearchResultListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_search_result_list_item, parent, false);
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final SearchHolder mHolder = (SearchHolder) holder;
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_from_bottom);
        mHolder.itemView.startAnimation(animation);
        SearchResultBean resultBean = resultList.get(position);
        mHolder.tvTitle.setText(resultBean.getTitle());
        mHolder.tvTags.setText(mContext.getString(R.string.string_search_result_tags_text, resultBean.getTypes()));
        mHolder.tvLatestChapter.setText(mContext.getString(R.string.string_search_result_latest_chapter_text, resultBean.getLatestChapterName()));
        mHolder.tvAuthors.setText(mContext.getString(R.string.string_search_result_authors_text, resultBean.getAuthors()));
        GlideUtil.loadImageWithUrl(mHolder.ivCover, resultBean.getCover());
        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(mHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (resultList == null ? 0 : resultList.size());
    }

    public SearchResultBean getItemAt(int position) {
        if (resultList.isEmpty() || position >= resultList.size() || position < 0) {
            return null;
        }
        return resultList.get(position);
    }

    public void appendItems(ArrayList<SearchResultBean> items) {
        int startPos = resultList.size();
        resultList.addAll(items);
        notifyItemRangeInserted(startPos, items.size());
    }

    public void setItems(ArrayList<SearchResultBean> items) {
        resultList = new ArrayList<>(items);
        notifyDataSetChanged();
    }

    public void setOnComicListItemClickListener(OnListItemClickListener listener) {
        this.clickListener = listener;
    }

    private class SearchHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvAuthors, tvLatestChapter, tvTags;
        private ImageView ivCover;

        public SearchHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_search_result_title);
            tvAuthors = itemView.findViewById(R.id.tv_search_result_author);
            tvLatestChapter = itemView.findViewById(R.id.tv_search_result_latest_chapter);
            tvTags = itemView.findViewById(R.id.tv_search_result_tags);
            ivCover = itemView.findViewById(R.id.iv_search_result_cover);
        }
    }
}

package com.edgar.yurihome.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.interfaces.OnChapterListItemClickListener;
import com.edgar.yurihome.scenarios.ComicReaderActivity;
import com.edgar.yurihome.scenarios.FullChapterListActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChapterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SHORT_LIST_LIMIT_NUM = 9;
    private static final int SORT_ORDER_ASC = 0;
    private static final int SORT_ORDER_DESC = 1;
    private Context mContext;
    private ArrayList<ComicDetailsBean.ChaptersBean.DataBean> shortDataList = new ArrayList<>();
    private ArrayList<ComicDetailsBean.ChaptersBean.DataBean> fullDataList = new ArrayList<>();
    private int lastChapterId;
    private boolean isCurOrderAsc = false;
    private boolean viewFullList = false;
    private OnChapterListItemClickListener itemClickListener;
    private int comicId;
    private String comicName, chapterLongTitle, chapterPartTitle;
    private long chapterUpdateTime;

    public ChapterListAdapter(Context mContext, List<ComicDetailsBean.ChaptersBean.DataBean> dataList,
                              int lastChapterId, boolean fullList, int comicId, String comicName, String chapterPartTitle) {
        this.mContext = mContext;
//        this.shortDataList = new ArrayList<>(dataList);
        this.fullDataList = new ArrayList<>(dataList);
        this.lastChapterId = lastChapterId;
        this.viewFullList = fullList;
        this.comicId = comicId;
        this.comicName = comicName;
        this.chapterPartTitle = chapterPartTitle;
        initData();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_chapter_list_item, parent, false);
        return new ChapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ComicDetailsBean.ChaptersBean.DataBean dataBean = shortDataList.get(position);
        ChapterHolder mHolder = (ChapterHolder) holder;
        if (dataBean.getItemType() == ComicDetailsBean.ChaptersBean.DataBean.MORE_ITEM_TYPE) {
            mHolder.btnChapterItem.setText("...");
        } else {
            mHolder.btnChapterItem.setText(dataBean.getChapterTitle());
        }
        if (lastChapterId == dataBean.getChapterId()) {
            mHolder.tvNewLabel.setVisibility(View.VISIBLE);
        } else {
            mHolder.tvNewLabel.setVisibility(View.GONE);
        }
        mHolder.btnChapterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (itemClickListener != null) {
//                    itemClickListener.onItemClick(position);
//                }
                chapterUpdateTime = dataBean.getUpdatetime();
                chapterLongTitle = chapterPartTitle + "/" + dataBean.getChapterTitle();
                if (dataBean.getItemType() == ComicDetailsBean.ChaptersBean.DataBean.MORE_ITEM_TYPE) {
                    Intent intent = new Intent(mContext, FullChapterListActivity.class);
                    intent.putExtra("FULL_DATA_LIST_JSON", getFullListJson());
                    intent.putExtra("LAST_CHAPTER_ID", lastChapterId);
                    intent.putExtra("COMIC_ID", comicId);
                    intent.putExtra("COMIC_NAME", comicName);
                    intent.putExtra("CHAPTER_ID", dataBean.getChapterId());
                    intent.putExtra("CHAPTER_UPDATE_TIME", chapterUpdateTime);
                    intent.putExtra("CHAPTER_PART_TITLE", chapterPartTitle);
                    mContext.startActivity(intent);
                } else {
                    Intent readerIntent = new Intent(mContext, ComicReaderActivity.class);
                    readerIntent.putExtra("COMIC_ID", comicId);
                    readerIntent.putExtra("COMIC_NAME", comicName);
                    readerIntent.putExtra("CHAPTER_ID", dataBean.getChapterId());
                    readerIntent.putExtra("CHAPTER_UPDATE_TIME", chapterUpdateTime);
                    readerIntent.putExtra("CHAPTER_LONG_TITLE", chapterLongTitle);
                    mContext.startActivity(readerIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return (shortDataList == null ? 0 : shortDataList.size());
    }

    public void switchOrder(int order) {
        if (order == SORT_ORDER_ASC && !isCurOrderAsc) {
            isCurOrderAsc = true;
            Collections.reverse(fullDataList);
            initData();
            notifyDataSetChanged();
        }
        if (order == SORT_ORDER_DESC && isCurOrderAsc) {
            isCurOrderAsc = false;
            Collections.reverse(fullDataList);
            initData();
            notifyDataSetChanged();
        }
    }

    private void initData() {
        if (fullDataList.size() > SHORT_LIST_LIMIT_NUM && !viewFullList) {
            List<ComicDetailsBean.ChaptersBean.DataBean> tempBeans = fullDataList.subList(0, SHORT_LIST_LIMIT_NUM - 1);
            shortDataList = new ArrayList<>(tempBeans);
            ComicDetailsBean.ChaptersBean.DataBean temp = new ComicDetailsBean.ChaptersBean.DataBean(ComicDetailsBean.ChaptersBean.DataBean.MORE_ITEM_TYPE);
            shortDataList.add(shortDataList.size(), temp);
        } else {
            shortDataList = new ArrayList<>(fullDataList);
        }
    }

    public String getFullListJson() {
        String jsonString;
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ComicDetailsBean.ChaptersBean.DataBean>>() {
        }.getType();
        jsonString = gson.toJson(fullDataList, type);
        return jsonString;
    }

    public void setOnChapterItemClickListener(OnChapterListItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class ChapterHolder extends RecyclerView.ViewHolder {

        private Button btnChapterItem;
        private TextView tvNewLabel;

        public ChapterHolder(@NonNull View itemView) {
            super(itemView);
            btnChapterItem = itemView.findViewById(R.id.btn_chapter_item);
            tvNewLabel = itemView.findViewById(R.id.tv_chapter_new_label);
        }
    }

}

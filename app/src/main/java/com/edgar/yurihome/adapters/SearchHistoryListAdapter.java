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
import com.edgar.yurihome.interfaces.OnHistoryItemClickListener;
import com.edgar.yurihome.utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "=================" + SearchHistoryListAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<String> showHistoryList = new ArrayList<>();
    private ArrayList<String> fullHistoryList = new ArrayList<>();
    private OnHistoryItemClickListener itemClickListener;

    public SearchHistoryListAdapter(Context mContext) {
        this.mContext = mContext;
        setFullHistories();
        if (this.fullHistoryList != null && !this.fullHistoryList.isEmpty()) {
            this.showHistoryList = new ArrayList<>(this.fullHistoryList);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_search_history_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final HistoryHolder mHolder = (HistoryHolder) holder;
        String history = showHistoryList.get(position);
        mHolder.tvHistory.setText(history);
        mHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHistory(mHolder.getAdapterPosition());
            }
        });

        mHolder.tvHistory.setOnClickListener(new View.OnClickListener() {
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
        return (showHistoryList == null ? 0 : showHistoryList.size());
    }

    public void deleteHistory(int position) {
        if (showHistoryList == null || position >= showHistoryList.size() || position < 0) return;
        fullHistoryList.remove(getHistoryAt(position));
        showHistoryList.remove(position);
        notifyItemRemoved(position);
        storeHistories();
    }

    public void appendHistory(String history) {
        if (showHistoryList.isEmpty()) {
            fullHistoryList.add(0, history);
            showHistoryList.add(0, history);
            notifyItemInserted(0);
        } else {
            if (fullHistoryList.remove(history)) {
                fullHistoryList.add(0, history);
            } else {
                fullHistoryList.add(0, history);
                showHistoryList.add(0, history);
                notifyItemInserted(0);
            }
        }
        storeHistories();
    }

    private void setShowHistoryList(ArrayList<String> list) {
        this.showHistoryList = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void filterMatchedList(String s) {
        if (s.length() == 0) {
            setShowHistoryList(fullHistoryList);
            return;
        }
        ArrayList<String> matchedList = new ArrayList<>();
        for (String history : fullHistoryList) {
            if (history.contains(s)) {
                matchedList.add(history);
            }
        }
        setShowHistoryList(matchedList);
    }

    private void storeHistories() {
        SharedPreferenceUtil.storeSearchHistories(mContext, fullHistoryList);
    }

    private void setFullHistories() {
        String[] temp = SharedPreferenceUtil.getSearchHistories(mContext);
        if (temp == null) {
            this.fullHistoryList = new ArrayList<>();
            return;
        }
        this.fullHistoryList = new ArrayList<>(Arrays.asList(temp));
    }

    public String getHistoryAt(int position) {
        if (showHistoryList.isEmpty() || position >= showHistoryList.size() || position < 0) {
            return null;
        }
        return showHistoryList.get(position);
    }

    public void setOnHistoryItemClickListener(OnHistoryItemClickListener listener) {
        this.itemClickListener = listener;
    }

    private class HistoryHolder extends RecyclerView.ViewHolder {
        private TextView tvHistory;
        private ImageView btnDelete;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            tvHistory = itemView.findViewById(R.id.tv_search_history);
            btnDelete = itemView.findViewById(R.id.btn_delete_search_history);
        }
    }
}

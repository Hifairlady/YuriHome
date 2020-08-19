package com.edgar.yurihome.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ViewPointBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPointListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ViewPointBean> viewPointBeans = new ArrayList<>();

    public ViewPointListAdapter(Context mContext, ArrayList<ViewPointBean> viewPointBeans) {
        this.mContext = mContext;
        this.viewPointBeans = viewPointBeans;
        Collections.sort(viewPointBeans, new Comparator<ViewPointBean>() {
            @Override
            public int compare(ViewPointBean t1, ViewPointBean t2) {
                if (t1.getNum() > t2.getNum()) return -1;
                if (t1.getNum() < t2.getNum()) return 1;
                if (t1.getPage() > t2.getPage()) return 1;
                if (t1.getPage() < t2.getPage()) return -1;
                if (t1.getViewPointId() > t2.getViewPointId()) return 1;
                if (t1.getViewPointId() < t2.getViewPointId()) return -1;
                return 0;
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_view_points_list_item, parent, false);
        return new ViewPointHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewPointHolder mHolder = (ViewPointHolder) holder;
        ViewPointBean bean = viewPointBeans.get(position);
        String content = bean.getContent();
        int num = bean.getNum();
        int page = bean.getPage();
        mHolder.tvContent.setText(mContext.getString(R.string.string_view_point_content_text, content, page, num));
    }

    @Override
    public int getItemCount() {
        return (viewPointBeans == null ? 0 : viewPointBeans.size());
    }

    private class ViewPointHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public ViewPointHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_view_point_content);
        }
    }
}

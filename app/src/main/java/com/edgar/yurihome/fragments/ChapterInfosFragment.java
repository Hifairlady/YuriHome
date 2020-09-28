package com.edgar.yurihome.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.edgar.yurihome.R;
import com.edgar.yurihome.adapters.ChapterListAdapter;
import com.edgar.yurihome.beans.ComicDetailsBean;

import java.util.ArrayList;

public class ChapterInfosFragment extends Fragment {

    private static final String TAG = "=====================" + ChapterInfosFragment.class.getSimpleName();

    private static final String ARG_COMIC_TITLE = "ARG_COMIC_TITLE";
    private static final String ARG_COMIC_DESCRIPTION = "ARG_COMIC_DESCRIPTION";

    private static final int SORT_ORDER_ASC = 0;
    private static final int SORT_ORDER_DESC = 1;

    private String mComicTitle;
    private String mComicDesc;
    private ComicDetailsBean mDetailsBean;

    private TextView tvDescTitle, tvDescIntro;
    private ImageView ivDescExpand;
    private LinearLayout llChaptersContainer;
//    private LinearLayout rootView;

    private RadioGroup rgOrderGroup;
    private RadioButton rbOrderDesc, rbOrderAsc;

    private ArrayList<ChapterListAdapter> listAdapters = new ArrayList<>();

    private boolean isDescExpand = false;

    private Context mContext;


    public ChapterInfosFragment() {
        // Required empty public constructor
    }

    public static ChapterInfosFragment newInstance(String comicTitle, String comicDesc) {
        ChapterInfosFragment fragment = new ChapterInfosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COMIC_TITLE, comicTitle);
        args.putString(ARG_COMIC_DESCRIPTION, comicDesc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mComicTitle = getArguments().getString(ARG_COMIC_TITLE) == null ? "Loading..." : getArguments().getString(ARG_COMIC_TITLE);
            mComicDesc = getArguments().getString(ARG_COMIC_DESCRIPTION) == null ? "Loading..." : getArguments().getString(ARG_COMIC_DESCRIPTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_infos, container, false);
        mContext = getContext();
        tvDescTitle = view.findViewById(R.id.tv_desc_comic_title);
        tvDescIntro = view.findViewById(R.id.tv_desc_brief_intro);
        ivDescExpand = view.findViewById(R.id.btn_expand_desc);
        llChaptersContainer = view.findViewById(R.id.ll_chapters_container);
        rgOrderGroup = view.findViewById(R.id.rg_chapter_details_order);
        rbOrderAsc = view.findViewById(R.id.rb_order_asc);
        rbOrderDesc = view.findViewById(R.id.rb_order_desc);

        rgOrderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbOrderAsc.setClickable(false);
                rbOrderDesc.setClickable(false);
                switch (i) {
                    case R.id.rb_order_asc:
                        switchSortOrder(SORT_ORDER_ASC);
                        break;

                    case R.id.rb_order_desc:
                        switchSortOrder(SORT_ORDER_DESC);
                        break;

                    default:
                        break;
                }
                rbOrderAsc.setClickable(true);
                rbOrderDesc.setClickable(true);
            }
        });

        tvDescTitle.setText(getString(R.string.string_desc_title_text, mComicTitle));
        tvDescIntro.setText(getString(R.string.string_desc_intro_text, mComicDesc));

        tvDescIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDescExpand) {
                    tvDescIntro.setMaxLines(3);
                    ivDescExpand.setImageResource(R.drawable.ic_expand_more_24);
                    isDescExpand = false;
                } else {
                    tvDescIntro.setMaxLines(99);
                    ivDescExpand.setImageResource(R.drawable.ic_expand_less_24);
                    isDescExpand = true;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initChaptersData(mContext);
    }

    public void setComicDetailsBean(ComicDetailsBean comicDetailsBean) {
        this.mDetailsBean = comicDetailsBean;
    }

    private void initChaptersData(Context context) {
        if (mDetailsBean == null) return;
        int comicId = mDetailsBean.getId();
        listAdapters = new ArrayList<>();
        for (ComicDetailsBean.ChaptersBean chaptersBean : mDetailsBean.getChapters()) {
            View chapterView = LayoutInflater.from(context).inflate(R.layout.layout_chapter_details, null, false);
            llChaptersContainer.addView(chapterView);
            TextView tvChapterPartTitle = chapterView.findViewById(R.id.tv_chapter_part_title);
            tvChapterPartTitle.setText(chaptersBean.getTitle());
            RecyclerView rvChapterPartList = chapterView.findViewById(R.id.rv_chapter_list);
            GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
            rvChapterPartList.setLayoutManager(layoutManager);
            ChapterListAdapter mAdapter = new ChapterListAdapter(context, chaptersBean.getData(),
                    mDetailsBean.getLastUpdateChapterId(), false, comicId, mDetailsBean.getTitle(), chaptersBean.getTitle());
            rvChapterPartList.setAdapter(mAdapter);
            listAdapters.add(mAdapter);
        }

    }

    private void switchSortOrder(int order) {
        if (mDetailsBean == null || listAdapters.size() != mDetailsBean.getChapters().size())
            return;
        for (ChapterListAdapter listAdapter : listAdapters) {
            listAdapter.switchOrder(order);
        }
    }


}
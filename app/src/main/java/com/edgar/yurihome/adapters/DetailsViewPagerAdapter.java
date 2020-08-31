package com.edgar.yurihome.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.fragments.ChapterInfosFragment;
import com.edgar.yurihome.fragments.ComicCommentsFragment;
import com.edgar.yurihome.fragments.RelatedComicsFragment;

public class DetailsViewPagerAdapter extends FragmentStateAdapter {
    private static final int PAGE_NUM = 3;
    private ComicDetailsBean mDetailsBean;

    public DetailsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, ComicDetailsBean beans) {
        super(fragmentActivity);
        this.mDetailsBean = beans;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                int comicId = 0;
                if (mDetailsBean != null) {
                    comicId = mDetailsBean.getId();
                }
                return RelatedComicsFragment.newInstance(comicId);

            case 1:
                String comicTitle = null, comicDesc = null;
                if (mDetailsBean != null) {
                    comicTitle = mDetailsBean.getTitle();
                    comicDesc = mDetailsBean.getDescription();
                }
                ChapterInfosFragment chapterInfosFragment = ChapterInfosFragment.newInstance(comicTitle, comicDesc);
                chapterInfosFragment.setComicDetailsBean(mDetailsBean);
                return chapterInfosFragment;

            default:
                int comicId1 = 0;
                if (mDetailsBean != null) {
                    comicId1 = mDetailsBean.getId();
                }
                return ComicCommentsFragment.newInstance(comicId1);
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_NUM;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}

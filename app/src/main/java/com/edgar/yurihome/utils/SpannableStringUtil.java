package com.edgar.yurihome.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.edgar.yurihome.beans.ComicDetailsBean;

import java.util.ArrayList;

public class SpannableStringUtil {

    private static final String TAG = "=======================" + SpannableStringUtil.class.getSimpleName();

    public static void setupSpannableString(String contentString, final ComicDetailsBean comicDetailsBean, TextView tvDisplay, final int flag) {
        SpannableString spannableString = new SpannableString(contentString);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(underlineSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                if (flag == 0) {
                    ArrayList<ComicDetailsBean.AuthorsBean> authorsBeans = new ArrayList<>(comicDetailsBean.getAuthors());
                    for (ComicDetailsBean.AuthorsBean authorsBean : authorsBeans) {
                        Log.d(TAG, "onClick: " + authorsBean.getTagId() + "_" + authorsBean.getTagName());
                        popupMenu.getMenu().add(Menu.NONE, authorsBean.getTagId(), Menu.NONE, authorsBean.getTagName());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            popupMenu.dismiss();
//                        String displayString = item.getTitle() + String.valueOf(item.getItemId());
//                        Snackbar.make(detailsRootView, displayString, Snackbar.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                } else if (flag == 1) {
                    ArrayList<ComicDetailsBean.TypesBean> typesBeans = new ArrayList<>(comicDetailsBean.getTypes());
                    StringBuilder stringBuilder = new StringBuilder();
                    int count = 0;
                    for (ComicDetailsBean.TypesBean typesBean : typesBeans) {
                        Log.d(TAG, "onClick: " + typesBean.getTagId() + "_" + typesBean.getTagName());
                        stringBuilder.append(typesBean.getTagName());
                        if (count < typesBeans.size() - 1) {
                            stringBuilder.append("/");
                        }
//                        popupMenu.getMenu().add(Menu.NONE, typesBean.getTagId(), Menu.NONE, typesBean.getTagName());
                        count++;
                    }
                    String typeString = stringBuilder.toString();
                    popupMenu.getMenu().add(Menu.NONE, Menu.NONE, Menu.NONE, typeString);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            popupMenu.dismiss();
//                        String displayString = item.getTitle() + String.valueOf(item.getItemId());
//                        Snackbar.make(detailsRootView, displayString, Snackbar.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
                popupMenu.show();
            }
        };
        spannableString.setSpan(clickableSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvDisplay.setText(spannableString);
        tvDisplay.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setAuthorSpanString(String authorsText, final ArrayList<ComicDetailsBean.AuthorsBean> authorsBeans, TextView tvAuthors) {
        SpannableString spannableString = new SpannableString(authorsText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(underlineSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                for (ComicDetailsBean.AuthorsBean authorsBean : authorsBeans) {
                    popupMenu.getMenu().add(Menu.NONE, authorsBean.getTagId(), Menu.NONE, authorsBean.getTagName());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        popupMenu.dismiss();
//                        String displayString = item.getTitle() + String.valueOf(item.getItemId());
//                        Snackbar.make(detailsRootView, displayString, Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        };
        spannableString.setSpan(clickableSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvAuthors.setText(spannableString);
        tvAuthors.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setTagSpanString(String tagText, final ArrayList<ComicDetailsBean.TypesBean> typesBeans, TextView tvTags) {
        SpannableString spannableString = new SpannableString(tagText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(underlineSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                StringBuilder stringBuilder = new StringBuilder();
                int count = 0;
                for (ComicDetailsBean.TypesBean typesBean : typesBeans) {
                    stringBuilder.append(typesBean.getTagName());
                    if (count < typesBeans.size() - 1) {
                        stringBuilder.append("/");
                    }
//                        popupMenu.getMenu().add(Menu.NONE, typesBean.getTagId(), Menu.NONE, typesBean.getTagName());
                    count++;
                }
                String typeString = stringBuilder.toString();
                popupMenu.getMenu().add(Menu.NONE, Menu.NONE, Menu.NONE, typeString);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        popupMenu.dismiss();
//                        String displayString = item.getTitle() + String.valueOf(item.getItemId());
//                        Snackbar.make(detailsRootView, displayString, Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        };
        spannableString.setSpan(clickableSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvTags.setText(spannableString);
        tvTags.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

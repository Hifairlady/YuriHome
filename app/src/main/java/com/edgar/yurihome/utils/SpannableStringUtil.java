package com.edgar.yurihome.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.scenarios.AuthorComicsActivity;

import java.util.ArrayList;

public class SpannableStringUtil {

    private static final String TAG = "=======================" + SpannableStringUtil.class.getSimpleName();

    public static void setAuthorSpanString(final Context mContext, String authorsText, final ArrayList<ComicDetailsBean.AuthorsBean> authorsBeans, TextView tvAuthors) {
        SpannableString spannableString = new SpannableString(authorsText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {

                if (authorsBeans.size() == 1) {
                    ComicDetailsBean.AuthorsBean authorsBean = authorsBeans.get(0);
                    Intent authorIntent1 = new Intent(mContext, AuthorComicsActivity.class);
                    authorIntent1.putExtra("AUTHOR_NAME", authorsBean.getTagName());
                    authorIntent1.putExtra("AUTHOR_ID", authorsBean.getTagId());
                    mContext.startActivity(authorIntent1);
                    return;
                }

                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                for (ComicDetailsBean.AuthorsBean authorsBean : authorsBeans) {
                    popupMenu.getMenu().add(Menu.NONE, authorsBean.getTagId(), Menu.NONE, authorsBean.getTagName());
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent authorIntent = new Intent(mContext, AuthorComicsActivity.class);
                        authorIntent.putExtra("AUTHOR_NAME", item.getTitle());
                        authorIntent.putExtra("AUTHOR_ID", item.getItemId());
                        mContext.startActivity(authorIntent);
                        popupMenu.dismiss();
                        return true;
                    }
                });
                popupMenu.show();
            }
        };
        if (authorsBeans.size() == 1) {
            spannableString.setSpan(foregroundColorSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(underlineSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(clickableSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            spannableString.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(underlineSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(clickableSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        tvAuthors.setText(spannableString);
        tvAuthors.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setTagSpanString(String tagText, final ArrayList<ComicDetailsBean.TypesBean> typesBeans, final TextView tvTags) {
        SpannableString spannableString = new SpannableString(tagText);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                StringBuilder stringBuilder = new StringBuilder();
                int count = 0;
                for (ComicDetailsBean.TypesBean typesBean : typesBeans) {
                    stringBuilder.append(typesBean.getTagName());
                    if (count < typesBeans.size() - 1) {
                        stringBuilder.append("/");
                    }
                    count++;
                }
                String typeString = stringBuilder.toString();
//                final PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
//                popupMenu.getMenu().add(Menu.NONE, Menu.NONE, Menu.NONE, typeString);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        popupMenu.dismiss();
//                        String displayString = item.getTitle() + String.valueOf(item.getItemId());
////                        Snackbar.make(detailsRootView, displayString, Snackbar.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//                popupMenu.show();
                ClipboardManager clipboard = (ClipboardManager) tvTags.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, typeString);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(tvTags.getContext(), typeString + " copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        };
//        spannableString.setSpan(foregroundColorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        spannableString.setSpan(underlineSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//        spannableString.setSpan(clickableSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannableString.setSpan(foregroundColorSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(underlineSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(clickableSpan, 3, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvTags.setText(spannableString);
        tvTags.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

package com.edgar.yurihome.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
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

import com.edgar.yurihome.R;
import com.edgar.yurihome.beans.ComicDetailsBean;
import com.edgar.yurihome.scenarios.AuthorComicsActivity;
import com.edgar.yurihome.scenarios.ComicReaderActivity;

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

                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(null, authorsBean.getTagName());
                    clipboard.setPrimaryClip(clipData);

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

                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(null, item.getTitle());
                        clipboard.setPrimaryClip(clipData);

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
                Context context = tvTags.getContext();
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
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, typeString);
                clipboard.setPrimaryClip(clipData);
                Toast.makeText(context, typeString + context.getString(R.string.string_copied_clipboard_text), Toast.LENGTH_SHORT).show();
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


    public static void addReadMore(final String text, final TextView textView) {
        if (text.length() <= 100) {
            textView.setText(text);
            return;
        }
        SpannableString ss = new SpannableString(text.substring(0, 100) + "... Read More");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(text, textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(textView.getContext().getResources().getColor(R.color.colorAccent));
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private static void addReadLess(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text + " Read Less");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(textView.getContext().getResources().getColor(R.color.colorAccent));
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static void setLastChapterSpannable(TextView tvLastChapter, final ComicDetailsBean comicDetailsBean) {
        final Context mContext = tvLastChapter.getContext();
        String content = mContext.getString(R.string.string_comic_details_last_chapter_text, comicDetailsBean.getLastUpdateChapterName());
        SpannableString spanString = new SpannableString(content);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xff3f51b5);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //            readerIntent.putExtra("COMIC_ID", comicId);
                //            readerIntent.putExtra("COMIC_NAME", comicName);
                //            readerIntent.putExtra("CHAPTER_ID", dataBean.getChapterId());
                //            readerIntent.putExtra("CHAPTER_UPDATE_TIME", chapterUpdateTime);
                //            readerIntent.putExtra("CHAPTER_LONG_TITLE", chapterLongTitle);
                Intent intent = new Intent(mContext, ComicReaderActivity.class);
                intent.putExtra("COMIC_ID", comicDetailsBean.getId());
                intent.putExtra("COMIC_NAME", comicDetailsBean.getTitle());
                intent.putExtra("CHAPTER_ID", comicDetailsBean.getLastUpdateChapterId());
                intent.putExtra("CHAPTER_UPDATE_TIME", comicDetailsBean.getLastUpdateTime());
                intent.putExtra("CHAPTER_LONG_TITLE", comicDetailsBean.getLastUpdateChapterName());
                mContext.startActivity(intent);
            }
        };
        spanString.setSpan(foregroundColorSpan, 4, spanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanString.setSpan(underlineSpan, 4, spanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spanString.setSpan(clickableSpan, 4, spanString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvLastChapter.setText(spanString);
        tvLastChapter.setMovementMethod(LinkMovementMethod.getInstance());
    }

}

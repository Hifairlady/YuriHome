package com.edgar.yurihome.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.edgar.yurihome.GlideApp;
import com.edgar.yurihome.R;

public class GlideUtil {
    public static GlideUrl getGlideUrl(String urlString) {
        GlideUrl url = new GlideUrl(urlString, new LazyHeaders.Builder()
                .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365))
                .addHeader("Referer", "http://images.dmzj.com/")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                .build());
        return url;
    }

    public static void loadImageWithUrl(final ImageView imageView, final String urlString) {
        GlideApp.with(imageView)
                .load(getGlideUrl(urlString))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(imageView);
    }

    public static void loadImageWithUrlNoCache(ImageView imageView, String urlString) {
        GlideApp.with(imageView)
                .load(getGlideUrl(urlString))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(imageView);
    }

    public static void loadReaderImageWithUrlNoCache(ImageView imageView, String urlString) {
        GlideApp.with(imageView)
                .load(getGlideUrl(urlString))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.reader_loading)
                .error(R.drawable.reader_error)
                .into(imageView);
    }


    public static void loadReaderImage(final ImageView imageView, final String urlString, final int position, final int[] loadCodes) {
        if (loadCodes[position] == -1) {
            GlideUtil.loadReaderImageWithUrlNoCache(imageView, urlString);
        } else {
            GlideApp.with(imageView)
                    .load(getGlideUrl(urlString))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            loadCodes[position] = -1;
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.reader_loading)
                    .error(R.drawable.reader_error)
                    .into(imageView);
        }
    }
}

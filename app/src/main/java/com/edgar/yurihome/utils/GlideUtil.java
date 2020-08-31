package com.edgar.yurihome.utils;

import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.edgar.yurihome.GlideApp;
import com.edgar.yurihome.R;

public class GlideUtil {
    private static GlideUrl getGlideUrl(String urlString) {
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
                .placeholder(R.drawable.reader_loading)
                .error(R.drawable.reader_error)
                .into(imageView);
    }

    public static void loadCircularImageWithUrl(ImageView imageView, String urlString) {
        GlideApp.with(imageView)
                .load(getGlideUrl(urlString))
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .into(imageView);
    }
}

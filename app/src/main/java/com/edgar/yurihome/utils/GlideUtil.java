package com.edgar.yurihome.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

public class GlideUtil {
    public static GlideUrl getGlideUrl(String urlString) {
        GlideUrl url = new GlideUrl(urlString, new LazyHeaders.Builder()
                .addHeader("Cache-Control", "max-age=" + (60 * 60 * 24 * 365))
                .addHeader("Referer", "http://images.dmzj.com/")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36")
                .build());
        return url;
    }
}

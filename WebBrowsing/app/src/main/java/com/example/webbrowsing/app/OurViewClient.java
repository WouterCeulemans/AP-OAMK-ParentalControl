package com.example.webbrowsing.app;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by nick on 31/03/14.
 */
public class OurViewClient extends WebViewClient
{
    @Override
    public boolean shouldOverrideUrlLoading(WebView v, String url)
    {
        v.loadUrl(url);
        return true;
    }
}

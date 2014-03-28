package com.example.webbrowsing.app;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView ourBrowser = (WebView)findViewById(R.id.wvBrowser);
        ourBrowser.loadUrl("http://www.google.com");

    }
}

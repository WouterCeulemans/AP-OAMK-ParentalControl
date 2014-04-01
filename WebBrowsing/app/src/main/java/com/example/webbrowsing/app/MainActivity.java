package com.example.webbrowsing.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {
    EditText url;
    WebView ourBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ourBrowser = (WebView) findViewById(R.id.wvBrowser);
        //enable javascript voor youtube bijvoorbeeld
        ourBrowser.getSettings().setJavaScriptEnabled(true);
        ourBrowser.getSettings().setLoadWithOverviewMode(true);// able to zoom out orso
        ourBrowser.getSettings().setUseWideViewPort(true);//set up a normal viewtop

        ourBrowser.setWebViewClient(new OurViewClient());
        try
        {
            ourBrowser.loadUrl("http://www.google.com");
        }
        catch ( Exception e)
        {
            e.printStackTrace();
        }


        ImageButton go       = (ImageButton) findViewById(R.id.btnGo);
        ImageButton back     = (ImageButton) findViewById(R.id.btnBack);
        ImageButton refresh  = (ImageButton)findViewById(R.id.btnRefresh);
        ImageButton forward  = (ImageButton) findViewById(R.id.btnForward);
        ImageButton settings = (ImageButton) findViewById(R.id.btnSettings);

        url = (EditText) findViewById(R.id.txtUrlBar);

        go.setOnClickListener(this);
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        forward.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    public void onClick(View arg0)
    {
        switch (arg0.getId())
        {
            case R.id.btnGo:
                //allow people to go to there own url like a bauwz
                String theWebsite = "http://" + url.getText().toString();
                ourBrowser.loadUrl(theWebsite);
                // for closing/hiding the keyboard
                InputMethodManager imm =( InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(url.getWindowToken(), 0);//this code will hide the keyboard after using the edit text

                break;
            case R.id.btnBack:
                if (ourBrowser.canGoBack())
                ourBrowser.goBack();

                break  ;
            case  R.id.btnForward:
                if (ourBrowser.canGoForward())
                    ourBrowser.goForward();

                break;
            case R.id.btnRefresh:
                ourBrowser.reload();

                break;
            case R.id.btnSettings:
                //voor het wissen van geschiedennis
                /*
                ourBrowser.clearHistory();
                 */

                break;
        }
    }
}

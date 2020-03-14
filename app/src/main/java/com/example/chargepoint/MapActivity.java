package com.example.chargepoint;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        String html = "<iframe src=\"https://map.openchargemap.io/?mode=embedded\" ></iframe>";

        webView = this.findViewById(R.id.mapWebView);
        //webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(html, "text/html", null);
    }
}

package com.monresto.acidlabs.monresto.UI.Checkout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.monresto.acidlabs.monresto.Base64;
import com.monresto.acidlabs.monresto.R;
import com.monresto.acidlabs.monresto.UI.Profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {
    @BindView(R.id.buttonClose)
    ImageView buttonClose;
    @BindView(R.id.webview)
    WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        buttonClose.setOnClickListener(e -> finish());

        int orderID = getIntent().getIntExtra("orderID", 89551);
        String payUrl = "";
        try {
            payUrl = getIntent().getStringExtra("payUrl");
        }catch (Exception ignored)
        {

        }
        String postData = "orderID=" + orderID;
        String html = null;

        webView.postUrl("https://www.monresto.net/processgpg.php", Base64.encode(postData.getBytes()).getBytes());


        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        /*webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });*/

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                if (request.getUrl().toString().contains("notification_payment")) {
                    startActivity(new Intent(PaymentActivity.this, ProfileActivity.class));
                    finish();
                }
                return true;
            }
        });
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);


        if (payUrl != null && !payUrl.equalsIgnoreCase("")) {
           /* html = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body onload='document.frm1.submit()'>" +
                    "<form action='" + payUrl + "' method='get' name='frm1'>" +
                    "  <input type='hidden' name='orderID' value='" + orderID + "'><br>" +
                    "</form>" +
                    "</body>" +
                    "</html>";*/
            webView.loadUrl(payUrl);
        } else {

            html = "<!DOCTYPE html>" +
                    "<html>" +
                    "<body onload='document.frm1.submit()'>" +
                    "<form action='https://www.monresto.net/processgpg.php' method='post' name='frm1'>" +
                    "  <input type='hidden' name='orderID' value='" + orderID + "'><br>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            webView.loadData(html, "text/html", "UTF-8");

        }


    }


}

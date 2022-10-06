package com.newsdetail.ui.browsing;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.newsdetail.databinding.ActivityBrowsingBinding;
import com.newsdetail.ui.browsing.BrowsingIntr;

public class BrowsingPresenter implements BrowsingIntr.Presenter {

    //Activity view
    private final ActivityBrowsingBinding activityBrowsingBinding;

    // instantiating the objects of View and Model Interface
    public BrowsingPresenter(ActivityBrowsingBinding binding) {
        this.activityBrowsingBinding = binding;
    }

    /**
     * load url on browser
     * @param link = browsable link
     */
    @Override
    public void loadWebPage(String link) {
        if (activityBrowsingBinding!=null)
        {
            //initialize progress bar
            activityBrowsingBinding.progressBar.setMax(100);
            activityBrowsingBinding.webView.loadUrl(link);
            activityBrowsingBinding.webView.getSettings().setJavaScriptEnabled(true);
            activityBrowsingBinding.webView.getSettings().setLoadWithOverviewMode(true);
            activityBrowsingBinding.webView.getSettings().setUseWideViewPort(true);
            activityBrowsingBinding.webView.getSettings().setDomStorageEnabled(true);
            activityBrowsingBinding.webView.setWebViewClient(new WebViewClient());
            activityBrowsingBinding.webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    //set progress on progress bar
                    activityBrowsingBinding.progressBar.setProgress(newProgress);
                    //dismiss progress bar
                    if (newProgress == 100) activityBrowsingBinding.progressBar.setProgress(0);
                }
            });
        }
    }

    @Override
    public void onDestroy() {

    }
}

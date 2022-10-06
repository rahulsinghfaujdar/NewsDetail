package com.newsdetail.ui.browsing;

public interface BrowsingIntr {

    interface Presenter {

        //method to load url on browser
        void loadWebPage(String link);

        // method to destroy lifecycle of MainActivity
        void onDestroy();
    }

}

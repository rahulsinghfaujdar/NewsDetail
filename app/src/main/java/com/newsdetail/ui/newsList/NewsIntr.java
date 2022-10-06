package com.newsdetail.ui.newsList;

import com.newsdetail.adapter.NewsRecyclerAdapter;
import com.newsdetail.model.ResNewsFeedModel;

import java.util.List;

/**
 * News Presenter interface with operation methods
 */
public interface NewsIntr {

    interface View {
        // method to display progress bar
        void showProgress();

        // method to hide progress bar
        void hideProgress();

        //method to show error
        void showError(String error);
    }

    interface Presenter {

        //method to get latest data from network
        void getLatestNewsByNetwork(NewsRecyclerAdapter newsRecyclerAdapter,List<ResNewsFeedModel> resNewsFeedModelList,boolean isRefresh);

        //method to sort data according to publish date & time
        void onSort(NewsRecyclerAdapter newsRecyclerAdapter);

        //method to search in list by Author
        void onSearch(String text, NewsRecyclerAdapter newsRecyclerAdapter, List<ResNewsFeedModel> resNewsFeedModelList);

        //change theme to day or dark mode
        void switchTheme();

        // method to destroy lifecycle of MainActivity
        void onDestroy();
    }

}

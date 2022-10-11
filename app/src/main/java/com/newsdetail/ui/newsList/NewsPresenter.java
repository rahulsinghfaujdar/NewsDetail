package com.newsdetail.ui.newsList;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatDelegate;

import com.newsdetail.BuildConfig;
import com.newsdetail.adapter.NewsRecyclerAdapter;
import com.newsdetail.constant.GlobalConstants;
import com.newsdetail.constant.NetworkConst;
import com.newsdetail.databinding.ActivityNewsBinding;
import com.newsdetail.model.ResNewsFeedModel;
import com.newsdetail.network.NetworkAdapter;
import com.newsdetail.utility.CommonUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * News Presenter class
 */
public class NewsPresenter implements NewsIntr.Presenter {

    //Activity view
    private final ActivityNewsBinding activityNewsBinding;
    // creating object of View Interface
    private NewsIntr.View mainView;
    //sort toggle
    private boolean isSortByLatest = true;
    //dark mode toggle
    private boolean isDarkMode = false;

    // instantiating the objects of View and Model Interface
    public NewsPresenter(NewsIntr.View mainView, ActivityNewsBinding binding) {
        this.mainView = mainView;
        this.activityNewsBinding = binding;
    }


    /**
     * get data from api
     *
     * @param newsRecyclerAdapter  = Recycler Adapter object
     * @param resNewsFeedModelList = Data Model
     * @param isRefresh            = Boolean tag to refresh data
     */
    @Override
    public void getLatestNewsByNetwork(NewsRecyclerAdapter newsRecyclerAdapter, List<ResNewsFeedModel> resNewsFeedModelList, boolean isRefresh) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        if (!isRefresh) {
            mainView.showProgress();
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //create instance of Network Adapter
                NetworkAdapter networkAdapter = new NetworkAdapter.
                        NetworkAdapterBuilder(NetworkAdapter.RequestType.GET, BuildConfig.API_BASE_URL, NetworkConst.ROUTE_ANDROID, NetworkConst.API_EP_NEWS_FEED)
                        .build();
                //config logs visibility state
                networkAdapter.setShowNetworkLogs(false);

                //call api by NetworkAdapter & get response data
                NetworkAdapter.BaseResponseModel result = networkAdapter.enqueeApi();

                //check response is not empty
                if (result != null && result.getResponseCode() == 200 && !TextUtils.isEmpty(result.getResponseMessage())) {
                    try {
                        //hold data from string to Json Object
                        JSONObject responseObject = new JSONObject(result.getResponseMessage());
                        //check Json object not null & have articles
                        if (responseObject != null && responseObject.has(GlobalConstants.ARTICLES)) {
                            //remove previous stored data from list
                            resNewsFeedModelList.removeAll(resNewsFeedModelList);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                //get articles list and store in Json array
                                JSONArray articlesJsonArray = new JSONArray(responseObject.getJSONArray(GlobalConstants.ARTICLES).toString());
                                //if articles Json array is not empty or contains items then create &  Response Models class
                                if (articlesJsonArray != null && articlesJsonArray.length() > 0) {
                                    for (int i = 0; i < articlesJsonArray.length(); i++) {
                                        JSONObject articles = (JSONObject) articlesJsonArray.get(i);
                                        JSONObject sourceObj = articles.getJSONObject(GlobalConstants.SOURCE);
                                        ResNewsFeedModel.source source = new ResNewsFeedModel.source(sourceObj.getString(GlobalConstants.ID), sourceObj.getString(GlobalConstants.NAME));
                                        ResNewsFeedModel resNewsFeedModel = new ResNewsFeedModel(
                                                source,
                                                articles.getString(GlobalConstants.AUTHOR),
                                                articles.getString(GlobalConstants.TITLE),
                                                articles.getString(GlobalConstants.DESCRIPTION),
                                                articles.getString(GlobalConstants.URL),
                                                articles.getString(GlobalConstants.URLTOIMAGE),
                                                articles.getString(GlobalConstants.PUBLISHEDAT),
                                                articles.getString(GlobalConstants.CONTENT)
                                        );
                                        //add item in list
                                        resNewsFeedModelList.add(resNewsFeedModel);
                                    }
                                    update(handler, newsRecyclerAdapter, resNewsFeedModelList, isRefresh);
                                } else {
                                    mainView.showError("");
                                }
                            }
                        } else {
                            mainView.showError("");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mainView.showError(result.getResponseMessage());
                    }
                } else {
                    mainView.showError(result.getResponseMessage());
                }
            }
        });
    }

    /**
     * Update on UI thread
     *
     * @param handler
     * @param newsRecyclerAdapter
     * @param resNewsFeedModelList
     */
    private void update(Handler handler, NewsRecyclerAdapter newsRecyclerAdapter, List<ResNewsFeedModel> resNewsFeedModelList, boolean isRefresh) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //notify adapter with new data
                newsRecyclerAdapter.updatedData(resNewsFeedModelList);
                if (activityNewsBinding.container.swipeLayout != null) {
                    activityNewsBinding.container.swipeLayout.setRefreshing(false);
                }
                if (!isRefresh) {
                    mainView.hideProgress();
                }
            }
        });
    }

    @Override
    public void onSort(NewsRecyclerAdapter newsRecyclerAdapter) {
        //sort for latest posts using collection
        if (isSortByLatest) {
            Collections.sort(newsRecyclerAdapter.getList(), new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    ResNewsFeedModel p1 = (ResNewsFeedModel) o1;
                    ResNewsFeedModel p2 = (ResNewsFeedModel) o2;
                    //convert time of both objects in millis
                    Long p1Millis = CommonUtility.getDateInMillis(p1.getPublishedAt(), GlobalConstants.INPUT_DATE_TIME_FORMAT);
                    Long p2Millis = CommonUtility.getDateInMillis(p2.getPublishedAt(), GlobalConstants.INPUT_DATE_TIME_FORMAT);
                    return p1Millis.compareTo(p2Millis);  //compare and return items for newer
                }
            });
            //update recycler adapter
            newsRecyclerAdapter.notifyDataSetChanged();
            //inverse value for older posts
            isSortByLatest = false;
        }
        //sort for older posts using collection
        else {
            Collections.sort(newsRecyclerAdapter.getList(), new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    ResNewsFeedModel p1 = (ResNewsFeedModel) o1;
                    ResNewsFeedModel p2 = (ResNewsFeedModel) o2;
                    //convert time of both objects in millis
                    Long p1Millis = CommonUtility.getDateInMillis(p1.getPublishedAt(), GlobalConstants.INPUT_DATE_TIME_FORMAT);
                    Long p2Millis = CommonUtility.getDateInMillis(p2.getPublishedAt(), GlobalConstants.INPUT_DATE_TIME_FORMAT);
                    return p2Millis.compareTo(p1Millis);  //compare and return items for older
                }
            });
            //update recycler adapter
            newsRecyclerAdapter.notifyDataSetChanged();
            //inverse value for newer posts
            isSortByLatest = true;
        }
    }

    @Override
    public void onSearch(String text, NewsRecyclerAdapter newsRecyclerAdapter, List<ResNewsFeedModel> resNewsFeedModelList) {
        //creating a new array list to filter
        ArrayList<ResNewsFeedModel> filteredlist = new ArrayList<ResNewsFeedModel>();

        if (!TextUtils.isEmpty(text)) {
            //running a for loop to compare elements.
            for (ResNewsFeedModel item : resNewsFeedModelList) {
                // checking if the entered string matched with any item of our recycler view.
                if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                } else if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                } else if (item.getSource().getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.add(item);
                }
            }
            //check new filtered list contains matched items then update & notify recycler adapter
            if (!filteredlist.isEmpty()) {
                mainView.hideProgress();
                newsRecyclerAdapter.updatedData(filteredlist);
            } else {
                mainView.showError("");
            }
        } else {
            mainView.hideProgress();
            newsRecyclerAdapter.updatedData(resNewsFeedModelList);
        }
    }

    //switch theme
    @Override
    public void switchTheme() {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isDarkMode = false;
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isDarkMode = true;
        }
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

}

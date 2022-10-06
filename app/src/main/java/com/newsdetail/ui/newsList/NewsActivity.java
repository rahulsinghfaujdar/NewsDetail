package com.newsdetail.ui.newsList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.newsdetail.R;
import com.newsdetail.adapter.NewsRecyclerAdapter;
import com.newsdetail.constant.GlobalConstants;
import com.newsdetail.databinding.ActivityNewsBinding;
import com.newsdetail.model.ResNewsFeedModel;
import com.newsdetail.ui.browsing.BrowsingActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsIntr.View {

    //create & initialize list for adapter
    private final List<ResNewsFeedModel> resNewsFeedModelList = new ArrayList<>();
    // creating object of News Presenter
    private NewsIntr.Presenter presenter;
    //binding variable for activity view
    private ActivityNewsBinding binding;
    //create object of recycler adapter
    private NewsRecyclerAdapter newsRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bind view to activity
        binding = ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize toolbar as Action Bar
        setSupportActionBar(binding.toolbar);

        // instantiating object of Presenter Interface
        presenter = new NewsPresenter(this, binding);

        //initialize recycler view
        initRecyclerView();
    }

    /**
     * Initialize views
     */
    private void initRecyclerView() {
        //initialize News RecyclerView
        binding.container.rvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
        //create recycler adapter object
        newsRecyclerAdapter = new NewsRecyclerAdapter(NewsActivity.this, resNewsFeedModelList, new NewsRecyclerAdapter.NewsRvAdapterClickHelper() {
            /**
             * listener to Item clicked for recycler view
             * @param resNewsFeedModel = selected item data object
             */
            @Override
            public void ItemClicked(ResNewsFeedModel resNewsFeedModel) {
                if (resNewsFeedModel != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(GlobalConstants.NEWS_TITLE, resNewsFeedModel.getTitle());
                    bundle.putString(GlobalConstants.NEWS_BROWSING_LINK, resNewsFeedModel.getUrl());
                    Intent intent = new Intent(NewsActivity.this, BrowsingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        binding.container.rvNews.setHasFixedSize(true);
        //set adapter to recycler view
        binding.container.rvNews.setAdapter(newsRecyclerAdapter);

        //initialize SwipeRefreshView
        binding.container.swipeLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        binding.container.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getLatestNewsByNetwork(newsRecyclerAdapter, resNewsFeedModelList, true);
            }
        });

        //call api and update data on recycler view
        presenter.getLatestNewsByNetwork(newsRecyclerAdapter, resNewsFeedModelList, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Initialise menu item search bar
        MenuItem searchViewItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);

        // attach setOnQueryTextListener to search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //search item in list by presenter
                presenter.onSearch(query, newsRecyclerAdapter, resNewsFeedModelList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //search item in list by presenter
                presenter.onSearch(newText, newsRecyclerAdapter, resNewsFeedModelList);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get sort Menu Item id
        int id = item.getItemId();
        if (id == R.id.sort) {
            presenter.onSort(newsRecyclerAdapter);
            return true;
        } else if (id == R.id.theme) {
            presenter.switchTheme();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroy presenter
        presenter.onDestroy();
    }

    /**
     * show progress bar
     */
    @Override
    public void showProgress() {
        binding.container.progressBar.setVisibility(View.VISIBLE);
        binding.container.rvNews.setVisibility(View.GONE);
        binding.container.tvError.setVisibility(View.GONE);
    }

    /**
     * hide progress bar
     */
    @Override
    public void hideProgress() {
        binding.container.progressBar.setVisibility(View.GONE);
        binding.container.tvError.setVisibility(View.GONE);
        binding.container.rvNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String error) {
        binding.container.progressBar.setVisibility(View.GONE);
        binding.container.rvNews.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(error)) {
            binding.container.tvError.setText(error);
        }
        binding.container.tvError.setVisibility(View.VISIBLE);
    }

}
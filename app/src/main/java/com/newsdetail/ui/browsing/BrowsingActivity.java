package com.newsdetail.ui.browsing;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.newsdetail.constant.GlobalConstants;
import com.newsdetail.databinding.ActivityBrowsingBinding;

public class BrowsingActivity extends AppCompatActivity {

    //binding variable for activity view
    private ActivityBrowsingBinding binding;
    // creating object of News Presenter
    private BrowsingIntr.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bind view to activity
        binding = ActivityBrowsingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // instantiating object of Presenter Interface
        presenter = new BrowsingPresenter(binding);

        //check intent contain data or not empty
        if (getIntent().hasExtra(GlobalConstants.NEWS_TITLE) && getIntent().hasExtra(GlobalConstants.NEWS_BROWSING_LINK))
        {
            //initialize Title asn set text
            binding.tvNewsTitle.setText(getIntent().getExtras().getString(GlobalConstants.NEWS_TITLE));
            //initialize close event
            binding.ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            //load new url on browser
            presenter.loadWebPage(getIntent().getExtras().getString(GlobalConstants.NEWS_BROWSING_LINK));
        }
    }

    //Define action on back event
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroy presenter
        presenter.onDestroy();
    }
}



package com.newsdetail.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.newsdetail.R;
import com.newsdetail.model.ResNewsFeedModel;
import com.newsdetail.constant.GlobalConstants;
import com.newsdetail.utility.CommonUtility;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final NewsRvAdapterClickHelper allBankRvAdapterClickHelper;
    private List<ResNewsFeedModel> list;

    public NewsRecyclerAdapter(Context context, List<ResNewsFeedModel> list, NewsRvAdapterClickHelper allBankRvAdapterClickHelper) {
        this.mContext = context;
        this.list = list;
        this.allBankRvAdapterClickHelper = allBankRvAdapterClickHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news, parent, false);
        return new NewsRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsRecyclerAdapter.MyViewHolder myViewHolder = (NewsRecyclerAdapter.MyViewHolder) holder;
        ResNewsFeedModel item = list.get(holder.getAdapterPosition());
        String formattedDate = CommonUtility.getFormattedDate(item.getPublishedAt(), GlobalConstants.INPUT_DATE_TIME_FORMAT, GlobalConstants.OUTPUT_DATE_TIME_FORMAT);
        myViewHolder.tvItem.setText("" + item.getTitle());
        myViewHolder.tvSourceName.setText("Source : "+item.getSource().getName());
        myViewHolder.tvPublishAt.setText(formattedDate);
        myViewHolder.tvDescription.setText(item.getDescription());
        if (!TextUtils.isEmpty(item.getUrlToImage()))
            Glide.with(mContext)
                    .load(item.getUrlToImage())
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .error(R.drawable.ic_launcher_background)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(myViewHolder.imageView);


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allBankRvAdapterClickHelper.ItemClicked(list.get(holder.getAdapterPosition()));
            }
        });
    }

    public void updatedData(List<ResNewsFeedModel> filterllist) {
        list = filterllist;
        notifyDataSetChanged();
    }

    public List<ResNewsFeedModel> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public interface NewsRvAdapterClickHelper {
        void ItemClicked(ResNewsFeedModel resNewsFeedModel);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItem,tvSourceName,tvPublishAt,tvDescription;
        private final ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            tvItem = view.findViewById(R.id.tvItem);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvSourceName = view.findViewById(R.id.tvSourceName);
            tvPublishAt = view.findViewById(R.id.tvPublishAt);
            imageView = view.findViewById(R.id.imageView);
        }
    }

}

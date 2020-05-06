package com.example.myfirstpracticeapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstpracticeapplication.R;
import com.example.myfirstpracticeapplication.model.HomeDataResult;

import java.util.List;

public class HomeInfoListAdapter extends RecyclerView.Adapter<HomeInfoListAdapter.ViewHolder> {

    private List<HomeDataResult.DataBean.NewsListBean> mNews;

    public HomeInfoListAdapter(List<HomeDataResult.DataBean.NewsListBean> mNews){
        this.mNews = mNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_industry_message_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeDataResult.DataBean.NewsListBean newsListBean = mNews.get(position);
        holder.newsText.setText(newsListBean.getTitle());
        holder.newsDate.setText(newsListBean.getCreate_time());
    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsText;
        TextView newsDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsText = itemView.findViewById(R.id.news_text);
            newsDate = itemView.findViewById(R.id.news_date);
        }
    }
}

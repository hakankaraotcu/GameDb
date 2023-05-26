package com.hakankaraotcu.gamedb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hakankaraotcu.gamedb.Model.News;
import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> newsList;
    private Context context;

    public NewsAdapter(ArrayList<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = newsList.get(position);
        holder.setData(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        TextView newsTitle, newsContent;
        ImageView newsImage;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_item_title);
            newsContent = itemView.findViewById(R.id.news_item_content);
            newsImage = itemView.findViewById(R.id.news_item_image);
        }

        public void setData(News news) {
            this.newsTitle.setText(news.getNewsTitle());
            this.newsContent.setText(news.getNewsContent());
            this.newsImage.setBackgroundResource(news.getNewsImage());
        }
    }
}

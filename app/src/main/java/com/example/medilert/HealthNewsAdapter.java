package com.example.medilert.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medilert.R;
import com.example.medilert.data.HealthNews;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HealthNewsAdapter extends RecyclerView.Adapter<HealthNewsAdapter.NewsViewHolder> {
    private List<HealthNews> newsList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        HealthNews news = newsList.get(position);
        holder.title.setText(news.title);
        holder.content.setText(news.content);
        holder.author.setText(news.author);
        holder.date.setText(dateFormat.format(new Date(news.timestamp)));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setNewsList(List<HealthNews> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView author;
        TextView date;

        NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            content = itemView.findViewById(R.id.newsContent);
            author = itemView.findViewById(R.id.newsAuthor);
            date = itemView.findViewById(R.id.newsDate);
        }
    }
}
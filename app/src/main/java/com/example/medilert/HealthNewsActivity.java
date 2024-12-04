package com.example.medilert;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.medilert.adapter.HealthNewsAdapter;
import com.example.medilert.data.HealthNews;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthNewsActivity extends AppCompatActivity {
    private HealthNewsAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_news);

        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        loadHealthNews();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Health News & Alerts");
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HealthNewsAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this::refreshNews);
    }

    private void loadHealthNews() {
        // TODO: Replace with actual API call to fetch health news
        List<HealthNews> demoNews = createDemoNews();
        adapter.setNewsList(demoNews);
    }

    private void refreshNews() {
        // TODO: Implement actual refresh logic with API
        loadHealthNews();
        swipeRefresh.setRefreshing(false);
    }

    private List<HealthNews> createDemoNews() {
        List<HealthNews> news = new ArrayList<>();

        HealthNews news1 = new HealthNews();
        news1.title = "COVID-19 Update: New Variant Information";
        news1.content = "Stay informed about the latest COVID-19 variant and recommended precautions...";
        news1.author = "WHO Health Alert";
        news1.timestamp = new Date().getTime();
        news1.category = "Alert";
        news.add(news1);

        HealthNews news2 = new HealthNews();
        news2.title = "Seasonal Flu Advisory";
        news2.content = "Flu season is approaching. Here's what you need to know about prevention...";
        news2.author = "CDC Health Updates";
        news2.timestamp = new Date().getTime() - 86400000; // 1 day ago
        news2.category = "Advisory";
        news.add(news2);

        return news;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
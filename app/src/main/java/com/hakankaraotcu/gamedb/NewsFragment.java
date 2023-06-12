package com.hakankaraotcu.gamedb;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hakankaraotcu.gamedb.Adapter.NewsAdapter;
import com.hakankaraotcu.gamedb.General.AppGlobals;
import com.hakankaraotcu.gamedb.Model.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class NewsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NewsAdapter adapter;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    private News news;
    private Query mQuery;
    private ArrayList<News> allNews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        //webscrape ile haberleri ign sitesinden almak için alttaki iki kodu çalıştır.
        //Description_webscrape dw = new Description_webscrape();
        //dw.execute();

        mRecyclerView = view.findViewById(R.id.news_recyclerView);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        allNews = new ArrayList<>();

        mQuery = AppGlobals.db.collection("News");
        mQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    News news = documentSnapshot.toObject(News.class);

                    assert news != null;
                    news.setId(documentSnapshot.getId());
                    allNews.add(news);
                }
                adapter = new NewsAdapter(allNews, getContext());
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

                adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(News news, int position) {
                        Uri uri = Uri.parse(news.getUrl());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private class Description_webscrape extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect("https://www.ign.com/pc?filter=articles").get();
                Elements elementsSummary = document.select("div.item-details");
                Elements elementsImage = document.select("div.item-thumbnail");
                Elements elementsUrl = document.select("a.item-body");

                for (Element element : elementsSummary) {
                    String title = element.select("span").text();
                    titles.add(title);
                    if(titles.size() == 10){
                        break;
                    }
                }

                for(Element element : elementsUrl){
                    String url = "https://www.ign.com" + element.attr("href");
                    urls.add(url);
                    if(urls.size() == 10){
                        break;
                    }
                }

                for (Element element : elementsImage) {
                    String imageUrl = element.select("span").select("img").attr("src");
                    images.add(imageUrl);
                    if(images.size() == 10){
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (int i = 0; i < titles.size(); i++) {
                news = new News(titles.get(i), images.get(i), urls.get(i));
                AppGlobals.db.collection("News").add(news);
            }
        }
    }
}
package com.hakankaraotcu.gamedb.Model;

import com.hakankaraotcu.gamedb.R;

import java.util.ArrayList;

public class News {
    private String newsTitle, newsContent;
    private int newsImage;

    public News(){

    }

    public News(String newsTitle, String newsContent){
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public int getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(int newsImage) {
        this.newsImage = newsImage;
    }

    static public ArrayList<News> getData(){
        ArrayList<News> newsList = new ArrayList<News>();
        String[] titles = {"News1", "News2", "News3", "News4", "News5", "News6", "News7"};
        String[] contents = {"Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra"};
        int[] images = {R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal,};

        for(int i = 0;i < titles.length;i++){
            News news = new News();
            news.setNewsTitle(titles[i]);
            news.setNewsContent(contents[i]);
            news.setNewsImage(images[i]);

            newsList.add((news));
        }

        return newsList;
    }
}

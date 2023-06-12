package com.hakankaraotcu.gamedb.Model;

public class News {
    private String id, newsTitle;
    private String newsImage;
    private String url;

    public News(){

    }

    public News(String newsTitle, String newsImage, String url){
        this.newsTitle = newsTitle;
        this.newsImage = newsImage;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

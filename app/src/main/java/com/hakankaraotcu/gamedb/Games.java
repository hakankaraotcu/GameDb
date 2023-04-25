package com.hakankaraotcu.gamedb;

public class Games {
    private String id, name, releaseDate, content, image;
    private int metacritic;

    public Games(String name, String releaseDate, String content, Integer metacritic, String image){
        this.name = name;
        this.releaseDate = releaseDate;
        this.content = content;
        this.metacritic = metacritic;
        this.image = image;
    }

    public Games(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(int metacritic) {
        this.metacritic = metacritic;
    }
}

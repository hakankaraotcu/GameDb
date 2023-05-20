package com.hakankaraotcu.gamedb.Model;

public class Game {
    private String id, name, releaseDate, content, image;
    private int metacritic, numberOfPlayers, numberOfReviews, numberOfLists;

    public Game(String name, String releaseDate, String content, Integer metacritic, String image, Integer numberOfPlayers, Integer numberOfReviews, Integer numberOfLists){
        this.name = name;
        this.releaseDate = releaseDate;
        this.content = content;
        this.metacritic = metacritic;
        this.image = image;
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfReviews = numberOfReviews;
        this.numberOfLists = numberOfLists;
    }

    public Game(){

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

    public int getNumberOfLists() {
        return numberOfLists;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public void setNumberOfLists(int numberOfLists) {
        this.numberOfLists = numberOfLists;
    }

    public void addNumberOfLists(){
        this.numberOfLists++;
    }

}

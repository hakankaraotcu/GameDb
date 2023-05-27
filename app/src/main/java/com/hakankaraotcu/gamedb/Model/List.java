package com.hakankaraotcu.gamedb.Model;

public class List {
    private String id, name, description, userID;
    private int numberOfGames;

    public List() {

    }

    public List(String name, String description, int numberOfGames, String userID) {
        this.name = name;
        this.description = description;
        this.numberOfGames = numberOfGames;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String content) {
        this.description = content;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public void addNumberOfGames() {
        this.numberOfGames++;
    }
}

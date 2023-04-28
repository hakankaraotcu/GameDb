package com.hakankaraotcu.gamedb;

public class Lists {
    private String id, name, description, userID, username;
    private int numberOfGames;

    public Lists(){

    }
    public Lists(String name, String description, int numberOfGames, String userID, String username){
        this.name = name;
        this.description = description;
        this.numberOfGames = numberOfGames;
        this.userID = userID;
        this.username = username;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public void addNumberOfGames(){
        this.numberOfGames++;
    }
}

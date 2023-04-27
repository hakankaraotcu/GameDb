package com.hakankaraotcu.gamedb;

import com.hakankaraotcu.gamedb.Model.User;

public class Lists {
    private String id, name, description;
    private int numberOfGames;
    private User user;

    public Lists(){

    }
    public Lists(String name, String description, int numberOfGames, User user){
        this.name = name;
        this.description = description;
        this.numberOfGames = numberOfGames;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

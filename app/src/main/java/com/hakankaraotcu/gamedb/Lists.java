package com.hakankaraotcu.gamedb;

import com.hakankaraotcu.gamedb.Model.User;

public class Lists {
    private String id, name, description;
    private int count;
    private User user;

    public Lists(){

    }
    public Lists(String name, String description, int count, User user){
        this.name = name;
        this.description = description;
        this.count = count;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

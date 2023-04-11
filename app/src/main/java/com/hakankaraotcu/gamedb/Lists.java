package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Lists {
    private String id, name, description,username;
    private String userName;
    private int count;

    public Lists(){

    }
    public Lists(String name, String description){
        this.name = name;
        this.description = description;
    }

    public Lists(String name, String description, String userName, int count){
        this.name = name;
        this.description = description;
        this.userName = userName;
        this.count = count;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    static public ArrayList<Lists> getData(){
        ArrayList<Lists> lists = new ArrayList<>();
        String[] titles = {"Horror", "Adventure", "Action", "RPG", "Strategy"};
        String[] descriptions = {"The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested."};
        String[] userNames = {"Luke", "Marilyn", "Jake", "Mike", "Angel"};
        int[] counts = {70, 120, 14, 25, 230};

        for(int i = 0;i < titles.length;i++){
            Lists list = new Lists();
            list.setName(titles[i]);
            list.setDescription(descriptions[i]);
            list.setUserName(userNames[i]);
            list.setCount(counts[i]);

            lists.add(list);
        }
        return lists;
    }
}

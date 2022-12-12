package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Lists {
    private String listName;
    private String content;
    private String userName;
    private int count;

    public Lists(){

    }

    public Lists(String listName, String content, String userName, int count){
        this.listName = listName;
        this.content = content;
        this.userName = userName;
        this.count = count;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        String[] contents = {"The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested.", "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested."};
        String[] userNames = {"Luke", "Marilyn", "Jake", "Mike", "Angel"};
        int[] counts = {70, 120, 14, 25, 230};

        for(int i = 0;i < titles.length;i++){
            Lists list = new Lists();
            list.setListName(titles[i]);
            list.setContent(contents[i]);
            list.setUserName(userNames[i]);
            list.setCount(counts[i]);

            lists.add(list);
        }
        return lists;
    }
}

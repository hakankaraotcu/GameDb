package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Games {
    private String id, name, releaseDate, content, img;
    private int metacritic;
    private int image;

    public Games(String name, String releaseDate, String content, Integer metacritic, String img){
        this.name = name;
        this.releaseDate = releaseDate;
        this.content = content;
        this.metacritic = metacritic;
        this.img = img;
    }

    public Games(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
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

    static public ArrayList<Games> getData(){
        ArrayList<Games> games = new ArrayList<>();
        int[] images = {R.drawable.spiderman, R.drawable.discoelysium, R.drawable.darksiders2, R.drawable.eldenring, R.drawable.rdr, R.drawable.halflife, R.drawable.thephantompain, R.drawable.thelastofus, R.drawable.re4, R.drawable.persona5};

        for(int i = 0;i < images.length;i++){
            Games game = new Games();
            game.setImage(images[i]);

            games.add(game);
        }
        return games;
    }
}

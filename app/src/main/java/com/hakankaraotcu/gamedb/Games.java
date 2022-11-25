package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Games {
    private int image;

    public Games(){

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    static public ArrayList<Games> getData(){
        ArrayList<Games> games = new ArrayList<>();
        int[] images = {R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman};

        for(int i = 0;i < images.length;i++){
            Games game = new Games();
            game.setImage(images[i]);

            games.add(game);
        }
        return games;
    }
}

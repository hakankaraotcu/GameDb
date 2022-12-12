package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Reviews {
    private String gameName, gameDate,reviewContent;
    private int reviewGameImage;

    public Reviews(){

    }

    public Reviews(String gameName, String reviewContent){
        this.gameName = gameName;
        this.reviewContent = reviewContent;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public int getReviewGameImage() {
        return reviewGameImage;
    }

    public void setReviewGameImage(int reviewGameImage) {
        this.reviewGameImage = reviewGameImage;
    }

    static public ArrayList<Reviews> getData(){
        ArrayList<Reviews> reviewsList = new ArrayList<Reviews>();
        String[] names = {"Spider-Man", "Batman Arkham City", "Grand Theft Auto IV", "Darksiders II", "The Elder Scrools V: Skyrim", "Portal 2", "Elden Ring"};
        String[] dates = {"2018", "2011", "2008", "2012", "2011", "2011", "2022"};
        String[] contents = {"Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra"};
        int[] images = {R.drawable.spiderman, R.drawable.batmanarkhamcity, R.drawable.gta4, R.drawable.darksiders2, R.drawable.skyrim, R.drawable.portal, R.drawable.eldenring,};

        for(int i = 0;i < names.length;i++){
            Reviews review = new Reviews();
            review.setGameName(names[i]);
            review.setGameDate(dates[i]);
            review.setReviewContent(contents[i]);
            review.setReviewGameImage(images[i]);

            reviewsList.add((review));
        }

        return reviewsList;
    }
}

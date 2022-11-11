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
        String[] names = {"Spider-Man", "Spider-Man", "Spider-Man", "Spider-Man", "Spider-Man", "Spider-Man", "Spider-Man"};
        String[] dates = {"2022", "2022", "2022", "2022", "2022", "2022", "2022"};
        String[] contents = {"Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra"};
        int[] images = {R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman, R.drawable.spiderman,};

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

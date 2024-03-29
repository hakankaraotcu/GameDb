package com.hakankaraotcu.gamedb.Model;

public class Review {
    private String id, reviewContent, reviewDate, gameImage, gameID, gameName, gameReleaseDate, userID;
    private Float gameRating;

    public Review(){

    }

    public Review(String reviewContent, String reviewDate, String gameImage, Float gameRating, String gameID, String gameName, String gameReleaseDate, String userID){
        this.reviewContent = reviewContent;
        this.reviewDate = reviewDate;
        this.gameImage = gameImage;
        this.gameRating = gameRating;
        this.gameID = gameID;
        this.gameName = gameName;
        this.gameReleaseDate = gameReleaseDate;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public String getGameImage() {
        return gameImage;
    }

    public void setGameImage(String gameImage) {
        this.gameImage = gameImage;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameReleaseDate() {
        return gameReleaseDate;
    }

    public void setGameReleaseDate(String gameReleaseDate) {
        this.gameReleaseDate = gameReleaseDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Float getGameRating() {
        return gameRating;
    }

    public void setGameRating(Float gameRating) {
        this.gameRating = gameRating;
    }
}

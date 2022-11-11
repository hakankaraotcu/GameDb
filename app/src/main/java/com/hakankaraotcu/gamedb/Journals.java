package com.hakankaraotcu.gamedb;

import java.util.ArrayList;

public class Journals {
    private String journalTitle, journalContent;
    private int journalImage;

    public Journals(){

    }

    public Journals(String journalTitle, String journalContent){
        this.journalTitle = journalTitle;
        this.journalContent = journalContent;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }

    public int getJournalImage() {
        return journalImage;
    }

    public void setJournalImage(int journalImage) {
        this.journalImage = journalImage;
    }

    static public ArrayList<Journals> getData(){
        ArrayList<Journals> journalsList = new ArrayList<Journals>();
        String[] titles = {"News1", "News2", "News3", "News4", "News5", "News6", "News7"};
        String[] contents = {"Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra", "Overwatch 2 Announces New Tank Hero, Ramattra"};
        int[] images = {R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal, R.drawable.journal,};

        for(int i = 0;i < titles.length;i++){
            Journals journal = new Journals();
            journal.setJournalTitle(titles[i]);
            journal.setJournalContent(contents[i]);
            journal.setJournalImage(images[i]);

            journalsList.add((journal));
        }

        return journalsList;
    }
}

package edu.lawrence.hw_survey;

/**
 * Created by user on 2/7/2018.
 */

public class Survey {
    private int idsurvey;
    private String title;
    private String prompt;
    private int owner;

    public Survey() {
    }

    public int getIdsurvey() {
        return idsurvey;
    }

    public void setIdsurvey(int idsurvey) {
        this.idsurvey = idsurvey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
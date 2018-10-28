package edu.lawrence.hw_survey;

/**
 * Created by user on 2/7/2018.
 */

public class Response {
    private transient int idresponse;
    private int survey; // id number of the survey this response is for
    private int question; // id number of the question this response is for
        private String response; // The user's response to the question

    public Response() {
    }

    public int getIdresponse() {
        return idresponse;
    }

    public void setIdresponse(int idresponse) {
        this.idresponse = idresponse;
    }

    public int getSurvey() {
        return survey;
    }

    public void setSurvey(int survey) {
        this.survey = survey;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
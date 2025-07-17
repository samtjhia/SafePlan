package com.b07safetyplanapp.models;

public class QuestionField {
    private String question_id;
    private String question;

    // Constructors
    public QuestionField() {}

    public QuestionField(String question_id, String question) {
        this.question_id = question_id;
        this.question = question;
    }

    // Getters and setters
    public String getQuestion_id() {

        return question_id;
    }
    public void setQuestion_id(String question_id) {

        this.question_id = question_id;
    }

    public String getQuestion() {

        return question;
    }
    public void setQuestion(String question) {

        this.question = question;
    }
}

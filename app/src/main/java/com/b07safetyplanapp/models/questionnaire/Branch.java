package com.b07safetyplanapp.models.questionnaire;

import java.util.List;

public class Branch {
    private String situation;
    private List<Question> questions;

    // Constructors
    public Branch() {}

    public Branch(String situation, List<Question> questions) {
        this.situation = situation;
        this.questions = questions;
    }

    // Getters and setters
    public String getSituation() {

        return situation;
    }

    public void setSituation(String situation) {

        this.situation = situation;
    }

    public List<Question> getQuestions() {

        return questions;
    }

    public void setQuestions(List<Question> questions) {

        this.questions = questions;
    }
}

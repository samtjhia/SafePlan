package com.b07safetyplanapp.models.questionnaire;

import java.util.List;

/**
 * Represents a specific situation in the questionnaire that contains a list of related questions.
 * This is used for branching logic where different scenarios may have different sets of questions.
 */
public class Branch {

    private String situation;

    private List<Question> questions;

    public Branch() {}

    /**
     * Constructs a new Branch with a specific situation and a list of questions.
     *
     * @param situation description or label for the situation
     * @param questions list of questions applicable to the situation
     */
    public Branch(String situation, List<Question> questions) {
        this.situation = situation;
        this.questions = questions;
    }


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

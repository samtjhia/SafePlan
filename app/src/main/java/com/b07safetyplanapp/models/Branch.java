package com.b07safetyplanapp.models;

import java.util.List;

/**
 * Represents a branching situation in the safety plan questionnaire.
 * Each branch corresponds to a specific situation and contains a list of follow-up questions.
 */
public class Branch {
    private String situation;
    private List<Question> questions;

    public Branch() {}

    /**
     * Constructs a Branch object with the specified situation and list of questions.
     *
     * @param situation a label or description of the situation (e.g., "at home", "in public")
     * @param questions a list of {@link Question} objects associated with this situation
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

package com.b07safetyplanapp.models;

import java.util.List;

/**
 * Represents a branching situation in the safety plan questionnaire.
 * Each branch corresponds to a specific situation and contains a list of follow-up questions.
 */
public class Branch {
    private String situation;
    private List<Question> questions;

    /**
     * Default constructor required for Firebase deserialization.
     */
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

    /**
     * Gets the situation associated with this branch.
     *
     * @return the situation label
     */
    public String getSituation() {
        return situation;
    }

    /**
     * Sets the situation label for this branch.
     *
     * @param situation the situation to set
     */
    public void setSituation(String situation) {
        this.situation = situation;
    }

    /**
     * Gets the list of questions associated with this branch.
     *
     * @return a list of {@link Question} objects
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions for this branch.
     *
     * @param questions a list of {@link Question} objects to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

package com.b07safetyplanapp.models.questionnaire;

import java.util.List;

/**
 * Represents a specific situation in the questionnaire that contains a list of related questions.
 * This is used for branching logic where different scenarios may have different sets of questions.
 */
public class Branch {

    /**
     * A description or identifier for the situation this branch covers (e.g., "school", "workplace").
     */
    private String situation;

    /**
     * A list of questions relevant to the given situation.
     */
    private List<Question> questions;

    /**
     * Default constructor required for data deserialization and frameworks like Firebase.
     */
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

    /**
     * Gets the situation associated with this branch.
     *
     * @return the situation label
     */
    public String getSituation() {
        return situation;
    }

    /**
     * Sets the situation for this branch.
     *
     * @param situation the situation label to set
     */
    public void setSituation(String situation) {
        this.situation = situation;
    }

    /**
     * Gets the list of questions tied to this situation.
     *
     * @return list of questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets the list of questions for this branch.
     *
     * @param questions list of questions to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}

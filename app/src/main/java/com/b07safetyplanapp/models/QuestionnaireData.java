package com.b07safetyplanapp.models;

import java.util.List;

/**
 * Represents the structured sections of the questionnaire.
 * It contains warm-up questions, branch-specific sections, and follow-up questions.
 */
public class QuestionnaireData {

    private List<Question> warm_up;
    private List<Branch> branch_specific;
    private List<Question> follow_up;


    public QuestionnaireData() {}

    /**
     * Gets the list of warm-up questions shown at the beginning of the questionnaire.
     *
     * @return A list of {@link Question} objects for warm-up.
     */
    public List<Question> getWarm_up() {
        return warm_up;
    }

    /**
     * Sets the warm-up questions.
     *
     * @param warm_up A list of {@link Question} objects for the warm-up section.
     */
    public void setWarm_up(List<Question> warm_up) {
        this.warm_up = warm_up;
    }

    /**
     * Gets the list of branch-specific questionnaire sections.
     * Each branch can contain its own set of questions.
     *
     * @return A list of {@link Branch} objects.
     */
    public List<Branch> getBranch_specific() {
        return branch_specific;
    }

    /**
     * Sets the branch-specific sections of the questionnaire.
     *
     * @param branch_specific A list of {@link Branch} objects to set.
     */
    public void setBranch_specific(List<Branch> branch_specific) {
        this.branch_specific = branch_specific;
    }

    /**
     * Gets the list of follow-up questions shown at the end of the questionnaire.
     *
     * @return A list of {@link Question} objects for follow-up.
     */
    public List<Question> getFollow_up() {
        return follow_up;
    }

    /**
     * Sets the follow-up questions.
     *
     * @param follow_up A list of {@link Question} objects for the follow-up section.
     */
    public void setFollow_up(List<Question> follow_up) {
        this.follow_up = follow_up;
    }
}

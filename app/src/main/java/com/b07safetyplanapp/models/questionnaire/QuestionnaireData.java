package com.b07safetyplanapp.models.questionnaire;

import java.util.List;

/**
 * Represents the structured data of a questionnaire, including its warm-up,
 * branch-specific, and follow-up questions.
 */
public class QuestionnaireData {


    private List<Question> warm_up;

    private List<Branch> branch_specific;


    private List<Question> follow_up;


    public QuestionnaireData() {}

    public List<Question> getWarm_up() {
        return warm_up;
    }


    public void setWarm_up(List<Question> warm_up) {
        this.warm_up = warm_up;
    }

    /**
     * Gets the list of branch-specific question groups.
     *
     * @return a list of {@link Branch} objects representing situation-based questions.
     */
    public List<Branch> getBranch_specific() {
        return branch_specific;
    }

    /**
     * Sets the list of branch-specific question groups.
     *
     * @param branch_specific the list of {@link Branch} objects to be assigned.
     */
    public void setBranch_specific(List<Branch> branch_specific) {
        this.branch_specific = branch_specific;
    }

    /**
     * Gets the list of follow-up questions.
     *
     * @return a list of {@link Question} objects for the follow-up section.
     */
    public List<Question> getFollow_up() {
        return follow_up;
    }

    /**
     * Sets the list of follow-up questions.
     *
     * @param follow_up the list of {@link Question} objects to be assigned.
     */
    public void setFollow_up(List<Question> follow_up) {
        this.follow_up = follow_up;
    }
}

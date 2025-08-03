package com.b07safetyplanapp.models.questionnaire;

import java.util.List;

public class QuestionnaireData {
    private List<Question> warm_up;
    private List<Branch> branch_specific;
    private List<Question> follow_up;

    // Constructor
    public QuestionnaireData() {}

    public List<Question> getWarm_up() {

        return warm_up;
    }
    public void setWarm_up(List<Question> warm_up) {

        this.warm_up = warm_up;
    }

    public List<Branch> getBranch_specific() {

        return branch_specific;
    }
    public void setBranch_specific(List<Branch> branch_specific) {

        this.branch_specific = branch_specific;
    }

    public List<Question> getFollow_up() {

        return follow_up;
    }
    public void setFollow_up(List<Question> follow_up) {

        this.follow_up = follow_up;
    }
}

package com.b07safetyplanapp.models;

import java.util.Map;

public class QuestionnaireRoot {
    private QuestionnaireData questionnaire;
    private Map<String, Object> tips;

    // Constructor
    public QuestionnaireRoot() {}

    // Getters and setters
    public QuestionnaireData getQuestionnaire() {
        return questionnaire;
    }
    public void setQuestionnaire(QuestionnaireData questionnaire) {
        this.questionnaire = questionnaire;
    }

    // Andy will change this below to match Tips class
    public Map<String, Object> getTips() {

        return tips;
    }
    public void setTips(Map<String, Object> tips) {

        this.tips = tips;
    }
}
package com.b07safetyplanapp.models.questionnaire;

import java.util.Map;

public class QuestionnaireRoot {
    private QuestionnaireData questionnaire;
    private Map<String, TipData> tips;

    // Constructor
    public QuestionnaireRoot() {}

    // Getters and setters
    public QuestionnaireData getQuestionnaire() {

        return questionnaire;
    }
    public void setQuestionnaire(QuestionnaireData questionnaire) {
        this.questionnaire = questionnaire;
    }
    public Map<String, TipData> getTips() {
        return tips;
    }
    public void setTips(Map<String, TipData> tips) {
        this.tips = tips;
    }
}
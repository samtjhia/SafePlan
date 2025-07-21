package com.b07safetyplanapp.models.questionnaire;

public class QuestionnaireRoot {
    private QuestionnaireData questionnaire;

    // Constructor
    public QuestionnaireRoot() {}

    // Getters and setters
    public QuestionnaireData getQuestionnaire() {

        return questionnaire;
    }
    public void setQuestionnaire(QuestionnaireData questionnaire) {
        this.questionnaire = questionnaire;
    }
}
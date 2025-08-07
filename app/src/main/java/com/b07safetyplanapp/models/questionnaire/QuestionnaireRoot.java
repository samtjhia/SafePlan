package com.b07safetyplanapp.models.questionnaire;

import java.util.Map;

/**
 * Represents the root structure of the questionnaire data model.
 * This class encapsulates the main questionnaire sections and the associated tips.
 */
public class QuestionnaireRoot {

    private QuestionnaireData questionnaire;

    private Map<String, TipData> tips;

    public QuestionnaireRoot() {}

    /**
     * Gets the questionnaire data.
     *
     * @return the {@link QuestionnaireData} object containing all sections of the questionnaire.
     */
    public QuestionnaireData getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Sets the questionnaire data.
     *
     * @param questionnaire the {@link QuestionnaireData} object to be assigned.
     */
    public void setQuestionnaire(QuestionnaireData questionnaire) {
        this.questionnaire = questionnaire;
    }


    public Map<String, TipData> getTips() {
        return tips;
    }

    /**
     * Sets the map of tips for the questionnaire.
     *
     * @param tips a map of question IDs to their respective {@link TipData}.
     */
    public void setTips(Map<String, TipData> tips) {
        this.tips = tips;
    }
}

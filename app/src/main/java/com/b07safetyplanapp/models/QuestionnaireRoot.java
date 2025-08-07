package com.b07safetyplanapp.models;

import java.util.Map;

/**
 * Represents the root structure of the questionnaire JSON.
 * It contains both the questionnaire data and the associated safety tips.
 */
public class QuestionnaireRoot {

    private QuestionnaireData questionnaire;
    private Map<String, Object> tips;

    public QuestionnaireRoot() {}

    /**
     * Gets the questionnaire data object.
     *
     * @return The {@link QuestionnaireData} containing questions and structure.
     */
    public QuestionnaireData getQuestionnaire() {
        return questionnaire;
    }

    /**
     * Sets the questionnaire data.
     *
     * @param questionnaire The {@link QuestionnaireData} to be set.
     */
    public void setQuestionnaire(QuestionnaireData questionnaire) {
        this.questionnaire = questionnaire;
    }

    /**
     * Gets the safety tips associated with the questionnaire.
     * Currently represented as a map of tips. This may be replaced with a strongly typed `Tips` class in the future.
     *
     * @return A {@link Map} of tips where the key is a string and the value is an object.
     */
    public Map<String, Object> getTips() {
        return tips;
    }

    public void setTips(Map<String, Object> tips) {
        this.tips = tips;
    }
}

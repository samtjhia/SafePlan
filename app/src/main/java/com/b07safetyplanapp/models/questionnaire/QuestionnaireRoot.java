package com.b07safetyplanapp.models.questionnaire;

import java.util.Map;

/**
 * Represents the root structure of the questionnaire data model.
 * This class encapsulates the main questionnaire sections and the associated tips.
 */
public class QuestionnaireRoot {

    /**
     * The main questionnaire data, including warm-up, branch-specific, and follow-up questions.
     */
    private QuestionnaireData questionnaire;

    /**
     * A map of tips associated with the questionnaire.
     * The key is typically a question ID, and the value contains detailed tip data.
     */
    private Map<String, TipData> tips;

    /**
     * Default constructor required for data binding or deserialization.
     */
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

    /**
     * Gets the map of tips associated with the questionnaire.
     *
     * @return a map where each key is a question ID and the value is its corresponding {@link TipData}.
     */
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

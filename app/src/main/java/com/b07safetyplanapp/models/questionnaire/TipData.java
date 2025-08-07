package com.b07safetyplanapp.models.questionnaire;

import java.util.Map;

/**
 * Represents a set of tips associated with a specific questionnaire question.
 * This may include a general tip and optionally tips based on specific answer options.
 */
public class TipData {

    /**
     * The type of the tip (e.g., "informational", "warning", "support").
     */
    private String tip_type;

    /**
     * A general tip related to the question, applicable regardless of the selected answer.
     */
    private String general_tip;

    /**
     * A map of option-specific tips, where the key is the answer option
     * and the value is the corresponding tip text.
     */
    private Map<String, String> option_tips;

    /**
     * Default constructor required for Firebase or serialization.
     */
    public TipData() {}

    /**
     * Gets the type of the tip.
     *
     * @return the tip type
     */
    public String getTip_type() {
        return tip_type;
    }

    /**
     * Sets the type of the tip.
     *
     * @param tip_type the tip type to set
     */
    public void setTip_type(String tip_type) {
        this.tip_type = tip_type;
    }

    /**
     * Gets the general tip associated with this question.
     *
     * @return the general tip
     */
    public String getGeneral_tip() {
        return general_tip;
    }

    /**
     * Sets the general tip text.
     *
     * @param general_tip the general tip to set
     */
    public void setGeneral_tip(String general_tip) {
        this.general_tip = general_tip;
    }

    /**
     * Gets the map of tips associated with specific answer options.
     *
     * @return a map of option-specific tips
     */
    public Map<String, String> getOption_tips() {
        return option_tips;
    }

    /**
     * Sets the option-specific tips map.
     *
     * @param option_tips a map where the key is an answer option and the value is the tip text
     */
    public void setOption_tips(Map<String, String> option_tips) {
        this.option_tips = option_tips;
    }
}

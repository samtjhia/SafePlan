package com.b07safetyplanapp.models;

/**
 * Represents a sub-question that is conditionally displayed based on a user's answer.
 * A sub-question has a condition (i.e., trigger value) and its associated question field.
 */
public class SubQuestion {

    private String condition;
    private QuestionField field;

    /**
     * Default constructor required for Firebase or JSON deserialization.
     */
    public SubQuestion() {}

    /**
     * Constructs a SubQuestion with a specified condition and field.
     *
     * @param condition The condition value that triggers this sub-question.
     * @param field     The question field that should be shown if the condition is met.
     */
    public SubQuestion(String condition, QuestionField field) {
        this.condition = condition;
        this.field = field;
    }

    /**
     * Gets the condition that triggers this sub-question.
     *
     * @return The condition value (e.g., answer to a parent question).
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition that triggers this sub-question.
     *
     * @param condition The condition value.
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the question field associated with this sub-question.
     *
     * @return The {@link QuestionField} object.
     */
    public QuestionField getField() {
        return field;
    }

    /**
     * Sets the question field for this sub-question.
     *
     * @param field The {@link QuestionField} object.
     */
    public void setField(QuestionField field) {
        this.field = field;
    }
}

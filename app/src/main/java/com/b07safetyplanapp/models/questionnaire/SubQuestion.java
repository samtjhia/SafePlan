package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a sub-question that is conditionally shown based on a user's answer.
 * This class holds the condition (answer value that triggers the sub-question)
 * and the actual question field to display when the condition is met.
 */
public class SubQuestion {

    /**
     * The condition value (usually an option string) that triggers the display of the sub-question.
     */
    private String condition;

    /**
     * The question field to be shown when the condition is satisfied.
     */
    private QuestionField field;

    /**
     * Default constructor required for data binding or deserialization.
     */
    public SubQuestion() {}

    /**
     * Constructs a SubQuestion with the specified condition and question field.
     *
     * @param condition the condition value that triggers this sub-question
     * @param field the QuestionField to display when the condition is met
     */
    public SubQuestion(String condition, QuestionField field) {
        this.condition = condition;
        this.field = field;
    }

    /**
     * Gets the condition that triggers this sub-question.
     *
     * @return the condition string
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Sets the condition that triggers this sub-question.
     *
     * @param condition the condition string
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Gets the question field to display when the condition is met.
     *
     * @return the QuestionField object
     */
    public QuestionField getField() {
        return field;
    }

    /**
     * Sets the question field to display when the condition is met.
     *
     * @param field the QuestionField object
     */
    public void setField(QuestionField field) {
        this.field = field;
    }
}

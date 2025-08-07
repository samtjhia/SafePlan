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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


    public QuestionField getField() {
        return field;
    }


    public void setField(QuestionField field) {
        this.field = field;
    }
}

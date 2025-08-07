package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a sub-question that is conditionally shown based on a user's answer.
 * This class holds the condition (answer value that triggers the sub-question)
 * and the actual question field to display when the condition is met.
 */
public class SubQuestion {

    private String condition;

    private QuestionField field;

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

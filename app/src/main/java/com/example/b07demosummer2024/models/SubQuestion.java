package com.example.b07demosummer2024.models;

public class SubQuestion {
    private String condition;
    private QuestionField field;

    // Constructors
    public SubQuestion() {}

    public SubQuestion(String condition, QuestionField field) {
        this.condition = condition;
        this.field = field;
    }

    // Getters and setters
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

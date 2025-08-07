package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a field that contains a single question and its identifier.
 * This is often used within sub-questions for conditional display.
 */
public class QuestionField {


    private String question_id;

    private String question;

    public QuestionField() {}

    /**
     * Constructs a new {@code QuestionField} with the specified ID and question text.
     *
     * @param question_id the unique identifier for the question
     * @param question    the actual question text
     */
    public QuestionField(String question_id, String question) {
        this.question_id = question_id;
        this.question = question;
    }

    public String getQuestion_id() {
        return question_id;
    }


    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

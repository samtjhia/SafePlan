package com.b07safetyplanapp.models;

/**
 * Represents a simplified version of a question, typically used within sub-questions.
 * It includes a question ID and the question text.
 */
public class QuestionField {

    private String question_id;
    private String question;

    public QuestionField() {}

    /**
     * Constructs a QuestionField with a question ID and the question text.
     *
     * @param question_id the unique identifier of the question
     * @param question the actual question text
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

package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a field that contains a single question and its identifier.
 * This is often used within sub-questions for conditional display.
 */
public class QuestionField {

    /**
     * Unique identifier for the question.
     */
    private String question_id;

    /**
     * The text/content of the question.
     */
    private String question;

    /**
     * Default constructor required for deserialization or data binding.
     */
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

    /**
     * Gets the question ID.
     *
     * @return the ID of the question
     */
    public String getQuestion_id() {
        return question_id;
    }

    /**
     * Sets the question ID.
     *
     * @param question_id the new question ID to set
     */
    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    /**
     * Gets the question text.
     *
     * @return the content of the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question text.
     *
     * @param question the new question content to set
     */
    public void setQuestion(String question) {
        this.question = question;
    }
}

package com.b07safetyplanapp.models;

/**
 * Represents a user's response to a questionnaire question.
 * Includes the question ID, main answer, and an optional sub-answer.
 */
public class UserResponse {

    private String questionId;
    private String answer;
    private String subAnswer;

    /**
     * Default constructor required for Firebase serialization.
     */
    public UserResponse() {}

    /**
     * Constructor with required fields.
     *
     * @param questionId The ID of the question answered.
     * @param answer     The user's main answer.
     */
    public UserResponse(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    /**
     * Gets the ID of the question.
     *
     * @return The question ID.
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Sets the ID of the question.
     *
     * @param questionId The question ID.
     */
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the main answer provided by the user.
     *
     * @return The user's answer.
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the main answer for the question.
     *
     * @param answer The user's answer.
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Gets the sub-answer, if any, provided by the user.
     *
     * @return The user's sub-answer.
     */
    public String getSubAnswer() {
        return subAnswer;
    }

    /**
     * Sets an optional sub-answer for the question.
     *
     * @param subAnswer The user's sub-answer.
     */
    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }
}

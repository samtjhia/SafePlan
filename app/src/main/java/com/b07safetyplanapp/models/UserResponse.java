package com.b07safetyplanapp.models;

/**
 * Represents a user's response to a questionnaire question.
 * Includes the question ID, main answer, and an optional sub-answer.
 */
public class UserResponse {

    private String questionId;
    private String answer;
    private String subAnswer;


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


    public String getQuestionId() {
        return questionId;
    }


    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }
}

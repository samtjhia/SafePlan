package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a user's response to a single questionnaire question.
 */
public class UserResponse {

    private String questionId;

    private String answer;


    public UserResponse() {}

    /**
     * Constructs a UserResponse with a given question ID and answer.
     *
     * @param questionId the unique identifier of the question
     * @param answer the user's answer to the question
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
}

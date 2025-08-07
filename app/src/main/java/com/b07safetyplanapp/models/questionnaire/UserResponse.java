package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a user's response to a single questionnaire question.
 */
public class UserResponse {

    /**
     * The ID of the question the user responded to.
     */
    private String questionId;

    /**
     * The answer provided by the user.
     */
    private String answer;

    /**
     * Default constructor required for Firebase and serialization.
     */
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

    /**
     * Returns the ID of the question.
     *
     * @return the question ID
     */
    public String getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question ID.
     *
     * @param questionId the unique identifier of the question
     */
    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    /**
     * Returns the user's answer to the question.
     *
     * @return the user's answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the user's answer.
     *
     * @param answer the answer to be set
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }
}

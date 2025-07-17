package com.example.b07demosummer2024.models;

public class UserResponse {
    private String questionId;
    private String answer;
    private String subAnswer;

    // Constructors
    public UserResponse() {}

    public UserResponse(String questionId, String answer) {
        this.questionId = questionId;
        this.answer = answer;
    }

    // Getters and setters
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

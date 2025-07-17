package com.b07safetyplanapp.models;

public class Question {
    private String question_id;
    private String question;
    private String[] options;
    private SubQuestion sub_question;

    // Constructors
    public Question() {}

    public Question(String question_id, String question, String[] options, SubQuestion sub_question) {
        this.question_id = question_id;
        this.question = question;
        this.options = options;
        this.sub_question = sub_question;
    }

    // Getters and setters
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

    public String[] getOptions() {

        return options;
    }
    public void setOptions(String[] options) {

        this.options = options;
    }

    public SubQuestion getSub_question() {

        return sub_question;
    }
    public void setSub_question(SubQuestion sub_question) {

        this.sub_question = sub_question;
    }

    public boolean hasOptions() {

        return options != null && options.length > 0;
    }
    public boolean hasSubQuestion() {

        return sub_question != null;
    }

}

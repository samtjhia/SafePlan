package com.b07safetyplanapp.models;

/**
 * Represents a question in the safety plan questionnaire.
 * Each question may contain a list of options and an optional sub-question based on a condition.
 */
public class Question {
    private String question_id;
    private String question;
    private String[] options;
    private SubQuestion sub_question;

    public Question() {}

    /**
     * Constructs a Question object with the specified parameters.
     *
     * @param question_id the unique identifier for the question
     * @param question the text of the question
     * @param options the available options for the question
     * @param sub_question an optional sub-question that may appear based on the selected option
     */
    public Question(String question_id, String question, String[] options, SubQuestion sub_question) {
        this.question_id = question_id;
        this.question = question;
        this.options = options;
        this.sub_question = sub_question;
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


    public String[] getOptions() {
        return options;
    }


    public void setOptions(String[] options) {
        this.options = options;
    }

    /**
     * Gets the sub-question associated with this question, if any.
     *
     * @return the sub-question or null
     */
    public SubQuestion getSub_question() {
        return sub_question;
    }

    /**
     * Sets the sub-question for this question.
     *
     * @param sub_question the sub-question to associate
     */
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

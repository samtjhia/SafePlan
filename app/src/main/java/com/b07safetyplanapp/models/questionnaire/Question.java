package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a question in the questionnaire, potentially with selectable options and a conditional sub-question.
 */
public class Question {

    /**
     * Unique identifier for the question.
     */
    private String question_id;

    /**
     * The actual question text to be presented to the user.
     */
    private String question;

    /**
     * An array of selectable options for this question.
     */
    private String[] options;

    /**
     * A sub-question that may be conditionally shown based on the user's answer to this question.
     */
    private SubQuestion sub_question;

    /**
     * Default constructor required for deserialization and data binding.
     */
    public Question() {}

    /**
     * Constructs a new {@code Question} with all fields specified.
     *
     * @param question_id   unique identifier for the question
     * @param question      the question text
     * @param options       array of answer options
     * @param sub_question  optional sub-question that appears conditionally
     */
    public Question(String question_id, String question, String[] options, SubQuestion sub_question) {
        this.question_id = question_id;
        this.question = question;
        this.options = options;
        this.sub_question = sub_question;
    }

    /**
     * Returns the unique ID of the question.
     *
     * @return the question ID
     */
    public String getQuestion_id() {
        return question_id;
    }

    /**
     * Sets the question ID.
     *
     * @param question_id the unique ID to assign
     */
    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    /**
     * Returns the text of the question.
     *
     * @return the question text
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets the question text.
     *
     * @param question the text of the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Returns the array of available options.
     *
     * @return array of options, or null if none
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Sets the answer options for this question.
     *
     * @param options array of option strings
     */
    public void setOptions(String[] options) {
        this.options = options;
    }

    /**
     * Returns the sub-question associated with this question, if any.
     *
     * @return the sub-question or null
     */
    public SubQuestion getSub_question() {
        return sub_question;
    }

    /**
     * Sets the sub-question.
     *
     * @param sub_question the sub-question object
     */
    public void setSub_question(SubQuestion sub_question) {
        this.sub_question = sub_question;
    }

    /**
     * Checks if the question has selectable options.
     *
     * @return true if options are present; false otherwise
     */
    public boolean hasOptions() {
        return options != null && options.length > 0;
    }

    /**
     * Checks if the question has a sub-question.
     *
     * @return true if a sub-question is defined; false otherwise
     */
    public boolean hasSubQuestion() {
        return sub_question != null;
    }
}

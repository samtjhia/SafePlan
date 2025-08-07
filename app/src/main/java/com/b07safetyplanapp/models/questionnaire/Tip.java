package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a basic tip consisting of a title and body.
 * Typically used for displaying helpful guidance or suggestions.
 */
public class Tip {

    private String title;

    private String body;

    /**
     * Constructs a new Tip with the specified title and body.
     *
     * @param title the title of the tip
     * @param body  the content of the tip
     */
    public Tip(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}

package com.b07safetyplanapp.models.questionnaire;

/**
 * Represents a basic tip consisting of a title and body.
 * Typically used for displaying helpful guidance or suggestions.
 */
public class Tip {

    /**
     * The title or headline of the tip.
     */
    private String title;

    /**
     * The main content or message of the tip.
     */
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

    /**
     * Returns the title of the tip.
     *
     * @return the tip title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the body content of the tip.
     *
     * @return the tip body
     */
    public String getBody() {
        return body;
    }
}

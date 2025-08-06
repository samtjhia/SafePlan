package com.b07safetyplanapp.models.questionnaire;

public class Tip {
    private String title;
    private String body;

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

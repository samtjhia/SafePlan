package com.b07safetyplanapp.models.supportresources;

/**
 * Represents a support resource such as a website, helpline, or informational page
 * that can assist users in developing or accessing their safety plan.
 */
public class SupportResource {

    private String name;
    private String type;
    private String url;


    public SupportResource() {}


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}

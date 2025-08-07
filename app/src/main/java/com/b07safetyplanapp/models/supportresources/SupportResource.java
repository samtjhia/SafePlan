package com.b07safetyplanapp.models.supportresources;

/**
 * Represents a support resource such as a website, helpline, or informational page
 * that can assist users in developing or accessing their safety plan.
 */
public class SupportResource {

    private String name;
    private String type;
    private String url;

    /**
     * Default constructor required for Firebase or Gson deserialization.
     */
    public SupportResource() {}

    /**
     * Returns the name of the support resource.
     *
     * @return the name of the resource (e.g., "Crisis Line", "Shelter Finder")
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of support resource.
     *
     * @return the type of resource (e.g., "Website", "Hotline", "PDF")
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the URL or link to access the support resource.
     *
     * @return the URL of the resource
     */
    public String getUrl() {
        return url;
    }
}

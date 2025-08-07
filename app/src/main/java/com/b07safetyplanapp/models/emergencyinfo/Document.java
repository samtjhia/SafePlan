package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents an emergency document (e.g., medical record, safety plan) stored for the user.
 */
public class Document {

    private String id;

    private String title;

    private String description;

    private String downloadUrl;

    private long timestamp;

    public Document() {
        // Default constructor
    }

    public Document(String id, String title, String description, String downloadUrl, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.downloadUrl = downloadUrl;
        this.timestamp = timestamp;
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

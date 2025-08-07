package com.b07safetyplanapp.models.emergencyinfo;

/**
 * Represents an emergency document (e.g., medical record, safety plan) stored for the user.
 */
public class Document {

    /**
     * Unique identifier for the document (e.g., Firebase key).
     */
    private String id;

    /**
     * Title of the document.
     */
    private String title;

    /**
     * Short description of the document's contents.
     */
    private String description;

    /**
     * URL to download or view the document.
     */
    private String downloadUrl;

    /**
     * Timestamp indicating when the document was uploaded or last modified.
     */
    private long timestamp;

    /**
     * Default constructor required for Firebase and serialization.
     */
    public Document() {
        // Default constructor
    }

    /**
     * Constructs a new Document with provided values.
     *
     * @param id          the unique document ID
     * @param title       the document title
     * @param description short description of the document
     * @param downloadUrl link to download the document
     * @param timestamp   upload or modification time (in milliseconds)
     */
    public Document(String id, String title, String description, String downloadUrl, long timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.downloadUrl = downloadUrl;
        this.timestamp = timestamp;
    }

    // Getters

    /**
     * Returns the document ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the document title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the document description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the URL to download or view the document.
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * Returns the document's timestamp in milliseconds.
     */
    public long getTimestamp() {
        return timestamp;
    }

    // Setters

    /**
     * Sets the document ID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the document title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the document description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the download URL for the document.
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * Sets the timestamp for the document.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

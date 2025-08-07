package com.b07safetyplanapp.reminders;

/**
 * Represents a reminder item with scheduling details such as time and frequency.
 * Used to store and retrieve reminder data from Firebase Realtime Database.
 */
public class Reminder {
    private String id;
    private String title;
    private String frequency;
    private int hour;
    private int minute;

    /**
     * Default no-argument constructor required for Firebase deserialization.
     */
    public Reminder() {
        // Required by Firebase
    }

    /**
     * Constructs a new Reminder instance with all properties initialized.
     *
     * @param id        Unique identifier for the reminder
     * @param title     Title or label of the reminder
     * @param frequency Frequency (e.g., "Daily", "Weekly", etc.)
     * @param hour      Hour (0–23) when the reminder should trigger
     * @param minute    Minute (0–59) when the reminder should trigger
     */
    public Reminder(String id, String title, String frequency, int hour, int minute) {
        this.id = id;
        this.title = title;
        this.frequency = frequency;
        this.hour = hour;
        this.minute = minute;
    }

    /** @return Unique identifier for the reminder */
    public String getId() { return id; }

    /** @return Title or description of the reminder */
    public String getTitle() { return title; }

    /** @return Frequency of the reminder (e.g., Daily, Weekly) */
    public String getFrequency() { return frequency; }

    /** @return Hour (0–23) at which the reminder triggers */
    public int getHour() { return hour; }

    /** @return Minute (0–59) at which the reminder triggers */
    public int getMinute() { return minute; }

    /** @param id Unique identifier to set for the reminder */
    public void setId(String id) { this.id = id; }

    /** @param title Title or label to set for the reminder */
    public void setTitle(String title) { this.title = title; }

    /** @param frequency Frequency string to set (e.g., Daily, Weekly) */
    public void setFrequency(String frequency) { this.frequency = frequency; }

    /** @param hour Hour (0–23) to set for the reminder trigger */
    public void setHour(int hour) { this.hour = hour; }

    /** @param minute Minute (0–59) to set for the reminder trigger */
    public void setMinute(int minute) { this.minute = minute; }
}

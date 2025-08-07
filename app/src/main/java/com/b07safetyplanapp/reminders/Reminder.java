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


    public String getId() { return id; }


    public String getTitle() { return title; }


    public String getFrequency() { return frequency; }


    public int getHour() { return hour; }


    public int getMinute() { return minute; }


    public void setId(String id) { this.id = id; }


    public void setTitle(String title) { this.title = title; }


    public void setFrequency(String frequency) { this.frequency = frequency; }


    public void setHour(int hour) { this.hour = hour; }


    public void setMinute(int minute) { this.minute = minute; }
}

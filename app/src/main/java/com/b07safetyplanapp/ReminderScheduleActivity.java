package com.b07safetyplanapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.provider.Settings;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReminderScheduleActivity extends AppCompatActivity {

    private EditText etReminderTitle;
    private Spinner spinnerFrequency;
    private TimePicker timePicker;
    private Button btnAddReminder;
    private DatabaseReference dbRef;

    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        // Initialize views
        etReminderTitle = findViewById(R.id.etReminderTitle);
        spinnerFrequency = findViewById(R.id.spinnerFrequency);
        timePicker = findViewById(R.id.timePicker);
        btnAddReminder = findViewById(R.id.btnAddReminder);
        dbRef = FirebaseDatabase.getInstance().getReference("Reminders");

        timePicker.setIs24HourView(true);

        // Request Android 13+ notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        // Request Android 12+ exact alarm permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Please allow exact alarms to activate reminders", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }

        // Save reminder and schedule it
        btnAddReminder.setOnClickListener(v -> {
            String title = etReminderTitle.getText().toString().trim();
            String frequency = spinnerFrequency.getSelectedItem().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            String reminderId = dbRef.push().getKey();
            Reminder reminder = new Reminder(reminderId, title, frequency, hour, minute);

            if (reminderId != null) {
                dbRef.child(reminderId).setValue(reminder)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show();
                            scheduleNotification(reminder);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void scheduleNotification(Reminder reminder) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("title", reminder.getTitle());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                reminder.getId().hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
        calendar.set(Calendar.MINUTE, reminder.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // If the time is already passed, fire it after 1 minute
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 1000);
        }

        long intervalMillis;
        switch (reminder.getFrequency().toLowerCase()) {
            case "daily":
                intervalMillis = AlarmManager.INTERVAL_DAY;
                break;
            case "weekly":
                intervalMillis = AlarmManager.INTERVAL_DAY * 7;
                break;
            case "monthly":
                intervalMillis = AlarmManager.INTERVAL_DAY * 30;
                break;
            default:
                intervalMillis = 0;
        }

        if (intervalMillis > 0) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    intervalMillis,
                    pendingIntent
            );
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );
        }
    }

    // Optional: Handle user's response to notification permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE && grantResults.length > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission is required to receive reminders", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.b07safetyplanapp.reminders;

import android.annotation.SuppressLint;
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

import com.b07safetyplanapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Calendar;

public class ReminderScheduleActivity extends AppCompatActivity {

    private EditText etReminderTitle;
    private Spinner spinnerFrequency;
    private TimePicker timePicker;
    private Button btnAddReminder;
    private LinearLayout reminderListContainer;

    private DatabaseReference dbRef;
    private FirebaseUser currentUser;
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;

    private Reminder editingReminder = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.b07safetyplanapp.R.layout.activity_reminder);

        //Resources id's used for a reminder
        etReminderTitle = findViewById(com.b07safetyplanapp.R.id.etReminderTitle);
        spinnerFrequency = findViewById(com.b07safetyplanapp.R.id.spinnerFrequency);
        timePicker = findViewById(com.b07safetyplanapp.R.id.timePicker);
        btnAddReminder = findViewById(com.b07safetyplanapp.R.id.btnAddReminder);
        reminderListContainer = findViewById(R.id.reminderListContainer);
        timePicker.setIs24HourView(true);

        ImageView backButton = findViewById(R.id.backButton);
        //Listener for back button from LinearLayout
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        //findViewById(R.id.btnBack).setOnClickListener(v -> finish());


        //MVP log-in connection
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        //Writing to db
        dbRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("reminders");

        requestPermissionsIfNeeded();
        loadReminders();

        //Creating reminder
        btnAddReminder.setOnClickListener(v -> {
            String title = etReminderTitle.getText().toString().trim();
            String frequency = spinnerFrequency.getSelectedItem().toString();
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }
            //Editing Reminder
            if (editingReminder == null) {
                String reminderId = dbRef.push().getKey();
                if (reminderId != null) {
                    Reminder reminder = new Reminder(reminderId, title, frequency, hour, minute);
                    dbRef.child(reminderId).setValue(reminder)
                            .addOnSuccessListener(unused -> {
                                scheduleNotification(reminder);
                                Toast.makeText(this, "Reminder saved", Toast.LENGTH_SHORT).show();
                                recreate();
                            });
                }
            } else {
                editingReminder.setTitle(title);
                editingReminder.setFrequency(frequency);
                editingReminder.setHour(hour);
                editingReminder.setMinute(minute);
                dbRef.child(editingReminder.getId()).setValue(editingReminder)
                        .addOnSuccessListener(unused -> {
                            scheduleNotification(editingReminder);
                            Toast.makeText(this, "Reminder updated", Toast.LENGTH_SHORT).show();
                            recreate();
                        });
            }
        });
    }

    //Requesting permissions to send reminders & notifications (new Andoroid versions)
    private void requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this, "Please allow exact alarms to activate reminders", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void loadReminders() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reminderListContainer.removeAllViews();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Reminder reminder = child.getValue(Reminder.class);
                    if (reminder != null) {
                        addReminderView(reminder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReminderScheduleActivity.this, "Failed to load reminders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Manage Reminders view
    private void addReminderView(Reminder reminder) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(8, 8, 8, 8);

        TextView text = new TextView(this);
        text.setText(reminder.getTitle() + " @ " + String.format("%02d:%02d", reminder.getHour(), reminder.getMinute()));
        text.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 2));

        //Button to edit Reminders
        Button editBtn = new Button(this);
        editBtn.setText("Edit");
        editBtn.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        editBtn.setOnClickListener(v -> loadReminderForEdit(reminder));


        //Button to delete reminders
        Button delBtn = new Button(this);
        delBtn.setText("Delete");
        delBtn.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        delBtn.setOnClickListener(v -> deleteReminder(reminder));

        row.addView(text);
        row.addView(editBtn);
        row.addView(delBtn);
        reminderListContainer.addView(row);
    }

    //Updating existing reminders view for editing purposes
    private void loadReminderForEdit(Reminder reminder) {
        etReminderTitle.setText(reminder.getTitle());

        ArrayAdapter adapter = (ArrayAdapter) spinnerFrequency.getAdapter();
        int pos = adapter.getPosition(reminder.getFrequency());
        spinnerFrequency.setSelection(pos);

        timePicker.setHour(reminder.getHour());
        timePicker.setMinute(reminder.getMinute());

        editingReminder = reminder;
        btnAddReminder.setText("Update Reminder");
    }

    //Deleteing a reminder
    private void deleteReminder(Reminder reminder) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, reminder.getId().hashCode(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        alarmManager.cancel(pendingIntent);

        dbRef.child(reminder.getId()).removeValue()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Reminder deleted", Toast.LENGTH_SHORT).show();
                    recreate();
                });
    }

    //Schedulinng a notification via Reminder Receiver
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

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.setTimeInMillis(System.currentTimeMillis() + 60 * 1000);
        }

        long interval;
        switch (reminder.getFrequency().toLowerCase()) {
            case "daily":
                interval = AlarmManager.INTERVAL_DAY;
                break;
            case "weekly":
                interval = AlarmManager.INTERVAL_DAY * 7;
                break;
            case "monthly":
                interval = AlarmManager.INTERVAL_DAY * 30;
                break;
            default:
                interval = 0;
        }

        if (interval > 0) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    interval,
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

    //Notification Permission (new Android versions)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == NOTIFICATION_PERMISSION_CODE && results.length > 0 && results[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Notification permission is required to receive reminders", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.b07safetyplanapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "silent_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Log and toast to confirm broadcast
        Log.d("ReminderReceiver", "Broadcast received");
        Toast.makeText(context, "Reminder Triggered!", Toast.LENGTH_SHORT).show();

        String title = intent.getStringExtra("title");
        if (title == null) title = "Scheduled Reminder";

        // Create notification channel if needed
        createSilentNotificationChannel(context);

        // Intent to open MainActivity on tap
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // fallback icon for testing
                .setContentTitle("Reminder")
                .setContentText(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSilent(true) // no sound or vibration
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createSilentNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);

            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Silent Reminders",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                channel.setDescription("Channel for silent reminder notifications");
                channel.setSound(null, (AudioAttributes) null);
                channel.enableVibration(false);

                manager.createNotificationChannel(channel);
            }
        }
    }
}

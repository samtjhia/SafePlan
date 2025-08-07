package com.b07safetyplanapp.reminders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.b07safetyplanapp.login.LoginActivity;

/**
 * BroadcastReceiver responsible for handling scheduled reminder alarms and displaying notifications.
 * When triggered, this receiver builds and shows a high-priority notification with a reminder message.
 */
public class ReminderReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "reminder_channel";

    /**
     * Called when the BroadcastReceiver receives the alarm broadcast.
     * Triggers a notification with the reminder message and a forced logout redirection.
     *
     * @param context The context in which the receiver is running.
     * @param intent  The intent being received, expected to contain the reminder title.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReminderReceiver", "Broadcast received");
        Toast.makeText(context, "Reminder triggered!", Toast.LENGTH_SHORT).show();

        // Get the reminder title from the intent
        String reminderTitle = intent.getStringExtra("title");
        if (reminderTitle == null || reminderTitle.trim().isEmpty()) {
            reminderTitle = "Unnamed Reminder";
        }

        // Prepare intent to redirect to LoginActivity and request logout
        Intent loginIntent = new Intent(context, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        loginIntent.putExtra("forceLogout", true);  // Signal to trigger logout in LoginActivity

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                loginIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build and display the notification
        createNotificationChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Reminder")
                .setContentText(reminderTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) System.currentTimeMillis(), builder.build());
    }

    /**
     * Creates a notification channel if it does not exist.
     * Required for API 26+ to display high-priority notifications.
     *
     * @param context The application context used to access system services.
     */
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Reminders",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel.setDescription("Reminder notifications with sound and alerts");

                // Set default system sound for the channel
                Uri soundUri = Settings.System.DEFAULT_NOTIFICATION_URI;
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                channel.setSound(soundUri, audioAttributes);
                channel.enableVibration(true);

                manager.createNotificationChannel(channel);
            }
        }
    }
}

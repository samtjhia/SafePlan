package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.b07safetyplanapp.reminders.ReminderScheduleActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * SettingsFragment provides a UI for users to manage application settings such as:
 * - Navigating to privacy controls
 * - Accessing reminder scheduling
 * - Logging out of the app
 *
 * This fragment replaces the activity-based settings implementation for modularity.
 */
public class SettingsFragment extends Fragment {


    /**
     * Inflates the settings fragment layout and initializes all button listeners:
     * Privacy Control, Reminders, Logout, and Back navigation.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views.
     * @param container          If non-null, this is the parent view that the fragment's UI should attach to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous state.
     * @return The View object for the fragment's UI.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings_fragment, container, false);

        // Bind buttons
        Button privacyButton = view.findViewById(R.id.btnSetting4);
        Button logoutButton = view.findViewById(R.id.btnLogout);
        Button reminderButton = view.findViewById(R.id.btnReminder);

        // Back button
        view.findViewById(R.id.backButton).setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Privacy Control Activity
        privacyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyControlActivity.class);
            startActivity(intent);
        });

        // Reminder Schedule Activity
        reminderButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ReminderScheduleActivity.class);
            startActivity(intent);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.b07safetyplanapp.reminders.ReminderScheduleActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private Button privacyButton, logoutButton, reminderButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings_fragment, container, false);

        // Bind buttons
        privacyButton = view.findViewById(R.id.btnSetting4);
        logoutButton = view.findViewById(R.id.btnLogout);
        reminderButton = view.findViewById(R.id.btnReminder);
        ImageView backButton = view.findViewById(R.id.backButton);

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
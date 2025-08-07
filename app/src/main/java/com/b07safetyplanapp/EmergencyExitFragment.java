package com.b07safetyplanapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * Fragment that provides an emergency exit button.
 * <p>
 * When clicked, the user is redirected to a safe external URL and the app is closed and directed to another website (Chrome).
 */
public class EmergencyExitFragment extends Fragment {

    /**
     * Inflates the emergency exit layout and sets up the exit button listener.
     *
     * @param inflater the LayoutInflater object used to inflate views
     * @param container the parent ViewGroup for the fragment UI
     * @param savedInstanceState the saved instance state, if any
     * @return the root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_exit, container, false);

        FloatingActionButton powerButton = view.findViewById(R.id.powerButton);
        powerButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(browserIntent);

            requireActivity().finishAffinity();
        });


        return view;
    }
}
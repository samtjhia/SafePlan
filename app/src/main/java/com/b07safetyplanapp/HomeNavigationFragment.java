package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * HomeNavigationFragment is a reusable UI fragment that displays the logo
 * and allows users to navigate back to the main activity (which hosts HomeFragment).
 * <p>
 * This fragment is typically embedded in the top bar or toolbar area of other screens.
 */
public class HomeNavigationFragment extends Fragment {

    /**
     * Inflates the fragment layout and sets up click listener for the logo.
     *
     * @param inflater           The LayoutInflater used to inflate the view.
     * @param container          The optional parent view to attach to.
     * @param savedInstanceState The saved state from a previous instance (if any).
     * @return The root view of this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_navigation, container, false);

        ImageView logo = view.findViewById(R.id.logoImage);
        logo.setOnClickListener(v -> {
            // Navigate to MainActivity (where HomeFragment is the default)
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish(); // Prevent back navigation to current screen
        });

        return view;
    }
}

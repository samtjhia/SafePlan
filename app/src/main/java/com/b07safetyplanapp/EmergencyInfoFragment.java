    package com.b07safetyplanapp;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.ImageButton;
    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;

    /**
     * Fragment that provides navigation to emergency-related data sections.
     * <p>
     * Includes buttons to view documents to pack, emergency contacts, safe locations,
     * and medications. Each button launches the appropriate activity.
     */
    public class EmergencyInfoFragment extends Fragment {
        /**
         * Inflates the layout for the emergency info fragment and initializes
         * button click listeners for navigation.
         *
         * @param inflater the LayoutInflater object used to inflate views
         * @param container the parent ViewGroup into which the fragment UI is inserted
         * @param savedInstanceState the previously saved instance state, if any
         * @return the root view of the fragment
         */
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_emergency_info, container, false);

            // Initialize all buttons
            setupClickListeners(view);

            return view;
        }

        /**
         * Sets up the click listeners for all buttons in the emergency info screen.
         * Each button launches a corresponding activity with a transition animation.
         *
         * @param view the root view containing the buttons
         */
        private void setupClickListeners(View view) {
            // Back button
            ImageButton backButton = view.findViewById(R.id.backButton);
            backButton.setOnClickListener(v -> {
                // Go back to previous fragment (HomeFragment)
                getParentFragmentManager().popBackStack();
            });

            // Documents to Pack button
            Button documentsButton = view.findViewById(R.id.documentsViewButton);
            documentsButton.setOnClickListener(v -> {
                // Start DocumentActivity
                Intent intent = new Intent(getActivity(), DocumentActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            // Emergency Contacts button
            Button contactsButton = view.findViewById(R.id.contactsViewButton);
            contactsButton.setOnClickListener(v -> {
                // Start EmergencyContactActivity
                Intent intent = new Intent(getActivity(), EmergencyContactActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            // Safe Locations button
            Button locationsButton = view.findViewById(R.id.locationsViewButton);
            locationsButton.setOnClickListener(v -> {
                // Start EmergencySafeLocationsActivity
                Intent intent = new Intent(getActivity(), EmergencySafeLocationsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });

            // Medications button
            Button medicationsButton = view.findViewById(R.id.medicationsViewButton);
            medicationsButton.setOnClickListener(v -> {
                // Start EmergencyMedicationActivity
                Intent intent = new Intent(getActivity(), EmergencyMedicationActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }
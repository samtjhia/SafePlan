package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        Button buttonRecyclerView = view.findViewById(R.id.buttonRecyclerView);
        Button buttonScroller = view.findViewById(R.id.buttonScroller);
        Button buttonSpinner = view.findViewById(R.id.buttonSpinner);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);

        Button buttonSettings = view.findViewById(R.id.buttonSettings);
        Button startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        Button documentsButton = view.findViewById(R.id.documents_button);
        Button emergencyContactButton = view.findViewById(R.id.emergency_contacts_button);
        Button emergencyMedicationButton = view.findViewById(R.id.emergency_Medications_button);
        Button emergencySafeLocationsButton = view.findViewById(R.id.emergency_Safe_Locations_button);


        buttonRecyclerView.setOnClickListener(v -> loadFragment(new RecyclerViewFragment()));
        buttonScroller.setOnClickListener(v -> loadFragment(new ScrollerFragment()));
        buttonSpinner.setOnClickListener(v -> loadFragment(new SpinnerFragment()));
        buttonManageItems.setOnClickListener(v -> loadFragment(new ManageItemsFragment()));

        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        startQuestionnaireButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
            startActivity(intent);
        });

        documentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DocumentActivity.class);
            startActivity(intent);
        });

        emergencyContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencyContactActivity.class);
            startActivity(intent);
        });

        emergencyMedicationButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencyMedicationActivity.class);
            startActivity(intent);
        });

        emergencySafeLocationsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencySafeLocationsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

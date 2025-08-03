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

        Button buttonPlan = view.findViewById(R.id.buttonPlan);
        Button buttonScroller = view.findViewById(R.id.buttonScroller);
        Button buttonSpinner = view.findViewById(R.id.buttonSpinner);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);
        Button buttonSettings = view.findViewById(R.id.buttonSettings);
        Button startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);

        // These load fragments
        buttonScroller.setOnClickListener(v -> loadFragment(new ScrollerFragment()));
        buttonSpinner.setOnClickListener(v -> loadFragment(new SpinnerFragment()));
        buttonManageItems.setOnClickListener(v -> loadFragment(new ManageItemsFragment()));

        //Start TipsActivity
        buttonPlan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsActivity.class);
            startActivity(intent);
        });

        //Start SettingsActivity (ACTIVITY, not Fragment!)
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });

        //Start QuestionnaireActivity
        startQuestionnaireButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
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

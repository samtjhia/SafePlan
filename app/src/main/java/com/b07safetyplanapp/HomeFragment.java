package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {
    private TextView textUserName;
    private FirebaseDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        // Initialize Firebase
        db = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");

        textUserName = view.findViewById(R.id.textUserName);
        CardView safetyCard = view.findViewById(R.id.safetyCard);
        Button startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        Button buttonPlan = view.findViewById(R.id.show_plan_button);
        Button reviewButton = view.findViewById(R.id.reviewButton);
        Button helpButton = view.findViewById(R.id.findButton);

        ImageButton closeButton = view.findViewById(R.id.closeDisclaimerButton);
        View disclaimerContainer = view.findViewById(R.id.disclaimerContainer);

        // Set up user name display
        setupUserName();

        // Set up click listeners
        safetyCard.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, new SettingsFragment())
                    .addToBackStack(null)
                    .commit();
        });

        reviewButton.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left,
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                    )
                    .replace(R.id.fragment_container, new EmergencyInfoFragment())
                    .addToBackStack(null)
                    .commit();
        });

        buttonPlan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        startQuestionnaireButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        helpButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SupportResourcesActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        closeButton.setOnClickListener(v -> {
            disclaimerContainer.setVisibility(View.GONE);
        });

        return view;
    }

    private void setupUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String uid = currentUser.getUid();
            DatabaseReference userRef = db.getReference("users").child(uid).child("fullName");

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String fullName = task.getResult().getValue(String.class);
                    textUserName.setText("Hello, " + fullName);
                } else {
                    textUserName.setText("Hello, " + currentUser.getEmail());
                }
            });
        } else {
            textUserName.setText("No user signed in.");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        // animations
        transaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
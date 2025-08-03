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

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {
    private Button startQuestionnaireButton;
    private String currentSessionId;
//    private FirebaseAuth mAuth;

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
        Button buttonPlan = view.findViewById(R.id.buttonPlan);
        Button documentsButton = view.findViewById(R.id.documents_button);
        Button emergencyContactButton = view.findViewById(R.id.emergency_contacts_button);

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

        buttonPlan.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsActivity.class);
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

        return view;
    }

    private String getCurrentSessionId() {
        // Generate session ID based on current time
        return "session_" + System.currentTimeMillis();
    }

    private void setupQuestionnaireButton() {
        // Check if responses exist in Firebase using session ID
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");
        DatabaseReference responsesRef = database.getReference("questionnaire_sessions")
                .child(currentSessionId).child("responses");

        responsesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            // Has previous responses - show edit mode
                            startQuestionnaireButton.setText("Edit Questionnaire");
                            startQuestionnaireButton.setOnClickListener(v -> startEditMode());
                        } else {
                            // No previous responses - show start mode
                            startQuestionnaireButton.setText("Start Questionnaire");
                            startQuestionnaireButton.setOnClickListener(v -> startNewQuestionnaire());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Default to start mode if error
                        startQuestionnaireButton.setText("Start Questionnaire");
                        startQuestionnaireButton.setOnClickListener(v -> startNewQuestionnaire());
                    });
                }
            }
        });
    }

    private void startNewQuestionnaire() {
        Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
        intent.putExtra("edit_mode", false);
        intent.putExtra("session_id", currentSessionId);
        startActivity(intent);
    }

    private void startEditMode() {
        Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
        intent.putExtra("edit_mode", true);
        intent.putExtra("session_id", currentSessionId);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh button state when returning to this fragment
        if (startQuestionnaireButton != null) {
            setupQuestionnaireButton();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

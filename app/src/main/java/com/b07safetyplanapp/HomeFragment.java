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

        Button buttonScroller = view.findViewById(R.id.buttonScroller);
        Button buttonSpinner = view.findViewById(R.id.buttonSpinner);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);

        buttonScroller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new ScrollerFragment());
            }
        });

        buttonSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SpinnerFragment());
            }
        });

        buttonManageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ManageItemsFragment());}
        });

        // Get reference to the button
        startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        Button showPlanButton = view.findViewById(R.id.show_plan_button);

        currentSessionId = getCurrentSessionId();

        setupQuestionnaireButton();

//        // Set onClick listener
//        startQuestionnaireButton.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
//            startActivity(intent);
//        });

        showPlanButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsActivity.class);
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

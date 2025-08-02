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

import com.b07safetyplanapp.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

//        Button buttonRecyclerView = view.findViewById(R.id.documents_button);
//        Button buttonScroller = view.findViewById(R.id.start_questionnaire_button);
//        Button buttonSpinner = view.findViewById(R.id.emergency_contacts_button);
//
//        buttonRecyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new RecyclerViewFragment());
//            }
//        });
//
//        buttonScroller.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new ScrollerFragment());
//            }
//        });
//
//        buttonSpinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadFragment(new SpinnerFragment());
//            }
//        });



        Button startQuestionnaireButton = view.findViewById(R.id.start_questionnaire_button);
        startQuestionnaireButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuestionnaireActivity.class);
            startActivity(intent);
        });

        Button documentsButton = view.findViewById(R.id.documents_button);
        documentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DocumentActivity.class);
            startActivity(intent);
        });

        Button emergencyContactButton = view.findViewById(R.id.emergency_contacts_button);
        emergencyContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencyContactActivity.class);
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

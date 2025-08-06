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

public class EmergencyInfo extends Fragment{
    private Button buttonToPack;
    private Button buttonContacts;
    private Button buttonSafeLocations;
    private Button buttonMedications;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datacategories, container, false);

        //set up buttons
        buttonToPack = view.findViewById(R.id.documents_button);
        buttonContacts = view.findViewById(R.id.contacts_button);
        buttonSafeLocations = view.findViewById(R.id.safe_locations_button);
        buttonMedications = view.findViewById(R.id.medications_button);

        buttonToPack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ToPackFragment());}
        });
        buttonContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ContactsFragment());}
        });
        /*
        buttonSafeLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new SafeLocationsFragment());}
        });
        */


        buttonMedications.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EmergencyMedicationActivity.class);
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

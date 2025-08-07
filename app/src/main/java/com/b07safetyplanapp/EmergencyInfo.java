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

/**
 * Fragment that serves as a dashboard for navigating emergency-related categories.
 * <p>
 * Provides navigation buttons to documents (to-pack), contacts, and medications.
 */
public class EmergencyInfo extends Fragment{
    private Button buttonToPack;
    private Button buttonContacts;
    private Button buttonSafeLocations;
    private Button buttonMedications;

    /**
     * Inflates the emergency info layout and initializes navigation buttons.
     * Handles navigation to corresponding fragments or activities.
     *
     * @param inflater the LayoutInflater used to inflate the layout
     * @param container the parent ViewGroup of the fragment
     * @param savedInstanceState the previously saved instance state
     * @return the inflated root view
     */
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

    /**
     * Replaces the current fragment with the given fragment and adds it to the back stack.
     *
     * @param fragment the fragment to load
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    }

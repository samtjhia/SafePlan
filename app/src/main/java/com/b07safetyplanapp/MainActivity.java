package com.b07safetyplanapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * MainActivity is the entry point of the application after login or signup.
 * <p>
 * It serves as the host for all fragments and manages navigation using the fragment container.
 * By default, it loads {@link HomeFragment} when no saved instance state is present.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState A saved state if the activity is being re-initialized, or null if it's newly created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load initial fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
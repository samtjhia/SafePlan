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

    /**
     * Loads the given fragment into the fragment container and adds the transaction to the back stack.
     *
     * @param fragment The fragment to display.
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Handles back button press. If there are multiple fragments in the back stack,
     * it pops the top one. Otherwise, it exits the activity.
     */
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
package com.b07safetyplanapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.b07safetyplanapp.models.questionnaire.UserResponse;
import com.b07safetyplanapp.models.supportresources.ResourceDirectory;
import com.b07safetyplanapp.models.supportresources.SupportResource;
import com.b07safetyplanapp.utils.ResourceParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * SupportResourcesActivity displays local support resources (hotlines, shelters, police, legal aid, victim services)
 * based on the user's city provided in the questionnaire. The resources are loaded from a local asset file,
 * and their URLs are opened with animated transitions.
 */
public class SupportResourcesActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button hotlineButton;
    private Button shelterButton;
    private Button policeButton;
    private Button legalButton;
    private Button victimButton;
    private TextView hotlineSubtext;
    private TextView shelterSubtext;
    private TextView policeSubtext;
    private TextView legalSubtext;
    private TextView victimSubtext;
    private FirebaseDatabase database;
    private DatabaseReference questionnaireRef;
    private String uid;
    private ResourceDirectory resourceDirectory;
    private List<SupportResource> resourceList;


    /**
     * Initializes the activity, UI components, Firebase reference, and loads support resources
     * based on the user's questionnaire response.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportresources);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        initializeFirebase();
        initializeViews();
        loadResources();
    }

    @Override
    public void finish() {
        // Back animation
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    /**
     * Initializes Firebase instance and retrieves the current user UID for referencing questionnaire data.
     */
    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity
            return;
        }

        uid = currentUser.getUid();

        // Path: users/{uid}/questionnaire
        questionnaireRef = database.getReference("users")
                .child(uid);
    }


    /**
     * Binds UI components (buttons and subtexts) to their corresponding layout views.
     */
    private void initializeViews() {
        hotlineButton = findViewById(R.id.HotlinesButton);
        shelterButton = findViewById(R.id.SheltersButton);
        policeButton = findViewById(R.id.PoliceButton);
        legalButton = findViewById(R.id.LegalButton);
        victimButton = findViewById(R.id.VictimButton);
        hotlineSubtext = findViewById(R.id.HotlinesSubtitle);
        shelterSubtext = findViewById(R.id.SheltersSubtitle);
        policeSubtext = findViewById(R.id.PoliceSubtitle);
        legalSubtext = findViewById(R.id.LegalSubtitle);
        victimSubtext = findViewById(R.id.VictimSubtitle);
    }


    /**
     * Loads the support resources based on the user's city (from questionnaire response).
     * Resources are parsed from local JSON and matched by city.
     */
    private void loadResources() {
        questionnaireRef.child("questionnaire")
                .child("responses")
                .child("city")
                .child("answer")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        String city = snapshot.getValue(String.class);

                        resourceDirectory = ResourceParser.loadResourceDirectory(this);
                        if(resourceDirectory.getResourceMap() == null) {
                            return;
                        }

                        resourceList = resourceDirectory.getResourceMap().get(city);
                        if(resourceList == null) {
                            return;
                        }

                        initializeUI();
                    }
                });
    }


    /**
     * Dynamically populates the UI with resource data and sets onClick listeners
     * to open URLs in a browser with slide animations.
     */
    private void initializeUI() {
        for(SupportResource s : resourceList) {
            switch(s.getType()) {
                case "Hotline":
                    hotlineSubtext.setText(s.getName());
                    hotlineButton.setOnClickListener(v -> {
                        openResourceWithAnimation(s.getUrl());
                    });
                    break;
                case "Shelter":
                    shelterSubtext.setText(s.getName());
                    shelterButton.setOnClickListener(v -> {
                        openResourceWithAnimation(s.getUrl());
                    });
                    break;
                case "Police":
                    policeSubtext.setText(s.getName());
                    policeButton.setOnClickListener(v -> {
                        openResourceWithAnimation(s.getUrl());
                    });
                    break;
                case "Legal Aid":
                    legalSubtext.setText(s.getName());
                    legalButton.setOnClickListener(v -> {
                        openResourceWithAnimation(s.getUrl());
                    });
                    break;
                case "Victim Services":
                    victimSubtext.setText(s.getName());
                    victimButton.setOnClickListener(v -> {
                        openResourceWithAnimation(s.getUrl());
                    });
                    break;
            }
        }
    }

    // Animation

    /**
     * Opens the provided URL using an Intent and applies slide-out animation.
     *
     * @param url The URL to open in the browser.
     */
    private void openResourceWithAnimation(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
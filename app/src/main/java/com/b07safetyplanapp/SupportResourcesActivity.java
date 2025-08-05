package com.b07safetyplanapp;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportresources);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        initializeFirebase();
        initializeViews();

    }

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

    private void initializeText() {
        questionnaireRef.child("questionnaire")
                .child("responses")
                .child("city")
                .child("answer")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();
                        String city = snapshot.getValue(String.class);

                        System.out.println(city);

                        ResourceDirectory rd = ResourceParser.loadResourceDirectory(this);
                        if(rd.getResourceMap() == null) {
                            return;
                        }
                        List<SupportResource> resourceList = rd.getResourceMap().get(city);
                        System.out.println(resourceList.get(0));
                    }
                });


    }
}

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supportresources);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        initializeFirebase();
        initializeViews();
        loadResources();

    }

    @Override
    public void finish() { // back animation
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

    private void initializeUI() {
        for(SupportResource s : resourceList) {
            switch(s.getType()) {
                case "Hotline":
                    hotlineSubtext.setText(s.getName());
                    hotlineButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s.getUrl()));
                        startActivity(intent);
                    });
                    break;
                case "Shelter":
                    shelterSubtext.setText(s.getName());
                    shelterButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s.getUrl()));
                        startActivity(intent);
                    });
                    break;
                case "Police":
                    policeSubtext.setText(s.getName());
                    policeButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s.getUrl()));
                        startActivity(intent);
                    });
                    break;
                case "Legal Aid":
                    legalSubtext.setText(s.getName());
                    legalButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s.getUrl()));
                        startActivity(intent);
                    });
                    break;
                case "Victim Services":
                    victimSubtext.setText(s.getName());
                    victimButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s.getUrl()));
                        startActivity(intent);
                    });
                    break;
            }
        }
    }
}

package com.b07safetyplanapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.b07safetyplanapp.models.questionnaire.QuestionnaireRoot;
import com.b07safetyplanapp.models.questionnaire.TipData;
import com.b07safetyplanapp.models.questionnaire.UserResponse;
import com.b07safetyplanapp.utils.QuestionnaireParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TipsActivity extends AppCompatActivity {

    private Map<String, TipData> tipMap;
    private List<String> tipList;
    private List<UserResponse> userResponses;

    private FirebaseDatabase database;
    private DatabaseReference questionnaireRef;

    private RecyclerView recyclerView;
    private TipAdapter tipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        recyclerView = findViewById(R.id.tipsRecyclerView); // Make sure this ID exists in your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tipList = new ArrayList<>();

        initializeFirebase();
        loadUserResponses();


    }

    private void initializeFirebase() {
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");
        questionnaireRef = database.getReference("questionnaire_sessions");
    }

    private void loadUserResponses() {
        questionnaireRef.child("session_1753579047919")
                .child("responses")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userResponses = new ArrayList<>();

                        DataSnapshot snapshot = task.getResult();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            UserResponse response = child.getValue(UserResponse.class);
                            userResponses.add(response);
                        }

                        loadTipMap();
                        generateTips();
                    }
                });
    }


    private void loadTipMap() {
        QuestionnaireRoot root = QuestionnaireParser.loadQuestionnaire(this);
        if (root != null) {
            tipMap = root.getTips();
        }
    }

    private void generateTips() {
        for(UserResponse u : userResponses) {
            String questionId = u.getQuestionId();

            TipData tipInfo = tipMap.get(questionId);
            if(tipInfo == null) {
                continue;
            }
            String tipType = tipInfo.getTip_type();

            if(tipType.equalsIgnoreCase("general")) {
                String tip = tipInfo.getGeneral_tip();
                tip = replacePlaceholders(tip);
                tipList.add(tip);
            }
            else {
                String tip = tipInfo.getOption_tips().get(u.getAnswer());
                tip = replacePlaceholders(tip);
                tipList.add(tip);
            }
        }

        tipAdapter = new TipAdapter(tipList);
        recyclerView.setAdapter(tipAdapter);
    }

    private String replacePlaceholders(String s) {
        if(!s.contains("{")) {
            return s;
        }

        String questionId = s.substring(s.indexOf("{") + 1, s.indexOf("}"));

        for(UserResponse u : userResponses) {
            if(u.getQuestionId().equalsIgnoreCase(questionId)) {
                return s.substring(0, s.indexOf("{")) + u.getAnswer() + s.substring(s.indexOf("}") + 1);
            }
        }
        return s;
    }



}

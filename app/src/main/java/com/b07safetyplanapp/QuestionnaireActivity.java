package com.b07safetyplanapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.b07safetyplanapp.models.questionnaire.Branch;
import com.b07safetyplanapp.models.questionnaire.Question;
import com.b07safetyplanapp.models.questionnaire.QuestionnaireData;
import com.b07safetyplanapp.models.questionnaire.QuestionnaireRoot;
import com.b07safetyplanapp.models.questionnaire.UserResponse;
import com.b07safetyplanapp.utils.QuestionnaireParser;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class QuestionnaireActivity extends AppCompatActivity {

    private TextView questionText;
    private RadioGroup optionsGroup;
    private EditText textInput;
    private Button nextButton;
    private Button backButton;
    private ProgressBar progressBar;
    private LinearLayout subQuestionLayout;
    private TextView subQuestionText;
    private EditText subQuestionInput;

    private List<Question> allQuestions;
    private List<UserResponse> userResponses;
    private int currentQuestionIndex = 0;
    private QuestionnaireData questionnaireData;

    private boolean branchQuestionsAdded = false;
    private boolean followUpQuestionsAdded = false;

    private boolean isEditMode = false;
    private boolean hasCompletedBefore = false;

    private FirebaseDatabase database;
    private DatabaseReference questionnaireRef;
    //    private FirebaseAuth mAuth;
    private String sessionId;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        isEditMode = getIntent().getBooleanExtra("edit_mode", false);

        // Initialize Firebase
        initializeFirebase();

        initializeViews();
        loadQuestionnaire();
        setupClickListeners();

        if (isEditMode) {
            loadPreviousResponsesFromFirebase();
        } else {
            displayCurrentQuestion();
        }
    }

    private void initializeFirebase() {

        // Initialize Firebase Database
//        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");


//        // Get user ID from intent or current user
//        userId = getIntent().getStringExtra("user_id");
//        if (userId == null) {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            if (currentUser != null) {
//                userId = currentUser.getUid();
//            } else {
//                // User not logged in - shouldn't happen, but handle gracefully
//                Toast.makeText(this, "Please log in to access questionnaire", Toast.LENGTH_LONG).show();
//                finish();
//                return;
//            }
//        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity
            return;
        }

        uid = currentUser.getUid();

        // Path: users/{uid}/questionnaire_sessions/{sessionId}
        sessionId = "session_" + System.currentTimeMillis();
        questionnaireRef = database.getReference("users")
                .child(uid)
                .child("questionnaire_sessions")
                .child(sessionId);
    }

    private void initializeViews() {
        questionText = findViewById(R.id.question_text);
        optionsGroup = findViewById(R.id.options_group);
        textInput = findViewById(R.id.text_input);
        nextButton = findViewById(R.id.next_button);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);
        subQuestionLayout = findViewById(R.id.sub_question_layout);
        subQuestionText = findViewById(R.id.sub_question_text);
        subQuestionInput = findViewById(R.id.sub_question_input);
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    private void loadQuestionnaire() {
        QuestionnaireRoot root = QuestionnaireParser.loadQuestionnaire(this);
        if (root != null) {
            questionnaireData = root.getQuestionnaire();

            allQuestions = new ArrayList<>();
            userResponses = new ArrayList<>();

            // Start with warm-up questions
            allQuestions.addAll(questionnaireData.getWarm_up());

            if (isEditMode) {
                loadAllQuestionsForEdit();
            }
        }
    }

    private void loadAllQuestionsForEdit() {
        // Add all warm-up questions (already added)

        // Add all branch-specific questions
        for (Branch branch : questionnaireData.getBranch_specific()) {
            allQuestions.addAll(branch.getQuestions());
        }

        // Add follow-up questions
        if (questionnaireData.getFollow_up() != null) {
            allQuestions.addAll(questionnaireData.getFollow_up());
        }

        branchQuestionsAdded = true;
        followUpQuestionsAdded = true;
    }

    private void loadPreviousResponsesFromFirebase() {
        questionnaireRef.child(sessionId).child("responses")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userResponses.clear();

                        for (DataSnapshot responseSnapshot : dataSnapshot.getChildren()) {
                            UserResponse response = responseSnapshot.getValue(UserResponse.class);
                            if (response != null) {
                                userResponses.add(response);
                            }
                        }

                        // Filter questions based on previous responses if not in edit mode
                        if (!isEditMode) {
                            filterQuestionsBasedOnResponses();
                        }

                        displayCurrentQuestion();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(QuestionnaireActivity.this,
                                "Failed to load previous responses", Toast.LENGTH_SHORT).show();
                        displayCurrentQuestion();
                    }
                });
    }

    private void filterQuestionsBasedOnResponses() {
        // Find the situation response to load appropriate branch questions
        for (UserResponse response : userResponses) {
            if (response.getQuestionId().equals("situation")) {
                String situation = response.getAnswer();
                List<Question> branchQuestions = null;
                for (Branch branch : questionnaireData.getBranch_specific()) {
                    if (branch.getSituation().equalsIgnoreCase(situation)) {
                        branchQuestions = branch.getQuestions();
                        break;
                    }
                }
                if (branchQuestions != null) {
                    allQuestions.addAll(branchQuestions);
                }
                break;
            }
        }

        // Add follow-up questions
        if (questionnaireData.getFollow_up() != null) {
            allQuestions.addAll(questionnaireData.getFollow_up());
        }

        branchQuestionsAdded = true;
        followUpQuestionsAdded = true;
    }

    private void setupClickListeners() {
        nextButton.setOnClickListener(v -> {
            try {
                if (saveCurrentAnswer()) {
                    // Save to Firebase
                    Question currQuestion = allQuestions.get(currentQuestionIndex);
                    saveResponseToFirebase(currQuestion.getQuestion_id());

                    if(currQuestion.getSub_question() != null) {
                        String subQuestionId = currQuestion.getSub_question().getField().getQuestion_id();
                        saveResponseToFirebase(subQuestionId);
                    }

                    moveToNextQuestion();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error processing answer. Please try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        backButton.setOnClickListener(v -> {
            try {
                moveToPreviousQuestion();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating back. Please try again.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // Listen for radio button changes to show/hide sub-questions
        optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            try {
                handleSubQuestionVisibility();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void displayCurrentQuestion() {
        try {
            if (currentQuestionIndex >= allQuestions.size()) {
                // Questionnaire complete
                if (isEditMode) {
                    Toast.makeText(this, "Questionnaire Editing Complete!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Questionnaire Complete!", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }

            Question currentQuestion = allQuestions.get(currentQuestionIndex);
            if (currentQuestion == null) {
                Toast.makeText(this, "Error loading question", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update question text
            if (questionText != null) {
                questionText.setText(currentQuestion.getQuestion());
            }

            // Update progress bar
            if (progressBar != null) {
                progressBar.setMax(allQuestions.size());
                progressBar.setProgress(currentQuestionIndex + 1);
            }

            // Clear previous views
            if (optionsGroup != null) {
                optionsGroup.removeAllViews();
                optionsGroup.setVisibility(View.GONE);
            }
            if (textInput != null) {
                textInput.setVisibility(View.GONE);
                textInput.setText("");
            }
            if (subQuestionLayout != null) {
                subQuestionLayout.setVisibility(View.GONE);
            }

            // Display question based on type
            if (currentQuestion.hasOptions()) {
                displayMultipleChoiceQuestion(currentQuestion);
            } else {
                displayTextQuestion();
            }

            // Update button states - FIXED: Hide back button on first question
            updateButtonStates();

            // Load previous answer if exists
            loadPreviousAnswer();

            updateButtonText();
        } catch (Exception e) {
            Toast.makeText(this, "Error displaying question", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void updateButtonStates() {
        if (backButton != null) {
            if (currentQuestionIndex > 0) {
                backButton.setVisibility(View.VISIBLE);
                backButton.setEnabled(true);
            } else {
                backButton.setVisibility(View.GONE);
            }
        }
    }

    private void updateButtonText() {
        if (nextButton != null) {
            if (currentQuestionIndex == allQuestions.size() - 1) {
                if (isEditMode) {
                    nextButton.setText("Done Editing");
                } else {
                    nextButton.setText("Finish");
                }
            } else {
                nextButton.setText("Next");
            }
        }
    }

    private void displayMultipleChoiceQuestion(Question question) {
        if (optionsGroup != null && question.getOptions() != null) {
            optionsGroup.setVisibility(View.VISIBLE);

            String[] options = question.getOptions();
            for (int i = 0; i < options.length; i++) {
                String option = options[i];
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(option);
                radioButton.setId(View.generateViewId());
                radioButton.setTextSize(16);
                radioButton.setPadding(16, 20, 16, 20); // Add padding for better spacing

                // Set Inter Regular font
                radioButton.setTypeface(ResourcesCompat.getFont(this, R.font.inter_regular));

                // Add margin between radio buttons
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                if (i > 0) {
                    params.topMargin = 12; // Add space between options
                }
                radioButton.setLayoutParams(params);

                optionsGroup.addView(radioButton);
            }
        }
    }

    private void displayTextQuestion() {
        if (textInput != null) {
            textInput.setVisibility(View.VISIBLE);
            textInput.setText("");
            textInput.setHint("Enter your answer here...");
        }
    }

    private void handleSubQuestionVisibility() {
        try {
            if (currentQuestionIndex >= allQuestions.size()) return;

            Question currentQuestion = allQuestions.get(currentQuestionIndex);
            if (currentQuestion == null || !currentQuestion.hasSubQuestion()) return;

            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadio = findViewById(selectedId);
                if (selectedRadio != null) {
                    String selectedAnswer = selectedRadio.getText().toString();

                    if (selectedAnswer.equals(currentQuestion.getSub_question().getCondition())) {
                        // Show sub-question
                        if (subQuestionLayout != null) {
                            subQuestionLayout.setVisibility(View.VISIBLE);
                        }
                        if (subQuestionText != null) {
                            subQuestionText.setText(currentQuestion.getSub_question().getField().getQuestion());
                        }
                        if (subQuestionInput != null) {
                            subQuestionInput.setText("");
                        }
                    } else {
                        // Hide sub-question
                        if (subQuestionLayout != null) {
                            subQuestionLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean saveCurrentAnswer() {
        try {
            if (currentQuestionIndex >= allQuestions.size()) {
                return false;
            }

            Question currentQuestion = allQuestions.get(currentQuestionIndex);
            if (currentQuestion == null) {
                Toast.makeText(this, "Error: Question not found", Toast.LENGTH_SHORT).show();
                return false;
            }

            String answer = "";

            // Get main answer
            if (currentQuestion.hasOptions()) {
                int selectedId = optionsGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                    return false;
                }
                RadioButton selectedRadio = findViewById(selectedId);
                if (selectedRadio == null) {
                    Toast.makeText(this, "Error: Could not get selected option", Toast.LENGTH_SHORT).show();
                    return false;
                }
                answer = selectedRadio.getText().toString();
            } else {
                if (textInput != null) {
                    answer = textInput.getText().toString().trim();
                    if (answer.isEmpty()) {
                        Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(this, "Error: Input field not found", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            // Save or update response
            UserResponse response = new UserResponse(currentQuestion.getQuestion_id(), answer);

            // Update or add response
            boolean found = false;
            for (int i = 0; i < userResponses.size(); i++) {
                if (userResponses.get(i).getQuestionId().equals(currentQuestion.getQuestion_id())) {
                    userResponses.set(i, response);
                    found = true;
                    break;
                }
            }
            if (!found) {
                userResponses.add(response);
            }

            // Get sub-answer if visible
            if (subQuestionLayout != null && subQuestionLayout.getVisibility() == View.VISIBLE) {
                if (subQuestionInput != null) {
                    String subAnswer = subQuestionInput.getText().toString().trim();
                    if (subAnswer.isEmpty()) {
                        Toast.makeText(this, "Please complete the additional field", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    String subQuestionId = currentQuestion.getSub_question().getField().getQuestion_id();
                    UserResponse subResponse = new UserResponse(subQuestionId, subAnswer);

                    boolean foundSub = false;
                    for (int i = 0; i < userResponses.size(); i++) {
                        if (userResponses.get(i).getQuestionId().equals(subQuestionId)) {
                            userResponses.set(i, subResponse);
                            foundSub = true;
                            break;
                        }
                    }
                    if (!foundSub) {
                        userResponses.add(subResponse);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            Toast.makeText(this, "Error saving answer. Please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    // Save response to Firebase
    private void saveResponseToFirebase(String questionId) {
        try {
            if (userResponses.isEmpty()) return;

            // Get the response by question id
            UserResponse response = null;
            for(UserResponse u : userResponses) {
                if(u.getQuestionId().equalsIgnoreCase(questionId)) {
                    response = u;
                    break;
                }
            }

            if(response != null) {
                // Save the UserResponse object directly to Firebase
                questionnaireRef.child(sessionId)
                        .child("responses")
                        .child(response.getQuestionId())
                        .setValue(response)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(this, "Failed to save response", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPreviousAnswer() {
        try {
            if (currentQuestionIndex >= allQuestions.size()) return;

            Question currentQuestion = allQuestions.get(currentQuestionIndex);
            if (currentQuestion == null) return;

            for (UserResponse response : userResponses) {
                if (response.getQuestionId().equals(currentQuestion.getQuestion_id())) {
                    if (currentQuestion.hasOptions() && optionsGroup != null) {
                        for (int i = 0; i < optionsGroup.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
                            if (radioButton != null && radioButton.getText().toString().equals(response.getAnswer())) {
                                radioButton.setChecked(true);
                                break;
                            }
                        }
                    } else if (textInput != null) {
                        textInput.setText(response.getAnswer());
                    }

                    if (currentQuestion.hasSubQuestion()) {
                        String subQuestionId = currentQuestion.getSub_question().getField().getQuestion_id();
                        for (UserResponse u : userResponses) {
                            if (u.getQuestionId().equals(subQuestionId)) {
                                if (subQuestionLayout != null) {
                                    subQuestionLayout.setVisibility(View.VISIBLE);
                                }
                                if (subQuestionText != null) {
                                    subQuestionText.setText(currentQuestion.getSub_question().getField().getQuestion());
                                }
                                if (subQuestionInput != null) {
                                    subQuestionInput.setText(u.getAnswer());
                                }
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveToNextQuestion() {
        try {
            if (currentQuestionIndex == allQuestions.size() - 1) {
                //onboarding complete
                getSharedPreferences("safeplan_prefs", MODE_PRIVATE)
                        .edit()
                        .putBoolean("questionnaire_complete", true)
                        .apply();

                if (isEditMode) {
                    Toast.makeText(this, "Questionnaire Editing Complete!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Questionnaire Complete!", Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(this, MainActivity.class));
                finish();
                return;
            }

            currentQuestionIndex++;

            if (!isEditMode) {
                if (!branchQuestionsAdded &&
                        currentQuestionIndex == questionnaireData.getWarm_up().size()) {
                    loadBranchSpecificQuestions();
                    branchQuestionsAdded = true;
                }

                if (!followUpQuestionsAdded &&
                        currentQuestionIndex == questionnaireData.getWarm_up().size() + getBranchQuestionsCount()) {
                    allQuestions.addAll(questionnaireData.getFollow_up());
                    followUpQuestionsAdded = true;
                }
            }

            displayCurrentQuestion();
        } catch (Exception e) {
            Toast.makeText(this, "Error moving to next question", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void moveToPreviousQuestion() {
        try {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayCurrentQuestion();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error moving to previous question", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadBranchSpecificQuestions() {
        try {
            for (UserResponse response : userResponses) {
                if (response.getQuestionId().equals("situation")) {
                    String situation = response.getAnswer();
                    List<Question> branchQuestions = null;
                    for (Branch branch : questionnaireData.getBranch_specific()) {
                        if (branch.getSituation().equalsIgnoreCase(situation)) {
                            branchQuestions = branch.getQuestions();
                            break;
                        }
                    }
                    if (branchQuestions != null) {
                        allQuestions.addAll(branchQuestions);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getBranchQuestionsCount() {
        try {
            int warmUpCount = questionnaireData.getWarm_up().size();

            int followUpCount = 0;
            if (questionnaireData.getFollow_up() != null) {
                followUpCount = questionnaireData.getFollow_up().size();
            }

            int totalSoFar = allQuestions.size();
            int branchCount = totalSoFar - warmUpCount - followUpCount;

            return branchCount;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

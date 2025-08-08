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

    private FirebaseDatabase database;
    private DatabaseReference questionnaireRef;
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

    @Override
    public void finish() { // back animation
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initializeFirebase() {

        database = FirebaseDatabase.getInstance("https://group8cscb07app-default-rtdb.firebaseio.com/");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity
            return;
        }

        uid = currentUser.getUid();

        // Path: users/{uid}/questionnaire
        questionnaireRef = database.getReference("users")
                .child(uid);
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

    /**
     * Loads the questionnaire structure from local assets using {@link QuestionnaireParser}.
     * Populates warm-up questions initially.
     * If in edit mode, loads all questions at once.
     */
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

        // Branch-specific questions
        for (Branch branch : questionnaireData.getBranch_specific()) {
            allQuestions.addAll(branch.getQuestions());
        }

        // Follow-up questions
        if (questionnaireData.getFollow_up() != null) {
            allQuestions.addAll(questionnaireData.getFollow_up());
        }

        branchQuestionsAdded = true;
        followUpQuestionsAdded = true;
    }

    private void loadPreviousResponsesFromFirebase() {
        questionnaireRef.child("questionnaire").child("responses")
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

                        // If not in edit mode
                        if (!isEditMode) {
                            filterQuestionsBasedOnResponses(); // proceed
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

    /**
     * Based on the user's "situation" response, this method loads the appropriate
     * branch-specific questions and follow-up questions.
     * This ensures that only relevant parts of the questionnaire are shown.
     */
    private void filterQuestionsBasedOnResponses() {
        // Use situation response to load branch questions
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

    /**
     * Sets up click listeners for navigation and answer selection:
     * - Handles "Next" and "Back" button actions.
     * - Saves the current answer to Firebase and advances questions.
     * - Detects changes in multiple-choice options to trigger sub-question visibility.
     */
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

        // Sub-questions
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

            // Display question based on if it has options or if it is text based
            if (currentQuestion.hasOptions()) {
                displayMultipleChoiceQuestion(currentQuestion);
            } else {
                displayTextQuestion();
            }

            updateButtonStates();
            loadPreviousAnswer();
            updateButtonText();
        } catch (Exception e) {
            Toast.makeText(this, "Error displaying question", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * Changes the label of the "Next" button depending on whether
     * the current question is the last in the list and if in edit mode.
     */
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
                radioButton.setPadding(16, 20, 16, 20); // Add padding

                radioButton.setTypeface(ResourcesCompat.getFont(this, R.font.inter_regular)); // Inter font

                // Add margin between buttons
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

    /**
     * Handles the visibility of a sub-question based on the currently selected answer.
     * If the selected option matches the sub-question condition, the sub-question UI is shown.
     * Otherwise, it is hidden.
     */
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

    /**
     * Saves the current answer (and sub-answer if applicable) entered by the user.
     * Validates input and updates or adds the user's response to the internal list.
     *
     * @return true if the answer was successfully saved; false otherwise
     */
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

            // Get sub-answer
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

    private void saveResponseToFirebase(String questionId) {
        try {
            if (userResponses.isEmpty()) return;

            // Get response by question id
            UserResponse response = null;
            for(UserResponse u : userResponses) {
                if(u.getQuestionId().equalsIgnoreCase(questionId)) {
                    response = u;
                    break;
                }
            }

            if(response != null) {
                // Save UserResponse object to Firebase
                questionnaireRef.child("questionnaire")
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

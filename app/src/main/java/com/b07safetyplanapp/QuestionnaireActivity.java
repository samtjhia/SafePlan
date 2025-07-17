package com.example.b07demosummer2024;

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

import com.example.b07demosummer2024.models.Question;
import com.example.b07demosummer2024.models.QuestionnaireData;
import com.example.b07demosummer2024.models.QuestionnaireRoot;
import com.example.b07demosummer2024.models.UserResponse;
import com.example.b07demosummer2024.models.Branch;
import com.example.b07demosummer2024.utils.QuestionnaireParser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private FirebaseDatabase database;
    private DatabaseReference questionnaireRef;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // Initialize Firebase
        initializeFirebase();

        initializeViews();
        loadQuestionnaire();
        setupClickListeners();
        displayCurrentQuestion();
    }

    private void initializeFirebase() {
        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance("https://cscb07-app-group8-default-rtdb.firebaseio.com/");
        questionnaireRef = database.getReference("questionnaire_sessions");

        // Create a unique session ID
        sessionId = "session_" + System.currentTimeMillis();
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
    }

    private void loadQuestionnaire() {
        QuestionnaireRoot root = QuestionnaireParser.loadQuestionnaire(this);
        if (root != null) {
            questionnaireData = root.getQuestionnaire();
            allQuestions = new ArrayList<>();
            userResponses = new ArrayList<>();

            // Start with warm-up questions
            allQuestions.addAll(questionnaireData.getWarm_up());
        }
    }

    private void setupClickListeners() {
        nextButton.setOnClickListener(v -> {
            if (saveCurrentAnswer()) {
                // Save to Firebase immediately after saving locally
                saveResponseToFirebase();
                moveToNextQuestion();
            }
        });

        backButton.setOnClickListener(v -> moveToPreviousQuestion());

        // Listen for radio button changes to show/hide sub-questions
        optionsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            handleSubQuestionVisibility();
        });
    }

    private void displayCurrentQuestion() {
        if (currentQuestionIndex >= allQuestions.size()) {
            // Questionnaire complete
            Toast.makeText(this, "Questionnaire Complete!", Toast.LENGTH_SHORT).show();
            return;
        }

        Question currentQuestion = allQuestions.get(currentQuestionIndex);

        // Update question text
        questionText.setText(currentQuestion.getQuestion());

        // Update progress bar
        progressBar.setMax(allQuestions.size());
        progressBar.setProgress(currentQuestionIndex + 1);

        // Clear previous views
        optionsGroup.removeAllViews();
        optionsGroup.setVisibility(View.GONE);
        textInput.setVisibility(View.GONE);
        subQuestionLayout.setVisibility(View.GONE);

        // Display question based on type
        if (currentQuestion.hasOptions()) {
            displayMultipleChoiceQuestion(currentQuestion);
        } else {
            displayTextQuestion();
        }

        // Update button states
        backButton.setEnabled(currentQuestionIndex > 0);

        // Load previous answer if exists
        loadPreviousAnswer();

        // Change Next button to "Finish" on last question
        if (currentQuestionIndex == allQuestions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }
    }

    private void displayMultipleChoiceQuestion(Question question) {
        optionsGroup.setVisibility(View.VISIBLE);

        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setId(View.generateViewId());
            optionsGroup.addView(radioButton);
        }
    }

    private void displayTextQuestion() {
        textInput.setVisibility(View.VISIBLE);
        textInput.setText("");
        textInput.setHint("Enter your answer here...");
    }

    private void handleSubQuestionVisibility() {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);

        if (currentQuestion.hasSubQuestion()) {
            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadio = findViewById(selectedId);
                String selectedAnswer = selectedRadio.getText().toString();

                if (selectedAnswer.equals(currentQuestion.getSub_question().getCondition())) {
                    // Show sub-question
                    subQuestionLayout.setVisibility(View.VISIBLE);
                    subQuestionText.setText(currentQuestion.getSub_question().getField().getQuestion());
                    subQuestionInput.setText("");
                } else {
                    // Hide sub-question
                    subQuestionLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private boolean saveCurrentAnswer() {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);
        String answer = "";
        String subAnswer = null;

        // Get main answer
        if (currentQuestion.hasOptions()) {
            int selectedId = optionsGroup.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return false;
            }
            RadioButton selectedRadio = findViewById(selectedId);
            answer = selectedRadio.getText().toString();
        } else {
            answer = textInput.getText().toString().trim();
            if (answer.isEmpty()) {
                Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Get sub-answer if visible
        if (subQuestionLayout.getVisibility() == View.VISIBLE) {
            subAnswer = subQuestionInput.getText().toString().trim();
            if (subAnswer.isEmpty()) {
                Toast.makeText(this, "Please complete the additional field", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Save or update response
        UserResponse response = new UserResponse(currentQuestion.getQuestion_id(), answer);
        if (subAnswer != null) {
            response.setSubAnswer(subAnswer);
        }

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

        return true;
    }

    // Save response to Firebase
    private void saveResponseToFirebase() {
        if (userResponses.isEmpty()) return;

        // Get the most recent response
        UserResponse latestResponse = userResponses.get(userResponses.size() - 1);

        // Save the UserResponse object directly to Firebase
        questionnaireRef.child(sessionId)
                .child("responses")
                .child(latestResponse.getQuestionId())
                .setValue(latestResponse)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Failed to save response", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadPreviousAnswer() {
        Question currentQuestion = allQuestions.get(currentQuestionIndex);

        for (UserResponse response : userResponses) {
            if (response.getQuestionId().equals(currentQuestion.getQuestion_id())) {
                if (currentQuestion.hasOptions()) {
                    for (int i = 0; i < optionsGroup.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) optionsGroup.getChildAt(i);
                        if (radioButton.getText().toString().equals(response.getAnswer())) {
                            radioButton.setChecked(true);
                            break;
                        }
                    }
                } else {
                    textInput.setText(response.getAnswer());
                }

                if (response.getSubAnswer() != null) {
                    subQuestionInput.setText(response.getSubAnswer());
                }
                break;
            }
        }
    }

    private void moveToNextQuestion() {
        if (currentQuestionIndex == allQuestions.size() - 1) {
            Toast.makeText(this, "Questionnaire Complete!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentQuestionIndex++;

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

        displayCurrentQuestion();
    }


    private void moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayCurrentQuestion();
        }
    }

    private void loadBranchSpecificQuestions() {
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
    }

    private int getBranchQuestionsCount() {
        int warmUpCount = questionnaireData.getWarm_up().size();

        int followUpCount = 0;
        if (questionnaireData.getFollow_up() != null) {
            followUpCount = questionnaireData.getFollow_up().size();
        }

        int totalSoFar = allQuestions.size();
        int branchCount = totalSoFar - warmUpCount - followUpCount;

        return branchCount;
    }
}
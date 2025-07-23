package com.b07safetyplanapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class TestExitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button exitButton = new Button(this);
        exitButton.setText("Exit App");

        exitButton.setOnClickListener(v -> finishAndRemoveTask());

        setContentView(exitButton);
    }
}

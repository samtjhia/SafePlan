package com.b07safetyplanapp.utils;

import android.content.Context;

import com.b07safetyplanapp.models.questionnaire.QuestionnaireRoot;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * QuestionnaireParser is a utility class responsible for loading and parsing
 * the questionnaire JSON file from the app's assets into a QuestionnaireRoot object.
 */
public class QuestionnaireParser {

    /**
     * Loads and parses the questionnaire.json file from the assets folder.
     *
     * @param context The context used to access the application's assets.
     * @return A QuestionnaireRoot object representing the parsed questionnaire structure,
     *         or null if an error occurs during loading or parsing.
     */
    public static QuestionnaireRoot loadQuestionnaire(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("questionnaire.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            return gson.fromJson(json, QuestionnaireRoot.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
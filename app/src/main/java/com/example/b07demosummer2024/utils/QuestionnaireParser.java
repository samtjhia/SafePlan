package com.example.b07demosummer2024.utils;

import android.content.Context;
import com.google.gson.Gson;
import com.example.b07demosummer2024.models.QuestionnaireRoot;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class QuestionnaireParser {

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
package com.b07safetyplanapp.utils;

import android.content.Context;

import com.b07safetyplanapp.models.questionnaire.QuestionnaireRoot;
import com.b07safetyplanapp.models.supportresources.ResourceDirectory;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceParser {
    public static ResourceDirectory loadResourceDirectory(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("support_resources.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            return gson.fromJson(json, ResourceDirectory.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

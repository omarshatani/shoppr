package com.shoppr.app.domain.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.AuthCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shoppr.app.data.firebase.AuthCredentialDeserializer;

import java.util.HashMap;

public class CredentialStorage {
    private final SharedPreferences preferences;
    private final Gson gson;
    private final String SHARED_PREF_CREDENTIALS = "pref_credentials";
    private final String SHARED_PREF_KEY = "credentials";

    public CredentialStorage(Context context) {
        preferences = context.getSharedPreferences(SHARED_PREF_CREDENTIALS, Context.MODE_PRIVATE);
        gson = new GsonBuilder().registerTypeAdapter(AuthCredential.class, new AuthCredentialDeserializer()).create();
    }

    public void saveCredentials(HashMap<String, String> credentials) {
        String credentialsJson = gson.toJson(credentials);
        preferences.edit().putString(SHARED_PREF_KEY, credentialsJson).apply();
    }

    public AuthCredential getCredentials() {
        String credentialsJson = preferences.getString(SHARED_PREF_KEY, null);
        if (credentialsJson != null) {
            return gson.fromJson(credentialsJson, AuthCredential.class);
        }
        return null;
    }

    public void clearCredentials() {
        preferences.edit().remove(SHARED_PREF_KEY).apply(); // Erase the credentials
    }
}
package com.shoppr.app.data.firebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class AuthCredentialDeserializer implements JsonDeserializer<AuthCredential> {
	@Override
	public AuthCredential deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		String idToken = json.getAsJsonObject().get("idToken").getAsString();

		if (idToken == null) {
			return null;
		}

		return GoogleAuthProvider.getCredential(idToken, null);
	}
}
package com.shoppr.app.domain.utils;

import android.app.Activity;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.shoppr.app.data.listing.model.Listing;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Listing> convertJsonToArrayList(String json) {
        Gson gson = new Gson();
        Listing[] articles = gson.fromJson(json, Listing[].class);
        return new ArrayList<>(java.util.Arrays.asList(articles));
    }

    public static ArrayList<Listing> convertJsonMockToListings(Activity activity) {
        AssetManager assetManager = activity.getAssets();
        String json;
        try {
            InputStream inputStream = assetManager.open("LISTINGS_MOCK.json");
            StringBuilder builder = new StringBuilder();
            int data;
            while ((data = inputStream.read()) != -1) {
                builder.append((char) data);
            }
            json = builder.toString();
            return JsonUtils.convertJsonToArrayList(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

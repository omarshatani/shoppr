package com.shoppr.app.domain.utils;

import com.google.gson.Gson;
import com.shoppr.app.data.listing.model.Listing;

import java.util.ArrayList;

public class JsonUtils {
    public static ArrayList<Listing> convertJsonToArrayList(String json) {
        Gson gson = new Gson();
        Listing[] articles = gson.fromJson(json, Listing[].class);
        return new ArrayList<>(java.util.Arrays.asList(articles));
    }
}

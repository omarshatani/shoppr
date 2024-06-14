package com.shoppr.app.domain.utils;

import android.app.Activity;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
	public static List<Listing> convertJsonToArrayList(String json) {
        Gson gson = new Gson();
		ListingItem[] articles = gson.fromJson(json, ListingItem[].class);
        return new ArrayList<>(java.util.Arrays.asList(articles));
    }

	public static List<Listing> convertJsonMockToListings(Activity activity) {
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

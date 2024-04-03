package com.shoppr.app.data.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class ClusterMarkerItem implements ClusterItem {
    private final LatLng position;
    private final String title;
    private final String snippet;
    private final Float zIndex;

    public ClusterMarkerItem(double lat, double lng, String title, String snippet) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.zIndex = null;
    }

    public ClusterMarkerItem(double lat, double lng, String title, String snippet, Float zIndex) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.zIndex = zIndex;
    }

    @NonNull
    @Override
    public LatLng getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getTitle() {
        return title;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return snippet;
    }

    @Nullable
    @Override
    public Float getZIndex() {
        return zIndex;
    }
}

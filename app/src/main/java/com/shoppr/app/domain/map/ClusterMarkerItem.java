package com.shoppr.app.domain.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.shoppr.app.data.listing.model.ListingType;

public class ClusterMarkerItem implements ClusterItem {
	private final LatLng position;
	private final String title;
	private final String snippet;
	private final Float zIndex;
	private final ListingType type;
	private String id;

	public ClusterMarkerItem(String id, double lat, double lng, String title, String snippet, ListingType type) {
		this.id = id;
		this.position = new LatLng(lat, lng);
		this.title = title;
		this.snippet = snippet;
		this.type = type;
		this.zIndex = null;
	}

	public ClusterMarkerItem(double lat, double lng, String title, String snippet, ListingType type) {
		this.position = new LatLng(lat, lng);
		this.title = title;
		this.snippet = snippet;
		this.type = type;
		this.zIndex = null;
	}

	public ClusterMarkerItem(double lat, double lng, String title, String snippet, ListingType type, Float zIndex) {
		this.position = new LatLng(lat, lng);
		this.title = title;
		this.snippet = snippet;
		this.type = type;
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

	public ListingType getType() {
		return type;
	}

	public String getId() {
		return id;
	}
}

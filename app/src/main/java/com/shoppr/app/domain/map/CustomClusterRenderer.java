package com.shoppr.app.domain.map;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.shoppr.app.data.listing.model.ListingType;

public class CustomClusterRenderer extends DefaultClusterRenderer<ClusterMarkerItem> {


	public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<ClusterMarkerItem> clusterManager) {
		super(context, map, clusterManager);
	}

	@Override
	protected void onBeforeClusterItemRendered(@NonNull ClusterMarkerItem item, @NonNull MarkerOptions markerOptions) {
		super.onBeforeClusterItemRendered(item, markerOptions);
		float hue = item.getType() == ListingType.BUY ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_GREEN;

		// Set the marker color based on the item's properties
		markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hue));
	}
}
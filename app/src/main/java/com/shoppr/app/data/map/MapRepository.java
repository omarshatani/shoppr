package com.shoppr.app.data.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.GoogleMap;

public class MapRepository {

    private static volatile MapRepository instance;

    private MapDataSource dataSource;

    private MapRepository(MapDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static MapRepository getInstance(MapDataSource dataSource) {
        if (instance == null) {
            instance = new MapRepository(dataSource);
        }
        return instance;
    }

}

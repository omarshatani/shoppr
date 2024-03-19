package com.shoppr.app.data.database;

import com.google.android.gms.tasks.Task;
import com.shoppr.app.data.common.Callback;

import java.util.ArrayList;

public abstract class Database<T> {
    public abstract Task<Void> add(Object data);

    public abstract void get(Callback<ArrayList<T>> callback);
}
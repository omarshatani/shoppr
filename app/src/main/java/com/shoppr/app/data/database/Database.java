package com.shoppr.app.data.database;

import com.google.android.gms.tasks.Task;
import com.shoppr.app.data.common.Callback;

import java.util.ArrayList;

public abstract class Database<T> {
    public abstract Task<Void> add(Object data);

    public abstract void add(String id, Object data);

    public abstract void getAll(Callback<ArrayList<T>> callback);
}
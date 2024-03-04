package com.shoppr.app.data.common;

import androidx.annotation.Nullable;

public class Resource<T, E extends Error> {
    private final T data;
    private final boolean isLoading;
    private final E error;

    public Resource(@Nullable T data, @Nullable E error, boolean isLoading) {
        this.data = data;
        this.error = error;
        this.isLoading = isLoading;
    }

    public Resource() {
        this.data = null;
        this.error = null;
        this.isLoading = false;
    }

    public Resource(@Nullable T data) {
        this.data = data;
        this.error = null;
        this.isLoading = false;
    }

    public Resource(@Nullable T data, @Nullable E error) {
        this.data = data;
        this.error = error;
        this.isLoading = false;
    }


    public T getData() {
        return this.data;
    }

    public E getError() {
        return this.error;
    }

    public boolean isLoading() {
        return isLoading;
    }
}

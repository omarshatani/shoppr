package com.shoppr.app.data.common;

import androidx.annotation.Nullable;

public interface Callback<T> {
    void onSuccess(@Nullable Result.Success<T> result);

    default void onComplete(@Nullable Result<T> result) {
    }

    default void onError(@Nullable Exception exception) {
    }

    ;

}

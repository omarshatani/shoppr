package com.shoppr.app.domain.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceUtils {
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            return displayMetrics.heightPixels;
        }

        return 0; // Return 0 if WindowManager is not available
    }
}

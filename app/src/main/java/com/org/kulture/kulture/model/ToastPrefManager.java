package com.org.kulture.kulture.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tkru on 9/13/2017.
 */

public class ToastPrefManager {
    SharedPreferences toastpref;
    SharedPreferences.Editor toasteditor;
    Context toast_context;

    // shared pref mode
    int TOAST_PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "toast-welcome";

    private static final String TOAST_IS_FIRST_TIME_LAUNCH = "IsFirstTimeToast";

    public ToastPrefManager(Context context) {
        this.toast_context = context;
        toastpref = toast_context.getSharedPreferences(PREF_NAME, TOAST_PRIVATE_MODE);
        toasteditor = toastpref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        toasteditor.putBoolean(TOAST_IS_FIRST_TIME_LAUNCH, isFirstTime);
        toasteditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return toastpref.getBoolean(TOAST_IS_FIRST_TIME_LAUNCH, true);
    }

}
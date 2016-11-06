package com.springer.patryk.tas_android.utils;

import android.widget.EditText;

/**
 * Created by Patryk on 2016-11-06.
 */

public class InputUtils {
    public static boolean checkIsEmpty(EditText editText) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setError("This field is required");
            return true;
        }
        return false;
    }
}

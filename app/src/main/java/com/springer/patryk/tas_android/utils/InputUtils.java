package com.springer.patryk.tas_android.utils;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Patryk on 2016-11-06.
 */

public class InputUtils {
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean checkIsEmpty(String text) {
        if (text.trim().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean checkIsValidEmail(String email) {
        return EMAIL_ADDRESS_PATTERN
                .matcher(email.trim())
                .matches();
    }

    public static boolean checkIsValidLogin(String login) {
        return Pattern.matches("^[a-zA-Zsda0-9]{1,10}$", login.trim());
    }

    public static boolean comparePassword(String password, String comparedPassword) {
        return password
                .trim()
                .equals(comparedPassword.trim());
    }
}

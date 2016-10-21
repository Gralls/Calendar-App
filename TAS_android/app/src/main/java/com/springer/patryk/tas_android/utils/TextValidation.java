package com.springer.patryk.tas_android.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Patryk on 2016-10-21.
 */

public abstract class TextValidation implements TextWatcher {

    private TextView fieldToValidate;

    public TextValidation(TextView fieldToValidate) {
        this.fieldToValidate=fieldToValidate;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String textToValidate = fieldToValidate.getText().toString();
        validate(fieldToValidate,textToValidate);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

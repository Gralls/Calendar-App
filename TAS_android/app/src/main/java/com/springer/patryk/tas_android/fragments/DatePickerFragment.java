package com.springer.patryk.tas_android.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import org.joda.time.DateTime;

import java.util.Calendar;

/**
 * Created by Patryk on 10.12.2016.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog.OnDateSetListener mListener;

    public DatePickerFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DateTime now = DateTime.now();
        return new DatePickerDialog(getActivity(),mListener,
                now.getYear(),
                now.getMonthOfYear(), now.getDayOfMonth());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }
}

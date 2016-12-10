package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Patryk on 10.12.2016.
 */

public class CreateTaskFragment extends DialogFragment {

    @BindView(R.id.setStartDate)
    TextView startDate;

    private Context mContext;
    public CreateTaskFragment() {
    }

    public static CreateTaskFragment newInstance() {
        CreateTaskFragment createTaskFragment = new CreateTaskFragment();

        return createTaskFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.new_task_dialog, container);
        ButterKnife.bind(this, rootView);


        return rootView;
    }
}

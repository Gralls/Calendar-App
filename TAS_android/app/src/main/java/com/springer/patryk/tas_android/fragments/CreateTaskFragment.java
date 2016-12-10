package com.springer.patryk.tas_android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class CreateTaskFragment extends Fragment {

    @BindView(R.id.setStartDate)
    TextView startDate;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.new_task_dialog,null);
        ButterKnife.bind(this, rootView);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.show(ft, "date_dialog");
            }
        });

        return rootView;
    }
}

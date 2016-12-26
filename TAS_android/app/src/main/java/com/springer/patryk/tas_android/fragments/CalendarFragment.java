package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.SessionManager;
import com.springer.patryk.tas_android.adapters.CalendarDayOfMonthAdapter;
import com.springer.patryk.tas_android.adapters.CalendarGridAdapter;
import com.springer.patryk.tas_android.models.Task;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 23.11.2016.
 */

public class CalendarFragment extends Fragment {

    private Context mContext;
    private static final String LOG_TAG = CalendarFragment.class.getSimpleName();


    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.nextArrow)
    ImageView nextArrow;
    @BindView(R.id.monthText)
    TextView currentMonth;
    @BindView(R.id.monthView)
    RecyclerView monthView;
    @BindView(R.id.daysTitle)
    GridView dayTitles;


    SessionManager sessionManager;

    private DateTime dateNow;
    private CalendarGridAdapter calendarGridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_fragment, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(mContext);
        mContext.getSharedPreferences("DayDetails",Context.MODE_PRIVATE).edit().clear().apply();
        dateNow = DateTime.now();

        dayTitles.setAdapter(new CalendarDayOfMonthAdapter(mContext, getResources().getStringArray(R.array.day_names)));

        calendarGridAdapter = new CalendarGridAdapter(mContext, dateNow);
        Log.d(LOG_TAG, Realm.getDefaultInstance().where(Task.class).equalTo("startDate", dateNow.toString()).findAll().toString());
        monthView.setAdapter(calendarGridAdapter);
        monthView.setLayoutManager(new GridLayoutManager(mContext,7));
        /*monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Task> tasks = (List<Task>) calendarGridAdapter.getItem(position);
                if(tasks.size()>0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tasks", (Serializable) tasks);
                    DayDetailsFragment fragment = new DayDetailsFragment();
                    fragment.setArguments(bundle);
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContent, fragment, null)
                            .addToBackStack(null)
                            .commit();
                }
                else
                    Toast.makeText(mContext,"No task at this day",Toast.LENGTH_SHORT).show();
            }
        });*/
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPreviousMonth();
                updateDate();
            }
        });
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMonth();
                updateDate();
            }
        });


        updateDate();
        getTask();
        return rootView;
    }

    public void updateDate() {
        String monthLabel = DateUtils.
                formatDateTime(
                        mContext,
                        dateNow,
                        DateUtils.FORMAT_SHOW_YEAR
                                | DateUtils.FORMAT_NO_MONTH_DAY
                );
        currentMonth.setText(monthLabel);
        calendarGridAdapter.setMonthToShow(dateNow);
    }

    public void setPreviousMonth() {
        dateNow = dateNow.minusMonths(1);
        updateDate();
    }

    public void setNextMonth() {
        dateNow = dateNow.plusMonths(1);
        updateDate();
    }

    public void getTask() {
        HashMap<String, String> userDetails = sessionManager.getUserDetails();
        Call<List<Task>> call = MyApp.getApiService().getTasks(userDetails.get("id"));
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(response.body());
                realm.commitTransaction();
                calendarGridAdapter.setTasks(response.body());
                Log.d(LOG_TAG,"Task: "+realm.where(Task.class).findAll().toString());
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {

            }
        });
    }

}

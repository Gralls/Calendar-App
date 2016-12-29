package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 23.11.2016.
 */

public class CalendarFragment extends BaseFragment {

    private Context mContext;
    private static final String LOG_TAG = CalendarFragment.class.getSimpleName();


    @BindView(R.id.backArrow)
    ImageView backArrow;
    @BindView(R.id.nextArrow)
    ImageView nextArrow;
    @BindView(R.id.monthText)
    TextView currentMonth;
    @BindView(R.id.monthView)
    RealmRecyclerView monthView;
    @BindView(R.id.daysTitle)
    GridView dayTitles;


    SessionManager sessionManager;

    private DateTime dateNow;
    private CalendarGridAdapter calendarGridAdapter;
    private CalendarDayOfMonthAdapter calendarDayOfMonthAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        sessionManager = new SessionManager(mContext);
        dateNow = DateTime.now();
        userDetails = sessionManager.getUserDetails();
        updateTasks(userDetails.get("id"));
        final RealmResults<Task> realmResults = realm.where(Task.class).findAll();
        realmResults.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> element) {
                calendarGridAdapter.notifyDataSetChanged();
            }
        });
        calendarGridAdapter = new CalendarGridAdapter(getActivity(), dateNow, realmResults, true, true);
        calendarDayOfMonthAdapter = new CalendarDayOfMonthAdapter(mContext, getResources().getStringArray(R.array.day_names));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayTitles.setAdapter(calendarDayOfMonthAdapter);
        monthView.setAdapter(calendarGridAdapter);

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext.getSharedPreferences("DayDetails", Context.MODE_PRIVATE).edit().clear().apply();
        return inflater.inflate(R.layout.calendar_fragment, container, false);
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

    public void updateUserTasks() {

        Call<List<Task>> call = MyApp.getApiService().getTasks(userDetails.get("id"));

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, final Response<List<Task>> response) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(response.body());
                    }
                });
            }
            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(mContext, "Check internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

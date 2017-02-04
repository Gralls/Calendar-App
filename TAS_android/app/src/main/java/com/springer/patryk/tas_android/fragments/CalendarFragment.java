package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.CalendarDayOfMonthAdapter;
import com.springer.patryk.tas_android.adapters.CalendarGridAdapter;
import com.springer.patryk.tas_android.models.UserState;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Patryk on 23.11.2016.
 */

public class CalendarFragment extends BaseFragment {

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
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private Context mContext;
    private static final String LOG_TAG = CalendarFragment.class.getSimpleName();
    private DateTime dateNow;
    private CalendarGridAdapter calendarGridAdapter;
    private CalendarDayOfMonthAdapter calendarDayOfMonthAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        dateNow = DateTime.now();

        syncWithServer(userDetails.get("id"));

        final RealmResults<UserState> realmResults = realm.where(UserState.class).findAll();
        realmResults.addChangeListener(new RealmChangeListener<RealmResults<UserState>>() {
            @Override
            public void onChange(RealmResults<UserState> element) {
                calendarGridAdapter.notifyDataSetChanged();
            }
        });

        calendarGridAdapter = new CalendarGridAdapter(mContext, dateNow, realmResults, true, true);
        calendarDayOfMonthAdapter = new CalendarDayOfMonthAdapter(mContext, getResources().getStringArray(R.array.day_names));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayTitles.setAdapter(calendarDayOfMonthAdapter);
        monthView.setAdapter(calendarGridAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncWithServer(userDetails.get("id"));
                refreshLayout.setRefreshing(false);
            }
        });
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
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

}

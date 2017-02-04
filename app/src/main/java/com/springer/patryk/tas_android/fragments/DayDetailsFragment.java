package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.adapters.MeetingsListAdapter;
import com.springer.patryk.tas_android.adapters.TaskListAdapter;
import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.BindView;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmResults;

/**
 * Created by Patryk on 2016-12-16.
 */

public class DayDetailsFragment extends BaseFragment {

    private static final int NUM_PAGES = 2;
    @BindView(R.id.dayDetailsPager)
    ViewPager mPager;
    private DayDetailsPagerAdapter mPagerAdapter;
    @BindView(R.id.dayDetailsTabs)
    TabLayout tabLayout;
    @BindView(R.id.currentDay)
    TextView currentDay;


    private Context mContext;
    private DateTime currentDate;

    private DateTimeFormatter dateTimeFormatter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        dateTimeFormatter= DateTimeFormat.forPattern("dd MMMM yyyy");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentDay.setText(dateTimeFormatter.print(currentDate));
        mPagerAdapter = new DayDetailsPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mPager);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.day_details, null);
        currentDate = (DateTime) getArguments().getSerializable("tasks");

        mContext.getSharedPreferences("DayDetails",Context.MODE_PRIVATE)
                .edit()
                .putString("CurrentDate",currentDate.toString())
                .apply();


        RealmResults<Task> realmResults = realm
                .where(Task.class)
                .equalTo("startDate", currentDate.toLocalDate().toString())
                .findAllSorted("startTime");

     //   taskAdapter = new TaskListAdapter(mContext, realmResults,userDetails.get("id"), true, true);

        RealmResults<Meeting> meetingRealmResults = realm
                .where(Meeting.class)
                .equalTo("startDate", currentDate.toLocalDate().toString())
                .findAllSorted("startTime");

      //  meetingsAdapter = new MeetingsListAdapter(mContext, meetingRealmResults, userDetails.get("id"), true, true);
        return rootView;
    }


    private class DayDetailsPagerAdapter extends FragmentStatePagerAdapter{

        public DayDetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            Bundle args = new Bundle();
            switch (position) {
                case 0:
                    args.putSerializable("TaskDate",currentDate.toLocalDate().toString());
                    fragment=new AllTasksFragment();
                    fragment.setArguments(args);
                    break;
                case 1:
                    args.putSerializable("MeetingDate",currentDate.toLocalDate().toString());
                    fragment=new AllMeetingsFragment();
                    fragment.setArguments(args);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Tasks";
                case 1:
                    return "Meetings";
            }
            return "";
        }
    }


}

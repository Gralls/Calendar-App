package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.fragments.DayDetailsFragment;
import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.UserState;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.StrictChronology;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by Patryk on 25.11.2016.
 */

public class CalendarGridAdapter extends RealmBasedRecyclerViewAdapter<UserState, CalendarGridAdapter.ViewHolder> {


    private DateTime currentDay;
    private List<DateTime> daysOfMonth;
    private FragmentManager manager;

    public CalendarGridAdapter(Context context, DateTime currentMonth, RealmResults<UserState> realmResults, boolean automaticUpdate, boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        this.currentDay = currentMonth;
        manager = ((AppCompatActivity) context).getSupportFragmentManager();
        setMonthToShow(currentMonth);
    }



    public class ViewHolder extends RealmViewHolder {
        TextView dayNumber;
        TextView dayTask;
        TextView dayMeeting;
        LinearLayout day;

        public ViewHolder(View itemView) {
            super(itemView);
            dayNumber = (TextView) itemView.findViewById(R.id.dayTitle);
            dayTask = (TextView) itemView.findViewById(R.id.dayTask);
            dayMeeting = (TextView) itemView.findViewById(R.id.dayMeeting);
            day = (LinearLayout) itemView.findViewById(R.id.dayItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateTime date = daysOfMonth.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("tasks", date);
                    DayDetailsFragment fragment = new DayDetailsFragment();
                    fragment.setArguments(bundle);
                    manager
                            .beginTransaction()
                            .replace(R.id.mainContent, fragment, null)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.day_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        DateTime date = daysOfMonth.get(position);

        if (date == null) {
            holder.day.setVisibility(View.INVISIBLE);
        } else {
            holder.dayNumber.setText(String.valueOf(date.getDayOfMonth()));
            holder.day.setVisibility(View.VISIBLE);
            int taskAtCurrentDay = tasksCountOnPosition(date);
            int meetingAtCurrentDay = meetingsCountOnPosition(date);
            if (taskAtCurrentDay > 0) {
                holder.dayTask.setVisibility(View.VISIBLE);
                holder.dayTask.setText(String.valueOf(taskAtCurrentDay));
            }
            else if (meetingAtCurrentDay>0){
                holder.dayMeeting.setVisibility(View.VISIBLE);
                holder.dayMeeting.setText(String.valueOf(meetingAtCurrentDay));
            }
            else {
                holder.dayTask.setVisibility(View.INVISIBLE);
                holder.dayMeeting.setVisibility(View.INVISIBLE);
            }
            if (date.toLocalDate().equals(currentDay.toLocalDate())) {
                holder.day.setBackgroundResource(R.drawable.today_background);
            } else {
                holder.day.setBackgroundResource(0);
            }
        }
    }

    public int tasksCountOnPosition(DateTime currentDate) {
        int taskCounter = 0;
        if(realmResults.size()>0) {
            UserState userState = realmResults.first();
            for (Task task : userState.getTask()) {
                LocalDate localDate = LocalDate.parse(task.getStartDate());
                if (currentDate.toLocalDate().equals(localDate)) {
                    taskCounter++;
                }
            }
        }
        return taskCounter;
    }

    public int meetingsCountOnPosition(DateTime currentDate) {
        int meetingCounter = 0;
        if(realmResults.size()>0) {
            UserState userState = realmResults.first();
            for (Meeting meeting : userState.getMeeting()) {
                LocalDate localDate = LocalDate.parse(meeting.getStartDate());
                if (currentDate.toLocalDate().equals(localDate)) {
                    meetingCounter++;
                }
            }
        }
        return meetingCounter;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }


    public void setMonthToShow(DateTime monthToShow) {
        daysOfMonth = convertToList(monthToShow);
        notifyDataSetChanged();
    }

    private List<DateTime> convertToList(DateTime monthToShow) {
        List<DateTime> month = new ArrayList<>();

        int firstDayOfDisplayedMonth = monthToShow.withDayOfMonth(1).getDayOfWeek() % 7;
        int emptyPosition = 0;

        while (emptyPosition < firstDayOfDisplayedMonth) {
            month.add(null);
            emptyPosition++;
        }

        int daysCountInMonth = monthToShow.dayOfMonth().getMaximumValue();

        monthToShow = monthToShow
                .dayOfMonth()
                .withMinimumValue();

        for (int i = 0; i < daysCountInMonth; i++) {

            month.add(monthToShow);
            monthToShow = monthToShow.plusDays(1);
        }
        return month;
    }

}

package com.springer.patryk.tas_android.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.fragments.CreateMeetingFragment;
import com.springer.patryk.tas_android.models.Guest;
import com.springer.patryk.tas_android.models.Meeting;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2017-01-02.
 */

public class MeetingsListAdapter extends RealmBasedRecyclerViewAdapter<Meeting, MeetingsListAdapter.ViewHolder> {
    private final DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
    private android.support.v4.app.FragmentManager manager;
    private String userID;
    private String token;

    public MeetingsListAdapter(Context context, RealmResults<Meeting> realmResults, String userID,String token, boolean automaticUpdate, boolean animateResults) {
        super(context, realmResults, automaticUpdate, animateResults);
        manager = ((AppCompatActivity) context).getSupportFragmentManager();
        this.userID = userID;
        this.token=token;
    }


    public class ViewHolder extends RealmViewHolder {
        TextView title;
        TextView description;
        TextView startDate;
        TextView creator;
        TextView place;
        TextView guests;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.meetingTitle);
            description = (TextView) view.findViewById(R.id.meetingDescription);
            startDate = (TextView) view.findViewById(R.id.meetingStartDate);
            creator = (TextView) view.findViewById(R.id.meetingCreator);
            place = (TextView) view.findViewById(R.id.meetingPlace);
            guests = (TextView) view.findViewById(R.id.meetingGuest);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userID.equals(realmResults.get(getAdapterPosition()).getUser())) {
                        Bundle args = new Bundle();
                        args.putSerializable("Meeting", realmResults.get(getAdapterPosition()).getId());
                        CreateMeetingFragment createMeetingFragment = new CreateMeetingFragment();
                        createMeetingFragment.setArguments(args);
                        manager.beginTransaction().replace(R.id.mainContent, createMeetingFragment, null).addToBackStack(null).commit();
                    } else
                        Toast.makeText(getContext(), "You cant edit this meeting", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.meeting_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder holder, int position) {
        Meeting meeting = realmResults.get(position);
        holder.title.setText(meeting.getTitle());
        holder.description.setText(meeting.getDescription());
        LocalDate localDate = LocalDate.parse(meeting.getStartDate());
        DateTime startDate = localDate.toDateTime(LocalTime.parse(meeting.getStartTime()));
        holder.startDate.setText(fmt.print(startDate));
        holder.creator.setText(meeting.getUser());
        holder.place.setText(meeting.getPlace());
        String guests = "";
        for (Guest guest :
                meeting.getGuests()) {
            guests = guests + guest.getLogin() + " - " + guest.getFlag() + '\n';
        }
        if (guests.length() > 0) {
            guests = guests.substring(0, guests.length() - 1);
        }
        holder.guests.setText(guests);
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getItemCount() {
        return realmResults.size();
    }


    @Override
    public void onItemSwipedDismiss(int position) {
        if (realmResults.get(position).getUser().equals(userID)) {
            deleteMeetingFromDB(position);
            super.onItemSwipedDismiss(position);
        } else {
            removeFromGuests(position);
        }
    }

    public void deleteMeetingFromDB(int position) {
        Call<Void> call = MyApp.getApiService().deleteMeeting(token,realmResults.get(position).getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(getContext(), "Meeting deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void removeFromGuests(int position) {

    }


}

package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.activities.MainActivity;
import com.springer.patryk.tas_android.models.Guest;
import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.User;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 04.01.2017.
 */

public class CreateMeetingFragment extends BaseFragment {

    @BindView(R.id.newMeetingTitle)
    EditText meetingTitle;
    @BindView(R.id.newMeetingDescription)
    EditText meetingDescription;
    @BindView(R.id.newMeetingPlace)
    EditText meetingPlace;
    @BindView(R.id.newMeetingStartDate)
    DatePicker meetingStartDate;
    @BindView(R.id.newMeetingStartTime)
    TimePicker meetingStartTime;
    @BindView(R.id.newMeetingGuests)
    EditText meetingGuests;
    @BindView(R.id.createNewMeeting)
    Button createMeeting;


    private SharedPreferences sharedPreferences;
    private boolean isNewMeeting;
    private Meeting meeting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_meeting_fragment, null);
        sharedPreferences = getContext()
                .getSharedPreferences("DayDetails", Context.MODE_PRIVATE);
        ((MainActivity) getActivity()).hideFabs();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (checkIsEdit()) {
            isNewMeeting = false;
            String id = (String) getArguments().getSerializable("Meeting");
            meeting = realm.where(Meeting.class).equalTo("id", id).findFirst();
            meeting = realm.copyFromRealm(meeting);
            prepareView();
        } else {
            isNewMeeting = true;
            DateTime currentDate;
            if (!sharedPreferences.getString("CurrentDate", "").equals("")) {
                currentDate = ISODateTimeFormat.dateTime().parseDateTime(sharedPreferences.getString("CurrentDate", ""));
            } else {
                currentDate = DateTime.now();
            }
            meetingStartDate.updateDate(currentDate.getYear(), currentDate.getMonthOfYear(), currentDate.getDayOfMonth());
            meetingStartTime.setCurrentHour(currentDate.getHourOfDay());
            meetingStartTime.setCurrentMinute(currentDate.getMinuteOfHour());
        }

        createMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMeeting();
            }
        });
    }

    private boolean checkIsEdit() {
        return getArguments() != null;
    }


    public void prepareView() {
        meetingTitle.setText(meeting.getTitle());
        meetingDescription.setText(meeting.getDescription());
        LocalDate localDate = LocalDate.parse(meeting.getStartDate());
        LocalTime localTime = LocalTime.parse(meeting.getStartTime());
        meetingStartDate.updateDate(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        meetingStartTime.setCurrentHour(localTime.getHourOfDay());
        meetingStartTime.setCurrentMinute(localTime.getMinuteOfHour());
        meetingGuests.setText(setGuestsInput());
        meetingPlace.setText(meeting.getPlace());
    }

    private String setGuestsInput() {
        String guests = "";
        for (Guest guest : meeting.getGuests()) {
            guests = guests + guest.getLogin() + ",";
        }
        if (guests.length() > 0)
            guests = guests.substring(0, guests.length() - 1);
        return guests;
    }

    public void initMeeting() {
        final RealmList<Guest> guestRealmList = convertInputGuests(meetingGuests.getText().toString());


        int x = 0;
    }

    public RealmList<Guest> convertInputGuests(final String input) {
        final String[] splitedGuests = input.trim().split(",");
        final RealmList<Guest> guests = new RealmList<>();
        Call<List<User>> call = MyApp.getApiService().getUsers(sessionManager.getToken(), splitedGuests);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!splitedGuests[0].equals("")) {
                    for (User user : response.body()) {
                        Guest guest = new Guest();
                        guest.setFlag("pending");
                        guest.setId(user.getId());
                        guest.setLogin(user.getLogin());
                        guests.add(guest);
                    }
                }


                if (isNewMeeting) {
                    meeting = new Meeting();
                    meeting.setUser(sessionManager.getUserDetails().get("id"));
                    meeting.setTitle(meetingTitle.getText().toString());
                    meeting.setDescription(meetingDescription.getText().toString());
                    LocalDate startDate = new LocalDate(meetingStartDate.getYear(), meetingStartDate.getMonth(), meetingStartDate.getDayOfMonth());
                    meeting.setStartDate(startDate.toString());
                    LocalTime startTime = new LocalTime(meetingStartTime.getCurrentHour(), meetingStartTime.getCurrentMinute());
                    meeting.setStartTime(startTime.toString());
                    meeting.setPlace(meetingPlace.getText().toString());
                    meeting.setGuests(guests);
                    createMeeting();
                } else {
                    meeting.setTitle(meetingTitle.getText().toString());
                    meeting.setDescription(meetingDescription.getText().toString());
                    LocalDate startDate = new LocalDate(meetingStartDate.getYear(), meetingStartDate.getMonth(), meetingStartDate.getDayOfMonth());
                    meeting.setStartDate(startDate.toString());
                    LocalTime startTime = new LocalTime(meetingStartTime.getCurrentHour(), meetingStartTime.getCurrentMinute());
                    meeting.setStartTime(startTime.toString());
                    meeting.setPlace(meetingPlace.getText().toString());
                    meeting.setGuests(guests);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.insertOrUpdate(meeting);
                        }
                    });
                    updateMeeting();
                }


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("CreateTask", t.getMessage());
                showToast("Check internet connection");
            }
        });
        return guests;
    }

    public void createMeeting() {
        Call<Void> call = MyApp.getApiService().createMeeting(sessionManager.getToken(), meeting);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showToast("Meeting created");
                syncWithServer(userDetails.get("id"));
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("CreateTask", t.getMessage());
                showToast("Check internet connection");
            }
        });
    }

    public void editMeetingInDB() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                meeting.setTitle(meetingTitle.getText().toString());
                meeting.setDescription(meetingDescription.getText().toString());

                LocalDate startDate = new LocalDate(meetingStartDate.getYear(), meetingStartDate.getMonth(), meetingStartDate.getDayOfMonth());
                meeting.setStartDate(startDate.toString());
                LocalTime startTime = new LocalTime(meetingStartTime.getCurrentHour(), meetingStartTime.getCurrentMinute());
                meeting.setStartTime(startTime.toString());
                meeting.setGuests(convertInputGuests(meetingGuests.getText().toString()));
                meeting.setPlace(meetingPlace.getText().toString());
            }
        });
    }

    public void updateMeeting() {
        Call<Void> call = MyApp.getApiService().editMeeting(sessionManager.getToken(), meeting.getId(), meeting);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                showToast("Meeting updated");
                getFragmentManager().popBackStack();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("EditMeeting", t.getMessage());
                showToast("Check internet connection");
            }
        });
    }

    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).showMainFab();
        super.onDestroyView();
    }
}

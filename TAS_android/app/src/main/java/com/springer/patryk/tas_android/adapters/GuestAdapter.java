package com.springer.patryk.tas_android.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.springer.patryk.tas_android.models.Guest;

import java.io.IOException;

/**
 * Created by Patryk on 06.01.2017.
 */

public class GuestAdapter extends TypeAdapter<Guest> {
    @Override
    public void write(JsonWriter out, Guest value) throws IOException {
        out.beginObject();
        out.name("id")
                .value(value.getId());
        out.name("flag")
                .value(value.getFlag());
        out.name("login")
                .value(value.getLogin());
        out.endObject();
    }

    @Override
    public Guest read(JsonReader in) throws IOException {
        Guest guest = new Guest();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    guest.setId(in.nextString());
                    break;
                case "flag":
                    guest.setFlag(in.nextString());
                    break;
                case "login":
                    guest.setLogin(in.nextString());
                    break;
            }
        }
        in.endObject();
        return guest;
    }
}

package com.victorneagu.dam.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.victorneagu.dam.Classes.Trip;
import com.victorneagu.dam.R;

import java.util.ArrayList;

public class TripsAdapter extends ArrayAdapter<Trip> {
    private int resourceLayout;
    private Context mContext;

    public TripsAdapter(Context context, int resource, ArrayList<Trip> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Trip t = getItem(position);

        if (t != null) {
            TextView date = v.findViewById(R.id.tvResultDate);
            TextView price = v.findViewById(R.id.tvResultPrice);
            TextView fromTo = v.findViewById(R.id.tvFromTo);
            TextView seats = v.findViewById(R.id.tvResultSeats);
            TextView driver = v.findViewById(R.id.tvResultUserName);
            TextView ageGender = v.findViewById(R.id.tvResultUserAgeGender);

            if (date != null) {
                String hours, minutes;
                if(t.getDate().getHours() == 0)
                    hours = "00";
                else hours = t.getDate().getHours() + "";
                if(t.getDate().getMinutes() == 0)
                    minutes = "00";
                else minutes = t.getDate().getMinutes() + "";
                date.setText(t.getDate().getDate() + "/" + (t.getDate().getMonth() + 1) + "/"
                        + (t.getDate().getYear() + 1900) + "  " + hours + ":" + minutes);
            }

            if (price != null) {
                price.setText(t.getPrice() + " RON");
            }

            if (fromTo != null) {
                fromTo.setText(t.getFrom() + " >> " + t.getTo() + "  (Pickup from " + t.getPickupPoint() + ")");
            }

            if (seats != null) {
                seats.setText(t.getNoAvailableSeats() + " available seats");
            }

            if (driver != null) {
                driver.setText("Driver: " + t.getDriver().getName());
            }

            if (ageGender != null) {
                ageGender.setText(t.getDriver().getAge() + "yo, " + t.getDriver().getGender());
            }
        }
        return v;
    }

}

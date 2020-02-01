package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.Trip;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class CreateRideActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;
    Button btnDatePicker, btnTimePicker;
    TextView tvDate, tvTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);
        setTitle(R.string.createRide);
        user = (User) getIntent().getSerializableExtra("user");
        btnDatePicker = findViewById(R.id.btn_date);
        btnTimePicker = findViewById(R.id.btn_time);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        dbManager = new DBManager(this);
        dbManager.open();
    }

    public void addRide(View view){
        EditText from = findViewById(R.id.etFrom);
        EditText to = findViewById(R.id.etTo);
        EditText pickup = findViewById(R.id.etPickup);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject = new Date();
        String dor_var = "";
        Date time = new Date();
        try{
            dor_var = tvDate.getText().toString();
            dateObject = formatter.parse(dor_var);
            time = new SimpleDateFormat("hh:mm").parse(tvTime.getText().toString());
            dateObject.setHours(time.getHours());
            dateObject.setMinutes(time.getMinutes());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        EditText seats = findViewById(R.id.etSeats);
        EditText price = findViewById(R.id.etPrice);
        if(from.getText().length() == 0 || to.getText().length() == 0 || pickup.getText().length() == 0 || tvDate.getText().length() == 0 ||
            tvTime.getText().length() == 0 || seats.getText().length() == 0 || price.getText().length() == 0)
            Toast.makeText(this, "Please complete all fields!", Toast.LENGTH_SHORT).show();
        else{
            if(dateObject.after(new Date())){
                if(Integer.parseInt(seats.getText().toString()) > 0 && Integer.parseInt(price.getText().toString()) > 0) {
                    Trip trip = new Trip(-1, from.getText().toString(), to.getText().toString(), pickup.getText().toString(),
                    dateObject, Integer.parseInt(price.getText().toString()), user, Integer.parseInt(seats.getText().toString()));
                    dbManager.insertTrip(trip);
                    MainActivity.user.addTrip(trip);
                    Toast.makeText(this, "Ride added!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else Toast.makeText(this, "Values out of bounds!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            tvTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}

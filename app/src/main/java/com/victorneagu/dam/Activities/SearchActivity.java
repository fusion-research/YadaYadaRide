package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.Trip;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;
    Button btnSearchDate, plus, minus;
    TextView tvDate, noSeats;
    EditText from, to;
    private int mYear, mMonth, mDay, seats;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle(R.string.search);
        user = (User) getIntent().getSerializableExtra("user");
        btnSearchDate = findViewById(R.id.btnSearchDate);
        from = findViewById(R.id.etSearchFrom);
        to = findViewById(R.id.etSearchTo);
        tvDate = findViewById(R.id.tvSearchDate);
        btnSearchDate.setOnClickListener(this);
        plus = findViewById(R.id.btnSearchSeatsPlus);
        minus = findViewById(R.id.btnSearchSeatsMinus);
        noSeats = findViewById(R.id.tvSearchNoSeats);
        minus.setEnabled(false);
        seats = Integer.parseInt(noSeats.getText().toString());
        dbManager = new DBManager(this);
        dbManager.open();
    }

    @Override
    public void onClick(View v) {
        if (v == btnSearchDate) {
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
    }

    public void plus(View view){
        seats++;
        noSeats.setText(String.valueOf(seats));
        if(seats > 1)
            minus.setEnabled(true);
    }

    public void minus(View view){
        seats--;
        noSeats.setText(String.valueOf(seats));
        if(seats == 1)
            minus.setEnabled(false);
    }

    public void submitSearch(View view){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObject = new Date();
        String dor_var = "";
        try{
            dor_var = tvDate.getText().toString();
            dateObject = formatter.parse(dor_var);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            Toast.makeText(this, "Please select date!", Toast.LENGTH_SHORT).show();
        }
        if(!dateObject.after(new Date()))
            Toast.makeText(this, "Please select future date", Toast.LENGTH_SHORT).show();
        else if(from.length() > 0 && to.length() > 0){
            ArrayList<Trip> trips = dbManager.fetchSearchTrip(from.getText().toString(), to.getText().toString(), dateObject, Integer.parseInt(noSeats.getText().toString()));
            /*trips.add(new Trip(-1, "bucuresti", "craiova", "metrou politehnica", new Date(), 50, user, 4));
            trips.add(new Trip(-1, "constanta", "bucuresti", "gara", new Date(), 40, user, 3));
            trips.add(new Trip(-1, "ploiesti", "brasov", "carrefour", new Date(), 30, user, 2));
            trips.add(new Trip(-1, "timisoara", "arad", "sagului", new Date(), 20, user, 1));
            trips.add(new Trip(-1, "arad", "satu mare", "benzinaria mol iuliu maniu", new Date(), 25, user, 4));
            trips.add(new Trip(-1, "brasov", "bucuresti", "coresi", new Date(), 35, user, 2));
            trips.add(new Trip(-1, "bucuresti", "mangalia", "metrou 1 decembrie", new Date(), 50, user, 3));*/
            Intent resultsIntent = new Intent(SearchActivity.this, TripsActivity.class);
            resultsIntent.putExtra("trips", trips);
            resultsIntent.putExtra("title", from.getText() + " >> " + to.getText());
            SearchActivity.this.startActivity(resultsIntent);
        }
        else
            Toast.makeText(this, "Complete all fields!", Toast.LENGTH_SHORT).show();
    }
}

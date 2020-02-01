package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

public class MainActivity extends AppCompatActivity {
    public static User user;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (User) getIntent().getSerializableExtra("user");
        TextView tv = findViewById(R.id.tvHello);
        tv.setText("Hello " + user.getName() + "!");
        dbManager = new DBManager(this);
        dbManager.open();
    }

    public void createRide(View view){
        Intent createRideIntent = new Intent(MainActivity.this, CreateRideActivity.class);
        createRideIntent.putExtra("user", user);
        MainActivity.this.startActivity(createRideIntent);
    }

    public void search(View view){
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        searchIntent.putExtra("user", user);
        MainActivity.this.startActivity(searchIntent);
    }

    public void trips(View view){
        user.setTrips(dbManager.fetchTripsOfUserID(user.getId()));
        Intent tripsIntent = new Intent(MainActivity.this, TripsActivity.class);
        tripsIntent.putExtra("trips", user.getTrips());
        tripsIntent.putExtra("title", getApplicationContext().getResources().getString(R.string.trips));
        MainActivity.this.startActivity(tripsIntent);
    }

    public void profile(View view){
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        profileIntent.putExtra("user", user);
        MainActivity.this.startActivity(profileIntent);
    }
}

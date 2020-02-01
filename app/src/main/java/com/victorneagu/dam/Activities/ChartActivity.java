package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.Trip;
import com.victorneagu.dam.R;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private ArrayList<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trips = (ArrayList<Trip>) getIntent().getSerializableExtra("trips");
        BarChart chart = findViewById(R.id.barChart);
        ArrayList entries = new ArrayList();
        ArrayList labels = new ArrayList();
        for(int i = 0; i < trips.size(); i++){
            int value = trips.get(i).getPrice() * trips.get(i).getNoAvailableSeats();
            entries.add(new BarEntry(value, i));
            labels.add(value + "");
        }
        BarDataSet bardataset = new BarDataSet(entries, "Maximum total payment (RON) / trip");
        chart.animateY(1500);
        BarData data = new BarData(labels, bardataset);
        bardataset.setColors(ColorTemplate.MATERIAL_COLORS);
        chart.setData(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

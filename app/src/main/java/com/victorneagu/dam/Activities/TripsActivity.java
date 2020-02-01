package com.victorneagu.dam.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.victorneagu.dam.Adapters.TripsAdapter;
import com.victorneagu.dam.Classes.DBManager;
import com.victorneagu.dam.Classes.Trip;
import com.victorneagu.dam.Classes.User;
import com.victorneagu.dam.R;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TripsActivity extends AppCompatActivity {
    private ArrayList<Trip> trips;
    private TripsAdapter adapter;
    private Trip trip;
    private ListView list;
    private String title;
    private boolean myTrips;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbManager = new DBManager(this);
        dbManager.open();
        title = getIntent().getStringExtra("title");
        setTitle(title);
        trips = (ArrayList<Trip>) getIntent().getSerializableExtra("trips");
        myTrips = title.equals(getApplicationContext().getResources().getString(R.string.trips));
        list = findViewById(R.id.lvTrips);
        adapter = new TripsAdapter(this, R.layout.row_item, trips);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(final AdapterView<?>adapter, View v, final int position, long id){
                trip = (Trip) adapter.getItemAtPosition(position);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setMessage(trip.getFrom() + " >> " + trip.getTo());
                builder1.setCancelable(true);
                if(myTrips){
                    builder1.setTitle("Delete trip");
                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            trips.remove(position);
                            dbManager.deleteTrip(MainActivity.user.deleteTrip(position).getId());
                            ((TripsAdapter)list.getAdapter()).notifyDataSetChanged();
                            Toast.makeText(TripsActivity.this, "Trip deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    builder1.setTitle("Join trip");
                    builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(trip.getStillAvailableSeats() > 0){
                                trip.addPassengerId(MainActivity.user.getId());
                                dbManager.updateTripPassengers(trip);
                                ((TripsAdapter)list.getAdapter()).notifyDataSetChanged();
                                Toast.makeText(TripsActivity.this, "Joined trip!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(TripsActivity.this, "Trip is already full!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert1 = builder1.create();
                alert1.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(myTrips){
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myTrips){
            switch (item.getItemId()){
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.btnPdf:
                    createPdf();
                    break;
                case R.id.btnChart:
                    Intent chartIntent = new Intent(this, ChartActivity.class);
                    chartIntent.putExtra("trips", trips);
                    startActivity(chartIntent);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void createPdf(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(TripsActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        else if(trips.isEmpty()){
            Toast.makeText(this, "No trips to generate report from!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String directory_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/trips.pdf";
            try{
                PdfWriter pdfWriter = new PdfWriter(directory_path);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument);

                for(int i = 0; i < trips.size(); i++){
                    Table table = new Table(2);
                    table.addCell("Trip #" + trips.get(i).getId());
                    table.addCell("");
                    table.addCell("From");
                    table.addCell(trips.get(i).getFrom() + " (" + trips.get(i).getPickupPoint() + ")");
                    table.addCell("To");
                    table.addCell(trips.get(i).getTo());
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    table.addCell("Date");
                    table.addCell(df.format(trips.get(i).getDate()));
                    table.addCell("Price");
                    table.addCell(trips.get(i).getPrice() + " RON");
                    table.addCell("Driver");
                    User driver = trips.get(i).getDriver();
                    table.addCell(driver.getName() + ", " + driver.getGender() + ", " + driver.getAge() + " Y.O.");
                    table.addCell("Initial available seats");
                    table.addCell(trips.get(i).getNoAvailableSeats() + "");
                    table.addCell("Passengers");
                    ArrayList<Integer> passengers = trips.get(i).getPassengersIDs();
                    if(passengers.size() > 0){
                        table.addCell("");
                        for(int j = 0; j < passengers.size(); i++){
                            User passenger = dbManager.fetchUser(passengers.get(i), false);
                            table.addCell(passenger.getName());
                            table.addCell(passenger.getGender() + ", " + passenger.getAge() + " Y.O.");
                        }
                    }
                    else
                        table.addCell("No passengers joined this trip");
                    document.add(table);
                    document.add(new Paragraph("\n\n"));
                }
                document.close();
                Toast.makeText(this, "File saved successfully in Downloads folder!", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException fnfe) {
                fnfe.printStackTrace();
                Toast.makeText(this, "Error! File not found Exception occurred!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error! Exception occurred!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
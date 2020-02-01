package com.victorneagu.dam.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private String[] userColumns = new String[] { DatabaseHelper._UID, DatabaseHelper.NAME, DatabaseHelper.EMAIL,
            DatabaseHelper.PASSWORD, DatabaseHelper.GENDER, DatabaseHelper.AGE, DatabaseHelper.PHONE };
    private String[] tripColumns = new String[] { DatabaseHelper._TID, DatabaseHelper.FROM, DatabaseHelper.TO, DatabaseHelper.PICKUP,
            DatabaseHelper.DATE, DatabaseHelper.PRICE, DatabaseHelper.DRIVER, DatabaseHelper.AVAILABLE_SEATS, DatabaseHelper.PASSENGERS };

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insertUser(User user) {
        ContentValues contentValue = new ContentValues();
        //contentValue.put(DatabaseHelper._UID, user.getId());
        contentValue.put(DatabaseHelper.NAME, user.getName());
        contentValue.put(DatabaseHelper.EMAIL, user.getEmail());
        contentValue.put(DatabaseHelper.PASSWORD, user.getPassword());
        contentValue.put(DatabaseHelper.GENDER, user.getGender() + "");
        contentValue.put(DatabaseHelper.AGE, user.getAge());
        contentValue.put(DatabaseHelper.PHONE, user.getPhone());
        database.insert(DatabaseHelper.TABLE_NAME_USERS, null, contentValue);
    }

    public void insertTrip(Trip trip) {
        ContentValues contentValue = new ContentValues();
        //contentValue.put(DatabaseHelper._TID, trip.getId());
        contentValue.put(DatabaseHelper.FROM, trip.getFrom());
        contentValue.put(DatabaseHelper.TO, trip.getTo());
        contentValue.put(DatabaseHelper.PICKUP, trip.getPickupPoint());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        contentValue.put(DatabaseHelper.DATE, df.format(trip.getDate()));
        contentValue.put(DatabaseHelper.PRICE, trip.getPrice());
        contentValue.put(DatabaseHelper.DRIVER, trip.getDriver().getId());
        contentValue.put(DatabaseHelper.AVAILABLE_SEATS, trip.getNoAvailableSeats());
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < trip.getPassengersIDs().size(); i++) {
            str.append(trip.getPassengersIDs().get(i) + ",");
        }
        String passengers = str.toString();
        contentValue.put(DatabaseHelper.PASSENGERS, passengers);
        database.insert(DatabaseHelper.TABLE_NAME_TRIPS, null, contentValue);
    }

    public int getUserCount(){
        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_USERS;
        Cursor cursor = database.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public User fetchUser(int _id, boolean trips) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_USERS, userColumns, DatabaseHelper._UID + " = ? ", new String[] {_id + ""},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return populateUser(cursor, trips);
        }
        return null;
    }

    public User loginUser(String email, String password){
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_USERS, userColumns, DatabaseHelper.EMAIL + " = ? AND " + DatabaseHelper.PASSWORD
                + " = ? ", new String[] {email, password}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return populateUser(cursor, true);
        }
        return null;
    }

    public String forgotPasswordUser(String email){
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_USERS, userColumns, DatabaseHelper.EMAIL + " = ? ",
                new String[] {email}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            User user = populateUser(cursor, false);
            if(user != null)
                return user.getPassword();
            else
                return "User doesn't exist!";
        }
        return null;
    }

    public Trip fetchTrip(int _id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_TRIPS, tripColumns, DatabaseHelper._TID + " = ? ", new String[] {_id + ""},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return populateTrip(cursor);
        }
        return null;
    }

    public ArrayList<Trip> fetchSearchTrip(String from, String to, Date date, int noSeats) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_TRIPS, tripColumns, DatabaseHelper.FROM + " = ? AND " + DatabaseHelper.TO +
                " = ? AND DATE(" + DatabaseHelper.DATE + ") = DATE(?) AND " + DatabaseHelper.AVAILABLE_SEATS + " >= ? ",
                new String[] {from, to, df.format(date), noSeats + ""}, null, null, null);
        ArrayList<Trip> trips = new ArrayList<Trip>();
        if (cursor != null) {
            cursor.moveToFirst();
        }
        else return null;
        while(!cursor.isAfterLast()) {
            trips.add(populateTrip(cursor));
            cursor.moveToNext();
        }
        return trips;
    }

    public ArrayList<Trip> fetchTripsOfUserID(int userId) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_TRIPS, tripColumns, DatabaseHelper.DRIVER + " = ? ", new String[] {userId + ""},
                null, null, "DATETIME(" + DatabaseHelper.DATE + ") DESC");
        ArrayList<Trip> trips = new ArrayList<Trip>();
        if (cursor != null) {
            cursor.moveToFirst();
        }
        else return null;
        while(!cursor.isAfterLast()) {
            trips.add(populateTrip(cursor));
            cursor.moveToNext();
        }
        return trips;
    }

    public int updateUser(User user) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, user.getName());
        contentValue.put(DatabaseHelper.EMAIL, user.getEmail());
        contentValue.put(DatabaseHelper.PASSWORD, user.getPassword());
        contentValue.put(DatabaseHelper.GENDER, user.getGender() + "");
        contentValue.put(DatabaseHelper.AGE, user.getAge());
        contentValue.put(DatabaseHelper.PHONE, user.getPhone());
        int i = database.update(DatabaseHelper.TABLE_NAME_USERS, contentValue, DatabaseHelper._UID + " = " + user.getId(), null);
        return i;
    }

    public int updateTrip(Trip trip) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.FROM, trip.getFrom());
        contentValue.put(DatabaseHelper.TO, trip.getTo());
        contentValue.put(DatabaseHelper.PICKUP, trip.getPickupPoint());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        contentValue.put(DatabaseHelper.DATE, df.format(trip.getDate()));
        contentValue.put(DatabaseHelper.PRICE, trip.getPrice());
        contentValue.put(DatabaseHelper.DRIVER, trip.getDriver().getId());
        contentValue.put(DatabaseHelper.AVAILABLE_SEATS, trip.getNoAvailableSeats());
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < trip.getPassengersIDs().size(); i++) {
            str.append(trip.getPassengersIDs().get(i) + ",");
        }
        String passengers = str.toString();
        contentValue.put(DatabaseHelper.PASSENGERS, passengers);
        int i = database.update(DatabaseHelper.TABLE_NAME_TRIPS, contentValue, DatabaseHelper._TID + " = " + trip.getId(), null);
        return i;
    }

    public int updateTripPassengers(Trip trip) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.AVAILABLE_SEATS, trip.getStillAvailableSeats());
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < trip.getPassengersIDs().size(); i++) {
            str.append(trip.getPassengersIDs().get(i) + ",");
        }
        String passengers = str.toString();
        contentValue.put(DatabaseHelper.PASSENGERS, passengers);
        int i = database.update(DatabaseHelper.TABLE_NAME_TRIPS, contentValue, DatabaseHelper._TID + " = " + trip.getId(), null);
        return i;
    }

    public void deleteUser(int _id) {
        database.delete(DatabaseHelper.TABLE_NAME_USERS, DatabaseHelper._UID + "=" + _id, null);
    }

    public void deleteTrip(int _id) {
        database.delete(DatabaseHelper.TABLE_NAME_TRIPS, DatabaseHelper._TID + "=" + _id, null);
    }

    private User populateUser(Cursor cursor, boolean trips){
        try
        {
            int idIndex = cursor.getColumnIndexOrThrow(DatabaseHelper._UID);
            int nameIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.NAME);
            int emailIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.EMAIL);
            int passwordIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.PASSWORD);
            int genderIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.GENDER);
            int ageIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.AGE);
            int phoneIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.PHONE);
            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String email = cursor.getString(emailIndex);
            String password = cursor.getString(passwordIndex);
            char gender = cursor.getString(genderIndex).charAt(0);
            int age = cursor.getInt(ageIndex);
            int phone = cursor.getInt(phoneIndex);
            User user = new User(id, name, email, password, gender, age, phone);
            if(trips){
                user.setTrips(fetchTripsOfUserID(id));
            }
            return user;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private Trip populateTrip(Cursor cursor){
        try
        {
            int idIndex = cursor.getColumnIndexOrThrow(DatabaseHelper._TID);
            int fromIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.FROM);
            int toIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.TO);
            int pickupIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.PICKUP);
            int dateIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.DATE);
            int priceIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.PRICE);
            int driverIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.DRIVER);
            int seatsIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.AVAILABLE_SEATS);
            int passengersIndex = cursor.getColumnIndexOrThrow(DatabaseHelper.PASSENGERS);
            int id = cursor.getInt(idIndex);
            String from = cursor.getString(fromIndex);
            String to = cursor.getString(toIndex);
            String pickup = cursor.getString(pickupIndex);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = df.parse(cursor.getString(dateIndex));
            int price = cursor.getInt(priceIndex);
            int driverID = cursor.getInt(driverIndex);
            int seats = cursor.getInt(seatsIndex);
            Trip trip = new Trip(id, from , to, pickup, date, price, fetchUser(driverID, false), seats);
            String passengersString = cursor.getString(passengersIndex);
            if(passengersString.length() > 0){
                String[] passengerListStr = passengersString.split(",");
                Integer[] passengerListInt = new Integer[passengerListStr.length];
                for (int i = 0; i < passengerListStr.length; i++) {
                    passengerListInt[i] = Integer.parseInt(passengerListStr[i]);
                }
                ArrayList<Integer> passengers = new ArrayList<Integer>(Arrays.asList(passengerListInt));
                trip.setPassengers(passengers);
            }
            return trip;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
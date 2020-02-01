package com.victorneagu.dam.Classes;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private char gender;
    private int age;
    private int phone;
    private ArrayList<Trip> trips;

    public User() {
    }

    public User(int id, String name, String email, String password, char gender, int age, int phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.trips = new ArrayList<Trip>();
    }

    public void joinTrip(Trip trip){
        trips.add(trip);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }

    public void addTrip(Trip trip){
        this.trips.add(trip);
    }

    public Trip deleteTrip(int position) {
        return this.trips.remove(position);
    }
}

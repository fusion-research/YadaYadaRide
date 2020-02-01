package com.victorneagu.dam.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Trip implements Serializable {
    private int id;
    private String from;
    private String to;
    private String pickupPoint;
    private Date date;
    private int price;
    private User driver;
    private int noAvailableSeats;
    private ArrayList<Integer> passengersIDs;

    public Trip() {
    }

    public Trip(int id, String from, String to, String pickupPoint, Date date, int price, User driver, int seats) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.pickupPoint = pickupPoint;
        this.date = date;
        this.price = price;
        this.driver = driver;
        this.noAvailableSeats = seats;
        this.passengersIDs = new ArrayList<Integer>();
    }

    public boolean addPassengerId(int id){
        if(this.passengersIDs.size() < this.noAvailableSeats){
            this.passengersIDs.add(id);
            return true;
        }
        else return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public int getNoAvailableSeats() {
        return noAvailableSeats;
    }

    public void setNoAvailableSeats(int noAvailableSeats) {
        this.noAvailableSeats = noAvailableSeats;
    }

    public ArrayList<Integer> getPassengersIDs() {
        return passengersIDs;
    }

    public void setPassengers(ArrayList<Integer> passengersIDs) {
        this.passengersIDs = passengersIDs;
    }

    public int getStillAvailableSeats(){
        return this.noAvailableSeats - this.passengersIDs.size();
    }

    public void addPassenger(int pid){
        this.passengersIDs.add(pid);
    }
}

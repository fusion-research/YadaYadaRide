package com.victorneagu.dam.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Table Names
    public static final String TABLE_NAME_USERS = "USERS";
    public static final String TABLE_NAME_TRIPS = "TRIPS";

    // Users Table columns
    public static final String _UID = "_id";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String GENDER = "gender";
    public static final String AGE = "age";
    public static final String PHONE = "phone";

    // Trips Table columns
    public static final String _TID = "_id";
    public static final String FROM = "from_here";
    public static final String TO = "to_there";
    public static final String PICKUP = "pickup";
    public static final String DATE = "date";
    public static final String PRICE = "price";
    public static final String DRIVER = "driver";
    public static final String AVAILABLE_SEATS = "available_seats";
    public static final String PASSENGERS = "passengers";

    // Database Information
    static final String DB_NAME = "YADAYADA_RIDE.DB";

    // database version
    static final int DB_VERSION = 2;

    // Creating users table query
    private static final String CREATE_TABLE_USERS = "create table " + TABLE_NAME_USERS + "(" + _UID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EMAIL + " TEXT NOT NULL, "
            + PASSWORD + " TEXT NOT NULL, " + GENDER + " VARCHAR(1) NOT NULL, " + AGE + " INTEGER NOT NULL, "
            + PHONE + " INTEGER NOT NULL);";

    // Creating trips table query
    private static final String CREATE_TABLE_TRIPS = "create table " + TABLE_NAME_TRIPS + "(" + _TID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FROM + " TEXT NOT NULL, " + TO + " TEXT NOT NULL, "
            + PICKUP + " TEXT NOT NULL, " + DATE + " DATE NOT NULL, " + PRICE + " INTEGER NOT NULL, "
            + DRIVER + " INTEGER NOT NULL, " + AVAILABLE_SEATS + " INTEGER NOT NULL, " + PASSENGERS +
            " TEXT, FOREIGN KEY(" + DRIVER + ") REFERENCES " + TABLE_NAME_USERS + "(" + _UID + "));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_TRIPS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRIPS);
        onCreate(db);
    }
}
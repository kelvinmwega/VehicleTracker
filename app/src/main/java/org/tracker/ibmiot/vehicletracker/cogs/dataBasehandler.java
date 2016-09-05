package org.tracker.ibmiot.vehicletracker.cogs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.tracker.ibmiot.vehicletracker.pojos.vehicleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 7/22/2016.
 */
public class dataBasehandler extends SQLiteOpenHelper {

    private static final int DB_Version = 1;
    private static final String DB_Name = "ibmIotDB";
    private static final String gpsTable = "gpsTable";
    private static final String usersTable = "usersTable";
    private static final String id = "ID";
    private static final String deviceName = "DEVICENAME";
    private static final String latt = "LATITUDE";
    private static final String lont = "LONGITUDE";
    private static final String speed = "SPEED";
    private static final String dateTime = "DATETIME";

    public dataBasehandler(Context context){
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createVehicleTable = "CREATE TABLE IF NOT EXISTS " + gpsTable + " ("+ id +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+deviceName+" VARCHAR(100), "+latt+" VARCHAR(20), "+lont+" VARCHAR(20), "+speed+" VARCHAR, "+dateTime+" DATETIME)";
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + usersTable + " ("+ id +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+deviceName+" VARCHAR(100), "+latt+" VARCHAR(20), "+lont+" VARCHAR(20), "+speed+" VARCHAR, "+dateTime+" DATETIME)";
        db.execSQL(createVehicleTable);
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + gpsTable);
        db.execSQL("DROP TABLE IF EXISTS " + usersTable);
        onCreate(db);
    }

    public void createUser(vehicleData vehicleData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(deviceName, vehicleData.getDeviceName());
        values.put(latt, vehicleData.getLatt());
        values.put(lont, vehicleData.getLont());
        values.put(speed, vehicleData.getSpeed());
        values.put(dateTime, vehicleData.getDateTime());
        System.out.println(values);
        db.insert(usersTable, null, values);
        db.close();
    }

    public List<vehicleData> getUsers(){
        List<vehicleData> usersList = new ArrayList<vehicleData>();
        String selectQuery = "SELECT * FROM " + usersTable;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                vehicleData vehicleData = new vehicleData();
                vehicleData.setID(Integer.parseInt(cursor.getString(0)));
                vehicleData.setDeviceName(cursor.getString(1));
                vehicleData.setLatt(cursor.getString(2));
                vehicleData.setLont(cursor.getString(3));
                vehicleData.setSpeed(cursor.getString(4));
                vehicleData.setDateTime(cursor.getString(5));
                usersList.add(vehicleData);
            }
            while (cursor.moveToNext());
        }

        return usersList;

    }

    public void addVehicleData(vehicleData vehicleData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(deviceName, vehicleData.getDeviceName());
        values.put(latt, vehicleData.getLatt());
        values.put(lont, vehicleData.getLont());
        values.put(speed, vehicleData.getSpeed());
        values.put(dateTime, vehicleData.getDateTime());
        System.out.println(values);
        db.insert(gpsTable, null, values);
        db.close();
    }

    public vehicleData getVehicleData(String deviceNames){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(usersTable, new String[] {
                id, deviceName, latt, lont, speed, dateTime }, deviceName + "=?",
                new String[] {String.valueOf(deviceNames)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        vehicleData vehicleData = new vehicleData(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return vehicleData;
    }

    public List<vehicleData> getAllVehicles(){
        List<vehicleData> vehicleDataList = new ArrayList<vehicleData>();
        String selectQuery = "SELECT * FROM " + gpsTable;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                vehicleData vehicleData = new vehicleData();
                vehicleData.setID(Integer.parseInt(cursor.getString(0)));
                vehicleData.setDeviceName(cursor.getString(1));
                vehicleData.setLatt(cursor.getString(2));
                vehicleData.setLont(cursor.getString(3));
                vehicleData.setSpeed(cursor.getString(4));
                vehicleData.setDateTime(cursor.getString(5));
                vehicleDataList.add(vehicleData);
            }
            while (cursor.moveToNext());
        }
        return vehicleDataList;
    }

    public String updateVehicle(vehicleData vehicleData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(deviceName, vehicleData.getDeviceName());
        values.put(latt, vehicleData.getLatt());
        values.put(lont, vehicleData.getLont());
        values.put(speed, vehicleData.getSpeed());
        values.put(dateTime, vehicleData.getDateTime());

        return String.valueOf(db.update(usersTable, values, deviceName + "=?",
                new String[] {String.valueOf(vehicleData.getDeviceName())}));
    }

}

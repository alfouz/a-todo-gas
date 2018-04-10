package com.atodogas.brainycar.OBD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class OBDDatabase extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS OBDTable (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "speed INTEGER," +
                    "rpm INTEGER," +
                    "fuelLevel REAL," +
                    "fuelRate REAL," +
                    "battery REAL," +
                    "throttlePos REAL," +
                    "ambientTemp REAL," +
                    "engineTemp REAL," +
                    "airIntakeTemp REAL," +
                    "massAirFlow REAL," +
                    "intakeManifoldPresure INT" +
                    ")";


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OBD.db";

    public OBDDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertRow(OBDDTO dto){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put("speed", dto.speed);
        values.put("rpm", dto.rpm);
        values.put("fuelLevel", dto.fuelTankLevel);
        values.put("fuelRate", dto.fuelRate);
        values.put("battery", dto.airIntakeTemp);
        values.put("throttlePos", dto.throttlePos);
        values.put("ambientTemp", dto.ambientTemp);
        values.put("engineTemp", dto.engineCoolantTemp);
        values.put("airIntakeTemp", dto.airIntakeTemp);
        values.put("massAirFlow", dto.massAirFlow);
        values.put("intakeManifoldPresure", dto.intakeManifoldPresure);

        long id = db.insert("OBDTable", null, values);
    }

    public ArrayList<OBDDTO> getAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("OBDTable", null, null, null, null, null, null);

        ArrayList<OBDDTO> data = new ArrayList<>();
        while(cursor.moveToNext()){
            OBDDTO obddata = new OBDDTO();

            obddata.speed = cursor.getInt(cursor.getColumnIndex("speed"));
            obddata.rpm = cursor.getInt(cursor.getColumnIndex("rpm"));
            obddata.fuelTankLevel = cursor.getFloat(cursor.getColumnIndex("fuelLevel"));
            obddata.fuelRate = cursor.getFloat(cursor.getColumnIndex("fuelRate"));
            obddata.moduleVoltage = cursor.getFloat(cursor.getColumnIndex("battery"));
            obddata.throttlePos = cursor.getFloat(cursor.getColumnIndex("throttlePos"));
            obddata.ambientTemp = cursor.getFloat(cursor.getColumnIndex("ambientTemp"));
            obddata.engineCoolantTemp = cursor.getFloat(cursor.getColumnIndex("engineTemp"));
            obddata.airIntakeTemp = cursor.getFloat(cursor.getColumnIndex("airIntakeTemp"));
            obddata.massAirFlow = cursor.getFloat(cursor.getColumnIndex("massAirFlow"));
            obddata.intakeManifoldPresure = cursor.getInt(cursor.getColumnIndex("intakeManifoldPresure"));

            data.add(obddata);
        }

        db.close();

        return data;
    }
}

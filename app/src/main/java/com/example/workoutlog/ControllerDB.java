package com.example.workoutlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;

public class ControllerDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "weightdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "weights";
    private static final String ID = "id";
    private static final String WEIGHT = "weight";
    private static final String DATA = "data";
    private static final String TIME = "time";
    private static final String TABLE_NAME2 = "dane";
    private static final String HEIGHT = "height";
    private static final String TWEIGHT = "tweight";
    private static final String TDATE = "tdate";
    private static final String BDATE = "bdate";
    private static final String BWEIGHT = "bweight";
    LocalDate ldt;

    public ControllerDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);


    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("test onCreate: ","test");
        createWeightTable(sqLiteDatabase);

    }

    public void createWeightTable(SQLiteDatabase sqLiteDatabase){
        Log.d("test onCreate: ","test 2");

            String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + WEIGHT + " DOUBLE(3,1),"
                    + DATA + " TEXT,"
                    + TIME + " TEXT)";

            sqLiteDatabase.execSQL(query);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createDataTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HEIGHT + " TEXT,"
                + TWEIGHT + " TEXT,"
                + TDATE + " TEXT,"
                + BWEIGHT + " TEXT,"
                + BDATE + " TEXT)";

        sqLiteDatabase.execSQL(query);


        ContentValues values = new ContentValues();

        values.put(HEIGHT, "150");

        values.put(TWEIGHT, "75.0");
        ldt = java.time.LocalDate.now();
        values.put(TDATE, String.valueOf(ldt));

        values.put(BWEIGHT, "90.0");

        values.put(BDATE, String.valueOf(ldt));
        sqLiteDatabase.insert(TABLE_NAME2, null, values);


    }



    public void updateDataWzrost(String data){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(HEIGHT, data);

        sqLiteDatabase.update(TABLE_NAME2, contentValues, HEIGHT + " =? ", new String[]{"0"});
    }

    public void updateDataCelu(String data){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TDATE, data);

        sqLiteDatabase.update(TABLE_NAME2, contentValues, TDATE + " =? ", new String[]{"0"});
    }
    public void updateDataWagaDocelowa(String data){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TWEIGHT, data);

        sqLiteDatabase.update(TABLE_NAME2, contentValues, TWEIGHT + " =? ", new String[]{"0"});
    }
    public void updateUserData(String data, String i){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(i, data);
        sqLiteDatabase.update(TABLE_NAME2, contentValues, ID + " =? ", new String[]{"1"});


    }

    public void deleteDataTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME2;
        sqLiteDatabase.execSQL(query);
    }

    public String getData(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor kursor = db.rawQuery("SELECT * FROM " + TABLE_NAME2 , null);
        kursor.move(1);
        return kursor.getString(i);
    }

    public void deleteWeightTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);
    }

    public void addWeight(Double weight, String data, String time) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WEIGHT, weight);
        values.put(DATA, data);
        values.put(TIME, time);

        sqLiteDatabase.insert(TABLE_NAME, null, values);
        //sqLiteDatabase.close();
    }

    public void removeWeight(int id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "DELETE FROM " + TABLE_NAME + " WHERE id= "+id;

        sqLiteDatabase.execSQL(query);
       // sqLiteDatabase.close();
    }

    public ArrayList<Rekord> wczytajWage() {

        Cursor kursor;
        SQLiteDatabase db = this.getReadableDatabase();

        kursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + DATA + " ASC", null);


        ArrayList<Rekord> wagaArrayList = new ArrayList<>();

        if (kursor.moveToFirst()) {
            do {
                wagaArrayList.add(new Rekord(kursor.getInt(0),
                        kursor.getDouble(1),
                        kursor.getString(2),
                        kursor.getString(3)));
            } while (kursor.moveToNext());
        }
        kursor.close();
        return wagaArrayList;
    }
    public ArrayList<Rekord> wczytajOstatniaWage() {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor kursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        ArrayList<Rekord> wagaArrayList = new ArrayList<>();

        if (kursor.moveToLast()) {


                wagaArrayList.add(new Rekord(kursor.getInt(0),
                        kursor.getDouble(1),
                        kursor.getString(2),
                        kursor.getString(3)));


        }
        kursor.close();

        return wagaArrayList;

    }
    public ArrayList<Rekord> getWeightById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor kursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+" WHERE id="+id , null);

        ArrayList<Rekord> wagaArrayList = new ArrayList<>();

        if (kursor.moveToLast()) {


            wagaArrayList.add(new Rekord(kursor.getInt(0),
                    kursor.getDouble(1),
                    kursor.getString(2),
                    kursor.getString(3)));


        }
        kursor.close();

        return wagaArrayList;

    }

    public void updateWeight(Rekord rekord){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, rekord.getId());
        contentValues.put(WEIGHT, rekord.getWaga());
        contentValues.put(DATA, rekord.getData());
        contentValues.put(TIME, rekord.getTime());

        sqLiteDatabase.update(TABLE_NAME, contentValues, ID + " =? ", new String[]{String.valueOf(rekord.getId())});


        //sqLiteDatabase.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Float returnMaxWeight(){
        Cursor kursor;
        SQLiteDatabase db = this.getReadableDatabase();

        kursor = db.rawQuery("SELECT "+ WEIGHT +" FROM " + TABLE_NAME + " ORDER BY " + WEIGHT + " DESC LIMIT 1", null);

        kursor.moveToFirst();
        Float maxWeight = kursor.getFloat(0);
        kursor.close();
        return maxWeight;
    }
    public Float returnMinWeight(){
        Cursor kursor;
        SQLiteDatabase db = this.getReadableDatabase();

        kursor = db.rawQuery("SELECT "+ WEIGHT +" FROM " + TABLE_NAME + " ORDER BY " + WEIGHT + " ASC LIMIT 1", null);

        kursor.moveToFirst();
        Float minWeight = kursor.getFloat(0);
        kursor.close();
        return minWeight;
    }
}

package com.example.embdes.dataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.embdes.constant.AppConstant;
import com.example.embdes.model.es2Model;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }


    public ArrayList<es2Model> getUserDetails() {

        ArrayList<es2Model> cards = new ArrayList<>();
        Cursor cursor = null;

        String condition = "";


        try {
            cursor = db.rawQuery("select * from "+DBHelper.REGISTRATION_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    es2Model card = new es2Model();
                    card.setFirstname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_FIRST_NAME)));
                    card.setLastname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAST_NAME)));
                    card.setEmailId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_REGISTER_EMAIL)));
                    card.setMobileNo(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_REGISTER_MOBILE)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }





    public void deleteRegistrationTable() {
        db.execSQL("delete from " + DBHelper.REGISTRATION_TABLE);
    }
    public void deleteAll() {
        deleteRegistrationTable();
    }



}

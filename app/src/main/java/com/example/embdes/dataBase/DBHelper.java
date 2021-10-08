package com.example.embdes.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.embdes.R;
import com.example.embdes.constant.AppConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.example.embdes.application.e2Application.getAppResources;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "embdes";
    private static final int DATABASE_VERSION = 1;


    public static final String REGISTRATION_TABLE = "registration";


    private Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL("CREATE TABLE " + REGISTRATION_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "firstname TEXT," +
                "lastname TEXT," +
                "address TEXT," +
                "mobile_no TEXT unique," +
                "email_id TEXT unique," +
                "key_value TEXT ," +
                "password TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + REGISTRATION_TABLE);
            onCreate(db);
        }
    }


}

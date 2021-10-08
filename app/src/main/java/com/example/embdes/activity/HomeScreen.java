package com.example.embdes.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.embdes.R;
import com.example.embdes.adapter.HomeScreenAdapter;
import com.example.embdes.dataBase.DBHelper;
import com.example.embdes.dataBase.dbData;
import com.example.embdes.databinding.HomeScreenBinding;
import com.example.embdes.dialog.MyDialog;
import com.example.embdes.model.es2Model;
import com.example.embdes.session.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity implements   MyDialog.myOnClickListener{
    public HomeScreenBinding homeScreenBinding;
    private RecyclerView recyclerView;
    private HomeScreenAdapter homeScreenAdapter;
    private PrefManager prefManager;
    public com.example.embdes.dataBase.dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private Activity context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.home_screen);
        homeScreenBinding.setActivity(this);
        context = this;
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView = homeScreenBinding.userList;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        new fetchUserdetails().execute();
    }



    public class fetchUserdetails extends AsyncTask<Void, Void,
            ArrayList<es2Model>> {
        @Override
        protected ArrayList<es2Model> doInBackground(Void... params) {
            dbData.open();
            ArrayList<es2Model> es2Models = new ArrayList<>();
            es2Models = dbData.getUserDetails();
            Log.d("user_count", String.valueOf(es2Models.size()));
            return es2Models;
        }

        @Override
        protected void onPostExecute(ArrayList<es2Model> es2Models) {
            super.onPostExecute(es2Models);
            recyclerView.setVisibility(View.VISIBLE);
            homeScreenAdapter = new HomeScreenAdapter(HomeScreen.this, es2Models);
            recyclerView.setAdapter(homeScreenAdapter);
        }
    }



    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void logout() {
        closeApplication();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }
}

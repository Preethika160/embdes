package com.example.embdes.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.embdes.R;
import com.example.embdes.constant.AppConstant;
import com.example.embdes.dataBase.DBHelper;
import com.example.embdes.dataBase.dbData;

import com.example.embdes.databinding.LoginScreenBinding;
import com.example.embdes.session.PrefManager;
import com.example.embdes.utils.FontCache;
import com.example.embdes.utils.Utils;

import org.json.JSONObject;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private String randString;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;

    private PrefManager prefManager;
    private int setPType;

    public LoginScreenBinding loginScreenBinding;
    public dbData dbData = new dbData(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.login_screen);

        loginScreenBinding.setActivity(this);
        intializeUI();
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        loginScreenBinding.btnSignin.setOnClickListener(this);

        loginScreenBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginScreenBinding.inputLayoutEmail.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
        loginScreenBinding.inputLayoutPassword.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.REGULAR));
        loginScreenBinding.btnSignin.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
        loginScreenBinding.inputLayoutEmail.setHintTextAppearance(R.style.InActive);
        loginScreenBinding.inputLayoutPassword.setHintTextAppearance(R.style.InActive);

        loginScreenBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });
        loginScreenBinding.password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Avenir-Roman.ttf"));


        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            loginScreenBinding.tvVersion.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        setPType = 1;
        loginScreenBinding.redEye.setOnClickListener(this);
    }

    public void showPassword() {
        if (setPType == 1) {
            setPType = 0;
            loginScreenBinding.password.setTransformationMethod(null);
            if (loginScreenBinding.password.getText().length() > 0) {
                loginScreenBinding.password.setSelection(loginScreenBinding.password.getText().length());
                loginScreenBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24px);
            }
        } else {
            setPType = 1;
            loginScreenBinding.password.setTransformationMethod(new PasswordTransformationMethod());
            if (loginScreenBinding.password.getText().length() > 0) {
                loginScreenBinding.password.setSelection(loginScreenBinding.password.getText().length());
                loginScreenBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_24px);
            }
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = loginScreenBinding.userName.getText().toString().trim();
        prefManager.setUserName(username);
        String password = loginScreenBinding.password.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the Email Id");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    public void checkLoginScreen() {
        final String username = loginScreenBinding.userName.getText().toString().trim();
        final String password = loginScreenBinding.password.getText().toString().trim();
        String decrypted_pass ="";
        if (!validate())
            return;
        else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
            dbData.open();
            String sql = "select * from "+DBHelper.REGISTRATION_TABLE+ " where email_id ='"+username+"'";
            Cursor cursor = db.rawQuery(sql,null);
            Key key = null;
            String password2 = null;
            if(cursor.getCount() > 0){
                if(cursor.moveToFirst()){
                    byte[] encodedKey = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.ENCRYPT_KEY)).getBytes();
                     key = new SecretKeySpec(encodedKey,0,encodedKey.length, "DES");
                    
                    password2 = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PASSWORD));
                }
                try {
                    decrypted_pass= Utils.decrypt(password2,key);
                    Log.d("decrypted_pass",decrypted_pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            String sql1 = "select * from "+DBHelper.REGISTRATION_TABLE+ " where email_id ='"+username+"' and password='"+decrypted_pass+"'";
            Cursor cursor1 = db.rawQuery(sql,null);
            if(cursor1.getCount() > 0){
                if(cursor1.moveToFirst()){
//                    Utils.showAlert(this,cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_FIRST_NAME)));
                    prefManager.setKeyFirstName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_FIRST_NAME)));
                }
                showHomeScreen();
            }
            else {
                Utils.showAlert(this,"Email Id or Password not exits");
            }
        } else {
            Utils.showAlert(this, "Please enter your Email and password!");
        }
    }


    private void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, HomeScreen.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void showRegisterScreen() {
        Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
        intent.putExtra("Home", "Register");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}

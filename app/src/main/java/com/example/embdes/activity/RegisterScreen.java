package com.example.embdes.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;

import com.example.embdes.R;
import com.example.embdes.constant.AppConstant;
import com.example.embdes.dataBase.DBHelper;
import com.example.embdes.dataBase.dbData;
import com.example.embdes.databinding.FragmentSignupBinding;
import com.example.embdes.session.PrefManager;

import com.example.embdes.utils.Utils;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import es.dmoral.toasty.Toasty;


public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {

    private Button btn_register;
    private Handler handler = new Handler();
    private PrefManager prefManager;

    private int setPType;
    public FragmentSignupBinding fragmentSignupBinding;

    private ImageView arrowImage, arrowImageUp;
    private NestedScrollView scrollView;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private Animation animation;
       private dbData dbData = new dbData(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        fragmentSignupBinding = DataBindingUtil.setContentView(this,R.layout.fragment_signup);
        fragmentSignupBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        arrowImage = (ImageView) findViewById(R.id.arrow_image);
        arrowImageUp = (ImageView) findViewById(R.id.arrow_image_up);
        arrowImage.setOnClickListener(this);
        arrowImageUp.setOnClickListener(this);
        fragmentSignupBinding.backImg.setOnClickListener(this);
        fragmentSignupBinding.btnRegister.setOnClickListener(this);
        fragmentSignupBinding.redEye.setOnClickListener(this);

        textFieldValidation();
    }

    public void showPassword() {
        if (setPType == 1) {
            setPType = 0;
            fragmentSignupBinding.passwordTv.setTransformationMethod(null);
            if (fragmentSignupBinding.passwordTv.getText().length() > 0) {
                fragmentSignupBinding.passwordTv.setSelection(fragmentSignupBinding.passwordTv.getText().length());
                fragmentSignupBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_off_24px);
            }
        } else {
            setPType = 1;
            fragmentSignupBinding.passwordTv.setTransformationMethod(new PasswordTransformationMethod());
            if (fragmentSignupBinding.passwordTv.getText().length() > 0) {
                fragmentSignupBinding.passwordTv.setSelection(fragmentSignupBinding.passwordTv.getText().length());
                fragmentSignupBinding.redEye.setBackgroundResource(R.drawable.ic_baseline_visibility_24px);
            }
        }

    }

    public void textFieldValidation() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        handler.postDelayed(runnable, 5000);
        fragmentSignupBinding.verifyPasswordTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!fragmentSignupBinding.passwordTv.getText().toString().equalsIgnoreCase(fragmentSignupBinding.verifyPasswordTv.getText().toString())) {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.red_rectangle_box);
                } else {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.rectangle_box);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!fragmentSignupBinding.passwordTv.getText().toString().equalsIgnoreCase(fragmentSignupBinding.verifyPasswordTv.getText().toString())) {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.red_rectangle_box);
                } else {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.rectangle_box);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!fragmentSignupBinding.passwordTv.getText().toString().equalsIgnoreCase(fragmentSignupBinding.verifyPasswordTv.getText().toString())) {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.red_rectangle_box);
                } else {
                    fragmentSignupBinding.verifyAccountLayout.setBackgroundResource(R.drawable.rectangle_box);
                }
            }
        });


        fragmentSignupBinding.mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isValidMobile(fragmentSignupBinding.mobileNo.getText().toString())) {
                    fragmentSignupBinding.phoneNoLayout.setBackgroundResource(R.drawable.rectangle_box);
                } else {
                    fragmentSignupBinding.phoneNoLayout.setBackgroundResource(R.drawable.red_rectangle_box);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        fragmentSignupBinding.emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Utils.isEmailValid(fragmentSignupBinding.emailId.getText().toString())) {
                    fragmentSignupBinding.emailIdLayout.setBackgroundResource(R.drawable.rectangle_box);
                } else {
                    fragmentSignupBinding.emailIdLayout.setBackgroundResource(R.drawable.red_rectangle_box);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


    }




    //The method for opening the registration page and another processes or checks for registering
    public void validateDetails() {
            if (!fragmentSignupBinding.firstName.getText().toString().isEmpty()) {
                if (!fragmentSignupBinding.lastName.getText().toString().isEmpty()) {
                    if (!fragmentSignupBinding.mobileNo.getText().toString().isEmpty()) {
                        if (Utils.isValidMobile(fragmentSignupBinding.mobileNo.getText().toString())) {
                            if (!fragmentSignupBinding.emailId.getText().toString().isEmpty()) {
                                if (Utils.isEmailValid(fragmentSignupBinding.emailId.getText().toString())) {
                                    if (!fragmentSignupBinding.address.getText().toString().isEmpty()) {
                                        if (!fragmentSignupBinding.passwordTv.getText().toString().isEmpty()) {
                                            if (!fragmentSignupBinding.verifyPasswordTv.getText().toString().isEmpty()) {
                                                if (fragmentSignupBinding.passwordTv.getText().toString().equalsIgnoreCase(fragmentSignupBinding.verifyPasswordTv.getText().toString())) {
                                                    register();
                                                } else {
                                                    Utils.showAlert(RegisterScreen.this, "Confirm Password is not Same !");
                                                }
                                            } else {
                                                Utils.showAlert(this, "Enter Confirm Password!");
                                            }
                                        } else {
                                            Utils.showAlert(this, "Enter Password!");
                                        }
                                    } else {
                                        Utils.showAlert(this, "Enter the Address!");
                                    }
                                } else {
                                    Utils.showAlert(this, "Enter the Valid Email Id!");
                                }
                            } else {
                                Utils.showAlert(this, "Enter the Email Id!");
                            }
                        } else {
                            Utils.showAlert(this, "Enter Valid Mobile No!");
                        }
                    } else {
                        Utils.showAlert(this, "Enter the Mobile No!");
                    }
                }  else{
                    Utils.showAlert(this, "Enter the Last Name!");
                }
            }else{
                Utils.showAlert(this, "Enter the First Name!");
            }
    }

    @Override
    public void onClick(View v) {

    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return '*'; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

     public void register() {
        String password =  fragmentSignupBinding.passwordTv.getText().toString();
        String encrpyted_pass = "";
         Key key = null;
        try {
            key= Utils.generateKey();
            encrpyted_pass = Utils.encrypt(password,key);
            //Log.d("encry",encrpyted_pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbData.open();


        String email_id = fragmentSignupBinding.emailId.getText().toString();
        String mobile_no =  fragmentSignupBinding.mobileNo.getText().toString();

        ContentValues dataSet = new ContentValues();
        dataSet.put(AppConstant.KEY_FIRST_NAME, fragmentSignupBinding.firstName.getText().toString());
        dataSet.put(AppConstant.KEY_LAST_NAME, fragmentSignupBinding.lastName.getText().toString());
        dataSet.put(AppConstant.KEY_REGISTER_MOBILE, mobile_no);
        dataSet.put(AppConstant.KEY_REGISTER_EMAIL,email_id );
        dataSet.put(AppConstant.KEY_REGISTER_ADDRESS, fragmentSignupBinding.address.getText().toString());
        dataSet.put(AppConstant.ENCRYPT_KEY, key.toString());
        dataSet.put(AppConstant.PASSWORD,  encrpyted_pass);

        Cursor cursorEmail = db.rawQuery("select * from "+DBHelper.REGISTRATION_TABLE+" where email_id='"+email_id+"'",null);
        Cursor cursorMobile = db.rawQuery("select * from "+DBHelper.REGISTRATION_TABLE+" where mobile_no='"+mobile_no+"'",null);

        if(cursorEmail.getCount() > 0 ){
            Utils.showAlert(this,"Email Id Already Registered");
        }
        else if(cursorMobile.getCount() > 0 ){
            Utils.showAlert(this,"Mobile Number Already Registered");
        }
        else {
            Long id = db.insert(DBHelper.REGISTRATION_TABLE,null,dataSet);
            if(id > 0) {
                Toasty.success(this,"Registered Successfully", Toast.LENGTH_LONG).show();
                Log.d("registered",dataSet.toString());
                showLoginScreen();
            }
        }


    }


    private void showLoginScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(RegisterScreen.this, LoginScreen.class);

                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, 2000);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
package com.example.embdes.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.embdes.R;

import com.example.embdes.dataBase.dbData;
import com.example.embdes.session.PrefManager;


public class MyDialog {
    public myOnClickListener myListener;
    private com.example.embdes.dataBase.dbData dbData;
    private PrefManager prefManager;

    public MyDialog(Activity context) {
        prefManager         = new PrefManager(context);
        this.myListener = (myOnClickListener) context;
        dbData = new dbData(context);

    }

    // This is my interface //
    public interface myOnClickListener {
        void onButtonClick(AlertDialog alertDialog, String type);

    }

    public void exitDialog(final Activity activity, String message, final String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(false);
        alertDialog.show();

       TextView tv_message = (TextView) dialogView.findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListener.onButtonClick(alertDialog, type);
                if(type.equals("Logout")) {
                   // dbData.open();
                   // dbData.deleteAll();
                    prefManager.clearSession();
                }


            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}

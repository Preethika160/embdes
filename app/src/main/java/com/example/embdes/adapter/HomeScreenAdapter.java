package com.example.embdes.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.embdes.R;
import com.example.embdes.databinding.HomescreenAdapterBinding;
import com.example.embdes.model.es2Model;
import com.example.embdes.session.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private ArrayList<es2Model> userlistValues;
    static JSONObject dataset = new JSONObject();

    private LayoutInflater layoutInflater;

    public HomeScreenAdapter(Activity context, ArrayList<es2Model> userListValues) {

        this.context = context;
        prefManager = new PrefManager(context);

        this.userlistValues = userListValues;
    }

    @Override
    public HomeScreenAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        HomescreenAdapterBinding homescreenAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.homescreen_adapter, viewGroup, false);
        return new HomeScreenAdapter.MyViewHolder(homescreenAdapterBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private HomescreenAdapterBinding homescreenAdapterBinding;

        public MyViewHolder(HomescreenAdapterBinding Binding) {
            super(Binding.getRoot());
            homescreenAdapterBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.homescreenAdapterBinding.name.setText(userlistValues.get(position).getFirstname());
        holder.homescreenAdapterBinding.emailId.setText(userlistValues.get(position).getEmailId());
        holder.homescreenAdapterBinding.mobileNo.setText(userlistValues.get(position).getMobileNo());



    }

    @Override
    public int getItemCount() {
        return userlistValues.size();
    }


}

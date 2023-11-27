package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dashbroad_disAdapter extends RecyclerView.Adapter<Dashbroad_disAdapter.ViewHolder> {

    private Context context;
    private List<Hosts> list;
    private String Surl,Suser,Spass,Sname;
    private List<HashMap<String, String>> infolist;

    public Dashbroad_disAdapter(Context context, List<Hosts> list,String Sname,String Surl,String Suser,String Spass) {
        this.context = context;
        this.list = list;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dash_dis_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hosts host = list.get(position);
        holder.edt_drulename.setText(host.getDrulename());
        holder.edt_hup.setText(host.getHup());
        holder.edt_hdown.setText(host.getHdown());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_drulename, edt_hup , edt_hdown;


        public ViewHolder(View itemView) {
            super(itemView);
            edt_drulename = itemView.findViewById(R.id.tv_drulename);
            edt_hup = itemView.findViewById(R.id.tv_hup);
            edt_hdown = itemView.findViewById(R.id.tv_hdown);
        }
    }

}
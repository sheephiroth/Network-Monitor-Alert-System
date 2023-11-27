package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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


public class DiscoveryAdapter extends RecyclerView.Adapter<DiscoveryAdapter.ViewHolder> {

    private Context context;
    private List<Hosts> list;
    private String Surl,Suser,Spass,Sname;
    private List<HashMap<String, String>> infolist;

    public DiscoveryAdapter(Context context, List<Hosts> list,String Sname,String Surl,String Suser,String Spass) {
        this.context = context;
        this.list = list;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.discovery_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hosts host = list.get(position);
        holder.edt_hostname.setText(host.getHostname());
        holder.edt_ip.setText(host.getIp());
        String dupdown = host.getDupdown();
        holder.edt_updown.setText(dupdown);

        if (dupdown != null) {
            if (dupdown.contains("UP")) {
                holder.edt_updown.setTextColor(Color.parseColor("#3BB143"));
                holder.edt_updown.setTypeface(null, Typeface.BOLD);
            } else if (dupdown.contains("DOWN")) {
                holder.edt_updown.setTextColor(Color.RED);
                holder.edt_updown.setTypeface(null, Typeface.BOLD);
            } else {
                holder.edt_updown.setTextColor(Color.BLACK);
                holder.edt_updown.setTypeface(null, Typeface.BOLD);
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  edt_hostname , edt_ip, edt_updown;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_hostname = itemView.findViewById(R.id.name);
            edt_ip = itemView.findViewById(R.id.ip);
            edt_updown = itemView.findViewById(R.id.updown);
        }
    }

}
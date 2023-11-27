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


public class ServerDiscoveryAdapter extends RecyclerView.Adapter<ServerDiscoveryAdapter.ViewHolder> {

    private Context context;
    private List<Hosts> list;
    private String Surl,Suser,Spass,Sname;
    private List<HashMap<String, String>> infolist;

    public ServerDiscoveryAdapter(Context context, List<Hosts> list,String Sname,String Surl,String Suser,String Spass) {
        this.context = context;
        this.list = list;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.discoveryid_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hosts host = list.get(position);
        holder.edt_druleid.setText(host.getDruleid());
        holder.edt_drulename.setText(host.getDrulename());
        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DiscoveryDetail.class);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                intent.putExtra("Sname", Sname);
                intent.putExtra("druleid", host.getDruleid());
                intent.putExtra("drulename", host.getDrulename());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_druleid, edt_drulename;
        public ImageView morebtn;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_druleid = itemView.findViewById(R.id.druleid);
            edt_drulename = itemView.findViewById(R.id.drulename);
            morebtn = itemView.findViewById(R.id.moreinfobtn);
        }
    }

}
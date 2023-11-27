package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server_problemsAdapter extends RecyclerView.Adapter<Server_problemsAdapter.ViewHolder> {
    private Context context;
    private List<Problems> list;

    public Server_problemsAdapter(Context context, List<Problems> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Server_problemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.server_problem_item, parent, false);
        return new Server_problemsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Server_problemsAdapter.ViewHolder holder, int position) {
        Problems problem = list.get(position);
        holder.edt_hostname.setText(problem.getHostname());
        holder.edt_detail.setText(problem.getDetail());
        holder.edt_severity.setText(problem.getSeverity());
        holder.edt_time.setText(problem.getTime());

        switch (problem.getSeverity().toLowerCase()) {
            case "information":
                holder.edt_severity.setTextColor(Color.BLUE);
                break;
            case "warning":
                holder.edt_severity.setTextColor(Color.rgb(108, 187, 60));
                break;
            case "average":
                holder.edt_severity.setTextColor(Color.rgb(255, 165, 0));
                break;
            case "high":
                holder.edt_severity.setTextColor(Color.RED);
                break;
            case "disaster":
                holder.edt_severity.setTextColor(Color.rgb(128, 0, 0));
                break;
            default:
                holder.edt_severity.setTextColor(Color.BLACK);
        }
        holder.edt_severity.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView  edt_hostname , edt_detail, edt_severity, edt_time;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_hostname = itemView.findViewById(R.id.tv_hostname);
            edt_detail = itemView.findViewById(R.id.tv_detail);
            edt_severity = itemView.findViewById(R.id.tv_severity);
            edt_time = itemView.findViewById(R.id.tv_datetime);
        }
    }

}

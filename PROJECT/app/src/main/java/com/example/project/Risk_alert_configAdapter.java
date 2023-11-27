package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class Risk_alert_configAdapter extends RecyclerView.Adapter<Risk_alert_configAdapter.ViewHoder> {
    private Context context;
    private ArrayList Item, Operator, Value, Severity;
    private DBHelper DB;
    private SQLiteDatabase sqLiteDatabase;

    private String Surl,hostname;



    public Risk_alert_configAdapter(Context context, ArrayList Item, ArrayList Operator, ArrayList Value, ArrayList Severity, String Surl, String hostname) {
        this.context = context;
        this.Item = Item;
        this.Operator = Operator;
        this.Value = Value;
        this.Severity = Severity;
        this.Surl = Surl;
        this.hostname = hostname;

    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ralert_item,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.edt_item.setText(String.valueOf(Item.get(position)));
        holder.edt_oper.setText(String.valueOf(Operator.get(position)));
        holder.edt_value.setText(String.valueOf(Value.get(position)));
        holder.edt_serverity.setText(String.valueOf(Severity.get(position)));
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB = new DBHelper(context);
                sqLiteDatabase = DB.getWritableDatabase();
                int adapterPosition = holder.getAdapterPosition();
                String item = String.valueOf(Item.get(adapterPosition));
                String oper = String.valueOf(Operator.get(adapterPosition));
                String value = String.valueOf(Value.get(adapterPosition));
                String severity = String.valueOf(Severity.get(adapterPosition));

                sqLiteDatabase.delete("Analysis_Alert", "S_url = ? AND Hostname = ? AND Item = ? AND Operator = ? AND Value = ? AND Severity = ?",
                        new String[]{Surl, hostname, item, oper, value, severity});

                String whereClause = "S_url = ? AND Hostname = ? AND Item = ? AND Operator = ? AND Value = ? AND Severity = ?";
                String[] whereArgs = new String[]{Surl,hostname, item, oper, value, severity};

                int deletedRows = sqLiteDatabase.delete("Alert_Config", whereClause, whereArgs);

                if (deletedRows > 0) {
                    Item.remove(adapterPosition);
                    Operator.remove(adapterPosition);
                    Value.remove(adapterPosition);
                    Severity.remove(adapterPosition);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    @Override
    public int getItemCount() {
        return Item.size();
    }
    public class ViewHoder extends RecyclerView.ViewHolder{
        private TextView edt_item, edt_oper, edt_value, edt_serverity, delbtn;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            edt_item=(TextView)itemView.findViewById(R.id.tv_itemname);
            edt_oper=(TextView)itemView.findViewById(R.id.tv_oper);
            edt_value=(TextView)itemView.findViewById(R.id.tv_value);
            edt_serverity=(TextView)itemView.findViewById(R.id.tv_severity);
            delbtn=(TextView)itemView.findViewById(R.id.delbtn);

        }
    }
}




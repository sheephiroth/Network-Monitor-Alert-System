package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.databinding.ActivityHomeBinding;
import com.example.project.databinding.ActivityRiskAlertConfigBinding;
import com.example.project.databinding.ActivityServerDetailBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Risk_alert_config extends AppCompatActivity {
    private ActivityRiskAlertConfigBinding binding;

    DBHelper DB;

    Risk_alert_configAdapter adapter;
    RecyclerView alist;

    ArrayList<String> Item , Operator , Value, Severity ;

    Handler handler = new Handler();
    int delay = 10*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRiskAlertConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String template = intent.getStringExtra("template");
        String ip = intent.getStringExtra("ip");

        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Risk_alert_config.this, Host_info.class);
                intent.putExtra("hostid", hostid);
                intent.putExtra("name",hostname);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                intent.putExtra("template", template);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });


        ImageView add =(ImageView) findViewById(R.id.addalert);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Risk_alert_config.this, Add_RiskAlert.class);
                intent.putExtra("hostid", hostid);
                intent.putExtra("name",hostname);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                intent.putExtra("template", template);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });

        binding.tvHostname.setText(hostname);



        Item = new ArrayList<>();
        Operator = new ArrayList<>();
        Value = new ArrayList<>();
        Severity = new ArrayList<>();
        DB = new DBHelper(this);
        alist = findViewById(R.id.ralertlist);
        adapter = new Risk_alert_configAdapter(this, Item, Operator, Value, Severity, Surl,hostname);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        alist.setLayoutManager(layoutManager);
        alist.setAdapter(adapter);

        displaydata();







    }

    private void displaydata()
    {

        Intent intent = getIntent();
        String Surl = intent.getStringExtra("Surl");
        String hostname = intent.getStringExtra("name");
        Cursor cursor = DB.getAlert_config(Surl,hostname);
        if(cursor.getCount()==0)
        {
            Toast.makeText(Risk_alert_config.this, "No Alert Config Data", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            while(cursor.moveToNext())
            {
                Item.add(cursor.getString(0));
                Operator.add(cursor.getString(1));
                Value.add(cursor.getString(2));
                Severity.add(cursor.getString(3));
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            public void run() {
                Item.clear();
                Operator.clear();
                Value.clear();
                Severity.clear();
                displaydata();
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }




}




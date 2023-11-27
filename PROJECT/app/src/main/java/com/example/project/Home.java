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
import com.example.project.databinding.ActivityServerDetailBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    private ActivityHomeBinding binding;

    RecyclerView slist;
    DBHelper DB;
    ServerAdapter adapter;

    ArrayList<String> Sname, Surl, Suser, Spass;

    Handler handler = new Handler();
    int delay = 15*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        ImageView addserver =(ImageView) findViewById(R.id.addserver);
        addserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Addserver.class);
                startActivity(i);
            }
        });







        Sname = new ArrayList<>();
        Surl = new ArrayList<>();
        Suser = new ArrayList<>();
        Spass = new ArrayList<>();
        DB = new DBHelper(this);
        slist = findViewById(R.id.serverlist);
        adapter = new ServerAdapter(this, Sname, Surl, Suser, Spass);
        slist.setAdapter(adapter);
        slist.setLayoutManager(new LinearLayoutManager(this));
        displaydata();

//        Log.d("5555555555555555555555555", Surl.toString());


        TextView alert =(TextView) findViewById(R.id.alertbtn);
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Alert.class);
                i.putStringArrayListExtra("Sname",Sname);
                i.putStringArrayListExtra("Surl",Surl);
                i.putStringArrayListExtra("Suser",Suser);
                i.putStringArrayListExtra("Spass",Spass);
                startActivity(i);
            }
        });


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


    }

    private void displaydata()
    {
        Cursor cursor = DB.getServer();
        if(cursor.getCount()==0)
        {
            Toast.makeText(Home.this, "No Servers Data", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            while(cursor.moveToNext())
            {
                Sname.add(cursor.getString(0));
                Surl.add(cursor.getString(1));
                Suser.add(cursor.getString(2));
                Spass.add(cursor.getString(3));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sname.clear();
        Surl.clear();
        Suser.clear();
        Spass.clear();
        displaydata();
        adapter.notifyDataSetChanged();

        handler.postDelayed(new Runnable() {
            public void run() {
                Sname.clear();
                Surl.clear();
                Suser.clear();
                Spass.clear();
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




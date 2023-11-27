package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.databinding.ActivityAddserverBinding;
import com.example.project.databinding.ActivityHomeBinding;

public class Addserver extends AppCompatActivity {
    private ActivityAddserverBinding binding;
    SQLiteDatabase sqLiteDatabase;
    EditText Sname, Surl, Suser, Spass;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddserverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DB = new DBHelper(this);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Addserver.this, Home.class);
                startActivity(i);
            }
        });

        Sname = findViewById(R.id.edt_name);
        Surl = findViewById(R.id.edt_url);
        Suser = findViewById(R.id.edt_user);
        Spass = findViewById(R.id.edt_pass);


        TextView save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase=DB.getWritableDatabase();

                ContentValues cv=new ContentValues();
                cv.put("S_name",Sname.getText().toString());
                cv.put("S_url",Surl.getText().toString());
                cv.put("S_user",Suser.getText().toString());
                cv.put("S_pass",Spass.getText().toString());
                Long recid=sqLiteDatabase.insert("Server",null,cv);
                if (recid!=null){
                    Toast.makeText(Addserver.this, "successfully insert", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Addserver.this, "something wrong try again", Toast.LENGTH_SHORT).show();
                }
                Intent i = new Intent(Addserver.this, Home.class);
                startActivity(i);
            }
        });


    }


}
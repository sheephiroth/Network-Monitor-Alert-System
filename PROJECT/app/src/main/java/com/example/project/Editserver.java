package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.databinding.ActivityEditserverBinding;

public class Editserver extends AppCompatActivity {
    private ActivityEditserverBinding binding;
    private EditText edt_sname, edt_surl, edt_suser, edt_spass;
    private Button update1;
    private DBHelper DB;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditserverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DB = new DBHelper(this);
        sqLiteDatabase = DB.getWritableDatabase();


        Intent intent = getIntent();
        String old_S_url = intent.getStringExtra("Surl");
        String name = intent.getStringExtra("Sname");
        String url = old_S_url;
        String user = intent.getStringExtra("Suser");
        String pass = intent.getStringExtra("Spass");


        binding.edtSname.setText(name);
        binding.edtSurl.setText(url);
        binding.edtSuser.setText(user);
        binding.edtSpass.setText(pass);
        Button updateButton = findViewById(R.id.updatebtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String S_name = binding.edtSname.getText().toString();
                String new_S_url = binding.edtSurl.getText().toString();
                String S_user = binding.edtSuser.getText().toString();
                String S_pass = binding.edtSpass.getText().toString();
                DB.updateServer( old_S_url, S_name, new_S_url, S_user, S_pass);
                Toast.makeText(Editserver.this, "Server updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });



        Button cancelButton = findViewById(R.id.cancelbtn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Editserver.this, Home.class);
                startActivity(i);
            }
        });
    }
}

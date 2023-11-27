package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.databinding.ActivityAddRiskAlertBinding;
import com.example.project.databinding.ActivityAddserverBinding;
import com.example.project.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class Add_RiskAlert extends AppCompatActivity {
    private ActivityAddRiskAlertBinding binding;
    SQLiteDatabase sqLiteDatabase;
    Spinner spinner_itemname,spinner_oper,spinner_severity;
    String item,oper,severity;
    DBHelper DB;
    ArrayList<String> ITEM = new ArrayList<>(Arrays.asList(
            "cpu_use_d",
            "c_space_use_d",
            "mem_use_d",
            "cpu_temp_d",
            "gpu_temp_d",
            "trigger_int",
            "failure_chance"
    ));

    ArrayList<String> Operator = new ArrayList<>(Arrays.asList(
            ">", "<", ">=", "<=", "==", "!="
    ));

    ArrayList<String> Severity = new ArrayList<>(Arrays.asList(
            "not classified",
            "information",
            "warning",
            "average",
            "high",
            "disaster"
    ));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRiskAlertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DB = new DBHelper(this);

        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String template = intent.getStringExtra("template");
        String ip = intent.getStringExtra("ip");
        ImageView back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_RiskAlert.this, Risk_alert_config.class);
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


        spinner_itemname = findViewById(R.id.spn_itemname);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_RiskAlert.this, android.R.layout.simple_spinner_item, ITEM);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_itemname.setAdapter(adapter);
        spinner_itemname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                item = ITEM.get(position);
                if (item.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "PLEASE SELECT ITEM", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_oper = findViewById(R.id.spn_oper);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Add_RiskAlert.this, android.R.layout.simple_spinner_item, Operator);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_oper.setAdapter(adapter2);
        spinner_oper.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                oper = Operator.get(position);
                if (oper.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT Operator", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "PLEASE SELECT Operator", Toast.LENGTH_SHORT).show();
            }
        });

        spinner_severity = findViewById(R.id.spn_severity);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(Add_RiskAlert.this, android.R.layout.simple_spinner_item, Severity);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_severity.setAdapter(adapter3);
        spinner_severity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                severity = Severity.get(position);
                if (severity.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "PLEASE SELECT Severity", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Toast.makeText(getApplicationContext(), "PLEASE SELECT Severity", Toast.LENGTH_SHORT).show();
            }
        });








        TextView save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase=DB.getWritableDatabase();


                String valueString = binding.edtValue.getText().toString();
                float valueFloat = Float.parseFloat(valueString);

                ContentValues cv=new ContentValues();
                cv.put("S_url",Surl);
                cv.put("Hostname",hostname);
                cv.put("Item",item);
                cv.put("Operator",oper);
                cv.put("Value",valueFloat);
                cv.put("Severity",severity);
                Long recid=sqLiteDatabase.insert("Alert_Config",null,cv);

                if (recid!=null){
                    Toast.makeText(Add_RiskAlert.this, "successfully insert", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Add_RiskAlert.this, "something wrong try again", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(Add_RiskAlert.this, Risk_alert_config.class);
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


    }


}
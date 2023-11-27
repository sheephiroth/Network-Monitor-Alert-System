package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityHostconfigListBinding;
import com.example.project.databinding.ActivityServerDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hostconfig_list extends AppCompatActivity {
    private ActivityHostconfigListBinding binding;
    private RecyclerView hlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Hosts> hostlist;
    private RecyclerView.Adapter adapter;



    private EditText edt_commu, edt_oid, edt_valtype, edt_newValue ;
    private Button update,cancel;

    Handler handler = new Handler();
    int delay = 15*1000;
    Runnable runnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostconfigListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String template = intent.getStringExtra("template");
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String ip = intent.getStringExtra("ip");




        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Hostconfig_list.this, Host_info.class);
                i.putExtra("hostid", hostid);
                i.putExtra("name",hostname);
                i.putExtra("Sname", Sname);
                i.putExtra("Surl", Surl);
                i.putExtra("Suser", Suser);
                i.putExtra("Spass", Spass);
                i.putExtra("template", template);
                i.putExtra("ip", ip);
                startActivity(i);
            }
        });

        edt_commu = findViewById(R.id.edt_commu);
        edt_oid = findViewById(R.id.edt_oid);
        edt_valtype = findViewById(R.id.edt_valtype);
        edt_newValue = findViewById(R.id.edt_newValue);
        update = findViewById(R.id.updatebtn);
        cancel = findViewById(R.id.cancelbtn);

        if ("Cisco IOS by SNMP".equals(template)) {

            edt_commu.setVisibility(View.VISIBLE);
            edt_oid.setVisibility(View.VISIBLE);
            edt_valtype.setVisibility(View.VISIBLE);
            edt_newValue.setVisibility(View.VISIBLE);
            update.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
        } else {

            edt_commu.setVisibility(View.GONE);
            edt_oid.setVisibility(View.GONE);
            edt_valtype.setVisibility(View.GONE);
            edt_newValue.setVisibility(View.GONE);
            update.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }




        binding.tvHostname.setText(hostname);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String ip = intent.getStringExtra("ip");;
                String Commu = edt_commu.getText().toString();
                String Oid = edt_oid.getText().toString();
                String Valtype = edt_valtype.getText().toString();
                String NewValue = edt_newValue.getText().toString();


                sendUpdateRequest(ip, Commu, Oid, Valtype,NewValue);

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_commu.setText("");
                edt_oid.setText("");
                edt_newValue.setText("");
                edt_valtype.setText("");
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            public void run() {
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }



    private void sendUpdateRequest(String ip, String Commu, String Oid, String Valtype, String NewValue) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.BASE_URL + "snmp.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            String message = res.getString("text");
                            Toast.makeText(Hostconfig_list.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ip", ip);
                params.put("commu", Commu);
                params.put("oid", Oid);
                params.put("valtype", Valtype);
                params.put("newValue", NewValue);
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };

        queue.add(request);
    }


}
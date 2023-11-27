package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityDashbroadBinding;
import com.example.project.databinding.ActivityHostInfoBinding;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashbroad extends AppCompatActivity {
    private ActivityDashbroadBinding binding;

    private RecyclerView hlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Hosts> hostlist;
    private RecyclerView.Adapter adapter;


    Handler handler = new Handler();
    int delay = 30*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashbroadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");



        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashbroad.this, Home.class);
                startActivity(intent);
            }
        });

        ImageView next =(ImageView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashbroad.this, Server_detail.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });

        binding.edtName.setText(Sname);


        getAva();
        getPro();

        hlist = findViewById(R.id.dislist);

        hostlist = new ArrayList<>();
        adapter = new Dashbroad_disAdapter(getApplicationContext(),hostlist,Sname,Surl,Suser,Spass);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(hlist.getContext(), linearLayoutManager.getOrientation());

        hlist.setHasFixedSize(true);
        hlist.setLayoutManager(linearLayoutManager);
        hlist.addItemDecoration(dividerItemDecoration);
        hlist.setAdapter(adapter);
        getDis();

    }

    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
                handler.postDelayed(this, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }


    public void getAva(){
        String url = Constants.BASE_URL + "getava.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String edt_a1 = res.getString("ava");
                    String edt_a2 = res.getString("unava");
                    String edt_a3 = res.getString("unknown");
                    String edt_a4 = res.getString("total");
                    binding.tvA1.setText(edt_a1);
                    binding.tvA2.setText(edt_a2);
                    binding.tvA3.setText(edt_a3);
                    binding.tvA4.setText(edt_a4);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };
        queue.add(request);
    }

    public void getPro(){
        String url = Constants.BASE_URL + "getpro.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String edt_p1 = res.getString("notclass");
                    String edt_p2 = res.getString("info");
                    String edt_p3 = res.getString("warn");
                    String edt_p4 = res.getString("aver");
                    String edt_p5 = res.getString("high");
                    String edt_p6 = res.getString("disaster");
                    binding.tvP1.setText(edt_p6);
                    binding.tvP2.setText(edt_p5);
                    binding.tvP3.setText(edt_p4);
                    binding.tvP4.setText(edt_p3);
                    binding.tvP5.setText(edt_p2);
                    binding.tvP6.setText(edt_p1);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };
        queue.add(request);
    }


    private void getDis() {
        String url = Constants.BASE_URL + "getdis.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        Hosts host = new Hosts();
                        host.setDrulename(jsonObject.getString("drulename"));
                        host.setHup(jsonObject.getString("hup"));
                        host.setHdown(jsonObject.getString("hdown"));
                        hostlist.add(host);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e) {
                    Log.d("TagERRAPI", response);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
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
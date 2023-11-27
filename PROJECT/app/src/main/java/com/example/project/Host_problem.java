package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityHostInfoBinding;
import com.example.project.databinding.ActivityHostProblemBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host_problem extends AppCompatActivity {
    private ActivityHostProblemBinding binding;

    String url = Constants.BASE_URL + "host_problem.php";

    private RecyclerView plist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Problems> problemslist;
    private RecyclerView.Adapter adapter;

    Handler handler = new Handler();
    int delay = 10*1000;
    Runnable runnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostProblemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String template = intent.getStringExtra("template");
        String ip = intent.getStringExtra("ip");

        Intent serviceIntent = new Intent(getApplicationContext(), RiskService.class);
        serviceIntent.putExtra("Sname", Sname);
        serviceIntent.putExtra("Surl", Surl);
        serviceIntent.putExtra("Suser", Suser);
        serviceIntent.putExtra("Spass", Spass);
        serviceIntent.putExtra("hostid", hostid);
        serviceIntent.putExtra("hostname", hostname);
        serviceIntent.putExtra("interface", ip);
        serviceIntent.putExtra("template", template);
        startService(serviceIntent);


        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopServiceIntent = new Intent(Host_problem.this, RiskService.class);
                stopService(stopServiceIntent);

                Intent i = new Intent(Host_problem.this, Host_info.class);
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



        binding.tvHostname.setText(hostname);
        plist = findViewById(R.id.hostproblemlist);

        problemslist = new ArrayList<>();
        adapter = new Host_problemAdapter(getApplicationContext(),problemslist);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(plist.getContext(), linearLayoutManager.getOrientation());

        plist.setHasFixedSize(true);
        plist.setLayoutManager(linearLayoutManager);
        plist.addItemDecoration(dividerItemDecoration);
        plist.setAdapter(adapter);
        getData(hostid);

    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        handler.postDelayed(new Runnable() {
            public void run() {
                problemslist.clear();
                getData(hostid);
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


    private void getData(String hostid) {
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostname = intent.getStringExtra("name");
        getAnalysisAlertData(Surl,hostname);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("ResPAPI",response);
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject eventObject = new JSONObject(arraydata.getString(i));
                        Problems problem = new Problems();
                        problem.setDetail(eventObject.getString("detail"));
                        problem.setSeverity(eventObject.getString("severity"));
                        problem.setTime(eventObject.getString("time"));
                        problemslist.add(problem);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e) {
                    Log.d("TagERRAPI", e.getMessage());
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
                params.put("hostids", hostid);
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };
        queue.add(request);
    }

    private void getAnalysisAlertData(String Surl,String hostname) {
        Cursor cursor = new DBHelper(this).getAna_alert2(Surl,hostname);

        problemslist.clear();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String host_name = cursor.getString(0);
                    String alertText = cursor.getString(1);
                    String severity = cursor.getString(2);
                    String date = cursor.getString(3);

                    Problems problem = new Problems();
                    problem.setHostname(host_name);
                    problem.setDetail(alertText);
                    problem.setSeverity(severity);
                    problem.setTime(date);

                    problemslist.add(problem);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }


}


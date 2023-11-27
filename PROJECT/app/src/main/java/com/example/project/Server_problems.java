package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityServerProblemsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server_problems extends AppCompatActivity {
    private ActivityServerProblemsBinding binding;

    String url = Constants.BASE_URL + "server_problems.php";

    private RecyclerView plist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Problems> problemslist;
    private RecyclerView.Adapter adapter;

    Handler handler = new Handler();
    int delay = 30*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityServerProblemsBinding.inflate(getLayoutInflater());


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
                Intent i = new Intent(Server_problems.this, Dashbroad.class);
                i.putExtra("Sname", Sname);
                i.putExtra("Surl", Surl);
                i.putExtra("Suser", Suser);
                i.putExtra("Spass", Spass);
                startActivity(i);
            }
        });

        TextView map = findViewById(R.id.mapsbtn);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_problems.this, Server_maps.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });


        TextView history =(TextView) findViewById(R.id.historybtn);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_problems.this, History.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });

        TextView Sdetail =(TextView) findViewById(R.id.hostsbtn);
        Sdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_problems.this, Server_detail.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });

        TextView discovery = findViewById(R.id.discoverybtn);
        discovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Server_problems.this, Server_Discovery.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });




        binding.edtName.setText(Sname);
        binding.edtUrl.setText(Surl);

        plist = findViewById(R.id.problemlist);

        problemslist = new ArrayList<>();
        adapter = new Server_problemsAdapter(getApplicationContext(),problemslist);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(plist.getContext(), linearLayoutManager.getOrientation());

        plist.setHasFixedSize(true);
        plist.setLayoutManager(linearLayoutManager);
        plist.addItemDecoration(dividerItemDecoration);
        plist.setAdapter(adapter);

        getData();

    }

    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
                problemslist.clear();
                getData();
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


    private void getData() {
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        getAnalysisAlertData(Surl);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        Problems problem = new Problems();
                        problem.setHostname(jsonObject.getString("hostname"));
                        problem.setDetail(jsonObject.getString("detail"));
                        problem.setSeverity(jsonObject.getString("severity"));
                        problem.setTime(jsonObject.getString("time"));

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
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };
        queue.add(request);
    }

    private void getAnalysisAlertData(String Surl) {
        Cursor cursor = new DBHelper(this).getAna_alert(Surl);

        problemslist.clear();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String hostname = cursor.getString(0);
                    String alertText = cursor.getString(1);
                    String severity = cursor.getString(2);
                    String date = cursor.getString(3);

                    Problems problem = new Problems();
                    problem.setHostname(hostname);
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
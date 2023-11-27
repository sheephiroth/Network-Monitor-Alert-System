package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityDiscoveryDetailBinding;
import com.example.project.databinding.ActivityServerDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoveryDetail extends AppCompatActivity {
    private ActivityDiscoveryDetailBinding binding;

    private RecyclerView hlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Hosts> hostlist;
    private RecyclerView.Adapter adapter;


    String url = Constants.BASE_URL + "discovery.php";

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
        binding = ActivityDiscoveryDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String druleid = intent.getStringExtra("druleid");
        String drulename = intent.getStringExtra("drulename");



        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DiscoveryDetail.this, Server_Discovery.class);
                i.putExtra("Sname", Sname);
                i.putExtra("Surl", Surl);
                i.putExtra("Suser", Suser);
                i.putExtra("Spass", Spass);
                startActivity(i);
            }
        });

        Log.d("test", "1");
        binding.edtName.setText(Sname);


        hlist = findViewById(R.id.discoverylist);

        hostlist = new ArrayList<>();
        adapter = new DiscoveryAdapter(getApplicationContext(),hostlist,Sname,Surl,Suser,Spass);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(hlist.getContext(), linearLayoutManager.getOrientation());

        hlist.setHasFixedSize(true);
        hlist.setLayoutManager(linearLayoutManager);
        hlist.addItemDecoration(dividerItemDecoration);
        hlist.setAdapter(adapter);
        getData();
    }
    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            public void run() {
                hostlist.clear();
                getData();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    private void getData() {
        Log.d("test", "2");
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String druleid = intent.getStringExtra("druleid");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        Hosts host = new Hosts();
                        host.setHostname(jsonObject.getString("hostname"));
                        host.setIp(jsonObject.getString("ip"));
                        host.setDupdown(jsonObject.getString("dupdown"));
                        Log.d("test", "3");
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
                params.put("druleids", druleid);
                return params;
            }
        };
        queue.add(request);
    }


}
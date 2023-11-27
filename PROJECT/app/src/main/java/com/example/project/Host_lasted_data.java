package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityHostLastedDataBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Host_lasted_data extends AppCompatActivity {
    private ActivityHostLastedDataBinding binding;
    String url = Constants.BASE_URL + "host_lasted_data.php";

    private RecyclerView dlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Datas> datalist;
    private RecyclerView.Adapter adapter;

    Handler handler = new Handler();
    int delay = 30*1000;
    Runnable runnable;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostLastedDataBinding.inflate(getLayoutInflater());
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

        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Host_lasted_data.this, Host_info.class);
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

        dlist = findViewById(R.id.dataslist);

        datalist = new ArrayList<>();
        adapter = new Host_lasteddataAdapter(getApplicationContext(),datalist);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(dlist.getContext(), linearLayoutManager.getOrientation());

        dlist.setHasFixedSize(true);
        dlist.setLayoutManager(linearLayoutManager);
        dlist.addItemDecoration(dividerItemDecoration);
        dlist.setAdapter(adapter);
        getData(hostid);

    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        handler.postDelayed(new Runnable() {
            public void run() {
                datalist.clear();
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
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        Datas data = new Datas();
                        data.setDataname(jsonObject.getString("dataname"));
                        data.setLastvalue(jsonObject.getString("lastvalue"));
                        data.setUnit(jsonObject.getString("unit"));
                        data.setLastcheck(jsonObject.getString("lastcheck"));
                        datalist.add(data);
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
}
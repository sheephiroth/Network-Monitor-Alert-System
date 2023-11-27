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
import com.example.project.databinding.ActivityServerDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server_detail extends AppCompatActivity  {
    private ActivityServerDetailBinding binding;

    private RecyclerView hlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Hosts> hostlist;
    private RecyclerView.Adapter adapter;
    List<String> mapids = new ArrayList<>();


    String url = Constants.BASE_URL + "server_detail.php";

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
        binding = ActivityServerDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");



        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopServiceIntent = new Intent(Server_detail.this, RiskService.class);
                stopService(stopServiceIntent);

                Intent i = new Intent(Server_detail.this, Dashbroad.class);
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
                Intent stopServiceIntent = new Intent(Server_detail.this, RiskService.class);
                stopService(stopServiceIntent);

                Intent intent = new Intent(Server_detail.this, Server_maps.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });

        TextView history = findViewById(R.id.historybtn);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopServiceIntent = new Intent(Server_detail.this, RiskService.class);
                stopService(stopServiceIntent);

                Intent intent = new Intent(Server_detail.this, History.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });

        TextView problems = findViewById(R.id.problemsbtn);
        problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent stopServiceIntent = new Intent(Server_detail.this, RiskService.class);
                stopService(stopServiceIntent);

                Intent intent = new Intent(Server_detail.this, Server_problems.class);
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
                Intent stopServiceIntent = new Intent(Server_detail.this, RiskService.class);
                stopService(stopServiceIntent);
                Intent intent = new Intent(Server_detail.this, Server_Discovery.class);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                startActivity(intent);
            }
        });



        binding.edtName.setText(Sname);
        binding.edtUrl.setText(Surl);


        hlist = findViewById(R.id.hostslist);

        hostlist = new ArrayList<>();
        adapter = new HostsAdapter(getApplicationContext(),hostlist,Sname,Surl,Suser,Spass);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(hlist.getContext(), linearLayoutManager.getOrientation());

        hlist.setHasFixedSize(true);
        hlist.setLayoutManager(linearLayoutManager);
        hlist.addItemDecoration(dividerItemDecoration);
        hlist.setAdapter(adapter);
        getData();

        getmap(new OnMapIDReadyListener() {
            @Override
            public void onMapIDReady() {
                getmap2();
            }
        });



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
                        host.setHost_id(jsonObject.getString("hostid"));
                        host.setHostname(jsonObject.getString("hostname"));
                        host.setIp(jsonObject.getString("interface"));
                        host.setType(jsonObject.getString("type"));
                        hostlist.add(host);
                        String shostid = jsonObject.getString("hostid");
                        String shostname = jsonObject.getString("hostname");
                        String sip = jsonObject.getString("interface");
                        String stemplate = jsonObject.getString("template");

                        Intent serviceIntent = new Intent(getApplicationContext(), RiskService.class);
                        serviceIntent.putExtra("Sname", Sname);
                        serviceIntent.putExtra("Surl", Surl);
                        serviceIntent.putExtra("Suser", Suser);
                        serviceIntent.putExtra("Spass", Spass);
                        serviceIntent.putExtra("hostid", shostid);
                        serviceIntent.putExtra("hostname", shostname);
                        serviceIntent.putExtra("interface", sip);
                        serviceIntent.putExtra("template", stemplate);

                        startService(serviceIntent);

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

    private void getmap(final Server_detail.OnMapIDReadyListener listener) {
        String url = Constants.BASE_URL + "map_id.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        Maps map = new Maps();
                        map.setMapid(jsonObject.getString("mapid"));
                        map.setMapname(jsonObject.getString("mapname"));

                        mapids.add(map.getMapid());

                    }
                    adapter.notifyDataSetChanged();
                    listener.onMapIDReady();
                } catch (Exception e) {
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


    private void getmap2() {
        String url = Constants.BASE_URL + "map_get.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);

        for (String mapid : mapids) {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        String mapid = res.getString("mapid");


                    } catch (JSONException e) {
                        e.printStackTrace();
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
                    params.put("mapid", mapid);
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

    public interface OnMapIDReadyListener {
        void onMapIDReady();
    }






}
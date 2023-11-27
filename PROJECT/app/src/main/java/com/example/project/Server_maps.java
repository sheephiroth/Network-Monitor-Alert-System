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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityHostGraphBinding;
import com.example.project.databinding.ActivityServerMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server_maps extends AppCompatActivity {
    private ActivityServerMapsBinding binding;

    private RecyclerView mlist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Maps> maplist;
    private RecyclerView.Adapter adapter;

    List<String> mapids = new ArrayList<>();



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
        binding = ActivityServerMapsBinding.inflate(getLayoutInflater());
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
                Intent i = new Intent(Server_maps.this, Server_detail.class);
                i.putExtra("Sname", Sname);
                i.putExtra("Surl", Surl);
                i.putExtra("Suser", Suser);
                i.putExtra("Spass", Spass);
                startActivity(i);
            }
        });

        binding.edtName.setText(Sname);

        mlist = findViewById(R.id.mapidlist);

        maplist = new ArrayList<>();
        adapter = new MapsidAdapter(getApplicationContext(),maplist,Sname,Surl,Suser,Spass);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mlist.getContext(), linearLayoutManager.getOrientation());

        mlist.setHasFixedSize(true);
        mlist.setLayoutManager(linearLayoutManager);
        mlist.addItemDecoration(dividerItemDecoration);
        mlist.setAdapter(adapter);



        getData(new OnDataReadyListener() {
            @Override
            public void onDataReady() {
//                getData2();
                getmap();
            }
        });
    }
    @Override
    protected void onResume() {
        handler.postDelayed(new Runnable() {
            public void run() {
                maplist.clear();
                mapids.clear();
                getData(new OnDataReadyListener() {
                    @Override
                    public void onDataReady() {
//                        getData2();
                        getmap();
                    }
                });
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

//    private void getData() {
//        String url = Constants.BASE_URL + "map_id.php";
//        Intent intent = getIntent();
//        String Sname = intent.getStringExtra("Sname");
//        String Surl = intent.getStringExtra("Surl");
//        String Suser = intent.getStringExtra("Suser");
//        String Spass = intent.getStringExtra("Spass");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONArray arraydata = new JSONArray(response);
//                    for (int i = 0; i < arraydata.length(); i++) {
//                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
//                        Maps map = new Maps();
//                        map.setMapid(jsonObject.getString("mapid"));
//                        map.setMapname(jsonObject.getString("mapname"));
//                        maplist.add(map);
//
//                        mapids.add(map.getMapid());
//                    }
//                    adapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    Log.d("TagERRAPI", response);
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("ERROR", String.valueOf(error));
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Sname", Sname);
//                params.put("Surl", Surl);
//                params.put("Suser", Suser);
//                params.put("Spass", Spass);
//                return params;
//            }
//        };
//        queue.add(request);
//    }

    private void getmap() {
        String url = Constants.BASE_URL + "map_create.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String mapname = intent.getStringExtra("mapname");

        RequestQueue queue = Volley.newRequestQueue(this);

        for (String mapid : mapids) {
            String mapinfo = "C:/Users/PC/Desktop/PROJECT/app/src/main/assets/Zabbix_map_" + mapid + ".json";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject res = new JSONObject(response);
                        String mapid = res.getString("mapid");

                        // Handle the response for each mapid if needed

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Handle the error for each mapid if needed
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mapinfo", mapinfo);
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


    private void getData(final OnDataReadyListener listener) {
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
                        maplist.add(map);

                        mapids.add(map.getMapid());

                    }
                    adapter.notifyDataSetChanged();
                    listener.onDataReady();
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
//
//
//    private void getData2() {
//        String url = Constants.BASE_URL + "map_get.php";
//        Intent intent = getIntent();
//        String Sname = intent.getStringExtra("Sname");
//        String Surl = intent.getStringExtra("Surl");
//        String Suser = intent.getStringExtra("Suser");
//        String Spass = intent.getStringExtra("Spass");
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        for (String mapid : mapids) {
//            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//                        JSONObject res = new JSONObject(response);
//                        String mapid = res.getString("mapid");
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new com.android.volley.Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("ERROR", String.valueOf(error));
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("mapid", mapid);
//                    params.put("Sname", Sname);
//                    params.put("Surl", Surl);
//                    params.put("Suser", Suser);
//                    params.put("Spass", Spass);
//                    return params;
//                }
//            };
//            queue.add(request);
//        }
//    }
//
//
//
    public interface OnDataReadyListener {
        void onDataReady();
    }





}


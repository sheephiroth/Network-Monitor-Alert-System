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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityAlertBinding;
import com.example.project.databinding.ActivityHostRiskBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alert extends AppCompatActivity {
    private ActivityAlertBinding binding;
    private RecyclerView alist;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Problems> alertlist;
    private RecyclerView.Adapter adapter;

    String url = Constants.BASE_URL + "server_alert.php";

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
        binding = ActivityAlertBinding.inflate( getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        ArrayList Sname = intent.getStringArrayListExtra("Sname");
        ArrayList Surl = intent.getStringArrayListExtra("Surl");
        ArrayList Suser = intent.getStringArrayListExtra("Suser");
        ArrayList Spass = intent.getStringArrayListExtra("Spass");
//        Log.d("55555555555555555555555555555", );

        TextView server =(TextView) findViewById(R.id.serverbtn);
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Alert.this, Home.class);
                startActivity(i);
            }
        });

        alist = findViewById(R.id.alertlist);

        alertlist = new ArrayList<>();
        adapter = new AlertAdapter(getApplicationContext(),alertlist);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(alist.getContext(), linearLayoutManager.getOrientation());

        alist.setHasFixedSize(true);
        alist.setLayoutManager(linearLayoutManager);
        alist.addItemDecoration(dividerItemDecoration);
        alist.setAdapter(adapter);

        getData();



    }

    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
                alertlist.clear();
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
        ArrayList Sname = intent.getStringArrayListExtra("Sname");
        ArrayList Surl = intent.getStringArrayListExtra("Surl");
        ArrayList Suser = intent.getStringArrayListExtra("Suser");
        ArrayList Spass = intent.getStringArrayListExtra("Spass");
        int x = Surl.size();
        for(int n=0;n < x;n++){
            int y = n;
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray arraydata = new JSONArray(response);
                        for (int i = 0; i < arraydata.length(); i++) {
                            JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                            Problems problem = new Problems();
                            problem.setSname(jsonObject.getString("server"));
                            problem.setHostname(jsonObject.getString("hostname"));
                            problem.setDetail(jsonObject.getString("detail"));
                            problem.setSeverity(jsonObject.getString("severity"));
                            problem.setTime(jsonObject.getString("time"));

                            alertlist.add(problem);
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
                    params.put("Sname", Sname.get(y).toString());
                    params.put("Surl", Surl.get(y).toString());
                    params.put("Suser", Suser.get(y).toString());
                    params.put("Spass", Spass.get(y).toString());
                    return params;
                }
            };
            queue.add(request);
        }

    }
}
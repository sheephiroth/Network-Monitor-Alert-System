package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivitySnmp1EdtnameBinding;
import com.example.project.databinding.ActivitySnmp2IpforwardingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SNMP_2_ipforwarding extends AppCompatActivity {

    private ActivitySnmp2IpforwardingBinding binding;
    private EditText edt_commu, edt_ipforw;
    private Button update1,cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnmp2IpforwardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                Intent i = new Intent(SNMP_2_ipforwarding.this, Hostconfig_list.class);
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

        edt_commu = findViewById(R.id.edt_commu);
        edt_ipforw = findViewById(R.id.edt_ipforw);
        update1 = findViewById(R.id.updatebtn);
        update1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String newValue = edt_ipforw.getText().toString();
                String ip = intent.getStringExtra("ip");;
                String commu = edt_commu.getText().toString();

                sendUpdateRequest(ip, commu, newValue);

            }
        });

        cancel = findViewById(R.id.cancelbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_commu.setText("");
                edt_ipforw.setText("");
            }
        });

    }

    private void sendUpdateRequest(String ip, String commu, String newValue) {
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
                            Toast.makeText(SNMP_2_ipforwarding.this, message, Toast.LENGTH_SHORT).show();
//                            updateHostname(newName);
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
                params.put("SNMP_NO", "2");
                params.put("ip", ip);
                params.put("commu", commu);
                params.put("newValue", newValue);
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
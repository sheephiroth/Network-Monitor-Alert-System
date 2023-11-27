package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityHostProblemBinding;
import com.example.project.databinding.ActivityHostRiskBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.tensorflow.lite.Interpreter;

public class Host_risk extends AppCompatActivity {
    private ActivityHostRiskBinding binding;
    private Interpreter tflite;


    String psu_status,ivo_in_temp, ivo_in_temp_status, ivo_out_temp, ivo_out_temp_status, npe_in_temp, npe_in_temp_status,npe_out_temp,
            npe_out_temp_status,cpu_use,c_space_use,mem_use,root_space_use,available_mem,sda_disk_use,cpu_temp,gpu_temp,trigger;
    Double psu_status_d = 999.0, ivo_in_temp_d = 999.0, ivo_in_temp_status_d = 999.0, ivo_out_temp_d = 999.0, ivo_out_temp_status_d = 999.0,
            npe_in_temp_d = 999.0, npe_in_temp_status_d = 999.0, npe_out_temp_d = 999.0, npe_out_temp_status_d = 999.0, cpu_use_d = 999.0,
            c_space_use_d = 999.0, mem_use_d = 999.0, root_space_use_d = 999.0, available_mem_d = 999.0,cpu_temp_d = 999.0 ,gpu_temp_d =999.0 ,sda_disk_use_d = 999.0;
    Integer trigger_int = 0;

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
        binding = ActivityHostRiskBinding.inflate( getLayoutInflater());
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
                Intent intent = new Intent(Host_risk.this, Host_info.class);
                intent.putExtra("hostid", hostid);
                intent.putExtra("name",hostname);
                intent.putExtra("Sname", Sname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                intent.putExtra("template", template);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });

        binding.hostname.setText(hostname);

        if ("Cisco IOS by SNMP".equals(template)) {
            getRouter(new OnDataReadyListener() {
                @Override
                public void onDataReady() {
                    processRouterData();
                }
            });
        } else if ("Zabbix server health".equals(template)) {
            getZabbixServer(new OnDataReadyListener() {
                @Override
                public void onDataReady() {
                    processZabbixServerData();
                }
            });
        } else if ("Windows by Zabbix agent".equals(template)) {
            getWindow(new OnDataReadyListener() {
                @Override
                public void onDataReady() {
                    processWindowData();
                }
            });
        }


    }
    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = getIntent();
                String template = intent.getStringExtra("template");
                if ("Cisco IOS by SNMP".equals(template)) {
                    getRouter(new OnDataReadyListener() {
                        @Override
                        public void onDataReady() {
                            processRouterData();
                        }
                    });
                } else if ("Zabbix server health".equals(template)) {
                    getZabbixServer(new OnDataReadyListener() {
                        @Override
                        public void onDataReady() {
                            processZabbixServerData();
                        }
                    });
                } else if ("Windows by Zabbix agent".equals(template)) {
                    getWindow(new OnDataReadyListener() {
                        @Override
                        public void onDataReady() {
                            processWindowData();
                        }
                    });
                }
            }
        }, delay);

        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    public interface OnDataReadyListener {
        void onDataReady();
    }


    private float[][] runModel(float[][] inputData) {
        float[][] outputData = new float[1][1];
        tflite.run(inputData, outputData);
        return outputData;
    }
    public void getRouter(OnDataReadyListener listener) {
        String url = Constants.BASE_URL + "host_risk.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String itemName = item.getString("name");
                        String itemValue = item.getString("value");
//                        Log.d("JSON_ITEM", "Item name: " + itemName + ", Item value: " + itemValue);

                        switch (itemName.toLowerCase().trim()) {
                            case "ac power supply: power supply status":
                                psu_status = itemValue;
                                if(psu_status == null){
                                    psu_status_d = 999.9;
                                }else{
                                    psu_status_d = Double.parseDouble(psu_status);
                                }
                                break;
                            case "i/o cont inlet: temperature status":
                                ivo_in_temp_status = itemValue;
                                if(ivo_in_temp_status == null){
                                    ivo_in_temp_status_d = 999.9;
                                }else{
                                    ivo_in_temp_status_d = Double.parseDouble(ivo_in_temp_status);
                                }
                                break;
                            case "i/o cont outlet: temperature status":
                                ivo_out_temp_status = itemValue;
                                if(ivo_out_temp_status == null){
                                    ivo_out_temp_status_d = 999.9;
                                }else{
                                    ivo_out_temp_status_d = Double.parseDouble(ivo_out_temp_status);
                                }
                                break;
                            case "npe inlet: temperature status":
                                npe_in_temp_status = itemValue;
                                if(npe_in_temp_status == null){
                                    npe_in_temp_status_d = 999.9;
                                }else{
                                    npe_in_temp_status_d = Double.parseDouble(npe_in_temp_status);
                                }
                                break;
                            case "npe outlet: temperature status":
                                npe_out_temp_status = itemValue;
                                if(npe_out_temp_status == null){
                                    npe_out_temp_status_d = 999.9;
                                }else{
                                    npe_out_temp_status_d = Double.parseDouble(npe_out_temp_status);
                                }
                                break;
                            case "i/o cont inlet: temperature":
                                ivo_in_temp = itemValue;
                                if(ivo_in_temp == null){
                                    ivo_in_temp_d = 999.9;
                                }else{
                                    ivo_in_temp_d = Double.parseDouble(ivo_in_temp);
                                }
                                break;
                            case "i/o cont outlet: temperature":
                                ivo_out_temp = itemValue;
                                if(ivo_out_temp == null){
                                    ivo_out_temp_d = 999.9;
                                }else{
                                    ivo_out_temp_d = Double.parseDouble(ivo_out_temp);
                                }
                                break;
                            case "npe inlet: temperature":
                                npe_in_temp = itemValue;
                                if(npe_in_temp == null){
                                    npe_in_temp_d = 999.9;
                                }else{
                                    npe_in_temp_d = Double.parseDouble(npe_in_temp);
                                }
                                break;
                            case "npe outlet: temperature":
                                npe_out_temp = itemValue;
                                if(npe_out_temp == null){
                                    npe_out_temp_d = 999.9;
                                }else{
                                    npe_out_temp_d = Double.parseDouble(npe_out_temp);
                                }
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onDataReady();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error: " + error.toString());
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
        });
    }

    public void getZabbixServer(OnDataReadyListener listener) {
        String url = Constants.BASE_URL + "host_risk.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String itemName = item.getString("name");
                        String itemValue = item.getString("value");
//                        Log.d("JSON_ITEM", "Item name: " + itemName + ", Item value: " + itemValue);

                        switch (itemName.toLowerCase().trim()) {
                            case "cpu utilization":
                                cpu_use = itemValue;
                                if(cpu_use == null){
                                    cpu_use_d = 999.9;
                                }else{
                                    cpu_use_d = Double.parseDouble(cpu_use);
                                }
                                break;
                            case "memory utilization":
                                mem_use = itemValue;
                                if(mem_use == null){
                                    mem_use_d = 999.9;
                                }else{
                                    mem_use_d = Double.parseDouble(mem_use);
                                }
                                break;
                            case "/: space utilization":
                                root_space_use = itemValue;
                                if(root_space_use == null){
                                    root_space_use_d = 999.9;
                                }else{
                                    root_space_use_d = Double.parseDouble(root_space_use);
                                }
                                break;
                            case "available memory in %":
                                available_mem = itemValue;
                                if(available_mem == null){
                                    available_mem_d = 999.9;
                                }else{
                                    available_mem_d = Double.parseDouble(available_mem);
                                }
                                break;
                            case "sda: Disk utilization":
                                sda_disk_use = itemValue;
                                if(sda_disk_use == null){
                                    sda_disk_use_d = 999.9;
                                }else{
                                    sda_disk_use_d = Double.parseDouble(sda_disk_use);
                                }
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onDataReady();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error: " + error.toString());
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
        });
    }

    public void getWindow(OnDataReadyListener listener) {
        String url = Constants.BASE_URL + "host_risk.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        String itemName = item.getString("name");
                        String itemValue = item.getString("value");
//                        Log.d("JSON_ITEM", "Item name: " + itemName + ", Item value: " + itemValue);

                        switch (itemName.toLowerCase().trim()) {
                            case "cpu utilization":
                                cpu_use = itemValue;
                                if(cpu_use == null){
                                    cpu_use_d = 999.9;
                                }else{
                                    cpu_use_d = Double.parseDouble(cpu_use);
                                }
                                break;
                            case "(c:): space utilization":
                                c_space_use = itemValue;
                                if(c_space_use == null){
                                    c_space_use_d = 999.9;
                                }else{
                                    c_space_use_d = Double.parseDouble(c_space_use);
                                }
                                break;
                            case "memory utilization":
                                mem_use = itemValue;
                                if(mem_use == null){
                                    mem_use_d = 999.9;
                                }else{
                                    mem_use_d = Double.parseDouble(mem_use);
                                }
                                break;
                            case "cpu temperature":
                                cpu_temp = itemValue;
                                if(cpu_temp == null){
                                    cpu_temp_d = 999.9;
                                }else{
                                    cpu_temp_d = Double.parseDouble(cpu_temp);
                                }
                                break;
                            case "gpu temperature":
                                gpu_temp = itemValue;
                                if(gpu_temp == null){
                                    gpu_temp_d = 999.9;
                                }else{
                                    gpu_temp_d = Double.parseDouble(gpu_temp);
                                }
                                break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onDataReady();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError", "Error: " + error.toString());
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
        });
    }

    public void getData2(OnDataReadyListener listener) {
        String url = Constants.BASE_URL + "host_risk2.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    trigger = jsonArray.getString(0);
                    trigger_int = Integer.parseInt(trigger);

                    if (listener != null) {
                        listener.onDataReady();
                    }

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

    private void loadRouterModel() {
        try {
            tflite = new Interpreter(loadModelRouter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private MappedByteBuffer loadModelRouter() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("Router.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadZabbixServerModel() {
        try {
            tflite = new Interpreter(loadModelZabbixServer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelZabbixServer() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("ZabbixServer.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadWindowModel() {
        try {
            tflite = new Interpreter(loadModelWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MappedByteBuffer loadModelWindow() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("Window.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private float[][] convertRouterDataToArray(double psu_status_d, double ivo_in_temp_d, double ivo_in_temp_status_d, double ivo_out_temp_d, double ivo_out_temp_status_d,
                                               double npe_in_temp_d, double npe_in_temp_status_d, double npe_out_temp_d, double npe_out_temp_status_d, int trigger_int) {
        float[] inputData = new float[10];
        inputData[0] = (float) psu_status_d;
        inputData[1] = (float) ivo_in_temp_d;
        inputData[2] = (float) ivo_in_temp_status_d;
        inputData[3] = (float) ivo_out_temp_d;
        inputData[4] = (float) ivo_out_temp_status_d;
        inputData[5] = (float) npe_in_temp_d;
        inputData[6] = (float) npe_in_temp_status_d;
        inputData[7] = (float) npe_out_temp_d;
        inputData[8] = (float) npe_out_temp_status_d;
        inputData[9] = (float) trigger_int;

        int n = 9;
        float[][] inputArray = new float[1][10];
        for (int i = 0; i < n+1; i++) {
            inputArray[0][i] = inputData[i];
        }

        return inputArray;
    }

    private float[][] convertZabbixServerDataToArray( double cpu_use_d, double mem_use_d, double root_space_use_d, double available_mem_d, double sda_disk_use_d, int trigger_int) {
        float[] inputData = new float[6];
        inputData[0] = (float) cpu_use_d;
        inputData[1] = (float) mem_use_d;
        inputData[2] = (float) root_space_use_d;
        inputData[3] = (float) available_mem_d;
        inputData[4] = (float) sda_disk_use_d;
        inputData[5] = (float) trigger_int;

        int n = 5;
        float[][] inputArray = new float[1][6];
        for (int i = 0; i < n+1; i++) {
            inputArray[0][i] = inputData[i];
        }

        return inputArray;
    }

    private float[][] convertWindowDataToArray( double cpu_use_d, double c_space_use_d, double mem_use_d, double cpu_temp_d, double gpu_temp_d, int trigger_int) {
        float[] inputData = new float[6];
        inputData[0] = (float) cpu_use_d;
        inputData[1] = (float) c_space_use_d;
        inputData[2] = (float) mem_use_d;
        inputData[3] = (float) cpu_temp_d;
        inputData[4] = (float) gpu_temp_d;
        inputData[5] = (float) trigger_int;

        int n = 5;
        float[][] inputArray = new float[1][6];
        for (int i = 0; i < n+1; i++) {
            inputArray[0][i] = inputData[i];
        }

        return inputArray;
    }
    private void processRouterData() {
        getData2(new OnDataReadyListener() {
            @Override
            public void onDataReady() {
                Intent intent = getIntent();
                String hostname = intent.getStringExtra("name");
                loadRouterModel();
                float[][] inputArray = convertRouterDataToArray(psu_status_d, ivo_in_temp_d, ivo_in_temp_status_d, ivo_out_temp_d, ivo_out_temp_status_d,
                        npe_in_temp_d, npe_in_temp_status_d, npe_out_temp_d, npe_out_temp_status_d, trigger_int);
                float[][] outputData = runModel(inputArray);
                float outputValue = outputData[0][0];
                float failure_chance = (float) (outputValue * 100);
                String f_failure_chance = String.format("%.4f", failure_chance);
                binding.tv1.setText("Power supply status : " + psu_status_d + "  \n I/O IN temp : " + ivo_in_temp_d +" °C \nI/O OUT temp : " + ivo_out_temp_d + " °C \nNPE IN temp : "
                        + npe_in_temp_d + " °C \nNPE OUT temp : " + npe_out_temp_d + " °C \nTriggerLv5 : "+ trigger_int);
                binding.tv2.setText("Failure chance of " + hostname + " is : " + f_failure_chance + " %");
                if (f_failure_chance.equals("0.0000")) {
                    binding.tv3.setText("No risk of failure detected.");
                } else if (Float.parseFloat(f_failure_chance) > 0.0 && Float.parseFloat(f_failure_chance) <= 25.0) {
                    binding.tv3.setText("Minor issues detected, but the device is still functioning properly.");
                } else if (Float.parseFloat(f_failure_chance) > 25.0 && Float.parseFloat(f_failure_chance) <= 50.0) {
                    binding.tv3.setText("Moderate risk of failure detected. Action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 50.0 && Float.parseFloat(f_failure_chance) <= 75.0) {
                    binding.tv3.setText("Critical issues detected. Urgent action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 75.0) {
                    binding.tv3.setText("Severe issues detected. Failure is imminent without action.");
                }
//                Router_Alert( psu_status_d, ivo_in_temp_d, ivo_in_temp_status_d, ivo_out_temp_d, ivo_out_temp_status_d,
//                 npe_in_temp_d, npe_in_temp_status_d, npe_out_temp_d, npe_out_temp_status_d,  trigger_int,  failure_chance);
            }
        });
    }

    private void processZabbixServerData() {
        getData2(new OnDataReadyListener() {
            @Override
            public void onDataReady() {
                Intent intent = getIntent();
                String hostname = intent.getStringExtra("name");
                loadZabbixServerModel();
                float[][] inputArray = convertZabbixServerDataToArray(cpu_use_d,  mem_use_d,  root_space_use_d, available_mem_d,  sda_disk_use_d, trigger_int);
                float[][] outputData = runModel(inputArray);
                float outputValue = outputData[0][0];
                float failure_chance = (float) (outputValue * 100);
                String f_failure_chance = String.format("%.4f", failure_chance);
                binding.tv1.setText("Cpu utilization : " + cpu_use_d + " % \nMemory utilization : " + mem_use_d +" % \n/: utilization : "
                        + root_space_use_d + "% \nAvailable memory : " + available_mem_d + " % \nSDA: Disk utilization : " + sda_disk_use_d + " % \nTriggerLv5 : "+ trigger_int);
                binding.tv2.setText("Failure chance of " + hostname + " is : " + f_failure_chance + " %");
                if (f_failure_chance.equals("0.0000")) {
                    binding.tv3.setText("No risk of failure detected.");
                } else if (Float.parseFloat(f_failure_chance) > 0.0 && Float.parseFloat(f_failure_chance) <= 25.0) {
                    binding.tv3.setText("Minor issues detected, but the device is still functioning properly.");
                } else if (Float.parseFloat(f_failure_chance) > 25.0 && Float.parseFloat(f_failure_chance) <= 50.0) {
                    binding.tv3.setText("Moderate risk of failure detected. Action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 50.0 && Float.parseFloat(f_failure_chance) <= 75.0) {
                    binding.tv3.setText("Critical issues detected. Urgent action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 75.0) {
                    binding.tv3.setText("Severe issues detected. Failure is imminent without action.");
                }
            }
        });
    }

    private void processWindowData() {
        getData2(new OnDataReadyListener() {
            @Override
            public void onDataReady() {
                Intent intent = getIntent();
                String Surl = intent.getStringExtra("Surl");
                String hostname = intent.getStringExtra("name");
                loadWindowModel();
                float[][] inputArray = convertWindowDataToArray(cpu_use_d,c_space_use_d,mem_use_d,cpu_temp_d,gpu_temp_d,trigger_int);
                float[][] outputData = runModel(inputArray);
                float outputValue = outputData[0][0];
                float failure_chance = (float) (outputValue * 100);
                String f_failure_chance = String.format("%.4f", failure_chance);
                binding.tv1.setText("Cpu utilization : " + cpu_use_d + " % \n(c:):space utilization : " + c_space_use_d +" % \nMemory utilization : "
                        + mem_use_d + "% \nCPU temperature : " + cpu_temp_d + " °C \nGPU temperature : " + gpu_temp_d + " °C \nTriggerLv5 : "+ trigger_int);
                binding.tv2.setText("Failure chance of " + hostname + " is : " + f_failure_chance + " %");
                if (f_failure_chance.equals("0.0000")) {
                    binding.tv3.setText("No risk of failure detected.");
                } else if (Float.parseFloat(f_failure_chance) > 0.0 && Float.parseFloat(f_failure_chance) <= 25.0) {
                    binding.tv3.setText("Minor issues detected, but the device is still functioning properly.");
                } else if (Float.parseFloat(f_failure_chance) > 25.0 && Float.parseFloat(f_failure_chance) <= 50.0) {
                    binding.tv3.setText("Moderate risk of failure detected. Action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 50.0 && Float.parseFloat(f_failure_chance) <= 75.0) {
                    binding.tv3.setText("Critical issues detected. Urgent action needed.");
                } else if (Float.parseFloat(f_failure_chance) > 75.0) {
                    binding.tv3.setText("Severe issues detected. Failure is imminent without action.Fix the problem quickly to prevent damage to the device.");
                }
                Window_Alert(Surl,hostname,cpu_use_d,c_space_use_d,mem_use_d,cpu_temp_d,gpu_temp_d,trigger_int,failure_chance);
            }
        });
    }

    public void Window_Alert(String Surl, String hostname,double cpu_use_d, double c_space_use_d, double mem_use_d, double cpu_temp_d, double gpu_temp_d, int trigger_int, float failure_chance) {
        DBHelper dbHelper = new DBHelper(this);
        Cursor cursor = new DBHelper(this).getAlert_config(Surl,hostname);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String item = cursor.getString(0);
                String operator = cursor.getString(1);
                float value = cursor.getFloat(2);
                String severity = cursor.getString(3);
                String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                String alert_text = "";

                double x = 0.0;

                if (item.equals("cpu_use_d")) {
                    x = cpu_use_d;
                    alert_text = "Cpu utilization is " + x;
                } else if (item.equals("c_space_use_d")) {
                    x = c_space_use_d;
                    alert_text = "(c:):space utilization is " + x;
                } else if (item.equals("mem_use_d")) {
                    x = mem_use_d;
                    alert_text = "Memory utilization is is " + x;
                } else if (item.equals("cpu_temp_d")) {
                    x = cpu_temp_d;
                    alert_text = "CPU temperature is " + x;
                } else if (item.equals("gpu_temp_d")) {
                    x = gpu_temp_d;
                    alert_text = "GPU temperature is " + x;
                } else if (item.equals("trigger_int")) {
                    x = trigger_int;
                    alert_text = "TriggerLv5 is " + x;
                } else if (item.equals("failure_chance")) {
                    x = failure_chance;
                }


                boolean result = false;
                if (operator.equals(">")) {
                    result = (x > value);
                } else if (operator.equals("<")) {
                    result = (x < value);
                } else if (operator.equals("==")) {
                    result = (x == value);
                } else if (operator.equals(">=")) {
                    result = (x >= value);
                } else if (operator.equals("<=")) {
                    result = (x <= value);
                } else if (operator.equals("!=")) {
                    result = (x != value);
                }

                if (result) {
                    if(item.equals("failure_chance")){
                        String f_failure_chance = String.format("%.4f", failure_chance);
                        float failureChanceFloat = Float.parseFloat(f_failure_chance);
                        alert_text = "Failure chance is " + failureChanceFloat;
                        value = failureChanceFloat;
                        dbHelper.insertAnalysisAlert(Surl,hostname,item,operator,value,severity,alert_text,date);
//                        Log.d("RESULT", "Condition is true for item: " + item);
                    }else {
                        dbHelper.insertAnalysisAlert(Surl,hostname,item,operator,value,severity,alert_text,date);
//                        Log.d("RESULT", "Condition is true for item: " + item);
                    }
                } else {
//                    Log.d("RESULT", "Condition is false for item: " + item);
                }
            }
            cursor.close();
        } else {
            Log.e("ERROR", "Cursor is null");
        }
    }


//    private void Window_Alert(double cpu_use_d, double c_space_use_d, double mem_use_d, double cpu_temp_d, double gpu_temp_d, int trigger_int, float failure_chance) {
//        Intent intent = getIntent();
//        String hostname = intent.getStringExtra("name");
//        String Surl = intent.getStringExtra("Surl");
//        String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
//        String f_failure_chance = String.format("%.4f", failure_chance);
//        DBHelper dbHelper = new DBHelper(this);
//
//        if (cpu_use_d > 60.0) {
//            dbHelper.insertAnalysisAlert("w1", Surl, hostname, "Cpu utilization is " + cpu_use_d + " %", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w1", Surl, hostname);
//        }
//
//        if (c_space_use_d > 95.0) {
//            dbHelper.insertAnalysisAlert("w2", Surl, hostname, "(c:):space utilization is " + c_space_use_d + " %", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w2", Surl, hostname);
//        }
//
//        if (mem_use_d > 90.0) {
//            dbHelper.insertAnalysisAlert("w3", Surl, hostname, "Memory utilization is " + mem_use_d + " %", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w3", Surl, hostname);
//        }
//
//        if (cpu_temp_d > 70.0) {
//            dbHelper.insertAnalysisAlert("w4", Surl, hostname, "CPU temperature is " + cpu_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w4", Surl, hostname);
//        }
//
//        if (gpu_temp_d > 70.0) {
//            dbHelper.insertAnalysisAlert("w5", Surl, hostname, "GPU temperature is " + gpu_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w5", Surl, hostname);
//        }
//
//        if (trigger_int > 0) {
//            dbHelper.insertAnalysisAlert("w6", Surl, hostname, "TriggerLv5 is " + trigger_int, "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w6", Surl, hostname);
//        }
//
//        if (failure_chance > 0) {
//            dbHelper.insertAnalysisAlert("w7", Surl, hostname, "Failure chance is " + f_failure_chance + " %", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("w7", Surl, hostname);
//        }
//
//    }

//    private void Router_Alert(double psu_status_d,double ivo_in_temp_d,double ivo_in_temp_status_d,double ivo_out_temp_d,double ivo_out_temp_status_d,
//                              double npe_in_temp_d,double npe_in_temp_status_d,double npe_out_temp_d,double npe_out_temp_status_d, int trigger_int, float failure_chance) {
//        Intent intent = getIntent();
//        String hostname = intent.getStringExtra("name");
//        String Surl = intent.getStringExtra("Surl");
//        String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
//        String f_failure_chance = String.format("%.4f", failure_chance);
//        DBHelper dbHelper = new DBHelper(this);
//
//        if (psu_status_d == 0.0) {
//            dbHelper.insertAnalysisAlert("r1", Surl, hostname, "Power supply status is " + psu_status_d , "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r1", Surl, hostname);
//        }
//
//        if (ivo_in_temp_d > 40.0) {
//            dbHelper.insertAnalysisAlert("r2", Surl, hostname, "I/O IN temp is " + ivo_in_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r2", Surl, hostname);
//        }
//
//
//        if (ivo_out_temp_d > 40.0) {
//            dbHelper.insertAnalysisAlert("r4", Surl, hostname, "I/O OUT temp is " + ivo_out_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r4", Surl, hostname);
//        }
//
//
//        if (npe_in_temp_d > 40.0) {
//            dbHelper.insertAnalysisAlert("r6", Surl, hostname, "NPE IN temp is " + npe_in_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r6", Surl, hostname);
//        }
//
//
//        if (npe_out_temp_d > 40.0) {
//            dbHelper.insertAnalysisAlert("r8", Surl, hostname, "NPE OUT temp is " + npe_out_temp_d + " °C", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r8", Surl, hostname);
//        }
//
//
//        if (trigger_int > 0) {
//            dbHelper.insertAnalysisAlert("r10", Surl, hostname, "TriggerLv5 is " + trigger_int, "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r10", Surl, hostname);
//        }
//
//        if (failure_chance > 0) {
//            dbHelper.insertAnalysisAlert("r11", Surl, hostname, "Failure chance is " + f_failure_chance + " %", "Information", date);
//        }else {
//            dbHelper.deleteAnalysisAlert("r11", Surl, hostname);
//        }
//    }








}
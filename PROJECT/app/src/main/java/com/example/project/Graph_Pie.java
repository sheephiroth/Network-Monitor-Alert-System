package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityGraphPieBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph_Pie extends AppCompatActivity {
    private ActivityGraphPieBinding binding;
    String url = Constants.BASE_URL + "graph_pie.php";

    Handler handler = new Handler();
    int delay = 30*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphPieBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String graphname = intent.getStringExtra("graphname");
        String graphid = intent.getStringExtra("graphid");
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

                Intent i = new Intent(Graph_Pie.this, Host_graph.class);
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
        binding.tvGraphname.setText(graphname);
        getData();

    }

    public void getData(){
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        String graphid = intent.getStringExtra("graphid");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<PieEntry> pieEntries = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject res = jsonArray.getJSONObject(i);
                        String valueStr = res.getString("value");
                        String name = res.getString("name");
                        String unit = res.getString("unit");

                        String lowerUnit = unit.toLowerCase();

                        if (lowerUnit.equals("b")) {
                            double value = Double.parseDouble(valueStr);
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "kb";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "mb";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "gb";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "tb";
                            }
                            else{
                                unit = "b";
                            }
                            valueStr = String.format("%.2f", value);
                        }

                        if (lowerUnit.equals("bps")) {
                            double value = Double.parseDouble(valueStr);
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "kbps";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "mbps";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "gbps";
                            }
                            if (value / 1024 > 1) {
                                value /= 1024;
                                unit = "tbps";
                            }
                            else{
                                unit = "bps";
                            }
                            valueStr = String.format("%.2f", value);
                        }

                        pieEntries.add(new PieEntry(Float.parseFloat(valueStr), name));
                        if (i == 0) {
                            binding.tv1.setText(name + ": " + valueStr + " " + unit);
                        } else if (i == 1) {
                            binding.tv2.setText(name + ": " + valueStr + " " + unit);
                        }
                    }


                    createPieChart(pieEntries);

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
                params.put("graphid", graphid);
                return params;
            }
        };
        queue.add(request);
    }

    private void createPieChart(List<PieEntry> pieEntries) {
        PieChart pieChart = findViewById(R.id.graph);


        Collections.sort(pieEntries, new Comparator<PieEntry>() {
            @Override
            public int compare(PieEntry o1, PieEntry o2) {
                return Float.compare(o2.getValue(), o1.getValue());
            }
        });


        float total = 0;
        for (PieEntry entry : pieEntries) {
            total += entry.getValue();
        }


        float firstValue = pieEntries.get(0).getValue();
        float secondValue = (pieEntries.size() > 1) ? pieEntries.get(1).getValue() : 0;
        float firstPercent = 100f;
        float secondPercent = (secondValue / firstValue) * 100f;
        if (secondPercent > 100f) {
            secondPercent = 100f;
        }

        // Update values of first and second entry
        pieEntries.get(0).setY(firstPercent);
        if (pieEntries.size() > 1) {
            pieEntries.get(1).setY(secondPercent);
        }


        List<Integer> colors = new ArrayList<>();
        int green = Color.parseColor("#03AC13");
        int red = Color.parseColor("#D0312D");
        colors.add(green);
        colors.add(red);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(14f);


        DecimalFormat df = new DecimalFormat("###.#");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return df.format(value) + " %";
            }
        });

        pieChart.setData(pieData);


        pieChart.getDescription().setEnabled(false);

        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        pieChart.getLegend().setTypeface(boldTypeface);
        pieChart.getLegend().setTextColor(Color.BLACK);
        pieChart.getLegend().setFormSize(14f);

        pieChart.setEntryLabelTypeface(boldTypeface);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieData.setValueTextColor(Color.BLACK);

        pieChart.invalidate();
    }

    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
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



}
package com.example.project;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityGraphNormal1Binding;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LegendRenderer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Graph_Normal_1 extends AppCompatActivity {
    private ActivityGraphNormal1Binding binding;
    private List<Graphs> graphlist;
    private RecyclerView glist;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphNormal1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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

                Intent i = new Intent(Graph_Normal_1.this, Host_graph.class);
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

        graphlist = new ArrayList<>();
        getData();


    }

    private void getData() {
        String url = Constants.BASE_URL + "graph_normal_1_text.php";
        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        String graphid = intent.getStringExtra("graphid");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject itemObject = new JSONObject(arraydata.getString(i));
                        Graphs item = new Graphs();
                        item.setItemid(itemObject.getString("itemid"));
                        item.setName(itemObject.getString("name"));
                        String lowerUnit = itemObject.getString("unit").toLowerCase();
                        double last = Double.parseDouble(itemObject.getString("last"));
                        double min = Double.parseDouble(itemObject.getString("min"));
                        double avg = Double.parseDouble(itemObject.getString("avg"));
                        double max = Double.parseDouble(itemObject.getString("max"));
                        String unit_last = "", unit_min = "", unit_avg = "", unit_max = "";
                        if (lowerUnit.equals("b")) {
                            if (last / 1024 < 1) {
                                unit_last = "b";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "kb";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "mb";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "gb";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "tb";
                            }

                            if (min / 1024 < 1) {
                                unit_min = "b";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "kb";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "mb";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "gb";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "tb";
                            }

                            if (avg / 1024 < 1) {
                                unit_avg = "b";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "kb";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "mb";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "gb";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "tb";
                            }

                            if (max / 1024 < 1) {
                                unit_max = "b";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "kb";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "mb";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "gb";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "tb";
                            }
                        }
                        if (lowerUnit.equals("bps")) {
                            if (last / 1024 < 1) {
                                unit_last = "bps";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "kbps";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "mbps";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "gbps";
                            }
                            if (last / 1024 > 1) {
                                last /= 1024;
                                unit_last = "tbps";
                            }

                            if (min / 1024 < 1) {
                                unit_min = "bps";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "kbps";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "mbps";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "gbps";
                            }
                            if (min / 1024 > 1) {
                                min /= 1024;
                                unit_min = "tbps";
                            }

                            if (avg / 1024 < 1) {
                                unit_avg = "bps";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "kbps";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "mbps";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "gbps";
                            }
                            if (avg / 1024 > 1) {
                                avg /= 1024;
                                unit_avg = "tbps";
                            }

                            if (max / 1024 < 1) {
                                unit_max = "bps";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "kbps";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "mbps";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "gbps";
                            }
                            if (max / 1024 > 1) {
                                max /= 1024;
                                unit_max = "tbps";
                            }
                        }
                        item.setLast(String.format("%.4f", last) + " " + unit_last);
                        item.setMin(String.format("%.4f", min) + " " + unit_min);
                        item.setAvg(String.format("%.4f", avg) + " " + unit_avg);
                        item.setMax(String.format("%.4f", max) + " " + unit_max);
                        graphlist.add(item);

                    }
                    getData2();
                } catch (Exception e) {
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
                params.put("graphid", graphid);
                params.put("hostids", hostid);
                return params;
            }
        };
        queue.add(request);
    }
    public void getData2(){
        String url = Constants.BASE_URL + "graph_normal_1_graph.php";
        final int[] COLORS = {
                Color.rgb(244, 67, 54),
                Color.rgb(33, 150, 243),
                Color.rgb(76, 175, 80),
                Color.rgb(255, 152, 0),
                Color.rgb(63, 81, 181),
                Color.rgb(96, 125, 139)
        };

        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String hostid = intent.getStringExtra("hostid");
        String graphid = intent.getStringExtra("graphid");
        LineChart lineChart = findViewById(R.id.graph);
        LineData lineData = new LineData();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Map<String, List<Entry>> dataMap = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject res = jsonArray.getJSONObject(i);
                        float value = Float.parseFloat(res.getString("last"));
                        String timeStr = res.getString("time");
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
                        Date date = format.parse(timeStr);
                        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String dateString = newFormat.format(date);
                        String newTimeStr = dateString + " " + timeStr;
                        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date newDate = format.parse(newTimeStr);
                        long timestamp = newDate.getTime();
                        String name = res.getString("name");

                        if (!dataMap.containsKey(name)) {
                            dataMap.put(name, new ArrayList<>());
                        }
                        List<Entry> entries = dataMap.get(name);
                        entries.add(new Entry(timestamp, value));
                    }

                    for (Map.Entry<String, List<Entry>> entry : dataMap.entrySet()) {
                        LineDataSet dataSet = new LineDataSet(entry.getValue(), entry.getKey());
                        dataSet.setLineWidth(2.5f);
                        dataSet.setCircleRadius(4.5f);
                        int color = COLORS[(lineData.getDataSetCount() % COLORS.length)];

                        dataSet.setColor(color);
                        dataSet.setCircleColor(color);
                        dataSet.setHighLightColor(color);
                        dataSet.setDrawValues(false);
                        lineData.addDataSet(dataSet);
                    }
                    lineChart.setData(lineData);
                    lineChart.invalidate();

                    createLineChart(lineData);


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                params.put("graphid", graphid);
                params.put("hostids", hostid);
                return params;
            }
        };
        queue.add(request);
    }

    private void createLineChart(LineData lineData) {
        LineChart lineChart = findViewById(R.id.graph);
        lineChart.setData(lineData);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setValueFormatter(new DateAxisValueFormatter());
        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(1000);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);


        TableLayout legendLayout = findViewById(R.id.legendLayout);
        createCustomLegend(legendLayout, lineData, graphlist);

        lineChart.invalidate();
    }

    private void createCustomLegend(TableLayout legendLayout, LineData lineData, List<Graphs> graphList) {
        int blockWidth = (int) Utils.convertDpToPixel(12f);
        int blockMargin = (int) Utils.convertDpToPixel(4f);
        int textMarginLeft = (int) Utils.convertDpToPixel(8f);

        TableRow columnNameRow = new TableRow(legendLayout.getContext());
        legendLayout.addView(columnNameRow);

        View offsetBlock = new View(columnNameRow.getContext());
        TableRow.LayoutParams offsetBlockParams = new TableRow.LayoutParams(blockWidth, blockWidth);
        offsetBlockParams.setMargins(0, 0, blockMargin, 0);
        offsetBlock.setLayoutParams(offsetBlockParams);
        columnNameRow.addView(offsetBlock);

        String[] columnNames = {"Name", "Last", "Min", "Avg", "Max"};
        for (String columnName : columnNames) {
            TextView columnNameView = new TextView(columnNameRow.getContext());
            TableRow.LayoutParams columnNameViewParams = new TableRow.LayoutParams();
            columnNameViewParams.setMargins(textMarginLeft, 0, 0, 0);
            columnNameView.setLayoutParams(columnNameViewParams);
            columnNameView.setText(columnName);
            columnNameView.setTextColor(Color.BLACK);
            columnNameView.setTextSize(10f);
            columnNameRow.addView(columnNameView);
        }

        for (ILineDataSet dataSet : lineData.getDataSets()) {
            String dataSetLabel = dataSet.getLabel();
            Graphs graphData = null;
            for (Graphs graph : graphList) {
                if (graph.getName().equals(dataSetLabel)) {
                    graphData = graph;
                    break;
                }
            }

            TableRow row = new TableRow(legendLayout.getContext());
            legendLayout.addView(row);

            View block = new View(row.getContext());
            TableRow.LayoutParams blockParams = new TableRow.LayoutParams(blockWidth, blockWidth);
            blockParams.setMargins(0, 0, blockMargin, 0);
            block.setLayoutParams(blockParams);
            block.setBackgroundColor(dataSet.getColor());
            row.addView(block);

            TextView labelView = new TextView(row.getContext());
            TableRow.LayoutParams labelViewParams = new TableRow.LayoutParams();
            labelViewParams.setMargins(textMarginLeft, 0, 0, 0);
            labelView.setLayoutParams(labelViewParams);
            labelView.setText(dataSetLabel);
            labelView.setTextColor(Color.BLACK);
            labelView.setTextSize(10f);
            row.addView(labelView);

            if (graphData != null) {
                TableRow.LayoutParams textViewParams = new TableRow.LayoutParams();
                textViewParams.setMargins(textMarginLeft, 0, 0, 0);

                TextView lastView = new TextView(row.getContext());
                lastView.setLayoutParams(textViewParams);
                lastView.setText(String.valueOf(graphData.getLast()));
                lastView.setTextColor(Color.BLACK);
                lastView.setTextSize(10f);
                row.addView(lastView);

                TextView minView = new TextView(row.getContext());
                minView.setLayoutParams(textViewParams);
                minView.setText(String.valueOf(graphData.getMin()));
                minView.setTextColor(Color.BLACK);
                minView.setTextSize(10f);
                row.addView(minView);

                TextView avgView = new TextView(row.getContext());
                avgView.setLayoutParams(textViewParams);
                avgView.setText(String.valueOf(graphData.getAvg()));
                avgView.setTextColor(Color.BLACK);
                avgView.setTextSize(10f);
                row.addView(avgView);

                TextView maxView = new TextView(row.getContext());
                maxView.setLayoutParams(textViewParams);
                maxView.setText(String.valueOf(graphData.getMax()));
                maxView.setTextColor(Color.BLACK);
                maxView.setTextSize(10f);
                row.addView(maxView);
            }
        }
    }




    private class DateAxisValueFormatter extends ValueFormatter {

        private final SimpleDateFormat timeFormat;
        private final SimpleDateFormat dateFormat;
        private boolean showDate;

        public DateAxisValueFormatter() {
            timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));
        }

        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            Date date = new Date(millis);
            String formattedValue;
            if (showDate) {
                formattedValue = dateFormat.format(date);
            } else {
                formattedValue = timeFormat.format(date);
            }
            return formattedValue;
        }

        public void setShowDate(boolean showDate) {
            this.showDate = showDate;
        }
    }








}
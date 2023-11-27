package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.project.databinding.ActivityGraphInfoBinding;
import com.example.project.databinding.ActivityHostGraphBinding;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class Graph_info extends AppCompatActivity {
    private ActivityGraphInfoBinding binding;

    String url = Constants.BASE_URL + "graph.php";
    SQLiteDatabase sqLiteDatabase;
    DBHelper DB;

    Handler handler = new Handler();
    int delay = 30*1000;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphInfoBinding.inflate(getLayoutInflater());
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

                Intent i = new Intent(Graph_info.this, Host_graph.class);
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




    }







    private void getData() {
        Intent intent = getIntent();
        String hostid = intent.getStringExtra("hostid");
        String hostname = intent.getStringExtra("name");
        String graphname = intent.getStringExtra("graphname");
        String graphid = intent.getStringExtra("graphid");
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    String itemid, iname, value, time;
                    int num,x1=0,x2=0,x3=0,x4=0,x5=0,x6=0,y;
                    int y11=-1,y12=-1,y13=-1,y14=-1,y15=-1,y16=-1,y17=-1,y18=-1,y19=-1,y20=-1,y111=-1,y112=-1,y113=-1,y114=-1,y115=-1,y116=-1,y117=-1,y118=-1,y119=-1,y120=-1;
                    int y21=-1,y22=-1,y23=-1,y24=-1,y25=-1,y26=-1,y27=-1,y28=-1,y29=-1,y30=-1,y211=-1,y212=-1,y213=-1,y214=-1,y215=-1,y216=-1,y217=-1,y218=-1,y219=-1,y220=-1;
                    int y31=-1,y32=-1,y33=-1,y34=-1,y35=-1,y36=-1,y37=-1,y38=-1,y39=-1,y40=-1,y311=-1,y312=-1,y313=-1,y314=-1,y315=-1,y316=-1,y317=-1,y318=-1,y319=-1,y320=-1;
                    int y41=-1,y42=-1,y43=-1,y44=-1,y45=-1,y46=-1,y47=-1,y48=-1,y49=-1,y50=-1,y411=-1,y412=-1,y413=-1,y414=-1,y415=-1,y416=-1,y417=-1,y418=-1,y419=-1,y420=-1;
                    int y51=-1,y52=-1,y53=-1,y54=-1,y55=-1,y56=-1,y57=-1,y58=-1,y59=-1,y60=-1,y511=-1,y512=-1,y513=-1,y514=-1,y515=-1,y516=-1,y517=-1,y518=-1,y519=-1,y520=-1;
                    int y61=-1,y62=-1,y63=-1,y64=-1,y65=-1,y66=-1,y67=-1,y68=-1,y69=-1,y70=-1,y611=-1,y612=-1,y613=-1,y614=-1,y615=-1,y616=-1,y617=-1,y618=-1,y619=-1,y620=-1;
                    JSONArray arraydata = new JSONArray(response);
                    for (int i = 0; i < arraydata.length(); i++) {
                        JSONObject jsonObject = new JSONObject(arraydata.getString(i));
                        num = jsonObject.getInt("no");
                        itemid = jsonObject.getString("itemid");
                        iname = jsonObject.getString("name");
                        value = jsonObject.getString("value");
                        time = jsonObject.getString("time");

                        y = num + 6;

                        if( (y%6)== 1 ){
                            if(num > x1){
                                x1=num;
                                binding.tv1.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y11 == -1){ y11 = new Integer(jsonObject.getString("value"));}
                                if(y12 == -1){ y12 = new Integer(jsonObject.getString("value"));}
                                if(y13 == -1){ y13 = new Integer(jsonObject.getString("value"));}
                                if(y14 == -1){ y14 = new Integer(jsonObject.getString("value"));}
                                if(y15 == -1){ y15 = new Integer(jsonObject.getString("value"));}
                                if(y16 == -1){ y16 = new Integer(jsonObject.getString("value"));}
                                if(y17 == -1){ y17 = new Integer(jsonObject.getString("value"));}
                                if(y18 == -1){ y18 = new Integer(jsonObject.getString("value"));}
                                if(y19 == -1){ y19 = new Integer(jsonObject.getString("value"));}
                                if(y20 == -1){ y20 = new Integer(jsonObject.getString("value"));}
                                if(y111 == -1){ y111 = new Integer(jsonObject.getString("value"));}
                                if(y112 == -1){ y112 = new Integer(jsonObject.getString("value"));}
                                if(y113 == -1){ y113 = new Integer(jsonObject.getString("value"));}
                                if(y114 == -1){ y114 = new Integer(jsonObject.getString("value"));}
                                if(y115 == -1){ y115 = new Integer(jsonObject.getString("value"));}
                                if(y116 == -1){ y116 = new Integer(jsonObject.getString("value"));}
                                if(y117 == -1){ y117 = new Integer(jsonObject.getString("value"));}
                                if(y118 == -1){ y118 = new Integer(jsonObject.getString("value"));}
                                if(y119 == -1){ y119 = new Integer(jsonObject.getString("value"));}
                                if(y120 == -1){ y120 = new Integer(jsonObject.getString("value"));}


                            }
                        }
                        if((y%6)== 2 ){
                            if(num > x2){
                                x2=num;
                                binding.tv2.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y21 == -1){ y21 = new Integer(jsonObject.getString("value"));}
                                if(y22 == -1){ y22 = new Integer(jsonObject.getString("value"));}
                                if(y23 == -1){ y23 = new Integer(jsonObject.getString("value"));}
                                if(y24 == -1){ y24 = new Integer(jsonObject.getString("value"));}
                                if(y25 == -1){ y25 = new Integer(jsonObject.getString("value"));}
                                if(y26 == -1){ y26 = new Integer(jsonObject.getString("value"));}
                                if(y27 == -1){ y27 = new Integer(jsonObject.getString("value"));}
                                if(y28 == -1){ y28 = new Integer(jsonObject.getString("value"));}
                                if(y29 == -1){ y29 = new Integer(jsonObject.getString("value"));}
                                if(y30 == -1){ y30 = new Integer(jsonObject.getString("value"));}
                                if(y211 == -1){ y211 = new Integer(jsonObject.getString("value"));}
                                if(y212 == -1){ y212 = new Integer(jsonObject.getString("value"));}
                                if(y213 == -1){ y213 = new Integer(jsonObject.getString("value"));}
                                if(y214 == -1){ y214 = new Integer(jsonObject.getString("value"));}
                                if(y215 == -1){ y215 = new Integer(jsonObject.getString("value"));}
                                if(y216 == -1){ y216 = new Integer(jsonObject.getString("value"));}
                                if(y217 == -1){ y217 = new Integer(jsonObject.getString("value"));}
                                if(y218 == -1){ y218 = new Integer(jsonObject.getString("value"));}
                                if(y219 == -1){ y219 = new Integer(jsonObject.getString("value"));}
                                if(y220 == -1){ y220 = new Integer(jsonObject.getString("value"));}
                            }
                        }
                        if((y%6)== 3 ){
                            if(num > x3){
                                x3=num;
                                binding.tv3.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y31 == -1){ y31 = new Integer(jsonObject.getString("value"));}
                                if(y32 == -1){ y32 = new Integer(jsonObject.getString("value"));}
                                if(y33 == -1){ y33 = new Integer(jsonObject.getString("value"));}
                                if(y34 == -1){ y34 = new Integer(jsonObject.getString("value"));}
                                if(y35 == -1){ y35 = new Integer(jsonObject.getString("value"));}
                                if(y36 == -1){ y36 = new Integer(jsonObject.getString("value"));}
                                if(y37 == -1){ y37 = new Integer(jsonObject.getString("value"));}
                                if(y38 == -1){ y38 = new Integer(jsonObject.getString("value"));}
                                if(y39 == -1){ y39 = new Integer(jsonObject.getString("value"));}
                                if(y40 == -1){ y40 = new Integer(jsonObject.getString("value"));}
                                if(y311 == -1){ y311 = new Integer(jsonObject.getString("value"));}
                                if(y312 == -1){ y312 = new Integer(jsonObject.getString("value"));}
                                if(y313 == -1){ y313 = new Integer(jsonObject.getString("value"));}
                                if(y314 == -1){ y314 = new Integer(jsonObject.getString("value"));}
                                if(y315 == -1){ y315 = new Integer(jsonObject.getString("value"));}
                                if(y316 == -1){ y316 = new Integer(jsonObject.getString("value"));}
                                if(y317 == -1){ y317 = new Integer(jsonObject.getString("value"));}
                                if(y318 == -1){ y318 = new Integer(jsonObject.getString("value"));}
                                if(y319 == -1){ y319 = new Integer(jsonObject.getString("value"));}
                                if(y320 == -1){ y320 = new Integer(jsonObject.getString("value"));}
                            }
                        }
                        if((y%6)== 4 ){
                            if(num > x4){
                                x4=num;
                                binding.tv4.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y41 == -1){ y41 = new Integer(jsonObject.getString("value"));}
                                if(y42 == -1){ y42 = new Integer(jsonObject.getString("value"));}
                                if(y43 == -1){ y43 = new Integer(jsonObject.getString("value"));}
                                if(y44 == -1){ y44 = new Integer(jsonObject.getString("value"));}
                                if(y45 == -1){ y45 = new Integer(jsonObject.getString("value"));}
                                if(y46 == -1){ y46 = new Integer(jsonObject.getString("value"));}
                                if(y47 == -1){ y47 = new Integer(jsonObject.getString("value"));}
                                if(y48 == -1){ y48 = new Integer(jsonObject.getString("value"));}
                                if(y49 == -1){ y49 = new Integer(jsonObject.getString("value"));}
                                if(y50 == -1){ y50 = new Integer(jsonObject.getString("value"));}
                                if(y411 == -1){ y411 = new Integer(jsonObject.getString("value"));}
                                if(y412 == -1){ y412 = new Integer(jsonObject.getString("value"));}
                                if(y413 == -1){ y413 = new Integer(jsonObject.getString("value"));}
                                if(y414 == -1){ y414 = new Integer(jsonObject.getString("value"));}
                                if(y415 == -1){ y415 = new Integer(jsonObject.getString("value"));}
                                if(y416 == -1){ y416 = new Integer(jsonObject.getString("value"));}
                                if(y417 == -1){ y417 = new Integer(jsonObject.getString("value"));}
                                if(y418 == -1){ y418 = new Integer(jsonObject.getString("value"));}
                                if(y419 == -1){ y419 = new Integer(jsonObject.getString("value"));}
                                if(y420 == -1){ y420 = new Integer(jsonObject.getString("value"));}
                            }
                        }
                        if((y%6)== 5 ){
                            if(num > x5){
                                x5=num;
                                binding.tv5.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y51 == -1){ y51 = new Integer(jsonObject.getString("value"));}
                                if(y52 == -1){ y52 = new Integer(jsonObject.getString("value"));}
                                if(y53 == -1){ y53 = new Integer(jsonObject.getString("value"));}
                                if(y54 == -1){ y54 = new Integer(jsonObject.getString("value"));}
                                if(y55 == -1){ y55 = new Integer(jsonObject.getString("value"));}
                                if(y56 == -1){ y56 = new Integer(jsonObject.getString("value"));}
                                if(y57 == -1){ y57 = new Integer(jsonObject.getString("value"));}
                                if(y58 == -1){ y58 = new Integer(jsonObject.getString("value"));}
                                if(y59 == -1){ y59 = new Integer(jsonObject.getString("value"));}
                                if(y60 == -1){ y60 = new Integer(jsonObject.getString("value"));}
                                if(y511 == -1){ y511 = new Integer(jsonObject.getString("value"));}
                                if(y512 == -1){ y512 = new Integer(jsonObject.getString("value"));}
                                if(y513 == -1){ y513 = new Integer(jsonObject.getString("value"));}
                                if(y514 == -1){ y514 = new Integer(jsonObject.getString("value"));}
                                if(y515 == -1){ y515 = new Integer(jsonObject.getString("value"));}
                                if(y516 == -1){ y516 = new Integer(jsonObject.getString("value"));}
                                if(y517 == -1){ y517 = new Integer(jsonObject.getString("value"));}
                                if(y518 == -1){ y518 = new Integer(jsonObject.getString("value"));}
                                if(y519 == -1){ y519 = new Integer(jsonObject.getString("value"));}
                                if(y520 == -1){ y520 = new Integer(jsonObject.getString("value"));}
                            }
                        }
                        if((y%6)== 0 ){
                            if(num > x6){
                                x6=num;
                                binding.tv6.setText(jsonObject.getString("name")+"   LastValue : "+jsonObject.getString("value")+" bps");
                                if(y61 == -1){ y61 = new Integer(jsonObject.getString("value"));}
                                if(y62 == -1){ y62 = new Integer(jsonObject.getString("value"));}
                                if(y63 == -1){ y63 = new Integer(jsonObject.getString("value"));}
                                if(y64 == -1){ y64 = new Integer(jsonObject.getString("value"));}
                                if(y65 == -1){ y65 = new Integer(jsonObject.getString("value"));}
                                if(y66 == -1){ y66 = new Integer(jsonObject.getString("value"));}
                                if(y67 == -1){ y67 = new Integer(jsonObject.getString("value"));}
                                if(y68 == -1){ y68 = new Integer(jsonObject.getString("value"));}
                                if(y69 == -1){ y69 = new Integer(jsonObject.getString("value"));}
                                if(y70 == -1){ y70 = new Integer(jsonObject.getString("value"));}
                                if(y611 == -1){ y611 = new Integer(jsonObject.getString("value"));}
                                if(y612 == -1){ y612 = new Integer(jsonObject.getString("value"));}
                                if(y613 == -1){ y613 = new Integer(jsonObject.getString("value"));}
                                if(y614 == -1){ y614 = new Integer(jsonObject.getString("value"));}
                                if(y615 == -1){ y615 = new Integer(jsonObject.getString("value"));}
                                if(y616 == -1){ y616 = new Integer(jsonObject.getString("value"));}
                                if(y617 == -1){ y617 = new Integer(jsonObject.getString("value"));}
                                if(y618 == -1){ y618 = new Integer(jsonObject.getString("value"));}
                                if(y619 == -1){ y619 = new Integer(jsonObject.getString("value"));}
                                if(y620 == -1){ y620 = new Integer(jsonObject.getString("value"));}
                            }
                        }
                    }
                    GraphView graph = (GraphView) findViewById(R.id.graph);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y11),
                            new DataPoint(100, y12),
                            new DataPoint(200, y13),
                            new DataPoint(300, y14),
                            new DataPoint(400, y15),
                            new DataPoint(500, y16),
                            new DataPoint(600, y17),
                            new DataPoint(700, y18),
                            new DataPoint(800, y19),
                            new DataPoint(900, y20),
                            new DataPoint(1000, y111),
                            new DataPoint(1100, y112),
                            new DataPoint(1200, y113),
                            new DataPoint(1300, y114),
                            new DataPoint(1400, y115),
                            new DataPoint(1500, y116),
                            new DataPoint(1600, y117),
                            new DataPoint(1700, y118),
                            new DataPoint(1800, y119),
                            new DataPoint(1900, y120)
                    });
                    graph.addSeries(series);
                    series.setColor(Color.GREEN);

                    LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y21),
                            new DataPoint(100, y22),
                            new DataPoint(200, y23),
                            new DataPoint(300, y24),
                            new DataPoint(400, y25),
                            new DataPoint(500, y26),
                            new DataPoint(600, y27),
                            new DataPoint(700, y28),
                            new DataPoint(800, y29),
                            new DataPoint(900, y30),
                            new DataPoint(1000, y211),
                            new DataPoint(1100, y212),
                            new DataPoint(1200, y213),
                            new DataPoint(1300, y214),
                            new DataPoint(1400, y215),
                            new DataPoint(1500, y216),
                            new DataPoint(1600, y217),
                            new DataPoint(1700, y218),
                            new DataPoint(1800, y219),
                            new DataPoint(1900, y220)
                    });
                    graph.addSeries(series2);
                    series2.setColor(Color.BLUE);

                    LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y31),
                            new DataPoint(100, y32),
                            new DataPoint(200, y33),
                            new DataPoint(300, y34),
                            new DataPoint(400, y35),
                            new DataPoint(500, y36),
                            new DataPoint(600, y37),
                            new DataPoint(700, y38),
                            new DataPoint(800, y39),
                            new DataPoint(900, y40),
                            new DataPoint(1000, y311),
                            new DataPoint(1100, y312),
                            new DataPoint(1200, y313),
                            new DataPoint(1300, y314),
                            new DataPoint(1400, y315),
                            new DataPoint(1500, y316),
                            new DataPoint(1600, y317),
                            new DataPoint(1700, y318),
                            new DataPoint(1800, y319),
                            new DataPoint(1900, y320)
                    });
                    graph.addSeries(series3);
                    series3.setColor(Color.RED);

                    LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y41),
                            new DataPoint(100, y42),
                            new DataPoint(200, y43),
                            new DataPoint(300, y44),
                            new DataPoint(400, y45),
                            new DataPoint(500, y46),
                            new DataPoint(600, y47),
                            new DataPoint(700, y48),
                            new DataPoint(800, y49),
                            new DataPoint(900, y50),
                            new DataPoint(1000, y411),
                            new DataPoint(1100, y412),
                            new DataPoint(1200, y413),
                            new DataPoint(1300, y414),
                            new DataPoint(1400, y415),
                            new DataPoint(1500, y416),
                            new DataPoint(1600, y417),
                            new DataPoint(1700, y418),
                            new DataPoint(1800, y419),
                            new DataPoint(1900, y420)
                    });
                    graph.addSeries(series4);
                    series4.setColor(Color.GRAY);

                    LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y51),
                            new DataPoint(100, y52),
                            new DataPoint(200, y53),
                            new DataPoint(300, y54),
                            new DataPoint(400, y55),
                            new DataPoint(500, y56),
                            new DataPoint(600, y57),
                            new DataPoint(700, y58),
                            new DataPoint(800, y59),
                            new DataPoint(900, y60),
                            new DataPoint(1000, y511),
                            new DataPoint(1100, y512),
                            new DataPoint(1200, y513),
                            new DataPoint(1300, y514),
                            new DataPoint(1400, y515),
                            new DataPoint(1500, y516),
                            new DataPoint(1600, y517),
                            new DataPoint(1700, y518),
                            new DataPoint(1800, y519),
                            new DataPoint(1900, y520)
                    });
                    graph.addSeries(series5);
                    series5.setColor(Color.LTGRAY);

                    LineGraphSeries<DataPoint> series6 = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint(0, y61),
                            new DataPoint(100, y62),
                            new DataPoint(200, y63),
                            new DataPoint(300, y64),
                            new DataPoint(400, y65),
                            new DataPoint(500, y66),
                            new DataPoint(600, y67),
                            new DataPoint(700, y68),
                            new DataPoint(800, y69),
                            new DataPoint(900, y70),
                            new DataPoint(1000, y611),
                            new DataPoint(1100, y612),
                            new DataPoint(1200, y613),
                            new DataPoint(1300, y614),
                            new DataPoint(1400, y615),
                            new DataPoint(1500, y616),
                            new DataPoint(1600, y617),
                            new DataPoint(1700, y618),
                            new DataPoint(1800, y619),
                            new DataPoint(1900, y620)
                    });
                    graph.addSeries(series6);
                    series6.setColor(Color.BLACK);
                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                        @Override
                        public String formatLabel(double value, boolean isValueX) {
                            if(isValueX){
                                if(value == 0 ){
                                    return super.formatLabel(value, isValueX);
                                }
                                else {
                                    double num = value/100;
                                    return super.formatLabel(num, isValueX);
                                }
                            }
                            else {
                                return super.formatLabel(value, isValueX);
                            }
                        }
                    });


                }catch (Exception e) {
                    Log.d("TagERRAPI", "ERROR1");
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
                params.put("graphid", graphid);
                params.put("Sname", Sname);
                params.put("Surl", Surl);
                params.put("Suser", Suser);
                params.put("Spass", Spass);
                return params;
            }
        };
        queue.add(request);
    }
    private void getGraph(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 0),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 1),
                new DataPoint(4, 6),
                new DataPoint(5, 1),
                new DataPoint(6, 5),
                new DataPoint(7, 0),
                new DataPoint(8, 10),
                new DataPoint(9, 99)
        });
        graph.addSeries(series);
        series.setColor(Color.GREEN);

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 3),
                new DataPoint(1, 2),
                new DataPoint(2, 6),
                new DataPoint(3, 2),
                new DataPoint(4, 5),
                new DataPoint(5, 6),
                new DataPoint(6, 2),
                new DataPoint(7, 10),
                new DataPoint(8, 20),
                new DataPoint(9, 100)
        });
        graph.addSeries(series2);
        series2.setColor(Color.BLUE);

        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 5),
                new DataPoint(1, 5),
                new DataPoint(2, 1),
                new DataPoint(3, 2),
                new DataPoint(4, 3)
        });
        graph.addSeries(series3);
        series3.setColor(Color.RED);

        LineGraphSeries<DataPoint> series4 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 4),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 7),
                new DataPoint(4, 6)
        });
        graph.addSeries(series4);
        series4.setColor(Color.GRAY);

        LineGraphSeries<DataPoint> series5 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 2),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 2)
        });
        graph.addSeries(series5);
        series5.setColor(Color.LTGRAY);

        LineGraphSeries<DataPoint> series6 = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 4),
                new DataPoint(2, 3),
                new DataPoint(3, 1),
                new DataPoint(4, 0)
        });
        graph.addSeries(series6);
        series6.setColor(Color.BLACK);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return "T "+super.formatLabel(value, isValueX);
                }
                else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }

    @Override
    protected void onResume() {

        handler.postDelayed(new Runnable() {
            public void run() {
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
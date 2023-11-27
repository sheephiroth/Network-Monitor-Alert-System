package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.project.databinding.ActivityMapInfoBinding;
import com.example.project.databinding.ActivityServerDetailBinding;
import com.example.project.databinding.ActivityServerMapsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Map_info extends AppCompatActivity {
    private ActivityMapInfoBinding binding;

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
        binding = ActivityMapInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(view);


        Intent intent = getIntent();
        String Sname = intent.getStringExtra("Sname");
        String Surl = intent.getStringExtra("Surl");
        String Suser = intent.getStringExtra("Suser");
        String Spass = intent.getStringExtra("Spass");
        String mapid = intent.getStringExtra("mapid");
        String mapname = intent.getStringExtra("mapname");

        ImageView back =(ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Map_info.this, Server_maps.class);
                i.putExtra("Sname", Sname);
                i.putExtra("Surl", Surl);
                i.putExtra("Suser", Suser);
                i.putExtra("Spass", Spass);
                startActivity(i);
            }
        });

        binding.mapName.setText(mapname);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    showmap();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 5000);

    }


//    public void getmap(){
//        String url = Constants.BASE_URL + "map_create.php";
//        Intent intent = getIntent();
//        String Sname = intent.getStringExtra("Sname");
//        String Surl = intent.getStringExtra("Surl");
//        String Suser = intent.getStringExtra("Suser");
//        String Spass = intent.getStringExtra("Spass");
//        String mapid = intent.getStringExtra("mapid");
//        String mapname = intent.getStringExtra("mapname");
//        String mapinfo = "C:/Users/PC/Desktop/PROJECT/app/src/main/assets/Zabbix_map_"+mapid+".json";
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject res = new JSONObject(response);
//
//                    String mapid = res.getString("mapid");
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("mapinfo", mapinfo);
//                params.put("Sname", Sname);
//                params.put("Surl", Surl);
//                params.put("Suser", Suser);
//                params.put("Spass", Spass);
//                return params;
//            }
//        };
//        queue.add(request);
//    }

    public void showmap() throws IOException {
        Intent intent = getIntent();
        String mapid = intent.getStringExtra("mapid");
        String imageName = mapid + ".png";

        // Load the image from the assets folder
        InputStream is = getAssets().open(imageName);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        is.close();

        // Set the loaded image to binding.map
        binding.map.setImageBitmap(bitmap);
    }


}
package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsidAdapter extends RecyclerView.Adapter<MapsidAdapter.ViewHolder> {

    private Context context;
    private List<Maps> list;
    private String Surl,Suser,Spass,Sname;
    private List<HashMap<String, String>> infolist;



    public MapsidAdapter(Context context, List<Maps> list,String Sname,String Surl,String Suser,String Spass) {
        this.context = context;
        this.list = list;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mapid_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Maps map = list.get(position);
        holder.edt_mapid.setText(map.getMapid());
        holder.edt_mapname.setText(map.getMapname());
        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Constants.BASE_URL + "map_create.php";
                String mapid = map.getMapid();
                String mapname = map.getMapname();
                String mapinfo = "C:/Users/PC/Desktop/PROJECT/app/src/main/assets/Zabbix_map_"+mapid+".json";
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        infolist = new ArrayList<HashMap<String, String>>();
                        try {
                            JSONObject res = new JSONObject(response);

                            String mapid = res.getString("mapid");

                            Intent intent = new Intent(context, Map_info.class);
                            intent.putExtra("Surl", Surl);
                            intent.putExtra("Suser", Suser);
                            intent.putExtra("Spass", Spass);
                            intent.putExtra("Sname", Sname);
                            intent.putExtra("mapid", mapid);
                            intent.putExtra("mapname", mapname);


                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    context.startActivity(intent);
                                }
                            }, 3000);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_mapid, edt_mapname;
        public ImageView morebtn;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_mapid = itemView.findViewById(R.id.mapid);
            edt_mapname = itemView.findViewById(R.id.mapname);
            morebtn = itemView.findViewById(R.id.moreinfobtn);
        }
    }

}
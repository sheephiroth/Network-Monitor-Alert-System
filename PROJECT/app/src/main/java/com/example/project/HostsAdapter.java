package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HostsAdapter extends RecyclerView.Adapter<HostsAdapter.ViewHolder> {

    private Context context;
    private List<Hosts> list;
    private String Surl,Suser,Spass,Sname;
    private List<HashMap<String, String>> infolist;

    public HostsAdapter(Context context, List<Hosts> list,String Sname,String Surl,String Suser,String Spass) {
        this.context = context;
        this.list = list;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.hosts_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hosts host = list.get(position);
        holder.edt_host_id.setText(host.getHost_id());
        holder.edt_hostname.setText(host.getHostname());
        holder.edt_ip.setText(host.getIp());
        holder.edt_type.setText(host.getType());
        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, Host_info.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                String id = host.getHost_id();
                String url = Constants.BASE_URL + "host_info.php";
                String ip = host.getIp();
                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        infolist = new ArrayList<HashMap<String, String>>();
                        try {
                            JSONObject res = new JSONObject(response);
                            String id = res.getString("hostids");
                            String name = res.getString("hostname");
                            String template = res.getString("template");
                            HashMap<String, String> List = new HashMap<String, String>();
                            List.put("hostid",id);
                            List.put("hostid",name);
                            infolist.add(List);

                            Intent intent = new Intent(context, Host_info.class);
                            intent.putExtra("hostid", id);
                            intent.putExtra("name", name);
                            intent.putExtra("Surl", Surl);
                            intent.putExtra("Suser", Suser);
                            intent.putExtra("Spass", Spass);
                            intent.putExtra("Sname", Sname);
                            intent.putExtra("template", template);
                            intent.putExtra("ip", ip);


                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);


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
                        params.put("hostids", id);
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
        public TextView edt_host_id, edt_hostname , edt_ip, edt_type;
        public ImageView morebtn;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_host_id = itemView.findViewById(R.id.hostid);
            edt_hostname = itemView.findViewById(R.id.hostname);
            edt_ip = itemView.findViewById(R.id.ip);
            edt_type = itemView.findViewById(R.id.type);
            morebtn = itemView.findViewById(R.id.moreinfobtn);
        }
    }

}
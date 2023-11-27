package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphidAdapter extends RecyclerView.Adapter<GraphidAdapter.ViewHolder> {
    private Context context;
    private List<Graphs> list;
    private List<HashMap<String, String>> infolist;
    private String hostid,Surl,Suser,Spass,Sname,hostname,template,ip;

    public GraphidAdapter(Context context, List<Graphs> list,String hostid,String Sname,String Surl,String Suser,String Spass,String hostname,String template,String ip) {
        this.context = context;
        this.list = list;
        this.hostid = hostid;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;
        this.hostname = hostname;
        this.template = template;
        this.ip = ip;

    }



    @Override
    public GraphidAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.graph_id_item, parent, false);
        return new GraphidAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GraphidAdapter.ViewHolder holder, int position) {
        Graphs graph = list.get(position);
        holder.edt_graph_id.setText(graph.getGraphid());
        holder.edt_graphname.setText(graph.getGraphname());
        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (graph.getGraphtype().toLowerCase().equals("normal")) {
                    intent = new Intent(context, Graph_Normal_1.class);
                } else if (graph.getGraphtype().toLowerCase().equals("pie")) {
                    intent = new Intent(context, Graph_Pie.class);
                }
                intent.putExtra("hostid", hostid);
                intent.putExtra("graphid", graph.getGraphid());
                intent.putExtra("graphname", graph.getGraphname());
                intent.putExtra("name",hostname);
                intent.putExtra("Surl", Surl);
                intent.putExtra("Suser", Suser);
                intent.putExtra("Spass", Spass);
                intent.putExtra("Sname", Sname);
                intent.putExtra("template", template);
                intent.putExtra("ip", ip);
                intent.putExtra("graphtype", graph.getGraphtype());


                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);



            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_graph_id, edt_graphname;
        public ImageView morebtn;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_graph_id = itemView.findViewById(R.id.graphid);
            edt_graphname = itemView.findViewById(R.id.graphname);
            morebtn = itemView.findViewById(R.id.moreinfobtn);
        }
    }

}
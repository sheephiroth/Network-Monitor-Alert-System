package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Host_lasteddataAdapter extends RecyclerView.Adapter<Host_lasteddataAdapter.ViewHolder> {
    private Context context;
    private List<Datas> list;

    public Host_lasteddataAdapter(Context context, List<Datas> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Host_lasteddataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.lasted_data_item, parent, false);
        return new Host_lasteddataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Host_lasteddataAdapter.ViewHolder holder, int position) {
        Datas data = list.get(position);
        holder.edt_dataname.setText(data.getDataname());
        holder.edt_lastvalue.setText(data.getLastvalue());
        holder.edt_unit.setText(data.getUnit());
        holder.edt_lastcheck.setText(data.getLastcheck());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_dataname , edt_lastvalue , edt_unit , edt_lastcheck;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_dataname = itemView.findViewById(R.id.tv_dataname);
            edt_lastvalue = itemView.findViewById(R.id.tv_lastvalue);
            edt_unit = itemView.findViewById(R.id.tv_unit);
            edt_lastcheck = itemView.findViewById(R.id.tv_lastcheck);
        }
    }

}


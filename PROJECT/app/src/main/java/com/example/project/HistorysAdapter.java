package com.example.project;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorysAdapter extends  RecyclerView.Adapter<HistorysAdapter.ViewHolder> {
    private Context context;
    private List<Historys> list;

    public HistorysAdapter(Context context, List<Historys> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HistorysAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.historys_item, parent, false);
        return new HistorysAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HistorysAdapter.ViewHolder holder, int position) {
        Historys history = list.get(position);
        holder.edt_hostname.setText(history.getHostname());
        holder.edt_detail.setText(history.getDetail());
        holder.edt_severity.setText(history.getSeverity());
        holder.edt_time.setText(history.getTime());
        holder.edt_status.setText(history.getStatus());
        if (history.getStatus().equals("RESOLVED")) {
            holder.edt_status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else if (history.getStatus().equals("PROBLEM")) {
            holder.edt_status.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        } else {
            holder.edt_status.setTextColor(ContextCompat.getColor(context, android.R.color.black));
        }
        holder.edt_status.setTypeface(null, Typeface.BOLD);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_hostname , edt_detail, edt_severity, edt_time, edt_status;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_hostname = itemView.findViewById(R.id.tv_hostname);
            edt_detail = itemView.findViewById(R.id.tv_detail);
            edt_severity = itemView.findViewById(R.id.tv_severity);
            edt_time = itemView.findViewById(R.id.tv_datetime);
            edt_status = itemView.findViewById(R.id.tv_status);
        }
    }

}

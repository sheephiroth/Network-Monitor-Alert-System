package com.example.project;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Host_problemAdapter extends RecyclerView.Adapter<Host_problemAdapter.ViewHolder> {
    private Context context;
    private List<Problems> list;

    public Host_problemAdapter(Context context, List<Problems> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Host_problemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.problem_item, parent, false);
        return new Host_problemAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Host_problemAdapter.ViewHolder holder, int position) {
        Problems problem = list.get(position);
        holder.edt_detail.setText(problem.getDetail());
        holder.edt_severity.setText(problem.getSeverity());
        holder.edt_time.setText(problem.getTime());

        switch (problem.getSeverity().toLowerCase()) {
            case "information":
                holder.edt_severity.setTextColor(Color.BLUE);
                break;
            case "warning":
                holder.edt_severity.setTextColor(Color.rgb(108, 187, 60));
                break;
            case "average":
                holder.edt_severity.setTextColor(Color.rgb(255, 165, 0));
                break;
            case "high":
                holder.edt_severity.setTextColor(Color.RED);
                break;
            case "disaster":
                holder.edt_severity.setTextColor(Color.rgb(128, 0, 0));
                break;
            default:
                holder.edt_severity.setTextColor(Color.BLACK);
        }
        holder.edt_severity.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edt_detail, edt_severity, edt_time;

        public ViewHolder(View itemView) {
            super(itemView);
            edt_detail = itemView.findViewById(R.id.tv_detail);
            edt_severity = itemView.findViewById(R.id.tv_severity);
            edt_time = itemView.findViewById(R.id.tv_datetime);
        }
    }

}

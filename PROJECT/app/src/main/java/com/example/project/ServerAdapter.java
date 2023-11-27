package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.ColorSpace;
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

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHoder> {
    private Context context;
    private ArrayList Sname, Surl, Suser, Spass;
    private DBHelper DB;
    private SQLiteDatabase sqLiteDatabase;



    public ServerAdapter(Context context, ArrayList Sname, ArrayList Surl, ArrayList Suser, ArrayList Spass) {
        this.context = context;
        this.Sname = Sname;
        this.Surl = Surl;
        this.Suser = Suser;
        this.Spass = Spass;

    }


    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.server_item,parent,false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        holder.edt_sname.setText(String.valueOf(Sname.get(position)));
        holder.edt_surl.setText(String.valueOf(Surl.get(position)));
        holder.edt_suser.setText(String.valueOf(Suser.get(position)));
        holder.edt_spass.setText(String.valueOf(Spass.get(position)));
        String text1 = holder.edt_sname.getText().toString();
        String text2 = holder.edt_surl.getText().toString();
        String text3 = holder.edt_suser.getText().toString();
        String text4 = holder.edt_spass.getText().toString();
        holder.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB = new DBHelper(context);
                sqLiteDatabase = DB.getWritableDatabase();
                int adapterPosition = holder.getAdapterPosition();
                String url = String.valueOf(Surl.get(adapterPosition));
                int deletedRows = sqLiteDatabase.delete("Server", "S_url = ?", new String[]{ url});
                if (deletedRows > 0) {
                    Sname.remove(adapterPosition);
                    Surl.remove(adapterPosition);
                    Suser.remove(adapterPosition);
                    Spass.remove(adapterPosition);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                String text1 = String.valueOf(Sname.get(adapterPosition));
                String text2 = String.valueOf(Surl.get(adapterPosition));
                String text3 = String.valueOf(Suser.get(adapterPosition));
                String text4 = String.valueOf(Spass.get(adapterPosition));
                Intent intent = new Intent(context, Dashbroad.class);
                intent.putExtra("Sname", text1);
                intent.putExtra("Surl", text2);
                intent.putExtra("Suser", text3);
                intent.putExtra("Spass", text4);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the position of the item in the RecyclerView
                int position = holder.getAdapterPosition();

                // Get the current data of the item at the position
                String currentName = String.valueOf(Sname.get(position));
                String currentUrl = String.valueOf(Surl.get(position));
                String currentUser = String.valueOf(Suser.get(position));
                String currentPass = String.valueOf(Spass.get(position));

                // Create an intent to open the EditServerActivity
                Intent intent = new Intent(context, Editserver.class);

                // Pass the current data to the EditServerActivity
                intent.putExtra("Sname", currentName);
                intent.putExtra("Surl", currentUrl);
                intent.putExtra("Suser", currentUser);
                intent.putExtra("Spass", currentPass);

                // Start the EditServerActivity
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Sname.size();
    }
    public class ViewHoder extends RecyclerView.ViewHolder{
        private TextView edt_sname, edt_surl, edt_suser, edt_spass, delbtn, editbtn;
        public ImageView morebtn;

        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            edt_sname=(TextView)itemView.findViewById(R.id.tv_sname);
            edt_surl=(TextView)itemView.findViewById(R.id.test1);
            edt_suser=(TextView)itemView.findViewById(R.id.test2);
            edt_spass=(TextView)itemView.findViewById(R.id.test3);
            delbtn=(TextView)itemView.findViewById(R.id.delbtn);
            editbtn=(TextView)itemView.findViewById(R.id.editbtn);

            morebtn= itemView.findViewById(R.id.moreinfobtn);

        }
    }
}




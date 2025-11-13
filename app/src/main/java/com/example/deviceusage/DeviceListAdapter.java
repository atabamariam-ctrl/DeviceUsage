package com.example.deviceusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {

    Context context;
    ArrayList<DevicesItem> DeviceList;
    private OnItemClickListener itemClickListener;
    private FirebaseServices fbs;

    public DeviceListAdapter(Context context, ArrayList<DevicesItem> DeviceList) {
        this.context = context;
        this.DeviceList = DeviceList;
        this.fbs = FirebaseServices.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DevicesItem Device = DeviceList.get(position);


        // ======== النصوص ===========
        holder.Name.setText(Device.getName());
        holder.Model.setText(Device.getModel());
        holder.Year.setText(Device.getYear());
        holder.Type.setText(Device.getType());







        // ======== النقر على العنصر ============
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return DeviceList.size();
    }

    // ========= المفضلة ========






    // ========= ViewHolder ===========
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Model, Year, Type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.tvName);
            Model = itemView.findViewById(R.id.tvModel);
            Year = itemView.findViewById(R.id.tvDate);
            Type = itemView.findViewById(R.id.tvType);

        }
    }

    // ========= Interface for onClick ========
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
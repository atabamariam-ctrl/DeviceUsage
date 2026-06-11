package com.example.deviceusage;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<DevicesItem> deviceList;
    private FirebaseServices fbs;

    public DeviceListAdapter(Context context, ArrayList<DevicesItem> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
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
        DevicesItem device = deviceList.get(position);

        holder.tvName.setText(device.getName());
        holder.tvType.setText("Type: " + device.getType());
        holder.tvBrandModel.setText("Brand/Model: " + device.getBrand() + " " + device.getModel());
        holder.tvBattery.setText("Battery: " + device.getBatteryLevel() + "%");
        holder.tvUsage.setText("Usage: " + device.getUsageHours() + " hours");
        holder.tvStatus.setText("Status: " + device.getStatus());

        if (device.getNotes() == null || device.getNotes().trim().isEmpty()) {
            holder.tvNotes.setText("Notes: No notes");
        } else {
            holder.tvNotes.setText("Notes: " + device.getNotes());
        }

        if (device.getStatus() != null) {
            if (device.getStatus().equalsIgnoreCase("Safe")) {
                holder.tvStatus.setTextColor(Color.parseColor("#16A34A"));
            } else if (device.getStatus().equalsIgnoreCase("Warning")) {
                holder.tvStatus.setTextColor(Color.parseColor("#CA8A04"));
            } else if (device.getStatus().equalsIgnoreCase("Dangerous")) {
                holder.tvStatus.setTextColor(Color.parseColor("#DC2626"));
            }
        }

        if (device.getPhoto() != null && !device.getPhoto().trim().isEmpty()) {
            Picasso.get()
                    .load(device.getPhoto())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivDevice);
        } else {
            holder.ivDevice.setImageResource(R.mipmap.ic_launcher);
        }

        if (device.isFavorite()) {
            holder.ivFavourite.setImageResource(R.drawable.favcheck);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.ic_fav);
        }

        holder.ivFavourite.setOnClickListener(v -> {
            if (device.getId() == null) {
                Toast.makeText(context, "Device ID missing", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean newFavoriteValue = !device.isFavorite();
            device.setFavorite(newFavoriteValue);

            fbs.getFire()
                    .collection("device")
                    .document(device.getId())
                    .update("favorite", newFavoriteValue)
                    .addOnSuccessListener(unused -> {
                        if (newFavoriteValue) {
                            holder.ivFavourite.setImageResource(R.drawable.favcheck);
                            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            holder.ivFavourite.setImageResource(R.drawable.ic_fav);
                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        device.setFavorite(!newFavoriteValue);
                        Toast.makeText(context, "Favorite update failed", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvType, tvBrandModel, tvBattery, tvUsage, tvStatus, tvNotes;
        ImageView ivDevice, ivFavourite;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvBrandModel = itemView.findViewById(R.id.tvBrandModel);
            tvBattery = itemView.findViewById(R.id.tvBattery);
            tvUsage = itemView.findViewById(R.id.tvUsage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNotes = itemView.findViewById(R.id.tvNotes);

            ivDevice = itemView.findViewById(R.id.ivDevice);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
        }
    }
}
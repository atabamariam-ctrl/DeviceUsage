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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DevicesItem> list;
    private OnItemClickListener listener;
    private FirebaseServices fbs;

    public MyAdapter(Context context, ArrayList<DevicesItem> list) {
        this.context = context;
        this.list = list;
        this.fbs = FirebaseServices.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DevicesItem item = list.get(position);

        holder.tvName.setText(item.getName());
        holder.tvType.setText("Type: " + item.getType());
        holder.tvBrandModel.setText("Brand/Model: " + item.getBrand() + " " + item.getModel());
        holder.tvBattery.setText("Battery: " + item.getBatteryLevel() + "%");
        holder.tvUsage.setText("Usage: " + item.getUsageHours() + " hours");
        holder.tvStatus.setText("Status: " + item.getStatus());

        if (item.getNotes() == null || item.getNotes().trim().isEmpty()) {
            holder.tvNotes.setText("Notes: No notes");
        } else {
            holder.tvNotes.setText("Notes: " + item.getNotes());
        }

        if (item.getStatus() != null) {
            if (item.getStatus().equalsIgnoreCase("Safe")) {
                holder.tvStatus.setTextColor(Color.parseColor("#16A34A"));
            } else if (item.getStatus().equalsIgnoreCase("Warning")) {
                holder.tvStatus.setTextColor(Color.parseColor("#CA8A04"));
            } else if (item.getStatus().equalsIgnoreCase("Dangerous")) {
                holder.tvStatus.setTextColor(Color.parseColor("#DC2626"));
            }
        }

        if (item.getPhoto() == null || item.getPhoto().trim().isEmpty()) {
            holder.ivDevice.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get()
                    .load(item.getPhoto())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.ivDevice);
        }

        if (item.isFavorite()) {
            holder.ivFavourite.setImageResource(R.drawable.favcheck);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.ic_fav);
        }

        holder.ivFavourite.setOnClickListener(v -> setFavourite(holder, item));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    private void setFavourite(@NonNull ViewHolder holder, DevicesItem device) {
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvType, tvBrandModel, tvBattery, tvUsage, tvStatus, tvNotes;
        ImageView ivDevice, ivFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvBrandModel = itemView.findViewById(R.id.tvBrandModel);
            tvBattery = itemView.findViewById(R.id.tvBattery);
            tvUsage = itemView.findViewById(R.id.tvUsage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNotes = itemView.findViewById(R.id.tvNotes);

            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            ivDevice = itemView.findViewById(R.id.ivDevice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
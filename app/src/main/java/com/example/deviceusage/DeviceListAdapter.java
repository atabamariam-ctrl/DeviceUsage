package com.example.deviceusage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
        DevicesItem device = DeviceList.get(position);
        User user = fbs.getCurrentUser();

        holder.Name.setText(device.getName());
        holder.Model.setText(device.getModel());
        holder.Brand.setText(device.getBrand());
        holder.Type.setText(device.getType());

        Picasso.get().load(R.drawable.ic_profile).into(holder.ivDevice);

        if (user != null && user.getFavorites() != null &&
                device.getId() != null &&
                user.getFavorites().contains(device.getId())) {
            Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
        } else {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
        }

        holder.ivFavourite.setOnClickListener(v -> {
            Toast.makeText(context, "Heart clicked", Toast.LENGTH_SHORT).show();

            User u = fbs.getCurrentUser();

            if (u == null) {
                Toast.makeText(context, "Login first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (device.getId() == null) {
                Toast.makeText(context, "Device ID missing", Toast.LENGTH_SHORT).show();
                return;
            }

            if (u.getFavorites() == null) {
                u.setFavorites(new ArrayList<>());
            }

            if (u.getFavorites().contains(device.getId())) {
                u.getFavorites().remove(device.getId());
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                u.getFavorites().add(device.getId());
                Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }

            fbs.updateUser(u);
        });

        holder.itemView.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return DeviceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Model, Brand, Type;
        ImageView ivFavourite, ivDevice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.tvName);
            Model = itemView.findViewById(R.id.tvModel);
            Brand = itemView.findViewById(R.id.tvBrand);
            Type = itemView.findViewById(R.id.tvType);
            ivFavourite = itemView.findViewById(R.id.ivFavourite);
            ivDevice = itemView.findViewById(R.id.ivDevice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
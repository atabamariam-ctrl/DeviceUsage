package com.example.deviceusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    Context context;
    ArrayList<DevicesItem> list;
    private OnItemClickListener listener;

    public MyAdapter(Context context, ArrayList<DevicesItem> list) {
        this.context = context;
        this.list = list;
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

        holder.name.setText(item.getName());
        holder.model.setText(item.getModel());
        holder.Brand.setText(item.getYear());
        holder.type.setText(item.getType());
        // إذا أردت عرض الصورة لاحقاً
        holder.name.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position);
    }

        });
        if (device.getPhoto() == null || device.getPhoto().isEmpty())
        {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivDevice);
        }
        else {
            Picasso.get().load(Device.getPhoto()).into(holder.ivDevice);
        }
        holder.ivFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavourite(holder, device);
            }
        });
    }
    private void setFavourite(@NonNull MyViewHolder holder, Device device) {
        /*
        if (isUserFavourite(device) == true)
        {
            holder.ivFavourite.setBackgroundResource(R.drawable.ic_fav);
        }
        else
        {

        } */
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, model, Brand , type;
        ImageView ivDevice, ivFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvDeviceName_deviceListFragment);
            model = itemView.findViewById(R.id.tvModel_deviceListFragment);
            Brand = itemView.findViewById(R.id.tvBrand_deviceListFragment);
            type = itemView.findViewById(R.id.tvType_deviceListFragment);
            ivFavourite = itemView.findViewById(R.id.ivFavoriteIcon);
            ivDevice = itemView.findViewById(R.id.ivDevicePhotoItem);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
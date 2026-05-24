package com.example.deviceusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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
        holder.Brand.setText(item.getBrand());
        holder.type.setText(item.getType());

        holder.name.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

        if (item.getPhoto() == null || item.getPhoto().isEmpty()) {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivDevice);
        } else {
            Picasso.get().load(item.getPhoto()).into(holder.ivDevice);
        }

        holder.ivFavourite.setOnClickListener(v -> {
            setFavourite(holder, item);
        });
    }

    private void setFavourite(@NonNull ViewHolder holder, DevicesItem device) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, model, Brand, type;
        ImageView ivDevice, ivFavourite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            model = itemView.findViewById(R.id.tvModel);
            Brand = itemView.findViewById(R.id.tvBrand);
            type = itemView.findViewById(R.id.tvType);
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
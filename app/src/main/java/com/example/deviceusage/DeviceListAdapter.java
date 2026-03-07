package com.example.deviceusage;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.MyViewHolder> {

    Context context;
    ArrayList<DevicesItem> DeviceList;
    private DeviceListAdapter.OnItemClickListener itemClickListener;
    private FirebaseServices fbs;

    public DeviceListAdapter(Context context, ArrayList<DevicesItem> DeviceList) {
        this.context = context;
        this.DeviceList = DeviceList;
        this.fbs = FirebaseServices.getInstance();
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*
                String selectedItem = filteredList.get(position).getNameDevice();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show(); */
                Bundle args = new Bundle();
                args.putParcelable("Device", DeviceList.get(position)); // or use Parcelable for better performance
                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot,cd);
                ft.commit();
            }
        } ;
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
        holder.Brand.setText(Device.getBrand());
        holder.Type.setText(Device.getType());

        // ======== النقر على العنصر ============
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null)
                itemClickListener.onItemClick(position);
        });
        /*
        holder.carName.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.setOnItemClick(position);
            }
        }); */
        if (device.getPhoto() == null .getPhoto().isEmpty());
        {
            Picasso.get().load(R.drawable.ic_fav).into(holder.ivDevice); }
        else {
            Picasso.get().load(device.getPhoto()).into(holder.ivDevice);
        }
    }

    @Override
    public int getItemCount() {
        return DeviceList.size();
    }

    // ========= المفضلة ========






    // ========= ViewHolder ===========
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Model, Brand, Type;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.tvName);
            Model = itemView.findViewById(R.id.tvModel);
            Brand = itemView.findViewById(R.id.tvBrand);
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
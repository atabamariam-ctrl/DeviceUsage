package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseServices fbs;
    private DeviceListAdapter myAdapter;
    private SearchView srchView;
    private ArrayList<DevicesItem> devices, filteredList;

    public FavoriteFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.rvDevicelist);
        fbs = FirebaseServices.getInstance();

        devices = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        devices = getDevices();
        myAdapter = new DeviceListAdapter(getActivity(), devices);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = devices.get(position).getName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putString("deviceName", devices.get(position).getName());

                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot, cd);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        srchView = getView().findViewById(R.id.srchViewfavoritefragment);
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void applyFilter(String query) {
        if (query.trim().isEmpty()) {
            myAdapter = new DeviceListAdapter(getContext(), devices);
            recyclerView.setAdapter(myAdapter);
            return;
        }

        filteredList.clear();

        for (DevicesItem device : devices) {
            if (device.getType().toLowerCase().contains(query.toLowerCase()) ||
                    device.getModel().toLowerCase().contains(query.toLowerCase()) ||
                    device.getName().toLowerCase().contains(query.toLowerCase()) ||
                    device.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    device.getPhoto().toLowerCase().contains(query.toLowerCase()) ||
                    device.getPhone().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(device);
            }
        }

        if (filteredList.size() == 0) {
            showNoDataDialogue();
            return;
        }

        myAdapter = new DeviceListAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = filteredList.get(position).getName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putString("deviceName", filteredList.get(position).getName());

                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot, cd);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    private void showNoDataDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("No Results");
        builder.setMessage("Try again!");
        builder.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    public ArrayList<DevicesItem> getDevices() {
        ArrayList<DevicesItem> devices = new ArrayList<>();

        try {
            devices.clear();
            fbs.getFire().collection("device")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                User u = fbs.getCurrentUser();

                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    DevicesItem device = document.toObject(DevicesItem.class);

                                    device.setId(document.getId());

                                    if (u != null &&
                                            u.getFavorites() != null &&
                                            u.getFavorites().contains(device.getId())) {

                                        devices.add(device);
                                    }
                                }

                                DeviceListAdapter adapter =
                                        new DeviceListAdapter(getActivity(), devices);

                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("getDevices(): ", e.getMessage());
        }

        return devices;
    }

    @Override
    public void onPause() {
        super.onPause();

        User u = fbs.getCurrentUser();
        if (u != null)
            fbs.updateUser(u);
    }
}
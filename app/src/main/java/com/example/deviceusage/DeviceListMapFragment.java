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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DeviceListMapFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView ivProfile;
    private FirebaseServices fbs;
    private DeviceListAdapter myAdapter;
    private SearchView srchView;
    private ArrayList<DevicesItem> Devices, filteredList;

    public DeviceListMapFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);

        recyclerView = getView().findViewById(R.id.rvDevicelistMap);
        ivProfile = getView().findViewById(R.id.ivProfileDeviceListMapFragment);
        fbs = FirebaseServices.getInstance();

        Devices = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new DeviceListAdapter(getActivity(), Devices);
        recyclerView.setAdapter(myAdapter);

        loadDevices();

        srchView = getView().findViewById(R.id.srchViewDeviceListMapFragment);
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilter(newText);
                return false;
            }
        });

        ivProfile.setOnClickListener(v -> gotoProfileFragment());
    }

    private void loadDevices() {
        Devices.clear();

        fbs.getFire().collection("device")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DevicesItem device = document.toObject(DevicesItem.class);
                                device.setId(document.getId());
                                if (device.getUserId() != null &&
                                        device.getUserId().equals(fbs.getAuth().getCurrentUser().getUid())) {

                                    Devices.add(device);
                                }
                            }

                            myAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Failed to load devices", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void applyFilter(String query) {
        if (query.trim().isEmpty()) {
            myAdapter = new DeviceListAdapter(getContext(), Devices);
            recyclerView.setAdapter(myAdapter);
            return;
        }

        filteredList.clear();

        for (DevicesItem device : Devices) {
            if (device.getName().toLowerCase().contains(query.toLowerCase()) ||
                    device.getModel().toLowerCase().contains(query.toLowerCase()) ||
                    device.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    device.getType().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(device);
            }
        }

        myAdapter = new DeviceListAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);
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

        return inflater.inflate(R.layout.fragment_device_list_map, container, false);
    }

    public void gotoProfileFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot, new ProfileFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
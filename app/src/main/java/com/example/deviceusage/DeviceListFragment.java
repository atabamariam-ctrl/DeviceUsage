package com.example.deviceusage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private DeviceListAdapter adapter;
    private FirebaseServices fbs;

    private ArrayList<DevicesItem> DeviceList = new ArrayList<>();
    private ArrayList<DevicesItem> filteredList = new ArrayList<>();

    public DeviceListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        loadDeviceFromFirebase();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.rvDeviceList);
        searchView = getView().findViewById(R.id.srchViewDevice);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        fbs = FirebaseServices.getInstance();

        adapter = new DeviceListAdapter(getActivity(), DeviceList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(position -> {
            Toast.makeText(getActivity(),
                    "Clicked: " + DeviceList.get(position).getName(),
                    Toast.LENGTH_SHORT).show();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                applyFilter(newText);
                return true;
            }
        });
    }

    private void loadDeviceFromFirebase() {
        fbs.getFirestore().collection("device" )

                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DeviceList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            DevicesItem sm = doc.toObject(DevicesItem.class);
                            DeviceList.add(sm);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void applyFilter(String query) {
        if (query.trim().isEmpty()) {
            adapter = new DeviceListAdapter(getActivity(), DeviceList);
            recyclerView.setAdapter(adapter);
            return;
        }

        filteredList.clear();

        for (DevicesItem sm : DeviceList) {
            if (sm.getName().toLowerCase().contains(query.toLowerCase()) ||
                    sm.getModel().toLowerCase().contains(query.toLowerCase()) ||
                    sm.getType().toLowerCase().contains(query.toLowerCase()) ||
                    sm.getYear().toLowerCase().contains(query.toLowerCase())
            ) {
                filteredList.add(sm);
            }
        }

        adapter = new DeviceListAdapter(getActivity(), filteredList);
        recyclerView.setAdapter(adapter);
    }
}
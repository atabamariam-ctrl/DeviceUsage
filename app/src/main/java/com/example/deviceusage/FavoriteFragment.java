package com.example.deviceusage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private ArrayList<DevicesItem> devices;
    private ArrayList<DevicesItem> filteredList;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.VISIBLE);

        fbs = FirebaseServices.getInstance();

        recyclerView = getView().findViewById(R.id.rvDevicelist);
        srchView = getView().findViewById(R.id.srchViewfavoritefragment);

        devices = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myAdapter = new DeviceListAdapter(getActivity(), devices);
        recyclerView.setAdapter(myAdapter);

        loadFavoriteDevices();

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
    }

    private void loadFavoriteDevices() {
        devices.clear();

        fbs.getFire()
                .collection("device")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            devices.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DevicesItem device = document.toObject(DevicesItem.class);
                                device.setId(document.getId());

                                boolean sameUser = true;

                                if (fbs.getAuth().getCurrentUser() != null) {
                                    sameUser = device.getUserId() != null &&
                                            device.getUserId().equals(fbs.getAuth().getCurrentUser().getUid());
                                }

                                if (sameUser && device.isFavorite()) {
                                    devices.add(device);
                                }
                            }

                            myAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(),
                                    "Failed to load favorites",
                                    Toast.LENGTH_SHORT).show();
                        }
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
            String name = device.getName() == null ? "" : device.getName();
            String model = device.getModel() == null ? "" : device.getModel();
            String brand = device.getBrand() == null ? "" : device.getBrand();
            String type = device.getType() == null ? "" : device.getType();

            if (name.toLowerCase().contains(query.toLowerCase()) ||
                    model.toLowerCase().contains(query.toLowerCase()) ||
                    brand.toLowerCase().contains(query.toLowerCase()) ||
                    type.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(device);
            }
        }

        myAdapter = new DeviceListAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);
    }
}
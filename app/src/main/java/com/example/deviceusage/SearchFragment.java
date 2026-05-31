package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private Button clearButton;
    private RecyclerView recyclerView;

    private FirebaseServices fbs;
    private DeviceListAdapter adapter;

    private ArrayList<DevicesItem> list;
    private ArrayList<DevicesItem> filteredList;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.VISIBLE);

        fbs = FirebaseServices.getInstance();

        searchView = getView().findViewById(R.id.searchViewSearchFragment);
        clearButton = getView().findViewById(R.id.clearButton);
        recyclerView = getView().findViewById(R.id.rvSearchFragment);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DeviceListAdapter(getActivity(), filteredList);
        recyclerView.setAdapter(adapter);

        loadDevices();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        clearButton.setOnClickListener(v -> {
            searchView.setQuery("", false);
            filteredList.clear();
            filteredList.addAll(list);
            adapter.notifyDataSetChanged();
        });
    }

    private void loadDevices() {
        list.clear();
        filteredList.clear();

        fbs.getFire().collection("device")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DevicesItem device = document.toObject(DevicesItem.class);
                                device.setId(document.getId());
                                list.add(device);
                            }

                            filteredList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filter(String text) {
        filteredList.clear();

        if (text.trim().isEmpty()) {
            filteredList.addAll(list);
        } else {
            for (DevicesItem device : list) {
                if (device.getName().toLowerCase().contains(text.toLowerCase()) ||
                        device.getModel().toLowerCase().contains(text.toLowerCase()) ||
                        device.getBrand().toLowerCase().contains(text.toLowerCase()) ||
                        device.getType().toLowerCase().contains(text.toLowerCase())) {

                    filteredList.add(device);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}
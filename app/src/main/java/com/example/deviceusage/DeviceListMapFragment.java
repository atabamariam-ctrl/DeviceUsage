package com.example.deviceusage;

import static android.widget.SearchView.*;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DeviceListMapFragment() {
    }

    public static DeviceListMapFragment newInstance(String param1, String param2) {
        DeviceListMapFragment fragment = new DeviceListMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recyclerView = getView().findViewById(R.id.rvDevicelistMap);
        ivProfile = getView().findViewById(R.id.ivProfileDeviceListMapFragment);
        fbs = FirebaseServices.getInstance();
        fbs.setUserChangeFlag(false);

        Devices = new ArrayList<>();
        filteredList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Devices = getDevices();

        myAdapter = new DeviceListAdapter(getActivity(), Devices);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedItem = Devices.get(position).getName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();

                Bundle args = new Bundle();
                args.putString("deviceName", Devices.get(position).getName());

                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot, cd);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        srchView = getView().findViewById(R.id.srchViewDeviceListMapFragment);
        srchView.setOnQueryTextListener(new OnQueryTextListener() {
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

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileFragment();
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
            if (device.getModel().toLowerCase().contains(query.toLowerCase()) ||
                    device.getName().toLowerCase().contains(query.toLowerCase()) ||
                    device.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    device.getType().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(device);
            }
        }

        if (filteredList.size() == 0) {
            showNoDataDialogue();
            return;
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
        ft.commit();
    }

    public ArrayList<DevicesItem> getDevices() {
        ArrayList<DevicesItem> Devices = new ArrayList<>();

        try {
            Devices.clear();
            fbs.getFire().collection("device")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    User u = fbs.getCurrentUser();

                                    DevicesItem device = document.toObject(DevicesItem.class);

                                    device.setId(document.getId());

                                    if (u != null &&
                                            u.getFavorites() != null &&
                                            u.getFavorites().contains(device.getId())) {

                                        Devices.add(device);
                                    }
                                }

                                DeviceListAdapter adapter = new DeviceListAdapter(getActivity(), Devices);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("getDevices(): ", e.getMessage());
        }

        return Devices;
    }

    @Override
    public void onPause() {
        super.onPause();
        User u = fbs.getCurrentUser();
        if (u != null && fbs.isUserChangeFlag())
            fbs.updateUser(u);
    }

}
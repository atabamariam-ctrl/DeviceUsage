package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class DeviceListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseServices fbs;
    private MyAdapter myAdapter;
    private ArrayList<Device> list, filteredList;
    private FloatingActionButton btnAdd;
    private SearchView srchView;
    private Button favIcon;
    private Map<String, Device> devicesMap;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceListFragment newInstance(String param1, String param2) {
        DeviceListFragment fragment = new DeviceListFragment();
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
        recyclerView = getView().findViewById(R.id.rvDevicelist);
        btnAdd = getView().findViewById(R.id.floatingButtonAddDeviceList);
        fbs = FirebaseServices.getInstance();
        //carsMap = new HashMap<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        filteredList = new ArrayList<>();
        //carsMap = getCarsMap();
        myAdapter = new MyAdapter(getActivity(), list );
        recyclerView.setAdapter(myAdapter);
        favIcon = getView().findViewById(R.id.ivFavoriteIcon);


        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = list.get(position).getDeviceName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("car", list.get(position)); // or use Parcelable for better performance
                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot,cd);
                ft.commit();
            }
        });

        fbs.getFire().collection("devices").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    Device device= dataSnapshot.toObject(Device.class);
                    list.add(device);
                }


                myAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        srchView = getView().findViewById(R.id.srchViewDeviceListFragment);
        srchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                applyFilter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //applyFilter(newText);
                return false;
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddDeviceFragment();
            }
        });
        btnAdd.setVisibility(View.INVISIBLE); // currently hidden

        ((MainActivity)getActivity()).pushFragment(new DeviceListFragment());
    }

    private void applyFilter(String query) {
        // TODO: add onBackspace - old and new query
        if (query.trim().isEmpty())
        {
            myAdapter = new MyAdapter(getContext(), list);
            recyclerView.setAdapter(myAdapter);
            //myAdapter.notifyDataSetChanged();
            return;
        }
        filteredList.clear();
        for(Device device : list)
        {
            if (device.getModel().toLowerCase().contains(query.toLowerCase()) ||
                    device.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                    device.getDeviceName().toLowerCase().contains(query.toLowerCase()) ||
                    device.getDeviceType().toLowerCase().contains(query.toLowerCase())
                   )
            {
                filteredList.add(device);
            }
        }
        if (filteredList.size() == 0)
        {
            showNoDataDialogue();
            return;
        }
        myAdapter = new MyAdapter(getContext(), filteredList);
        recyclerView.setAdapter(myAdapter);
        myAdapter= new MyAdapter(getActivity(),filteredList);
        recyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = filteredList.get(position).getDeviceName();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("device", filteredList.get(position)); // or use Parcelable for better performance
                DeviceDetailsFragment cd = new DeviceDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.framelayot,cd);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_list, container, false);
    }

    public void gotoAddDeviceFragment() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new AddDeviceFragment());
        ft.commit();
    }

    /*
    public Map<String, Device> getDeviceMap()
    {
        Map<String, Device> devices = new HashMap<>();

        try {
            device.clear();
            fbs.getFire().collection("devices")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    device.put(document.getId(), document.toObject(Device.class));
                                }

                                DeviceListAdapter adapter = new DeviceListAdapter2(getActivity(), devices);
                                recyclerView.setAdapter(adapter);
                                //addUserToCompany(companies, user);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getCompaniesMap(): ", e.getMessage());
        }

        return device;
    } */
}
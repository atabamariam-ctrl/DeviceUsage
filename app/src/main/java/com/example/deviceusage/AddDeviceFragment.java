package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class AddDeviceFragment extends Fragment {

    private EditText etDeviceName;
    private EditText etBatteryLevel;
    private EditText etUsageHours;
    private EditText etDeviceStatus;
    private EditText etNotes;

    private Button btnAddDevice;

    private FirebaseServices fbs;

    public AddDeviceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_device,
                container,
                false);
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.VISIBLE);

        init();
    }

    private void init() {

        fbs = FirebaseServices.getInstance();

        etDeviceName =
                getView().findViewById(R.id.etDeviceNameAddFragment);

        etBatteryLevel =
                getView().findViewById(R.id.etBatteryLevelAddFragment);

        etUsageHours =
                getView().findViewById(R.id.etUsageHoursAddFragment);

        etDeviceStatus =
                getView().findViewById(R.id.etDeviceStatusAddFragment);

        etNotes =
                getView().findViewById(R.id.etNotesAddFragment);

        btnAddDevice =
                getView().findViewById(R.id.btnAddDevice);

        btnAddDevice.setOnClickListener(v -> addDevice());
    }

    private void addDevice() {

        String name =
                etDeviceName.getText().toString().trim();

        String battery =
                etBatteryLevel.getText().toString().trim();

        String usage =
                etUsageHours.getText().toString().trim();

        String status =
                etDeviceStatus.getText().toString().trim();

        String notes =
                etNotes.getText().toString().trim();

        if (name.isEmpty() ||
                battery.isEmpty() ||
                usage.isEmpty() ||
                status.isEmpty() ||
                notes.isEmpty()) {

            Toast.makeText(getActivity(),
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        DevicesItem device =
                new DevicesItem(
                        name,
                        usage,
                        status,
                        notes,
                        battery,
                        ""
                );

        fbs.getFire()
                .collection("device")
                .add(device)

                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Toast.makeText(getActivity(),
                                "Device Added Successfully",
                                Toast.LENGTH_SHORT).show();

                        gotoDeviceListFragment();
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(),
                                e.getMessage(),
                                Toast.LENGTH_LONG).show();

                        Log.e("Firestore Error",
                                e.getMessage());
                    }
                });
    }

    private void gotoDeviceListFragment() {

        FragmentTransaction ft =
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        ft.replace(R.id.framelayot,
                new DeviceListMapFragment());

        ft.addToBackStack(null);

        ft.commit();
    }
}
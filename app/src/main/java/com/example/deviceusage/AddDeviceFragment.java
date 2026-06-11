package com.example.deviceusage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class AddDeviceFragment extends Fragment {

    private EditText etDeviceName;
    private EditText etDeviceType;
    private EditText etBrand;
    private EditText etModel;
    private EditText etPhone;
    private EditText etBatteryLevel;
    private EditText etUsageHours;
    private EditText etNotes;

    private Button btnAddDevice;
    private FirebaseServices fbs;

    public AddDeviceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_device, container, false);
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

        etDeviceName = getView().findViewById(R.id.etDeviceNameAddFragment);
        etDeviceType = getView().findViewById(R.id.etDeviceTypeAddFragment);
        etBrand = getView().findViewById(R.id.etBrandAddFragment);
        etModel = getView().findViewById(R.id.etModelAddFragment);
        etPhone = getView().findViewById(R.id.etPhoneAddFragment);
        etBatteryLevel = getView().findViewById(R.id.etBatteryLevelAddFragment);
        etUsageHours = getView().findViewById(R.id.etUsageHoursAddFragment);
        etNotes = getView().findViewById(R.id.etNotesAddFragment);

        btnAddDevice = getView().findViewById(R.id.btnAddDevice);

        btnAddDevice.setOnClickListener(v -> addDevice());
    }

    private void addDevice() {
        String name = etDeviceName.getText().toString().trim();
        String type = etDeviceType.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String model = etModel.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String batteryLevel = etBatteryLevel.getText().toString().trim();
        String usageHours = etUsageHours.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() ||
                type.isEmpty() ||
                brand.isEmpty() ||
                model.isEmpty() ||
                phone.isEmpty() ||
                batteryLevel.isEmpty() ||
                usageHours.isEmpty()) {

            Toast.makeText(getActivity(),
                    "Please fill all required fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String status = calculateStatus(batteryLevel, usageHours);

        if (status == null) {
            Toast.makeText(getActivity(),
                    "Battery level and usage hours must be valid numbers",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Device device = new Device(
                name,
                type,
                brand,
                model,
                phone,
                batteryLevel,
                usageHours,
                status,
                notes,
                "",
                false
        );

        if (fbs.getAuth().getCurrentUser() != null) {
            device.setUserId(fbs.getAuth().getCurrentUser().getUid());
        } else {
            Toast.makeText(getActivity(),
                    "Please login first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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

                        Log.e("Firestore Error", e.getMessage());
                    }
                });
    }

    private String calculateStatus(String batteryLevel, String usageHours) {
        try {
            int battery = Integer.parseInt(batteryLevel);
            double usage = Double.parseDouble(usageHours);

            if (battery < 0 || battery > 100 || usage < 0) {
                return null;
            }

            if (battery <= 15 || usage >= 8) {
                return "Dangerous";
            } else if (battery <= 30 || usage >= 5) {
                return "Warning";
            } else {
                return "Safe";
            }

        } catch (Exception e) {
            return null;
        }
    }

    private void gotoDeviceListFragment() {
        FragmentTransaction ft =
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        ft.replace(R.id.framelayot, new DeviceListMapFragment());
        ft.commit();
    }
}
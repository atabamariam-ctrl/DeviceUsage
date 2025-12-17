package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;


public class AddDevice extends Fragment {
    private EditText editDeviceName, editDeviceType, editModel, editBrand;
    private Button btnAddDevice;
    private FirebaseFirestore db;
    ImageView img;
    private String imageStr;
    private Utils utils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_device, container, false);

        editDeviceName = view.findViewById(R.id.editDeviceName);
        editDeviceType = view.findViewById(R.id.editDeviceType);
        editModel = view.findViewById(R.id.editModel);
        editBrand = view.findViewById(R.id.editBrand);
        btnAddDevice = view.findViewById(R.id.btnAddDevice);

        db = FirebaseFirestore.getInstance();

        btnAddDevice.setOnClickListener(v -> addDevice());

        return view;
    }

    private void addDevice() {
        String name = editDeviceName.getText().toString().trim();
        String type = editDeviceType.getText().toString().trim();
        String model = editModel.getText().toString().trim();
        String brand = editBrand.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(type) ||
                TextUtils.isEmpty(model) || TextUtils.isEmpty(brand)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Device device = new Device(name, type, model, brand);

        db.collection("devices")
                .add(device)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Device added successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}


package com.example.deviceusage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DeviceDetailsFragment extends Fragment {

    private TextView tvName;
    private TextView tvBattery;
    private TextView tvUsage;
    private TextView tvStatus;

    private ImageView ivDevice;

    private Button btnMonitor;
    private Button btnOptimize;
    private Button btnStatus;

    private DevicesItem myDevice;

    public DeviceDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_device_details,
                container,
                false);
    }

    @Override
    public void onStart() {
        super.onStart();

        init();
    }

    private void init() {

        tvName =
                getView().findViewById(R.id.tvNameDetailsFragment);

        tvBattery =
                getView().findViewById(R.id.tvBatteryDetailsFragment);

        tvUsage =
                getView().findViewById(R.id.tvUsageDetailsFragment);

        tvStatus =
                getView().findViewById(R.id.tvStatusDetailsFragment);

        ivDevice =
                getView().findViewById(R.id.ivDeviceDetailsFragment);

        btnMonitor =
                getView().findViewById(R.id.btnSMS);

        btnOptimize =
                getView().findViewById(R.id.btnWhatsApp);

        btnStatus =
                getView().findViewById(R.id.btnCall);

        Bundle args = getArguments();

        if (args != null) {

            myDevice =
                    (DevicesItem) args.getSerializable("device");

            if (myDevice != null) {

                tvName.setText(myDevice.getName());

                tvBattery.setText(
                        "Battery Level: " + myDevice.getPhoto() + "%");

                tvUsage.setText(
                        "Daily Usage: " + myDevice.getModel() + " Hours");

                tvStatus.setText(
                        "Status: " + myDevice.getBrand());

                Picasso.get()
                        .load(R.drawable.ic_profile)
                        .into(ivDevice);
            }
        }

        btnMonitor.setOnClickListener(v -> {
            Toast.makeText(getActivity(),
                    "Monitoring device...",
                    Toast.LENGTH_SHORT).show();
        });

        btnOptimize.setOnClickListener(v -> {
            Toast.makeText(getActivity(),
                    "Optimization started",
                    Toast.LENGTH_SHORT).show();
        });

        btnStatus.setOnClickListener(v -> {
            Toast.makeText(getActivity(),
                    "Device status checked",
                    Toast.LENGTH_SHORT).show();
        });
    }
}
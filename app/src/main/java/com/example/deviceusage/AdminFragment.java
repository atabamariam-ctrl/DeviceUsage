package com.example.deviceusage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AdminFragment extends Fragment {

    private Button btnHome, btnAddDevice, btnSearch, btnFavorites, btnProfile;

    public AdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);

        btnHome = getView().findViewById(R.id.btnAdminHome);
        btnAddDevice = getView().findViewById(R.id.btnAdminAddDevice);
        btnSearch = getView().findViewById(R.id.btnAdminSearch);
        btnFavorites = getView().findViewById(R.id.btnAdminFavorites);
        btnProfile = getView().findViewById(R.id.btnAdminProfile);

        btnHome.setOnClickListener(v -> openFragment(new DeviceListMapFragment()));
        btnAddDevice.setOnClickListener(v -> openFragment(new AddDeviceFragment()));
        btnSearch.setOnClickListener(v -> openFragment(new SearchFragment()));
        btnFavorites.setOnClickListener(v -> openFragment(new FavoriteFragment()));
        btnProfile.setOnClickListener(v -> openFragment(new ProfileFragment()));
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        ft.replace(R.id.framelayot, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
package com.example.deviceusage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etAddress, etPhone;
    private Button btnUpdate;
    private ImageView ivUser;
    private FirebaseServices fbs;
    private Utils utils;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        utils = Utils.getInstance();

        etFirstName = getView().findViewById(R.id.etFirstnameUserDetailsEdit);
        etLastName = getView().findViewById(R.id.etLastnameUserDetailsEdit);
        etAddress = getView().findViewById(R.id.etAddressUserDetailsEdit);
        etPhone = getView().findViewById(R.id.etPhoneUserDetailsEdit);

        ivUser = getView().findViewById(R.id.ivUserDetailsEdit);
        btnUpdate = getView().findViewById(R.id.btnUpdateUserDetailsEdit);

        Picasso.get()
                .load(R.drawable.ic_profile)
                .into(ivUser);

        fillUserData();

        btnUpdate.setOnClickListener(v -> updateProfile());
    }

    private void fillUserData() {
        User current = fbs.getCurrentUser();

        if (current == null) {
            return;
        }

        etFirstName.setText(current.getFirstName());
        etLastName.setText(current.getLastName());
        etAddress.setText(current.getAddress());
        etPhone.setText(current.getPhone());

        if (current.getPhoto() != null && !current.getPhoto().isEmpty()) {
            Picasso.get()
                    .load(current.getPhoto())
                    .into(ivUser);
        }
    }

    private void updateProfile() {
        String firstname = etFirstName.getText().toString().trim();
        String lastname = etLastName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (firstname.isEmpty() ||
                lastname.isEmpty() ||
                address.isEmpty() ||
                phone.isEmpty()) {

            Toast.makeText(getActivity(),
                    "Some fields are empty",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        User current = fbs.getCurrentUser();

        if (current == null) {
            Toast.makeText(getActivity(),
                    "No user logged in",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        current.setFirstName(firstname);
        current.setLastName(lastname);
        current.setAddress(address);
        current.setPhone(phone);

        fbs.updateUser(current);

        Toast.makeText(getActivity(),
                "Profile updated successfully",
                Toast.LENGTH_SHORT).show();
    }
}
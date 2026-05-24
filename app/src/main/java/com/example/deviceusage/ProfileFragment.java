package com.example.deviceusage;

import android.net.Uri;
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
    EditText etFirstName, etLastName, etAddress, etPhone;
    private Button btnUpdate;
    private ImageView ivUser;
    private FirebaseServices fbs;
    private Utils utils;
    private String imageStr;
    private boolean flagAlreadyFilled = false;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        fbs = FirebaseServices.getInstance();

        etFirstName = getView().findViewById(R.id.etFirstnameUserDetailsEdit);
        etLastName = getView().findViewById(R.id.etLastnameUserDetailsEdit);
        etAddress = getView().findViewById(R.id.etAddressUserDetailsEdit);
        etPhone = getView().findViewById(R.id.etPhoneUserDetailsEdit);

        ivUser = getView().findViewById(R.id.ivUserDetailsEdit);
        btnUpdate = getView().findViewById(R.id.btnUpdateUserDetailsEdit);

        utils = Utils.getInstance();

        if (imageStr == null) {
            Picasso.get().load(R.drawable.ic_profile).into(ivUser);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();

                if (firstname.trim().isEmpty() || lastname.trim().isEmpty() || address.trim().isEmpty() ||
                        phone.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                User current = fbs.getCurrentUser();

                if (current != null) {
                    User user = new User(
                            firstname,
                            lastname,
                            fbs.getAuth().getCurrentUser().getEmail(),
                            address,
                            phone,
                            current.getPhoto()
                    );

                    fbs.updateUser(user);
                    utils.showMessageDialog(getActivity(), "Data updated succesfully!");
                    fbs = FirebaseServices.getInstance();
                }
            }
        });

        fillUserData();
        flagAlreadyFilled = true;
    }

    private void fillUserData() {
        if (flagAlreadyFilled)
            return;

        User current = fbs.getCurrentUser();

        if (current != null) {
            etFirstName.setText(current.getFirstName());
            etLastName.setText(current.getLastName());
            etAddress.setText(current.getAddress());
            etPhone.setText(current.getPhone());

            if (current.getPhoto() != null && !current.getPhoto().isEmpty()) {
                Picasso.get().load(current.getPhoto()).into(ivUser);
                fbs.setSelectedImageURL(Uri.parse(current.getPhoto()));
            }
        }
    }
}
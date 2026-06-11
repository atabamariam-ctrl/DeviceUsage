package com.example.deviceusage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private EditText etFirstName, etLastName, etAddress, etPhone;
    private Button btnUpdate;
    private ImageView ivUser;

    private TextView tvTotalCountAdmin;
    private TextView tvSafeCountAdmin;
    private TextView tvWarningCountAdmin;
    private TextView tvDangerousCountAdmin;
    private TextView tvFavoriteCountAdmin;

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

        tvTotalCountAdmin = getView().findViewById(R.id.tvTotalCountAdmin);
        tvSafeCountAdmin = getView().findViewById(R.id.tvSafeCountAdmin);
        tvWarningCountAdmin = getView().findViewById(R.id.tvWarningCountAdmin);
        tvDangerousCountAdmin = getView().findViewById(R.id.tvDangerousCountAdmin);
        tvFavoriteCountAdmin = getView().findViewById(R.id.tvFavoriteCountAdmin);

        Picasso.get()
                .load(R.drawable.ic_profile)
                .into(ivUser);

        fillUserData();
        loadAdminData();

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

    private void loadAdminData() {
        fbs.getFire()
                .collection("device")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            int total = 0;
                            int safe = 0;
                            int warning = 0;
                            int dangerous = 0;
                            int favorite = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DevicesItem device = document.toObject(DevicesItem.class);

                                boolean sameUser = true;

                                if (fbs.getAuth().getCurrentUser() != null) {
                                    sameUser = device.getUserId() != null &&
                                            device.getUserId().equals(fbs.getAuth().getCurrentUser().getUid());
                                }

                                if (sameUser) {
                                    total++;

                                    if (device.getStatus() != null) {
                                        if (device.getStatus().equalsIgnoreCase("Safe")) {
                                            safe++;
                                        } else if (device.getStatus().equalsIgnoreCase("Warning")) {
                                            warning++;
                                        } else if (device.getStatus().equalsIgnoreCase("Dangerous")) {
                                            dangerous++;
                                        }
                                    }

                                    if (device.isFavorite()) {
                                        favorite++;
                                    }
                                }
                            }

                            tvTotalCountAdmin.setText(String.valueOf(total));
                            tvSafeCountAdmin.setText(String.valueOf(safe));
                            tvWarningCountAdmin.setText(String.valueOf(warning));
                            tvDangerousCountAdmin.setText(String.valueOf(dangerous));
                            tvFavoriteCountAdmin.setText(String.valueOf(favorite));

                        } else {
                            Toast.makeText(getActivity(),
                                    "Failed to load dashboard data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
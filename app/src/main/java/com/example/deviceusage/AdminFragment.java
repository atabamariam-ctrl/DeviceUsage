package com.example.deviceusage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AdminFragment extends Fragment {

    private TextView tvTotalCountAdmin;
    private TextView tvSafeCountAdmin;
    private TextView tvWarningCountAdmin;
    private TextView tvDangerousCountAdmin;
    private TextView tvFavoriteCountAdmin;

    private FirebaseServices fbs;

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

        getActivity().findViewById(R.id.bottomNavigationView)
                .setVisibility(View.VISIBLE);

        fbs = FirebaseServices.getInstance();

        tvTotalCountAdmin = getView().findViewById(R.id.tvTotalCountAdmin);
        tvSafeCountAdmin = getView().findViewById(R.id.tvSafeCountAdmin);
        tvWarningCountAdmin = getView().findViewById(R.id.tvWarningCountAdmin);
        tvDangerousCountAdmin = getView().findViewById(R.id.tvDangerousCountAdmin);
        tvFavoriteCountAdmin = getView().findViewById(R.id.tvFavoriteCountAdmin);

        loadAdminData();
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
                                    "Failed to load admin data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
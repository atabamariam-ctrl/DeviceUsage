package com.example.deviceusage;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ForgetPasswordFragment extends Fragment {
    private FirebaseServices fbs;
    private EditText etEmail;
    private Button btnReset;
    private ImageView ivBack;

    public ForgetPasswordFragment() {
    }

    public static ForgetPasswordFragment newInstance(String param1, String param2) {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    public void onStart() {
        super.onStart();

        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.GONE);

        fbs = FirebaseServices.getInstance();

        etEmail = getView().findViewById(R.id.etForgotPassword);
        btnReset = getView().findViewById(R.id.btnResetPassword);
        ivBack = getView().findViewById(R.id.ivBackForgetPassword);

        ivBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();

                if (email.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                fbs.getAuth().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Check your email", Toast.LENGTH_SHORT).show();

                            FragmentTransaction ft = getActivity()
                                    .getSupportFragmentManager()
                                    .beginTransaction();

                            ft.replace(R.id.framelayot, new LoginFragment());
                            ft.commit();

                        } else {
                            Log.e("ResetPasswordError", task.getException().getMessage());

                            Toast.makeText(getActivity(),
                                    "Reset failed, check Logcat",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
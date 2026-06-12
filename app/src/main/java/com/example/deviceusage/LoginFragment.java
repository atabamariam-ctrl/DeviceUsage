package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private TextView tvSignupLink;
    private TextView tvForgotPasswordLink;
    private Button btnLogin;
    private FirebaseServices fbs;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        fbs = FirebaseServices.getInstance();

        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.GONE);

        etUsername = getView().findViewById(R.id.etUsernameLogin);
        etPassword = getView().findViewById(R.id.etPasswordLogin);
        tvSignupLink = getView().findViewById(R.id.tvSignupLinkLogin);
        tvForgotPasswordLink = getView().findViewById(R.id.tvForgotPasswordLogin);
        btnLogin = getView().findViewById(R.id.btnLoginLogin);

        tvSignupLink.setOnClickListener(v -> gotoSignupFragment());

        tvForgotPasswordLink.setOnClickListener(v -> gotoForgetPasswordFragment());

        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getActivity(),
                    "Some fields are empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);

        fbs.getAuth()
                .signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        btnLogin.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Welcome",
                                    Toast.LENGTH_SHORT).show();

                            gotoDeviceListMap();
                            setNavigationBarVisible();

                        } else {
                            String error = "Failed to login";

                            if (task.getException() != null) {
                                error = task.getException().getMessage();
                            }

                            Toast.makeText(getActivity(),
                                    error,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setNavigationBarVisible() {
        getActivity()
                .findViewById(R.id.bottomNavigationView)
                .setVisibility(View.VISIBLE);
    }

    public void gotoDeviceListMap() {
        FragmentTransaction ft =
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        ft.replace(R.id.framelayot, new DeviceListMapFragment());
        ft.commit();
    }

    private void gotoSignupFragment() {
        FragmentTransaction ft =
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        ft.replace(R.id.framelayot, new SignupFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoForgetPasswordFragment() {
        FragmentTransaction ft =
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

        ft.replace(R.id.framelayot, new ForgetPasswordFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
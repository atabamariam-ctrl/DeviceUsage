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

    private EditText etUsername,etPassword;
    private TextView tvSignupLink;
    private TextView tvForgotPasswardLink;
    private Button btnLogin;
    private Button asGuest;
    private FirebaseServices fbs;



    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();
        // connecting components
        fbs = FirebaseServices.getInstance();
        asGuest=getView().findViewById(R.id.btnGuest);
        asGuest.setVisibility(View.INVISIBLE);
        etUsername=getView().findViewById(R.id.etUsernameLogin);
        tvSignupLink=getView().findViewById(R.id.tvSignupLinkLogin);
        tvForgotPasswardLink=getView().findViewById(R.id.tvForgotPasswordLogin);
        etPassword=getView().findViewById(R.id.etPasswordLogin);
        btnLogin=getView().findViewById(R.id.btnLoginLogin);

        /*
        asGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCarList();
                setNavigationBarVisible();
            }
        }); */

        tvSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignupFragment();
            }
        });

        tvForgotPasswardLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFrgotPasswordFragment();
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Data validation

                String username=etUsername.getText().toString();
                String password=etPassword.getText().toString();
                if(username.trim().isEmpty()||password.trim().isEmpty()){
                    Toast.makeText(getActivity(), "some fields are empty", Toast.LENGTH_SHORT).show();
                    return;

                }
//                String name=fbs.getFire().collection("users").getParent().getId().toString();

                //Signup procedure

                fbs.getAuth().signInWithEmailAndPassword(username,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            //Toast.makeText(getActivity(), "you have succesfully logged", Toast.LENGTH_SHORT).show();
                            //gotoAddCarFragment();
                            fbs = FirebaseServices.reloadInstance();
                            gotoCarListMap();
                            Toast.makeText(getActivity(), "Welcome ", Toast.LENGTH_SHORT).show();

                            setNavigationBarVisible();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "failed to login! check user or password", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }

    private void setNavigationBarVisible() {
        ((MainActivity)getActivity()).getBottomNavigationView().setVisibility(View.VISIBLE);
    }

    public void gotoCarListMap() {
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new DeviceListMapFragment());
        ft.commit();
    }
    private void gotoAddCarFragment() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new AddDeviceFragment());
        ft.commit();
    }
    private void gotoSignupFragment() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new SignupFragment());
        ft.commit();

    }
    private void gotoFrgotPasswordFragment() {
        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new ForgetPasswordFragment());
        ft.commit();

    }}

/*
    public void gotologin(View view) {
        gotoCarList();
        setNavigationBarVisible();
    } */

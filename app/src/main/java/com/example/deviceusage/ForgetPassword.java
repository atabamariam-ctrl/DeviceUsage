package com.example.deviceusage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class ForgetPassword extends Fragment {
 private FirebaseServices fbs;
    private EditText etEmail;
    private Button btnReset;



    public ForgetPassword() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forget_password, container, false);
    }

    public void onStart() {
        super.onStart();
        // connecting components
        fbs=FirebaseServices.getInstance();
        etEmail=getView().findViewById(R.id.etForgotPassword);
        btnReset=getView().findViewById(R.id.btnResetPassword);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbs.getAuth().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "check your Email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "failed . check the Email address you entered !", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

    }
}
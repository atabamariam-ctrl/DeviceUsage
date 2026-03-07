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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    EditText etFirstName, etLastName, etAddress, etPhone;
    private static final int GALLERY_REQUEST_CODE = 134;
    private Button btnUpdate;
    private ImageView ivUser;
    private FirebaseServices fbs;
    private Utils utils;
    private String imageStr;
    private boolean flagAlreadyFilled = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init()
    {
        fbs = FirebaseServices.getInstance();
        etFirstName=getView().findViewById(R.id.etFirstnameUserDetailsEdit);
        etLastName=getView().findViewById(R.id.etLastnameUserDetailsEdit);
        etAddress=getView().findViewById(R.id.etAddressUserDetailsEdit);
        etPhone=getView().findViewById(R.id.etPhoneUserDetailsEdit);
        ivUser = getView().findViewById(R.id.ivUserDetailsEdit);
        btnUpdate = getView().findViewById(R.id.btnUpdateUserDetailsEdit);
        utils = Utils.getInstance();
        if(imageStr == null){
            Glide.with(getContext()).load(com.google.android.gms.base.R.drawable.common_google_signin_btn_text_dark_focused).into(ivUser);}
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Data validation
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
                if (current != null)
                {
                    if (!current.getFirstName().equals(firstname)  ||
                            !current.getLastName().equals(lastname)    ||
                            !current.getAddress().equals(address)      ||
                            !current.getPhone().equals(phone)          ||
                            !current.getPhoto().equals(fbs.getSelectedImageURL().toString()))
                    {
                        User user;
                        if (fbs.getSelectedImageURL() != null)
                            user = new User(firstname, lastname, fbs.getAuth().getCurrentUser().getEmail(), address, phone, fbs.getSelectedImageURL().toString());
                        else
                            user = new User(firstname, lastname, fbs.getAuth().getCurrentUser().getEmail(), address, phone,"");

                        fbs.updateUser(user);
                        utils.showMessageDialog(getActivity(), "Data updated succesfully!");
                        fbs.reloadInstance();
                    }
                    else
                    {
                        utils.showMessageDialog(getActivity(), "No changes!");
                    }
                }
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        fillUserData();
        flagAlreadyFilled = true;
    }

    private void fillUserData() {
        if (flagAlreadyFilled)
            return;
        User current = fbs.getCurrentUser();
        if (current != null)
        {
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
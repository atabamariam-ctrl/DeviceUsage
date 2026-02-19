package com.example.deviceusage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Type;
import java.util.UUID;


public class AddDeviceFragment extends Fragment {
    private EditText etDeviceName, etDeviceType, etModel, etBrand , etPhoto ;
    private Button btnAddDevice;
    private FirebaseServices fbs;
    ImageView img;
    private String imageStr;
    private Utils utils;
    private static final int GALLERY_REQUEST_CODE = 123;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AddDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDeviceFragment newInstance(String param1, String param2) {
        AddDeviceFragment fragment = new AddDeviceFragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                              Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_device, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        init();
    }
    private void init() {
        // ---->    פרטי הוספת רכב    <----
        //editText
        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();
        etDeviceName=getView().findViewById(R.id.etDeviceName);
        etBrand=getView().findViewById(R.id.etBrand);
        etDeviceType=getView().findViewById(R.id.etDeviceType);
        etModel=getView().findViewById(R.id.etModel);

        //button for add car
        btnAddDevice=getView().findViewById(R.id.btnAddDevice);
        img = getView().findViewById(R.id.deviceImage);
         btnAddDevice.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              // adding to firestore  'device' collection

              addToFirestore();
        }});
         img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openGallery();
        }
    });
        ((MainActivity)getActivity()).pushFragment(new AddDeviceFragment());
}
private void addToFirestore() {

    String DeviceName, DeviceType, Model, Brand;
//get data from screen

    DeviceName = etDeviceName.getText().toString();
    DeviceType = etDeviceType.getText().toString();
    Model = etModel.getText().toString();
    Brand = etBrand.getText().toString();
    if (DeviceName.trim().isEmpty() ||
            DeviceType.trim().isEmpty() ||
            Model.trim().isEmpty() ||
            Brand.trim().isEmpty()) {
        Toast.makeText(getActivity(), "sorry some data missing incorrect !", Toast.LENGTH_SHORT).show();
        return;
    }
    Device device;
    if (fbs.getSelectedImageURL() == null) {
        device = new Device(DeviceName, DeviceType, Model, Brand, "");
    } else {
        device = new Device(DeviceName, DeviceType, Model, Brand, fbs.getSelectedImageURL().toString());
    }

    fbs.getFire().collection("device").add(device)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(), "ADD Device is Succesed ", Toast.LENGTH_SHORT).show();
                    Log.e("addToFirestore() - add to collection: ", "Successful!");
                    gotoDeviceListFragment();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("addToFirestore() - add to collection: ", e.getMessage());
                }

            });

}



    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            img.setImageURI(selectedImageUri);
            utils.uploadImage(getActivity(), selectedImageUri);
        }
    }
    public void gotoDeviceListFragment() {

        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot,new DeviceListFragment());
        ft.commit();
    }
    public void toBigImg(View view) {
    }
      /*
    public void uploadImage(Uri selectedImageUri) {
        if (selectedImageUri != null) {
            imageStr = "images/" + UUID.randomUUID() + ".jpg"; //+ selectedImageUri.getLastPathSegment();
            StorageReference imageRef = fbs.getStorage().getReference().child("images/" + selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //selectedImageUri = uri;
                            fbs.setSelectedImageURL(uri);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                    Toast.makeText(getActivity(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please choose an image first", Toast.LENGTH_SHORT).show();
        }
    } */
}


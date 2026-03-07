package com.example.deviceusage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceDetailsFragment extends Fragment {

    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;
    private FirebaseServices fbs;
    private TextView  tvnameDevice , tvDevice_type,tvBrand , tvPhone ,tvDevice_model;
    private ImageView ivDevicePhoto;
    private DevicesItem myDevice;
    private Button sendSMSButton, btnWhatsapp, btnCall;

    private boolean isEnlarged = false; //משתנה כדי לעקוב אחרי המצב הנוכחי של התמונה (האם היא מגודלת או לא)


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceDetailsFragment newInstance(String param1, String param2) {
        DeviceDetailsFragment fragment = new DeviceDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceDetailsFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_device_details, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        init();
        ImageView ivDevicePhoto = getView().findViewById(R.id.ivDeviceDetailsFragment);

        ivDevicePhoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = ivDevicePhoto.getLayoutParams();
                if (isEnlarged) {
                    layoutParams.height =500;
                } else {
                    layoutParams.height = 2200;
                }
                ivDevicePhoto.setLayoutParams(layoutParams);

                // נשנה את המצב הנוכחי של התמונה
                isEnlarged = !isEnlarged;

            }
        });


    }

    public void init()
    {
/*Car(String nameDevice,String Device_type , String year, String Device_model, String photo)
* */


        fbs= FirebaseServices.getInstance();

        tvnameDevice=getView().findViewById(R.id.tvNameDetailsFragment);
        tvBrand = getView().findViewById(R.id.tvBrandDetailsFragment);
        tvDevice_model=getView().findViewById(R.id.tvModelDetailsFragment);
        tvDevice_type=getView().findViewById(R.id.tvTypeDetailsFragment);
        ivDevicePhoto = getView().findViewById(R.id.ivDeviceDetailsFragment);
        tvPhone=getView().findViewById(R.id.tvPhoneDetailsFragment);
        Bundle args = getArguments();
        if (args != null) {
            myDevice = args.getParcelable("Device");
            if (myDevice != null) {
                //String data = myObject.getData();
                // Now you can use 'data' as needed in FragmentB
                tvnameDevice.setText(myDevice.getName());
                tvDevice_type.setText(myDevice.getType());
                tvBrand.setText(myDevice.getYear());
                tvDevice_model.setText(myDevice.getModel());
                tvPhone.setText(myDevice.getPhone());


                if (myDevice.getPhoto() == null || myDevice.getPhoto().isEmpty())
                {
                    Picasso.get().load(R.drawable.ic_fav).into(ivDevicePhoto);
                }
                else {
                    Picasso.get().load(myDevice.getPhoto()).into(ivDevicePhoto);
                }
            }
        }
        sendSMSButton = getView().findViewById(R.id.btnSMS);
        sendSMSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSendSMS();            }
        });

        btnWhatsapp = getView().findViewById(R.id.btnWhatsApp);
        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWhatsAppMessage(v);

            }
        });

        btnCall = getView().findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void checkAndSendSMS() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            sendSMS();
        }
    }

    private void sendSMS() {
        String phoneNumber = myDevice.getPhone();
        String message = "I am Interested in your  "+myDevice.getName()+"  device: " ;

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            }
        }
    }
// TODO : check Phone number is not correct;
public void sendWhatsAppMessage(View view) {

    String smsNumber = "+972"+myDevice.getPhone();
    Intent sendIntent = new Intent(Intent.ACTION_SEND);
    //  Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
    sendIntent.setType("text/plain");
    sendIntent.putExtra(Intent.EXTRA_TEXT, " I am Interested in your  " +myDevice.getName()+ "  device:  " ) ;
    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
    sendIntent.setPackage("com.whatsapp");

    startActivity(sendIntent);

//        String phoneNumber ="+972"+ myDevice.getPhone(); // Replace with the recipient's phone number
//        String message = "Hello, this is a WhatsApp message!"; // Replace with your message
//        String phoneNumber2=  phoneNumber;
//        boolean isWhatsAppInstalled  =isAppInstalled("com.whatsapp");
//
//        if(isWhatsAppInstalled ){
//            Intent intent=new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+phoneNumber+"&text="+message));
//            startActivity(intent);
//        }
//        else {
//            Toast.makeText(getActivity(), "whatsapp is not installed", Toast.LENGTH_SHORT).show();
//        }

//
//
//        // Create an intent with the ACTION_SENDTO action and the WhatsApp URI
//        Intent intent = new Intent(Intent.ACTION_SENDTO);
//        intent.setData(Uri.parse("smsto:" + phoneNumber));
//        intent.putExtra("sms_body", message);
//
//        // Verify if WhatsApp is installed
//        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            // WhatsApp is not installed
//            // You can handle this case as per your app's requirement
//        }
}
//  888 whatsapp setting
private boolean isAppInstalled(String s) {
    PackageManager packageManager= getActivity().getPackageManager();
    boolean is_installed;
    try{
        packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
        is_installed=true;
    } catch (PackageManager.NameNotFoundException e) {
        is_installed=false;
        throw new RuntimeException(e);
    }
    return  is_installed;
}

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
//        } else {
//            startCall();
//        }
    }
    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + myDevice.getPhone()));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }


//        Intent callIntent = new Intent(Intent.ACTION_CALL);
//        callIntent.setData(Uri.parse(myDevice.getPhone()));
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            startActivity(callIntent);
//        }
    }

}
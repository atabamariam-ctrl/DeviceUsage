package com.example.deviceusage;


import android.os.Parcel;
import android.os.Parcelable;

public class Device {

    private String deviceName;
    private String phone;
    private String deviceType;
    private String model;
    private String brand;
    private String photo;

    // Required empty constructor for Firestore
    public Device() {}

    public Device(String phone ,String deviceName, String deviceType, String model, String brand , String photo ) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.model = model;
        this.brand = brand;
        this.photo = photo;
        this.phone = phone;

    }
    protected Device( Parcel in ) {
        this.deviceName = in.readString() ;
        this.deviceType = in.readString() ;
        this.model = in.readString() ;
        this.brand  = in.readString() ;
        this.photo = in.readString() ;
        this.phone = in.readString();
    }
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void writeToParcel  (Parcel dest , int flags ) {
        dest.writeString(this.deviceName);
        dest.writeString(this.deviceType);
        dest.writeString(this.model);
        dest.writeString(this.brand);
        dest.writeString(this.photo);
        dest.writeString(this.phone);
    }

    public String getDeviceName() { return deviceName; }
    public void setdeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getModel() { return model; }
    public void setModel(String model) {
        this.model = model;
    }
    public String getBrand() { return brand; }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public String toString() {
        return "Device{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", photo='" + photo + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public int describeContents() {
        return 0;
    }
}


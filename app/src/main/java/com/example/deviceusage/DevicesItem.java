package com.example.deviceusage;

import android.os.Parcel;
import android.os.Parcelable;

public class DevicesItem {

        private String name;
        private String model;
        private String brand;
        private String type;
        private String photo;
        private String phone;

        public DevicesItem() { }

        public DevicesItem(String name, String model, String brand, String type, String photo , String phone) {
            this.name = name;
            this.model = model;
            this.brand = brand;
            this.type = type;
            this.photo = photo;
        }

    protected DevicesItem(Parcel in) {

        this.name = in.readString();
        this.brand = in.readString();
        this.model = in.readString();
        this.type = in.readString();
        this.photo = in.readString();
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

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeString(this.phone);
    }
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
        public String getModel() { return model;}
    public void setModel(String model) {
        this.photo = model;
    }
        public String getBrand() { return brand; }
    public void setBrand(String brand) {
        this.brand = brand;
    }
        public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
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

                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", photo='" + photo + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public int describeContents() {
        return 0;
    }


}


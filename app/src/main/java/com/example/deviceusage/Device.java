package com.example.deviceusage;

public class Device {

    private String deviceName;
    private String deviceType;
    private String model;
    private String brand;

    // Required empty constructor for Firestore
    public Device() {}

    public Device(String deviceName, String deviceType, String model, String brand) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.model = model;
        this.brand = brand;
    }

    public String getDeviceName() { return deviceName; }
    public String getDeviceType() { return deviceType; }
    public String getModel() { return model; }
    public String getBrand() { return brand; }
}


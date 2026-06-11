package com.example.deviceusage;

public class Device {

    private String id;
    private String userId;

    private String name;
    private String type;
    private String brand;
    private String model;
    private String phone;

    private String batteryLevel;
    private String usageHours;
    private String status;
    private String notes;

    private String photo;
    private boolean favorite;

    public Device() {
    }

    public Device(String name, String type, String brand, String model,
                  String phone, String batteryLevel, String usageHours,
                  String status, String notes, String photo, boolean favorite) {

        this.name = name;
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.phone = phone;
        this.batteryLevel = batteryLevel;
        this.usageHours = usageHours;
        this.status = status;
        this.notes = notes;
        this.photo = photo;
        this.favorite = favorite;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPhone() {
        return phone;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public String getUsageHours() {
        return usageHours;
    }

    public String getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void setUsageHours(String usageHours) {
        this.usageHours = usageHours;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
package com.example.deviceusage;

public class DevicesItem {

    private String id;
    private String name;
    private String model;
    private String brand;
    private String type;
    private String photo;
    private String phone;

    public DevicesItem() {
    }

    public DevicesItem(String name,
                       String model,
                       String brand,
                       String type,
                       String photo,
                       String phone) {

        this.name = name;
        this.model = model;
        this.brand = brand;
        this.type = type;
        this.photo = photo;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public String getPhoto() {
        return photo;
    }

    public String getPhone() {
        return phone;
    }
}
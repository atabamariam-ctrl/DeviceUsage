package com.example.deviceusage;

public class DevicesItem {

        private String name;
        private String model;
        private String year;
        private String type;
        private String photo;

        public DevicesItem() { }

        public DevicesItem(String name, String model, String year, String type, String photo) {
            this.name = name;
            this.model = model;
            this.year = year;
            this.type = type;
            this.photo = photo;
        }

        public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
        public String getModel() { return model;}
    public void setModel(String model) {
        this.photo = model;
    }
        public String getYear() { return year; }
    public void setYear(String year) {
        this.year = year;
    }
        public String getType() { return type; }
    public void setType(String type) {
        this.type = type;
    }
        public String getPhoto() { return photo; }
    public void setPhoto(String photo) {
        this.photo = photo;
    }


    }


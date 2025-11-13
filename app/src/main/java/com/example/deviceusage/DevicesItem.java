package com.example.deviceusage;

public class DevicesItem {

        private String name;
        private String model;
        private String year;
        private String type;
        private String photo;

        public DevicesItem() { }

        public DevicesItem(String name, String mood, String year, String type, String photo) {
            this.name = name;
            this.model = model;
            this.year = year;
            this.type = type;
            this.photo = photo;
        }

        public String getName() { return name; }
        public String getModel() { return model;}
        public String getYear() { return year; }
        public String getType() { return type; }
        public String getPhoto() { return photo; }


    }


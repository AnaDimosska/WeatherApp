package com.example.watherapp.Weather.model;

public class Temperature {
    String date;
    String minTemp;

    public void setDate(String date) {
        this.date = date;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setLink(String link) {
        this.link = link;
    }

    String maxTemp;
    String link;

    public String getDate() {
        return date;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getLink() {
        return link;
    }
}

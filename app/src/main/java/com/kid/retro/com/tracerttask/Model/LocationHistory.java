package com.kid.retro.com.tracerttask.Model;

/**
 * Created by Kid on 1/30/2016.
 */
public class LocationHistory {

    private int location_id;
    private String latitude;
    private String longitude;
    private String on_date;
    private String on_time;
    private String email_id;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOn_date() {
        return on_date;
    }

    public void setOn_date(String on_date) {
        this.on_date = on_date;
    }

    public String getOn_time() {
        return on_time;
    }

    public void setOn_time(String on_time) {
        this.on_time = on_time;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}

package com.example.user.hello;

import java.io.Serializable;

/**
 * Created by user on 2017-08-16.
 */

public class TourMap implements Serializable{
    private double latitude; //위도
    private double longitude; //경도
    private String name; // 관광지 이름
    private String address;
    private double dis;

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public TourMap(double latitude, double longitude, String name, String address, double dis) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.dis = dis;
    }

    public double getLatitude() {
        return latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TourMap(double latitude, double longitude, String name, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "TourMap{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", name='" + name + '\'' +
                '}';
    }


    /**
     * Created by user on 2017-08-23.
     */


}

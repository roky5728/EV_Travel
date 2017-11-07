package com.example.user.hello;

import java.io.Serializable;

/**
 * Created by user on 2017-08-23.
 */

public class StationMap implements Serializable{

    private String name;
    private String address;
    private String closed; // 연중 무휴
    private String start;
    private String finish;

    public StationMap(String name, String address, String closed, String type, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.closed=closed;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    private String type;
    private String slow; //완속 충전기 대수
    private String rapid;
    private double fee;
    private String management;
    private String phone;
    private double latitude;
    private double longitude;

    public StationMap(String name, String address, String closed, String start, String finish, String type, String slow, String rapid, double fee, String management, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.closed = closed;
        this.start = start;
        this.finish = finish;
        this.type = type;
        this.slow = slow;
        this.rapid = rapid;
        this.fee = fee;
        this.management = management;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getClosed() {

        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlow() {
        return slow;
    }

    public void setSlow(String slow) {
        this.slow = slow;
    }

    public String getRapid() {
        return rapid;
    }

    public void setRapid(String rapid) {
        this.rapid = rapid;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getManagement() {
        return management;
    }

    public void setManagement(String management) {
        this.management = management;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }




}

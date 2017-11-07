package com.example.user.hello;

import java.io.Serializable;

/**
 * Created by user on 2017-08-23.
 */

public class CultureMap implements Serializable {
    private String codename;//장르
    private String name;
    private String seat;
    private String phone;
    private String open;
    private double latitude;
    private double longitude;
    private double entrfree;// 1이면 돈내공 0이면 돈 안내는 건강?
    private String img;
    private String fee;
    private String closed;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    private String address;
    private String homepage;
    private String fax;


    public CultureMap(String codename, String name, String seat, String phone, String open, double latitude, double longitude, double entrfree, String img, String fee, String closed, String address, String homepage, String fax) {
        this.codename = codename;
        this.name = name;
        this.seat = seat;
        this.phone = phone;
        this.open = open;
        this.latitude = latitude;
        this.longitude = longitude;
        this.entrfree = entrfree;
        this.img = img;
        this.fee = fee;
        this.closed = closed;
        this.address = address;
        this.homepage = homepage;
        this.fax = fax;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
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

    public double getEntrfree() {
        return entrfree;
    }

    public void setEntrfree(double entrfree) {
        this.entrfree = entrfree;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}

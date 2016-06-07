package com.example.liusai.locationtest.Entity;

/**
 * Created by liusai on 16/5/13.
 * latitude 纬度
 * longitude 经度
 * radius 半径
 * address 物理地址
 */
public class LatLngAddrEntity {

    private double latitude;
    private double longitude;
    private float radius;
    private String address;

    public LatLngAddrEntity(){}

    public LatLngAddrEntity(String address, double latitude, double longitude, float radius){
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.address = address;
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

package com.shoppr.app.data.user.model;

import androidx.annotation.Nullable;

public class User {
	private String uuid;
    private String name;
    private String email;
    private String phoneNumber;
	private String address;
    private double latitude;
    private double longitude;

	public User() {
	}

    public User(String uuid) {
        this.uuid = uuid;
    }

    public User(String uuid, String name, String email, String phoneNumber) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public User(String uuid, String name, String email, String phoneNumber, long latitude, long longitude) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

	public User(String uuid, String name, String email, String phoneNumber, @Nullable String address) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getUuid() {
        return uuid;
    }

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

	public String getAddress() {
        return address;
    }

	public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

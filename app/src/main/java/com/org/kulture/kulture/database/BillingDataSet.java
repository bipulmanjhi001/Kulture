package com.org.kulture.kulture.database;

/**
 * Created by tkru on 8/30/2017.
 */

public class BillingDataSet {
    //private variables
    private String first_name;
    private String last_name;
    private String company;
    private String country ;
    private String email;
    private String mob_number;
    private String address;
    private int pincode;
    private String city;
    private String state;

    public BillingDataSet(){

    }

    public BillingDataSet(String first_name, String last_name, String company, String country, String email, String mob_number, String address, int pincode, String city, String state) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.company = company;
        this.country = country;
        this.email = email;
        this.mob_number = mob_number;
        this.address = address;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMob_number() {
        return mob_number;
    }

    public void setMob_number(String mob_number) {
        this.mob_number = mob_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

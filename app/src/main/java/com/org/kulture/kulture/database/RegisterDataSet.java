package com.org.kulture.kulture.database;

/**
 * Created by tkru on 8/18/2017.
 */

public class RegisterDataSet {
    private String f_name;
    private String email;
    private String lname;
    private String pass;
    private String check;

    public RegisterDataSet() {

    }

    public RegisterDataSet(String f_name, String email, String lname, String pass, String check) {
        this.f_name = f_name;
        this.email = email;
        this.lname = lname;
        this.pass = pass;
        this.check = check;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
package com.org.kulture.kulture.database;

/**
 * Created by tkru on 10/25/2017.
 */

public class RegGetData {
    private String reg_fname;
    private String reg_lname;
    private String reg_email;
    private String reg_con_pass;

    public RegGetData(){

    }

    public RegGetData(String reg_fname, String reg_lname, String reg_email, String reg_con_pass) {
        this.reg_fname = reg_fname;
        this.reg_lname = reg_lname;
        this.reg_email = reg_email;
        this.reg_con_pass = reg_con_pass;
    }

    public String getReg_fname() {
        return reg_fname;
    }

    public void setReg_fname(String reg_fname) {
        this.reg_fname = reg_fname;
    }

    public String getReg_lname() {
        return reg_lname;
    }

    public void setReg_lname(String reg_lname) {
        this.reg_lname = reg_lname;
    }

    public String getReg_email() {
        return reg_email;
    }

    public void setReg_email(String reg_email) {
        this.reg_email = reg_email;
    }

    public String getReg_con_pass() {
        return reg_con_pass;
    }

    public void setReg_con_pass(String reg_con_pass) {
        this.reg_con_pass = reg_con_pass;
    }
}

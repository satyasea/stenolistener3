package com.blake.login;

/**
 * Created by horse on 8/8/2014.
 */
public class ClockworkUserBean {

    int workerId;
    int tsheetsId;
    String fname;
    String lname;
    String phone;

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int worker_id) {
        this.workerId = worker_id;
    }

    public int getTsheetsId() {
        return tsheetsId;
    }

    public void setTsheetsId(int tsheets_id) {
        this.tsheetsId = tsheets_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

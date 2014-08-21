package com.blake.where;

import java.security.Timestamp;
import java.util.Date;

/**
 * Created by blake on 6/28/14.
 */
public class ClockWorkUserEntry {


    private int id;

    private int entryType;

    private String lat;
    private String longit;

    private Date entryTime;

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongit() {
        return longit;
    }

    public void setLongit(String longit) {
        this.longit = longit;
    }

    public ClockWorkUserEntry(int id){
        this.id=id;
    }

    public ClockWorkUserEntry(){

    }


    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }





}

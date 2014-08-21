package com.blake.where.tsheets;


import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.ClockWorkUserEntry;
import com.blake.where.clockpunch.ClockPunchWebService;

public class TsheetsCreateGeoTask extends AsyncTask<String,Void,String>{

    private int userId;
    private Context context;
    private String latitude;
    private String longitude;
    private int tSheets;


    public TsheetsCreateGeoTask(Context context, ClockWorkUserEntry entry, int tSheets) {
        if(context == null || entry.getId() == 0 || entry.getLat() == null || entry.getLongit() == null){
            return;
        }
        this.context = context;
        this.userId= entry.getId();
        this.latitude=entry.getLat();
        this.longitude=entry.getLongit();
        this.tSheets = tSheets;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        return TsheetCreateGeoWebService.postCreateGeoEntry(tSheets, latitude, longitude);

    }


    @Override
    protected void onPostExecute(String result){
    }


}
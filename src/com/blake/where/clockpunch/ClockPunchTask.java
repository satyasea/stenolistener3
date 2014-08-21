package com.blake.where.clockpunch;


import android.content.Context;
import android.os.AsyncTask;
import com.blake.login.OnLoginTaskCompleted;
import com.blake.where.ClockWorkUserEntry;
import com.nuance.nmdp.sample.ClockWorkMainActivity;

public class ClockPunchTask extends AsyncTask<String,Void,String>{

    private int userId;
    private Context context;
    private int entry_type;
    private String latitude;
    private String longitude;

    private OnClockPunchTaskCompleted listener;


    public ClockPunchTask(ClockWorkMainActivity activity, ClockWorkUserEntry entry) {
        if(activity == null || entry.getId() == 0 || entry.getLat() == null || entry.getLongit() == null){
            return;
        }
        this.context = activity;
        this.userId= entry.getId();
        this.entry_type=entry.getEntryType();
        this.latitude=entry.getLat();
        this.longitude=entry.getLongit();
        this.listener=activity;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
            return ClockPunchWebService.postClockPunchEntry(Integer.valueOf(this.userId), entry_type, latitude, longitude);

    }


    @Override
    protected void onPostExecute(String result){

        listener.onClockPunchTaskCompleted(result);
    }

}
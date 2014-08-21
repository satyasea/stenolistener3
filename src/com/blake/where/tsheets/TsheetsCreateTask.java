package com.blake.where.tsheets;


import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.ClockWorkUserEntry;

public class TsheetsCreateTask extends AsyncTask<String,Void,String>{

    private int userId;
    private Context context;


    public TsheetsCreateTask(Context context, int userId) {
        if(context == null || userId == 0 ){
            return;
        }
        this.context = context;
        this.userId= userId;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
            return TsheetCreateWebService.postCreateSheetEntry(Integer.valueOf(userId));

    }


    @Override
    protected void onPostExecute(String result){
    }

}
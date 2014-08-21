package com.blake.where.tsheets;


import android.content.Context;
import android.os.AsyncTask;
import com.blake.where.ClockWorkUserEntry;

public class TsheetsEditTask extends AsyncTask<String,Void,String>{

    private int sheetUserId;
    private Context context;


    public TsheetsEditTask(Context context, int sheetUserId) {
        if(context == null || sheetUserId == 0 ){
            return;
        }
        this.context = context;
        this.sheetUserId= sheetUserId;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
            return TsheetEditWebService.postEditSheetEntry(Integer.valueOf(this.sheetUserId));

    }


    @Override
    protected void onPostExecute(String result){
    }

}
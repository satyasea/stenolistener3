package com.blake.login;

import android.content.Context;
import android.os.AsyncTask;



public class LoginTask extends AsyncTask<String,Void,String>{

    private Context context;
    private OnLoginTaskCompleted listener;


    public LoginTask(Context context, OnLoginTaskCompleted listener) {
        this.context = context;
        this.listener=listener;
    }

    @Override
    protected void onPreExecute(){
    }

    @Override
    protected String doInBackground(String... arg0) {
        String phone = arg0[0];
        String pass  = arg0[1];
        return LoginWebService.postVerifyUserObtainWorkerId(phone,pass);

    }


    @Override
    protected void onPostExecute(String result){
        //hand off result to caller (LoginActivity)
        listener.onLoginTaskCompleted(result);
    }
}
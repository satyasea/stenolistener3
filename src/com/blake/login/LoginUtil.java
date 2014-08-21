package com.blake.login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by horse on 8/8/2014.
 */
public class LoginUtil {


    public static ClockworkUserBean getUserBeanFromResultsJSON(String result) {

        ClockworkUserBean bean = new ClockworkUserBean();
        int worker_id;
        int tsheets_id;
        String fname;
        String lname;
        String phone;
        JSONArray array = null;
        if(result.length()==0){
            return bean;
        }
        try {
            array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                worker_id = row.getInt("worker_id");
                tsheets_id = row.getInt("tsheets_id");
                fname = row.getString("worker_fname");
                lname = row.getString("worker_lname");
                phone = row.getString("phone_num");
                bean.setWorkerId(worker_id);
                bean.setTsheetsId(tsheets_id);
                bean.setFname(fname);
                bean.setLname(lname);
                bean.setPhone(phone);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }finally{
            return bean;
        }
    }
}

package com.blake.where.state;

import com.blake.Config;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blake on 7/12/14.
 */
public class ClockStateWebService {


    public static String obtainLastEntryType(String workerId) {
        HttpClient httpclient = new DefaultHttpClient();
        String link="http://ineeduneed.com/clockwork/clock_state.php";
        if(Config.IS_AEH_BUILD) {
            link="http://ineeduneed.com/clockwork_aeh/clock_state.php";
        }
        HttpPost httppost = new HttpPost(link);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("worker_id", workerId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line="";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        } catch (ClientProtocolException e) {
            return new String("Exception: " + e.getMessage());
        } catch (IOException e) {
            return new String("Exception: " + e.getMessage());
        }
    }


}
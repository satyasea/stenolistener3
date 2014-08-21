package com.blake.where.tsheets;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by blake on 7/12/14.
 */
public class TsheetCreateGeoWebService {


    public static String postCreateGeoEntry(int worker_id, String lat, String lon) {
        HttpClient httpclient = new DefaultHttpClient();
        String link="http://ineeduneed.com/clockwork/tsheets/tsheets_geo_add.php";
       // String link="http://ineeduneed.com/clockwork_aeh/tsheets/tsheets_geo_add.php";
        HttpPost httppost = new HttpPost(link);
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(1);
            params.add(new BasicNameValuePair("worker_id", String.valueOf(worker_id)));
            params.add(new BasicNameValuePair("lat", lat));
            params.add(new BasicNameValuePair("lon", lon));
            httppost.setEntity(new UrlEncodedFormEntity(params));
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
package com.blake.login;

import com.blake.Config;
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
public class LoginWebService {


    public static String postVerifyUserObtainWorkerId(String phone, String pass) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        String link="http://ineeduneed.com/clockwork/clock_auth.php";
        if(Config.IS_AEH_BUILD) {
            link="http://ineeduneed.com/clockwork_aeh/clock_auth.php";
        }
        HttpPost httppost = new HttpPost(link);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("phone_num", phone));
            nameValuePairs.add(new BasicNameValuePair("password", pass));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
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
            // TODO Auto-generated catch block
            return new String("Exception: " + e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return new String("Exception: " + e.getMessage());
        }
    }


}
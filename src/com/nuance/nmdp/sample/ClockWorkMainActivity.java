package com.nuance.nmdp.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blake.voice.tasks.ActionTask;
import com.blake.where.ClockWorkUserEntry;
import com.blake.where.OnClockStateTaskCompleted;
import com.blake.where.clockpunch.ClockPunchTask;
import com.blake.where.clockpunch.OnClockPunchTaskCompleted;
import com.blake.where.location.WhereActivity;
import com.blake.login.ClockworkUserBean;
import com.blake.where.state.ClockStateTask;
import com.blake.where.tsheets.TsheetsCreateGeoTask;
import com.blake.where.tsheets.TsheetsCreateTask;
import com.blake.where.tsheets.TsheetsEditTask;

import java.util.Date;

public class ClockWorkMainActivity extends Activity implements OnClockStateTaskCompleted, OnClockPunchTaskCompleted {

    ImageView statusColor;
    private TextView txt;
    private TextView welcome;
    Button login;
    Button logout;
    static int ENTRY_TYPE_LOGIN = 0;
    static int ENTRY_TYPE_LOGOUT = 1;
    private String[] coords = new String[2];
    public boolean isLoggedIn = false;
    ClockWorkUserEntry entry;
    ClockworkUserBean user;
    boolean isCommand = false;
    String clockCmd;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.main_clock);
       txt = (TextView) findViewById(R.id.txt);
      //  txt.setVisibility(View.VISIBLE);
        statusColor=(ImageView) findViewById(R.id.img);
        welcome = (TextView) findViewById(R.id.hello);
        login = (Button) findViewById(R.id.btn_login);
        logout = (Button) findViewById(R.id.btn_logout);
        logout = (Button) findViewById(R.id.btn_logout);
        //build button activity anon class
        Button.OnClickListener l = new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (v == login){
                    login();
                } else if (v == logout)  {
                    logout();
                }
            }
        };
        login.setOnClickListener(l);
        logout.setOnClickListener(l);


        // if we are voice command, run clock out function...
        Intent intent = getIntent();
        clockCmd = intent.getStringExtra(ActionTask.EXTRA_MESSAGE);
        if(clockCmd !=null && !clockCmd.equals("")){
            isCommand = true;
        }
        if(isCommand){
            SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
            String firstName = settings.getString("fname", "George");
            String lastName = settings.getString("lname", "Bush");
            int workerId = Integer.valueOf(settings.getString("worker_id", "0"));
            int tsheetsId = Integer.valueOf(settings.getString("tsheets_id", "0"));
            String phone = settings.getString("phone", "8005551212");
            user = new ClockworkUserBean();
            user.setWorkerId(workerId);
            user.setTsheetsId(tsheetsId);
            user.setFname(firstName);
            user.setLname(lastName);
            user.setPhone(phone);
            if(user.getWorkerId() !=0 || user.getTsheetsId() !=0){
                entry = new ClockWorkUserEntry(user.getWorkerId());
            }
            //check app login state / status, results are received by onTaskCompleted(String value)
            String clockState = settings.getString("clock_state", "foo");
            if(clockState.equals("foo")){
                new ClockStateTask(this,this).execute(String.valueOf(user.getWorkerId()));
            }else{
                Intent i = new Intent(this, WhereActivity.class);
                if (clockCmd.equals("in")) {
                    entry.setEntryType(ENTRY_TYPE_LOGIN);
                    startActivityForResult(i, 0);
                } else {
                    entry.setEntryType(ENTRY_TYPE_LOGOUT);
                    startActivityForResult(i, 1);
                }
            }
        }else{
            setGUI();
        }
    }


    private void setGUI(){
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        String firstName = settings.getString("fname", "George");
        String lastName = settings.getString("lname", "Bush");
        int workerId = Integer.valueOf(settings.getString("worker_id", "0"));
        int tsheetsId = Integer.valueOf(settings.getString("tsheets_id", "0"));
        String phone = settings.getString("phone", "8005551212");

        user = new ClockworkUserBean();
        user.setWorkerId(workerId);
        user.setTsheetsId(tsheetsId);
        user.setFname(firstName);
        user.setLname(lastName);
        user.setPhone(phone);
        if(user.getWorkerId()==0 || user.getTsheetsId()==0){
            txt.setText("Login Fail!");
        }else {
            txt.setText(user.getFname());
            entry = new ClockWorkUserEntry(user.getWorkerId());
        }
        //check app login state / status, results are received by onTaskCompleted(String value)
        String clockState = settings.getString("clock_state", "foo");
        if(clockState.equals("foo")){
            new ClockStateTask(this,this).execute(String.valueOf(user.getWorkerId()));
        }else{
            if(clockState.equals("in")){
                isLoggedIn=true;
                setUIClockedIn();
            }else if(clockState.equals("out")){
                isLoggedIn=false;
                setUIClockedOut();
            }
        }
    }


    /*
 activity will interact with user, will receive user input
 calls onPause()
  */
    @Override
    public void onResume(){
        super.onResume();
        //check app login state / status, results are received by onTaskCompleted(String value)
     //   setGUI();


    }

    private void login(){
        if(isLoggedIn) return;
        entry.setEntryType(ENTRY_TYPE_LOGIN);
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 0);
    }

    private void logout(){
        if(!isLoggedIn) return;
        entry.setEntryType(ENTRY_TYPE_LOGOUT);
        Intent i = new Intent(this, WhereActivity.class);
        startActivityForResult(i, 1);
    }



    /*
       results of webservicetask check for application state (eg, last login type)
     */
    @Override
    public void onClockStateTaskCompleted(String value) {

        if (value.length() > 0) {
            //voice commands made, show no UI
            if (isCommand) {
                //here we are going to set the state in the future, so we want to show current state
                Intent i = new Intent(this, WhereActivity.class);
                if (clockCmd.equals("in") && Integer.parseInt(value) == 1 ) {
                    isLoggedIn = false;
                    entry.setEntryType(ENTRY_TYPE_LOGIN);
                    startActivityForResult(i, 0);
                } else if(clockCmd.equals("out") && Integer.parseInt(value) == 0 ){
                    isLoggedIn = true;
                    entry.setEntryType(ENTRY_TYPE_LOGOUT);
                    startActivityForResult(i, 1);
                }
                // manual buttons pushed, show UI
            } else {
                SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                //set the UI to the current state
                if (Integer.parseInt(value) == 0) {
                    isLoggedIn = true;
                    editor.putString("clock_state", "in");
                    setUIClockedIn();
                } else if (Integer.parseInt(value) == 1) {
                    isLoggedIn = false;
                    editor.putString("clock_state", "out");
                    setUIClockedOut();
                }
                editor.commit();
            }
        } else {
            //first time logging in, no data, or no data...
            if (isCommand) {
                isLoggedIn = false;
                entry.setEntryType(ENTRY_TYPE_LOGIN);
                entry.setEntryTime(new Date());
                Intent i = new Intent(this, WhereActivity.class);
                startActivityForResult(i, 0);
            } else {
                isLoggedIn = false;
                setUINoLogData();

            }
        }

    }

    /*
here are the results from calling whereactivity for gps coordinates
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(null!=data){
                coords[0] = data.getStringExtra("lat");
                coords[1]= data.getStringExtra("long");
                entry.setLat(coords[0]);
                entry.setLongit(coords[1]);
                entry.setEntryTime(new Date());
                //Here is where the clockpunchtask is executed after the entry is built with location data
                new ClockPunchTask(this, entry).execute("");

            }
        }else {
            isLoggedIn=false;
            setUILocationFailed();
        }
    }


// todo: this where we go when we are done punching the clock.
    @Override
    public void onClockPunchTaskCompleted(String value){
                /*
            todo: should tighten this up and get a confirmation from the web service that the insert was successful
             */
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        // handle entry type and actions with tsheets ..
        if(entry.getEntryType()==ENTRY_TYPE_LOGIN) {
            new TsheetsCreateTask(this, user.getTsheetsId()).execute("");
            new TsheetsCreateGeoTask(this, entry, user.getTsheetsId()).execute("");
            editor.putString("clock_state", "in");
            isLoggedIn=true;
            editor.commit();
            setUIClockedIn();
        }else{
            new TsheetsCreateGeoTask(this, entry, user.getTsheetsId()).execute("");
            new TsheetsEditTask(this, user.getTsheetsId()).execute("");
            editor.putString("clock_state", "out");
            isLoggedIn=false;
            editor.commit();
            setUIClockedOut();
        }
    }




    private void setUIClockedIn(){
        statusColor.setImageResource(R.drawable.green_square);
        statusColor.setVisibility(View.VISIBLE);
        welcome.setText("Clocked In");
        welcome.setTextColor(Color.GREEN);
        welcome.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.VISIBLE);
    }

    private void setUIClockedOut(){
        statusColor.setImageResource(R.drawable.red_square);
        statusColor.setVisibility(View.VISIBLE);
        welcome.setText("Clocked Out");
        welcome.setTextColor(Color.RED);
        welcome.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
    }

    private void setUILocationFailed(){
        welcome.setText("Exit and Enable Location.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        login.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.INVISIBLE);
    }
    // there is no data from db, either because it failed or was empty
    private void setUINoLogData(){
        statusColor.setImageResource(R.drawable.orange_square);
        statusColor.setVisibility(View.VISIBLE);
        logout.setVisibility(View.INVISIBLE);
        login.setVisibility(View.VISIBLE);
        welcome.setText("Clock data unavailable.");
        welcome.setTextColor(Color.YELLOW);
        welcome.setVisibility(View.VISIBLE);
    }



    public boolean isLoggedIn() {
        return isLoggedIn;
    }


    /*
called by onResume(),
do short tasks before resuming previous activity - save changes etc
    if activity goes to front it calls onResume().
if activity is hidden it calls onStop()
*/
    @Override
    public void onPause(){

        super.onPause();
    }
    /*
    activity not visible, covered by other activity
    calls onRestart() if activity is coming back to user
    calls onDestroy() if activity is going away
     */
    @Override
    public void onStop(){

        super.onStop();
    }
    /*
    called by finish(), or because system destroyed it.
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    //end lifecycle overrides

    /*
    Called after stopping, calls onStart()
    */
    @Override
    public void onRestart(){

        super.onRestart();
    }
    /*
    Called by methods onCreate() and onRestart(),
                  if activity goes to front it calls onResume().
        if activity is hidden it calls onStop()
     */
    @Override
    public void onStart(){

        super.onStart();
    }

}

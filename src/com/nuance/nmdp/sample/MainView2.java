package com.nuance.nmdp.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.blake.Config;
import com.blake.StartListenerService;
import com.blake.db.MySQLiteHelper;
import com.blake.login.ClockworkUserBean;
import com.blake.login.LoginActivity;
import com.blake.personalize.PersonalizeActivity;
import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.SpeechKit;

public class MainView2 extends Activity {

    private static SpeechKit _speechKit;
    private static final String TAG = "MainView2";
    public static final String PREFS_NAME = "MyPrefsFile";
    public static String username;

    ClockworkUserBean bean = new ClockworkUserBean();

    TextView userLabel;
    Button clockWorkBtn;





    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        final Button dictationButton = (Button) findViewById(R.id.btn_dictation);
        final Button ttsButton = (Button) findViewById(R.id.btn_tts);
        clockWorkBtn = (Button) findViewById(R.id.btn_clock);
        Button buttonQ = (Button) findViewById(R.id.btn_quit);
        userLabel = (TextView) findViewById(R.id.txt_user);




        checkIdentity();
        setMainMenuGUI();



        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        // If this Activity is being recreated due to a config change (e.g. 
        // screen rotation), check for the saved SpeechKit instance.
     //   _speechKit = (SpeechKit) getLastNonConfigurationInstance();
        _speechKit = SpeechKitHolder.getSpeechKit(this);

        boolean isNetWorkAvail = false;
        if (isNetworkAvailable(getApplicationContext())) {
            isNetWorkAvail = true;
            //    Toast.makeText(this, "AAA InternetAvailable", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "XXX Internet Not Available", Toast.LENGTH_LONG).show();
            userLabel.setText("Please Enable Internet.");
            userLabel.setVisibility(View.VISIBLE);
            userLabel.setTextColor(Color.RED);
        }
        boolean online = false;
        if (isOnline()) {
            online = true;
            //  Toast.makeText(this, "AAA online", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "XXX Not online", Toast.LENGTH_LONG).show();
            userLabel.setText("Please Enable Internet.");
            userLabel.setVisibility(View.VISIBLE);
            userLabel.setTextColor(Color.RED);
        }
        Log.d(TAG, "online=" + online);
        Log.d(TAG, "networked=" + isNetWorkAvail);
        //only initialize if speechkit is null....
        if (_speechKit != null && (online | isNetWorkAvail)) {
            Prompt beep = _speechKit.defineAudioPrompt(R.raw.beep);
            _speechKit.setDefaultRecognizerPrompts(beep, Prompt.vibration(100), null, null);
            _speechKit.connect();
        }else if (_speechKit == null && (online | isNetWorkAvail)){
            _speechKit = SpeechKitHolder.getSpeechKit(this);
            Prompt beep = _speechKit.defineAudioPrompt(R.raw.beep);
            _speechKit.setDefaultRecognizerPrompts(beep, Prompt.vibration(100), null, null);
            _speechKit.connect();
        }


        Button.OnClickListener l = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dictationButton) {
                    // TODO: here is the entry point for calling the dictationView. The voice listener will replace the button.onclicklistener, and create intent instead
                    Intent intent = null;
                    //    intent = new Intent(v.getContext(), DictationServiceView.class)
                    intent = new Intent(v.getContext(), CommandView.class);

                    MainView2.this.startActivity(intent);
                } else if (v == ttsButton) {
                    Intent intent = new Intent(v.getContext(), TtsNameView.class);
                    startActivity(intent);
                }
            }
        };

        dictationButton.setOnClickListener(l);
        ttsButton.setOnClickListener(l);



        buttonQ.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                finish();



                //start listener again, if we are quitting.
                Intent service = new Intent(MainView2.this, StartListenerService.class);
                startService(service);




            }
        });


        clockWorkBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClockWorkMainActivity.class);
                startActivity(intent);
            }
        });


        Button buttonWhat = (Button) findViewById(R.id.btn_what);
        buttonWhat.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TtsCommandsHistoryView.class);
                startActivity(intent);
            }
        });


        Button buttonPers = (Button) findViewById(R.id.btn_pers);
        buttonPers.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PersonalizeActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_speechKit != null) {
            _speechKit.release();
            _speechKit = null;
            if(Config.IS_MULTIUSER){
                //destroy any data on quit of application.
                SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.clear();
                editor.commit();
                MySQLiteHelper db = new MySQLiteHelper(this);
                db.deleteAllData();
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        // Save the SpeechKit instance, because we know the Activity will be
        // immediately recreated.
        SpeechKit sk = _speechKit;
        _speechKit = null; // Prevent onDestroy() from releasing SpeechKit
        return sk;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    /*
    Called after stopping, calls onStart()
    */
    @Override
    public void onRestart()
    {
        super.onRestart();
    }

    /*
    Called by methods onCreate() and onRestart(),
                  if activity goes to front it calls onResume().
        if activity is hidden it calls onStop()
     */
    @Override
    public void onStart() {

        super.onStart();
    }

    /*
    activity will interact with user, will receive user input
    calls onPause()
     */
    @Override
    public void onResume() {
        super.onResume();

       setMainMenuGUI();


    }


    private void setMainMenuGUI() {

        // Restore preferences, getting the username in the app..
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        String firstName = settings.getString("fname", "George");

        //todo: new features personalization and clock state to main screen
        //can set as Samantha or Tom
        String avatar = settings.getString("avatar", "Samantha");
        ImageView img = (ImageView) findViewById(R.id.img_q);
        if (avatar.equals("Samantha")) {
            img.setImageResource(R.drawable.money);
        } else if (avatar.equals("Tom")) {
            img.setImageResource(R.drawable.q);
        }


        //todo: new features personalization and clock state to main screen
        // String avatar = settings.getString("avatar", "Tom");
        String clockState = settings.getString("clock_state", "foo");
        if (clockState.equals("in")) {
            clockWorkBtn.setTextColor(Color.GREEN);
        } else if (clockState.equals("out")) {
            clockWorkBtn.setTextColor(Color.RED);
        } else {
            clockWorkBtn.setTextColor(Color.YELLOW);
        }
        userLabel.setText(bean.getFname());
    }


    private void checkIdentity(){
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        String firstName = settings.getString("fname", "George");
        String lastName = settings.getString("lname", "Bush");
        int workerId = Integer.valueOf(settings.getString("worker_id", "0"));
        int tsheetsId = Integer.valueOf(settings.getString("tsheets_id", "0"));
        String phone = settings.getString("phone", "8005551212");
        if (firstName.equals("George") && lastName.equals("Bush")) {
            //todo security entry point for login activity....
            // todo: with this setup, you won't have to tell it your name, heh heh...
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        //    finish();
        } else {
            //create bean
            bean = new ClockworkUserBean();
            bean.setFname(firstName);
            bean.setLname(lastName);
            bean.setWorkerId(workerId);
            bean.setTsheetsId(tsheetsId);
            bean.setPhone(phone);
        }
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
      //  setMainMenuGUI();
    }
    /*
    activity not visible, covered by other activity
    calls onRestart() if activity is coming back to user
    calls onDestroy() if activity is going away
     */

}
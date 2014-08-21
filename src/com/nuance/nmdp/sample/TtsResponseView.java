package com.nuance.nmdp.sample;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blake.voice.tasks.ActionTask;
import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.Vocalizer;

public class TtsResponseView extends Activity
{
    public static final String TTS_KEY = "com.nuance.nmdp.sample.tts";

    private Vocalizer _vocalizer;
    private Object _lastTtsContext = null;

    private String output;

    String avatar;

    private class SavedState
    {
        int TextColor;
        String Text;
        Vocalizer Vocalizer;
        Object Context;
    }

    public TtsResponseView()
    {
        super();
    }
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tts_name);
        //todo: build data feeding voice
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        String firstName = settings.getString("fname", "George");
        String lastName = settings.getString("lname", "Bush");
        int workerId = Integer.valueOf(settings.getString("worker_id", "0"));
        String phone = settings.getString("phone", "8005551212");

        //todo: new features personalization and clock state to main screen
        avatar = settings.getString("avatar", "Samantha");
        String mi6="";
        ImageView img = (ImageView) findViewById(R.id.img_q) ;
        if(avatar.equals("Samantha")){
            img.setImageResource(R.drawable.money);
            mi6="Moneypenny";
        }else if(avatar.equals("Tom")){
            img.setImageResource(R.drawable.q);
            mi6="Q";
        }





        String username = firstName;
        // Get the message from the intent
        Intent intent = getIntent();
        String response = intent.getStringExtra(ActionTask.EXTRA_MESSAGE);
        if(username != null && response != null){
            output = username + " " + response;
        }

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // So that the 'Media Volume' applies to this activity


        TextView txt  = (TextView) findViewById(R.id.text_name);
        txt.setText(mi6+" talks back.");


         Button button = (Button)findViewById(R.id.btn_main);
        button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(TtsResponseView.this, MainView2.class);
                startActivity(i);
            }
        });

        // Configure "Stop" button
      /*  Button button = (Button)findViewById(R.id.btn_stopTts);
        button.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                _vocalizer.cancel();
                updateCurrentText("", Color.YELLOW, false);
            }
        });
        
        // Configure "Start" button
        button = (Button)findViewById(R.id.btn_startTts);
        Button.OnClickListener clickListener = new Button.OnClickListener()
        {
            @Override
            public void onClick(View v) 
            {
                EditText et = (EditText)findViewById(R.id.text_ttsSource);
               // String text = et.getText().toString();

                _lastTtsContext = new Object();
               // _vocalizer.speakString(text, _lastTtsContext);
                _vocalizer.speakString(output, _lastTtsContext);
                updateCurrentText("Starting TTS playback...", Color.YELLOW, true);
            }
        };
        button.setOnClickListener(clickListener);
*/
        // Configure the spinner to change the Vocalizer voice when changed
        /*
        Spinner spinner = (Spinner)findViewById(R.id.spin_ttsVoice);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> spinner, View arg1,
                    int arg2, long arg3) 
            {
                Object item = spinner.getSelectedItem();
                if (item != null)
                {
                    String voice = item.toString();
                    if (voice == null)
                    {
                        _vocalizer.setLanguage("en_US");
                    } else
                    {
                        _vocalizer.setVoice(voice);

                    }
                } 
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        */
        
        // Create Vocalizer listener
        Vocalizer.Listener vocalizerListener = new Vocalizer.Listener()
        {
            @Override
            public void onSpeakingBegin(Vocalizer vocalizer, String text, Object context) {
                updateCurrentText("Playing text: \"" + text + "\"", Color.GREEN, false);
                // for debugging purpose: printing out the speechkit session id
                android.util.Log.d("Nuance SampleVoiceApp", "Vocalizer.Listener.onSpeakingBegin: session id ["
                        + MainView2.getSpeechKit().getSessionId() + "]");
            }

            @Override
            public void onSpeakingDone(Vocalizer vocalizer,
                    String text, SpeechError error, Object context) 
            {
                // Use the context to detemine if this was the final TTS phrase
                if (context != _lastTtsContext)
                {
                    updateCurrentText("More phrases remaining", Color.YELLOW, false);
                } else
                {
                    updateCurrentText("", Color.YELLOW, false);
                }
                // for debugging purpose: printing out the speechkit session id
                android.util.Log.d("Nuance SampleVoiceApp", "Vocalizer.Listener.onSpeakingDone: session id ["
                        + MainView2.getSpeechKit().getSessionId() + "]");
            }
        };
        
        // If this Activity is being recreated due to a config change (e.g. 
        // screen rotation), check for the saved state.
        SavedState savedState = (SavedState)getLastNonConfigurationInstance();
        if (savedState == null)
        {
            // Create a single Vocalizer here.
            _vocalizer = MainView2.getSpeechKit().createVocalizerWithLanguage("en_US", vocalizerListener, new Handler());

            // Get selected voice from the spinner and set the Vocalizer voice
            /*
            Object item = spinner.getSelectedItem();
            if (item != null)
            {
                String voice = item.toString();
                if (voice != null) {
                    _vocalizer.setVoice(voice);
                }
            }
            */

            _vocalizer.setVoice(avatar);

            // Check for text from the intent (present if we came from DictationView)

            String tts = getIntent().getStringExtra(TTS_KEY);

            if (tts != null)
            {
              /*  EditText t = (EditText)findViewById(R.id.text_ttsSource);
                t.setText(tts);
               clickListener.onClick(button); // Simulate button click
               */
            }


        } else
        {
            _vocalizer = savedState.Vocalizer;
            _lastTtsContext = savedState.Context;
            updateCurrentText(savedState.Text, savedState.TextColor, false);
            
            // Need to update the listener, since the old listener is pointing at
            // the old TtsView activity instance.
            _vocalizer.setListener(vocalizerListener);
        }

           // Todo: copied what was in the button action to here
        _lastTtsContext = new Object();
        _vocalizer.setVoice(avatar);
        _vocalizer.speakString(output, _lastTtsContext);
        updateCurrentText("Q is speaking...", Color.YELLOW, true);
    }
    
    private void updateCurrentText(String text, int color, boolean onlyIfBlank)
    {
     /*   TextView v = (TextView)findViewById(R.id.text_currentTts);

        if (!onlyIfBlank || v.getText().length() == 0)
        {
            v.setTextColor(color);
            v.setText(text);
        }
        */
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_vocalizer != null)
        {
            _vocalizer.cancel();
            _vocalizer = null;
        }
    }
    
    @Override
    public Object onRetainNonConfigurationInstance()
    {
        // Save the Vocalizer state, because we know the Activity will be
        // immediately recreated.
        TextView textView = (TextView)findViewById(R.id.text_currentTts);
        
        SavedState savedState = new SavedState();
        savedState.Text = textView.getText().toString();
        savedState.TextColor = textView.getTextColors().getDefaultColor();
        savedState.Vocalizer = _vocalizer;
        savedState.Context = _lastTtsContext;

        _vocalizer = null; // Prevent onDestroy() from canceling
        return savedState;
    }
}

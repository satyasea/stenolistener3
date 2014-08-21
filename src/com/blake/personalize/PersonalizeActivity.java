package com.blake.personalize;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import com.nuance.nmdp.sample.MainView2;
import com.nuance.nmdp.sample.R;


public class PersonalizeActivity extends Activity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal);

    }



    public void onMaleQSelected(View v){
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("avatar", "Tom");
        editor.commit();
        finish();
    }


    public void onFemaleMoneyPennySelected(View v){
        SharedPreferences settings = getSharedPreferences(MainView2.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("avatar", "Samantha");
        editor.commit();
        finish();

    }


}




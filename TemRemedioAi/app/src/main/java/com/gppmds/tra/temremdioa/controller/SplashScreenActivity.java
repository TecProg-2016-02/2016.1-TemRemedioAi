/*
 * File: SplashScreenActivity.java
 * Purpose: Create and open the splash screen
 */

package com.gppmds.tra.temremdioa.controller;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import com.tra.gppmds.temremdioa.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

    private static final int WAIT_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // time of splash screen determined with the variable WAIT_TIME
        // show splash screen with the support from a thread
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                openMainActivity();
                finish();
            }
        }, WAIT_TIME);
    }

    /*
    * Transition from splash screen to main screen
    * @param none
    * @return boolean - if found intent return true else return false
    */
    public boolean openMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }
}

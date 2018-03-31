package com.laurent.budgiescare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.laurent.budgiescare.R;


/**
 * Shows the app logo for an amount of miliseconds and starts the mainactivity
 */
public class SplashActivity extends BaseActivity {

    public static final String TAG = SplashActivity.class.getSimpleName();
    public static final int SPLASH_TIMEOUT_INTERVAL = 2000;
    public static final int NOT_CONNECTED_TIMEOUT_INTERVAL = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        goToMain(true);

    }


//    private boolean checkConnection() {
//        JSch jsch;
//        try {
//            jsch = new JSch();
//
//            jsch.setConfig("StrictHostKeyChecking", "no");
//
//            //open a new session
//            final Session session = jsch.getSession(PI_USERNAME, PI_IP_ADDRESS, PI_PORT_NR);
//            session.setConfig("StrictHostKeyChecking", "no");
//            session.setPassword(PI_PASSWORD);
//            session.setTimeout(TIMEOUT_INTERVAL); // when at HvA maybe change it or remove it
//
//            return true;
//        } catch (JSchException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }

    /**
     * Method to check whether the app can go to "MainActivity"
     *
     * @param canIgo is true when app is allowed to go to main when theres a raspberry pi connection and
     *               false when the raspberry pi connection has not been established yet.
     */
    private void goToMain(boolean canIgo) {
        if (canIgo) {
            Log.e(TAG, "Going to the mainactivity");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, SPLASH_TIMEOUT_INTERVAL);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, NOT_CONNECTED_TIMEOUT_INTERVAL);
        }
    }


}

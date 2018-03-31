package com.laurent.budgiescare.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 */
public abstract class BaseActivity extends AppCompatActivity {

    // CHANGE ACCORDING TO HVA001
    protected static final String PI_USERNAME = "pi";
    protected static final String PI_PASSWORD = "raspberry";
//    protected static  String PI_IP_ADDRESS = "192.168.0.109";/* TODO change according*/
    protected static  String PI_IP_ADDRESS = "192.168.43.101";/* TODO change according*/
    protected static final int TIMEOUT_INTERVAL = 5000;

    protected static final int PI_PORT_NR = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}

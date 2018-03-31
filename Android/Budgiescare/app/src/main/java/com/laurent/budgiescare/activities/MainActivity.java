package com.laurent.budgiescare.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.laurent.budgiescare.R;
import com.laurent.budgiescare.models.FirebaseResponse;
import com.laurent.budgiescare.models.HeaderRequestInterceptor;
import com.laurent.budgiescare.util.PreferenceUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    // Pi credentials for sending commands to pi
    public static final String TAG = MainActivity.class.getSimpleName();

    // List for all the commands
    private List<String> mCommandList = new ArrayList<>();

    // Variables to control the current SSH connection process.
    private Session mSession;
    private Channel mChannel;

    // For the async task, when the connection has already been established we will ignore the whole block
    private boolean mAlreadyWorking = false;
    private boolean mClickedOnBudgie = false;

    // Views
    private Holder mHolder;

    private Vibrator mVibe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        initViews();

        initList();

        setPIAddress();

        new ControlPiNetwork(true).execute();
    }

    private void setPIAddress() {
        if (!PreferenceUtils.getInstance().getIP().equals("")) {
            PI_IP_ADDRESS = PreferenceUtils.getInstance().getIP();
        }
    }

    private void initList() {
        mCommandList.add("cd /home/pi/Desktop/"); // Navigate to desktop where the music is located
//        mCommandList.add("mplayer crec.mp3"); // Play an audio file from the raspberry Pi
        mCommandList.add("mplayer OJ.mp3"); // Play an audio file from the raspberry Pi
    }

    private void initViews() {
        mHolder = new Holder();

        // Instantiate the button with given IDs
        mHolder.mBudgieButton = (ImageView) findViewById(R.id.charliebutton);
        mHolder.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mHolder.mProgressLayout = (RelativeLayout) findViewById(R.id.progressBar_layout);

        initClickListeners();
    }

    private void initClickListeners() {
        mHolder.mProgressBar.setVisibility(View.GONE);
        mHolder.mProgressLayout.setVisibility(View.GONE);

        mHolder.mBudgieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation zoomAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ivzoom);

                view.startAnimation(zoomAnimation);

                mHolder.mProgressBar.setVisibility(View.VISIBLE);
                mHolder.mProgressLayout.setVisibility(View.VISIBLE);

                new ControlPiNetwork().execute();

            }
        });

        mHolder.mBudgieButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mVibe.vibrate(100);
                Toast.makeText(getApplicationContext(), "Press this button to play a sound on Pi!", Toast.LENGTH_SHORT).show();

                return false;
            }
        });


    }

    /**
     * This method makes the connection with the raspberry Pi Via SSH
     *
     * @return true when the mCommandList have been executed succesfully.
     */
    private boolean initPi() {
        JSch jsch;
        try {
            jsch = new JSch();

            jsch.setConfig("StrictHostKeyChecking", "no");

            //open a new session
            final Session session = jsch.getSession(PI_USERNAME, PI_IP_ADDRESS, PI_PORT_NR);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PI_PASSWORD);
            session.setTimeout(TIMEOUT_INTERVAL); // when at HvA maybe change it or remove it

            session.connect();

            final Channel channel = session.openChannel("shell"); //only shell
            channel.setOutputStream(System.out);
            PrintStream shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience
            channel.connect();

            for (String command : mCommandList) {
                shellStream.println(command);
                shellStream.flush();
            }

            mChannel = channel;
            mSession = session;

            return true;

        } catch (Exception e) {
            Log.e(TAG, "Failed to Create Session or Timed Out\n".concat(e.getMessage()));

            return false;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Todo test if it works
     * @return
     */
    private Boolean initSensor() {
        JSch jsch;
        try {
            jsch = new JSch();

            jsch.setConfig("StrictHostKeyChecking", "no");

            //open a new session
            final Session session = jsch.getSession(PI_USERNAME, PI_IP_ADDRESS, PI_PORT_NR);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(PI_PASSWORD);
            session.setTimeout(TIMEOUT_INTERVAL); // when at HvA maybe change it or remove it

            session.connect();

            final Channel channel = session.openChannel("shell"); //only shell
            channel.setOutputStream(System.out);
            PrintStream shellStream = new PrintStream(channel.getOutputStream());  // printStream for convenience
            channel.connect();

            shellStream.println("cd /home/pi/Desktop/");
            shellStream.flush();

            shellStream.println("sudo python motion.py");
            shellStream.flush();

            return true;

        } catch (Exception e) {
            Log.e(TAG, "Failed to Create Session or Timed Out\n".concat(e.getMessage()));

            return false;
        }

    }

    class ControlPiNetwork extends AsyncTask<String, String, Boolean> {

        private boolean mStartSensor = false;

        public ControlPiNetwork() {
        }

        public ControlPiNetwork(boolean startSensor) {
            this.mStartSensor = startSensor;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) throws NoClassDefFoundError {
            if (mStartSensor) {
                return initSensor();
            } else {
                initSensor();
                return /*!mAlreadyWorking &&*/ initPi();
            }
        }


        @Override
        protected void onPostExecute(Boolean alreadyWorking) {

            if (!mStartSensor) {
                mAlreadyWorking = alreadyWorking;

                Log.e(TAG, "Is it working: " + alreadyWorking);

                mClickedOnBudgie = false;

                if (alreadyWorking) {
                    PreferenceUtils.getInstance().setIP(PI_IP_ADDRESS);
                    Toast.makeText(getApplicationContext(), "Sound is being played", Toast.LENGTH_SHORT).show();
                    mVibe.vibrate(100);
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.. there was no connection at all..", Toast.LENGTH_SHORT).show();
                    mVibe.vibrate(100);
                    popup();
                }
            } else{
                if(alreadyWorking){
                    Toast.makeText(getApplicationContext(), "Sensor activitated", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Sensor not working ", Toast.LENGTH_SHORT).show();
                }
            }


            mHolder.mProgressBar.setVisibility(View.GONE);
            mHolder.mProgressLayout.setVisibility(View.GONE);
        }

        private void popup() {
            final Animation zoomAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ivzoom);

            mHolder.mBudgieButton.startAnimation(zoomAnimation);

            mHolder.mProgressBar.setVisibility(View.VISIBLE);
            mHolder.mProgressLayout.setVisibility(View.VISIBLE);


            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this, R.style.myDialogStyle);

            final EditText edittext = new EditText(getApplicationContext());
            alert.setMessage("Try another ip-address");
            alert.setTitle("Enter the Pi ip-address");

            alert.setView(edittext);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String ipaddress = edittext.getText().toString();
                    PI_IP_ADDRESS = ipaddress;

                    new ControlPiNetwork().execute();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        }

    }

    private static final String FIREBASE_SERVER_KEY = "xxxxxxxxxxxxxxxxxxxxxxxHqgVED6gWSwI_G4z7hnA";

    public CompletableFuture<FirebaseResponse> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        FirebaseResponse firebaseResponse = restTemplate.postForObject("https://fcm.googleapis.com/fcm/send", entity, FirebaseResponse.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }

    private class Holder {
        ImageView mBudgieButton;
        ProgressBar mProgressBar;
        RelativeLayout mProgressLayout;
    }
}
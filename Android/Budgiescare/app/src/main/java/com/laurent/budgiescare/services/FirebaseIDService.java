package com.laurent.budgiescare.services;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;

import java.io.IOException;

/**
 * Created by LaurentKlVB on 26-9-2016.
 */
public class FirebaseIDService extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDService";
    private static final String PC_IP = "http://192.168.43.101/"; //TODO CHANGE ACCORDING TO HVA001


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.

        initServerRegistration();

    }

    private void initServerRegistration() {
        new SendRegistrationToServer().execute();
    }

    private void saveRegistrationID() {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "my firebase token" + refreshedToken);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(PC_IP + "New/insertregistration.php?&parameter=".concat(refreshedToken));

        try {
            BasicHttpResponse response = (BasicHttpResponse) httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();


        } catch (ClientProtocolException e) {
            Log.w("com.name.pkg", e);
        } catch (IOException e) {
            Log.w("com.name.pkg", e);
        }
    }

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     */
    class SendRegistrationToServer extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) throws NoClassDefFoundError {
            saveRegistrationID();
            return true;
        }


    }
}
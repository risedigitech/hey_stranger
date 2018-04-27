package com.houseofapps.heystranger;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by it's me on 10/10/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    public static final String MY_PREFS_NAME = "HeyStranger";
    SharedPreferences.Editor pref;


    @Override
    public void onTokenRefresh() {
       // Log.e("heloooooooo","asdbjhsd3");
        //Getting registration token
       // String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
     //   Log.e("Refreshedtoken: " ,""+ refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }
    private void sendRegistrationToServer(String token) {

        pref = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        pref.putString("fcm_token",token);
        Log.e("store_token",token);
        pref.commit();


       // Log.e("Refreshedtoken: " ,""+ token);
//        SharedPreferences.Editor prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
//        prefs.putString("FCMtoken",token);
//        prefs.commit();
    }
}
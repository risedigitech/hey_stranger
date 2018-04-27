package com.houseofapps.heystranger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Accept extends AppCompatActivity {
    Button accept_btn;
    private FirebaseAuth mAuth;
    String TAG = "Accept";
    public static final String MY_PREF_NAME = "HeyStranger";
    SharedPreferences.Editor edit;
    SharedPreferences preferences;
    String check_auth = "";
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    DatabaseReference register =root.child("Register_Devices");
    String get_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);
        mAuth = FirebaseAuth.getInstance();
        accept_btn = (Button)findViewById(R.id.accept_btn);
        preferences = getSharedPreferences(MY_PREF_NAME,MODE_PRIVATE);
       // get_token =  preferences.getString("fcm_token",null);
        get_token =   Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put(""+get_token,"Active");
                register.updateChildren(map);
                signin();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    public void signin(){

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            edit = getSharedPreferences(MY_PREF_NAME,MODE_PRIVATE).edit();
                            edit.putString("check_auth","1");
                            edit.commit();
                            Intent intent = new Intent(Accept.this,Home.class);
                            intent.putExtra("chat_again_check","0");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(Accept.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                        // ...
                    }
                });
    }
}

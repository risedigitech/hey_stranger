package com.houseofapps.heystranger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    public static final String MY_PREF_NAME = "HeyStranger";
    SharedPreferences preferences;
    Context context;
    private static int SPLASH_TIME_OUT = 5000;
    String get_pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        bindLogo();
        preferences = getSharedPreferences(MY_PREF_NAME,MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferences.contains("check_auth")){
                    Intent i = new Intent(Splash.this, Home.class);
                    i.putExtra("chat_again_check","0");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Splash.this, Accept.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                finish();
            }
        },SPLASH_TIME_OUT);
    }


    private void bindLogo() {
        // Start animating the image
        final ImageView splash = (ImageView) findViewById(R.id.splash_logo);
        final AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(700);
        final AlphaAnimation animation2 = new AlphaAnimation(1.0f, 0.2f);
        animation2.setDuration(700);
        //animation1 AnimationListener
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                splash.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        //animation2 AnimationListener
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                splash.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }
        });

        splash.startAnimation(animation1);
    }



}

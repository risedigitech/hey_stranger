package com.houseofapps.heystranger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;



public class ImageOpen extends AppCompatActivity {

    ImageView full_image;
    byte [] bytes;
    String get_image = "";
    String get_chatroom_id = "";
    Bitmap bitmap;
    public static final String MyPRef = "HeyStranger";

    SharedPreferences.Editor preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_open);
        full_image = (ImageView)findViewById(R.id.full_image);
        bytes = getIntent().getByteArrayExtra("decodedByte");
        get_chatroom_id = getIntent().getStringExtra("chatroom_id");
//        preferences =getSharedPreferences(MyPRef,MODE_PRIVATE).edit();
//        preferences.putString("delete_chatroom",""+get_chatroom_id);
//        preferences.commit();
        startService(new Intent(getBaseContext(),ExitService.class));

        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        full_image.setImageBitmap(bitmap);


    }
}

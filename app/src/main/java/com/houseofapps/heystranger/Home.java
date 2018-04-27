package com.houseofapps.heystranger;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Home extends AppCompatActivity {

    Button start_chat_btn;
    ImageView sound_off,notification_off;
    LinearLayout nav_notification,nav_loveit,nav_share,nav_fb,nav_terms_n_con,nav_feedback,nav_sound;
    View headerview;
    String check_sound = "";
    String check_notifiaction = "";
    Context context;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    Query root_online = root.child("ONLINE_DEVICES").orderByValue().equalTo("online");
    String get_random = "";
    String get_online_node_id = "";
    String get_another_person_key = "";
    MarshMallowPermission mallowPermission;
    String chat_again_check = "";
    Button report_btn;
    String s_checkbox_abusive = "",s_checkbox_nudity="",s_checkbox_spammer = ""  ,s_edit_msg ="" ;
    String get_token = "";
    DatabaseReference online_child;
    String check = "0";
    DatabaseReference bussy;
    SharedPreferences.Editor preferences;
    String timsestamp = "";
    public static final String MyPRef = "HeyStranger";
    int count_chil = 0;
    String check_online_node = "";
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        start_chat_btn = (Button)findViewById(R.id.start_chat_btn);
        report_btn = (Button)findViewById(R.id.report_btn);
        context = this;
        check_sound = "1";
        check = "0";
        check_notifiaction = "1";
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerview = navigationView.getHeaderView(0);
        sound_off = (ImageView) headerview.findViewById(R.id.sound_off);
        notification_off = (ImageView) headerview.findViewById(R.id.notification_off);
        nav_notification = (LinearLayout) headerview.findViewById(R.id.nav_notification);
        nav_loveit = (LinearLayout) headerview.findViewById(R.id.nav_loveit);
        nav_share = (LinearLayout) headerview.findViewById(R.id.nav_share);
        nav_fb = (LinearLayout) headerview.findViewById(R.id.nav_fb);
        nav_terms_n_con = (LinearLayout) headerview.findViewById(R.id.nav_terms_n_con);
        nav_feedback = (LinearLayout) headerview.findViewById(R.id.nav_feedback);
        nav_sound = (LinearLayout) headerview.findViewById(R.id.nav_sound);
        mallowPermission = new MarshMallowPermission(this);
        askForCameraPermission();
        preferences =getSharedPreferences(MyPRef,MODE_PRIVATE).edit();


        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait loading...");
        pDialog.setCancelable(false);

        get_token = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        timsestamp = simpleDateFormat.format(new Date());
        chat_again_check = getIntent().getStringExtra("chat_again_check");
        if (chat_again_check.equals("1")){
            start_chat_btn.setText("Start Again");
            report_btn.setVisibility(View.VISIBLE);
        }
        else {
            report_btn.setVisibility(View.GONE);
        }
        report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        online_child = root.child("ONLINE_DEVICES");
        bussy = root.child("BUSY_NODES");
        start_chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showpDialog();

                call_check();
            }
        });
        nav_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check it out. Your message goes here";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            }
        });
        nav_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_sound.equals("1")){
                    sound_off.setVisibility(View.VISIBLE);
                    check_sound = "0";
                    AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                }
                else {
                    sound_off.setVisibility(View.GONE);
                    check_sound = "1";
                    AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
            }
        });

        nav_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_notifiaction.equals("1")){
                    notification_off.setVisibility(View.VISIBLE);
                    check_notifiaction = "0";
                    preferences.putString("FCM_ON_OFF","0");
                    preferences.commit();
                }
                else {
                    notification_off.setVisibility(View.GONE);
                    check_notifiaction = "1";
                    preferences.putString("FCM_ON_OFF","1");
                    preferences.commit();
                }
            }
        });
    }


    private void showpDialog() {
        if (!pDialog.isShowing()){
            pDialog.show();}
    }
    private void hidepDialog() {
        if (pDialog.isShowing()){
            pDialog.dismiss();}
    }

    public void askForCameraPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if ((! mallowPermission.checkPermissionForExternalStorage())|| (!mallowPermission.checkPermissionForCamera() )) {
                mallowPermission.requestPermissionForExternalStorage();
                mallowPermission.requestPermissionForCamera();

            }else {
             }
        } else {
        }
    }

    private void call_check() {
        online_child.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i =dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                 check_online_node = set.toString();
                Log.e("check_online_node123",""+check_online_node);
                if (set.toArray().length ==0&&check.equals("0")){
                    Log.e("check_online_node123","hygvhgavvajcf");
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put(""+get_token,"online");
                    online_child.updateChildren(map);
                    check = "1";
                    hidepDialog();
                   Intent intent = new Intent(Home.this,ChatScreen1.class);
                   startActivity(intent);

                }

                else{
                    if (check.equals("0")) {
                        check = "1";
                        try {
                            JSONArray  jsonArray = new JSONArray(check_online_node);
                            String get_node_2 = jsonArray.getString(0);
                            Log.e("get_node_2", "" + get_node_2);
                            Map<String,Object> map = new HashMap<String,Object>();
                            String temp_key = bussy.push().getKey();
                            bussy.updateChildren(map);
                            DatabaseReference mag_root = bussy.child(temp_key).child("MEMBERS");
                            Map<String,Object> map2 = new HashMap<String,Object>();
                            map2.put(""+get_token,"NODE1");
                            map2.put(""+get_node_2,"NODE2");
                            mag_root.updateChildren(map2);
                            online_child.child(get_node_2).removeValue();
                            online_child.child(get_token).removeValue();
                            Map<String, Object> map4 = new HashMap<String, Object>();
                            String temp_key1 = bussy.push().getKey();
                            bussy.updateChildren(map4);
                            DatabaseReference mag_root2 = bussy.child(temp_key).child("CHATROOM").child(temp_key1);
                            Map<String, Object> map3 = new HashMap<String, Object>();
                            map3.put("NAME", ""+get_token);
                            map3.put("DATA_TYPE", "Text");
                            map3.put("TIME_STAMP", ""+timsestamp);
                            map3.put("MSG", "Hey" );
                            map3.put("CHATROOM_ID", ""+temp_key);
                            mag_root2.updateChildren(map3);
//                            DatabaseReference status_type = bussy.child(temp_key).child("STATUS");
//                            Map<String,Object> map8 = new HashMap<String,Object>();
//                            map8.put(""+get_token,"Online");
//                            map8.put(""+get_node_2,"Online");
//                            status_type.updateChildren(map8);
                            hidepDialog();
                            Intent intent = new Intent(Home.this,ChatScreen1.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                        }
//                        try {
//
//                            bussy.addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                                    String get_child_keys = dataSnapshot.getKey();
//                                    Log.e("get_child_keys",""+get_child_keys);
//                                    Log.e("get_child_keys","hjbvvhbshbv 89");
//                                    bussy.child(get_child_keys).child("MEMBERS").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            Set<String> set1 = new HashSet<String>();
//                                            Iterator i =dataSnapshot.getChildren().iterator();
//
//                                            while (i.hasNext()) {
//                                                set1.add(((DataSnapshot) i.next()).getKey());
//                                                count_chil++;
//                                            }
//                                            Log.e("count_chil","count_chil"+count_chil);
//
//                                            if (count_chil==1){
//                                                try {
//                                                    JSONArray  jsonArray = new JSONArray(check_online_node);
//                                                    String get_node_2 = jsonArray.getString(0);
//                                                    Log.e("get_node_2", "" + get_node_2);
//                                                    Map<String,Object> map = new HashMap<String,Object>();
//                                                    String temp_key = bussy.push().getKey();
//                                                    bussy.updateChildren(map);
//                                                    DatabaseReference mag_root = bussy.child(temp_key).child("MEMBERS");
//                                                    Map<String,Object> map2 = new HashMap<String,Object>();
//                                                    map2.put(""+get_token,"NODE1");
//                                                    map2.put(""+get_node_2,"NODE2");
//                                                    mag_root.updateChildren(map2);
//                                                    online_child.child(get_node_2).removeValue();
//                                                    online_child.child(get_token).removeValue();
//                                                    Map<String, Object> map4 = new HashMap<String, Object>();
//                                                    String temp_key1 = bussy.push().getKey();
//                                                    bussy.updateChildren(map4);
//                                                    DatabaseReference mag_root2 = bussy.child(temp_key).child("CHATROOM").child(temp_key1);
//                                                    Map<String, Object> map3 = new HashMap<String, Object>();
//                                                    map3.put("NAME", ""+get_token);
//                                                    map3.put("DATA_TYPE", "Text");
//                                                    map3.put("TIME_STAMP", ""+timsestamp);
//                                                    map3.put("MSG", "Hey" );
//                                                    map3.put("CHATROOM_ID", ""+temp_key);
//                                                    mag_root2.updateChildren(map3);
//                                                    DatabaseReference status_type = bussy.child(temp_key).child("STATUS");
//                                                    Map<String,Object> map8 = new HashMap<String,Object>();
//                                                    map8.put(""+get_token,"Online");
//                                                    map8.put(""+get_node_2,"Online");
//                                                    status_type.updateChildren(map8);
//                                                    Intent intent = new Intent(Home.this,ChatScreen1.class);
//                                                    startActivity(intent);
//                                                } catch (JSONException e) {
//                                                }
//                                            }
//                                            else if (count_chil==2){
//                                                Map<String,Object> map = new HashMap<String,Object>();
//                                                map.put(""+get_token,"online");
//                                                online_child.updateChildren(map);
//                                                check = "1";
//                                                Intent intent = new Intent(Home.this,ChatScreen1.class);
//                                                startActivity(intent);
//
//                                            }
//
//
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//
//                                        }
//                                    });
////
////                                    Log.e("count_chil","count_chil45"+set1);
//
////                                    Set<String> set1 = new HashSet<String>();
////                                    Iterator i =dataSnapshot.getChildren().iterator();
////
////                                    while (i.hasNext()){
////                                        set1.add(((DataSnapshot)i.next()).getKey());
////                                        String check_empty = dataSnapshot.getChildren().toString();
////
////                                        Log.e("gvhgac",""+check_empty);
////  bussy.child(check_empty).child("MEMBERS").addChildEventListener(new ChildEventListener() {
////                                            @Override
////                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////
////                                            }
////
////                                            @Override
////                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
////
////                                            }
////
////                                            @Override
////                                            public void onChildRemoved(DataSnapshot dataSnapshot) {
////
////                                            }
////
////                                            @Override
////                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
////
////                                            }
////
////                                            @Override
////                                            public void onCancelled(DatabaseError databaseError) {
////
////                                            }
////                                        });
////
////
////                                    }
////
////                                    String check_bussy_node = set1.toString();
////                                    Log.e("check_bussy_node",""+check_bussy_node);
//                                }
//
//                                @Override
//                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//
//
//                 //           Log.e("count_chil","count_chil"+count_chil);
//
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        online_child.orderByKey().equalTo(get_token).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("working_proper","done1"+dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("working_proper","done2");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("working_proper","done3"+dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void moveFirebaseRecord(DatabaseReference fromPath, final DatabaseReference toPath)
    {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null)
                        {
                            System.out.println("Copy failed");
                        }
                        else
                        {
                            System.out.println("Success");
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError firebaseError)
            {
                System.out.println("Copy failed");
            }
        });
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_abuse_dialog);
        dialog.show();



        final CheckBox checkbox_abusive = (CheckBox)dialog.findViewById(R.id.checkbox_abusive);
        final CheckBox checkbox_nudity = (CheckBox)dialog.findViewById(R.id.checkbox_nudity);
        final CheckBox checkbox_spammer = (CheckBox)dialog.findViewById(R.id.checkbox_spammer);
        final EditText edit_msg = (EditText)dialog.findViewById(R.id.edit_msg);
        Button submit = (Button)dialog.findViewById(R.id.submit);

        checkbox_abusive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_abusive.isChecked()){
                    s_checkbox_abusive = "Abusive";
                }
                else{
                    s_checkbox_abusive = "";
                }
            }
        });

        checkbox_nudity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_nudity.isChecked()){
                    s_checkbox_nudity = "Abusive";
                }
                else{

                    s_checkbox_nudity = "";
                }
            }
        });

        checkbox_spammer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_spammer.isChecked()){
                    s_checkbox_spammer = "spammer";
                }
                else{

                    s_checkbox_spammer = "";
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                s_edit_msg = s_checkbox_abusive+s_checkbox_nudity+s_checkbox_spammer+edit_msg.getText().toString();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.setPackage("com.google.android.gm");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"heystrangr@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "For Query");
                i.putExtra(Intent.EXTRA_TEXT   , ""+s_edit_msg);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                    Toast.makeText(context, "Your message is sent...", Toast.LENGTH_SHORT).show();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Home.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.invite_nearn_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Check it out. Your message goes here";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void get_online_nodes(){

        online_child.orderByValue().equalTo("online").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i =dataSnapshot.getChildren().iterator();


                //   Log.e("datasnapshot",""+dataSnapshot.getChildren());
                while (i.hasNext()){
                    //  set.add((String) ((DataSnapshot)i.next()).getValue());
                    set.add(((DataSnapshot)i.next()).getKey());

                    //   x.add(""+(String) ((DataSnapshot)i.next()).getValue());
                    //get_chat_room.add(""+((DataSnapshot)i.next()).getKey());
                }




                get_another_person_key = String.valueOf(set.toArray()[0]);

                Log.e("set_chat123",""+set.toArray()[0]);
//              get_chat_room = String.valueOf(set.toArray()[0]);
                //     Log.e("set_chat_rooms:s",""+get_chat_room);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}

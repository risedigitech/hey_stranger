package com.houseofapps.heystranger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ChatScreen1 extends AppCompatActivity {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    EditText edit_msg;
    ImageButton btn_ok;
    TextView status_type;
    String temp_key = "";
    LinearLayout.LayoutParams layoutparams;
    LinearLayout ll_m,custom_layout;
    NestedScrollView scrollView;
    String chat_timestamp = "";
    MarshMallowPermission mallowPermission;
    private ProgressDialog pDialog;
    public static final String MyPRef = "HeyStranger";
    SharedPreferences.Editor preferences;
    TextView textView1;
    String notification_msg = "";
    LinearLayout bef_connect;
    String second_node_status = "";
    String get_token = "";
    Context context;
    String check_node_key = "";
    DatabaseReference root;
    DatabaseReference online_child;
    DatabaseReference bussy;
    ImageView imageView;
    String me_node = "";
    String you_node = "";
    String chat_username = "";
    String chat_msg = "";
    String timsestamp = "";
    String check = "0";
    DatabaseReference find_chat_room;
    DatabaseReference move_node;
    DatabaseReference Done_chats;
    String check_not = "";
    DatabaseReference remove_node;
    String check_status = "0";
    Bitmap decodedByte;
    ImageView report_abs,cancel;
    TextView tv_timestamp;
    String s_checkbox_abusive = "",s_checkbox_nudity="",s_checkbox_spammer = ""  ,s_edit_msg ="" ;
String check_connect_another = "";
    DatabaseReference checktyping_status;
String check_2 = "0";
int note_count = 0;

     List<ChatMessage> items = new ArrayList<>();
    List<ChatMessage> chats = new ArrayList<>();
    public static ChatAdapter mAdapter;
    String final_check_to_fcm_noti = "1";

String check_on_pause_working = "0";
    int check_counters = 0;
    RecyclerView chat_recycler;
    SharedPreferences pref_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        context = this;
        mallowPermission = new MarshMallowPermission(this);
        initialization_ids();
        showpDialog();
        check_connect_another = "0";
        get_token =   Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        online_child = FirebaseDatabase.getInstance().getReference().getRoot().child("ONLINE_DEVICES");
        bussy = FirebaseDatabase.getInstance().getReference().getRoot().child("BUSY_NODES");
        find_chat_room = FirebaseDatabase.getInstance().getReference().child("BUSY_NODES").child(get_token).getParent();
        Log.e("find_chat_room",""+find_chat_room);
        preferences.putString("delete_chatroom",""+get_token);
        preferences.putString("check_which","1");
        preferences.commit();
        startService(new Intent(getBaseContext(),ExitService.class));
        find_chat_room.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("dataSnapshot123",""+dataSnapshot.getKey());

                if (check.equals("0")){
                    check_node_key = ""+dataSnapshot.getKey();
                    check = "1";
                    check_on_pause_working = "1";
                    move_node = bussy.child(check_node_key);
                    Done_chats = FirebaseDatabase.getInstance().getReference().getRoot().child("DONE_CHATS").child(check_node_key);
                    remove_node = FirebaseDatabase.getInstance().getReference().getRoot().child("BUSY_NODES").child(check_node_key);
                    //checktyping_status = FirebaseDatabase.getInstance().getReference().getRoot().child("BUSY_NODES").child(check_node_key).child("STATUS");
                    preferences.putString("delete_chatroom",""+check_node_key);
                    preferences.putString("check_which","0");
                    preferences.commit();
                    startService(new Intent(getBaseContext(),ExitService.class));
                    get_chat_history();
                    hidepDialog();
                    status_type.setText("Online");
//                    check_status_type();
//                    test_typing();

                }

                // Log.e("dataSnapshot123",""+dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            get_listeners();
    }

    public void showDialog_() {

        final Dialog dialog = new Dialog(ChatScreen1.this);
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
                if (s_edit_msg.equals("")){dialog.dismiss();}else {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.setPackage("com.google.android.gm");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"heystrangr@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, "For Query");
                    i.putExtra(Intent.EXTRA_TEXT, "" + s_edit_msg);
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                        Toast.makeText(context, "Your message is sent...", Toast.LENGTH_SHORT).show();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ChatScreen1.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private void get_listeners() {
        check_status = "0";
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_();
            }
        });
        report_abs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (edit_msg.getText().toString().equals("")){
                    }

                    else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        temp_key = root.push().getKey();
                        root.updateChildren(map);
                        DatabaseReference mag_root = root.child(temp_key);
                        Map<String, Object> map2 = new HashMap<String, Object>();
                        map2.put("NAME", "" + get_token);
                        map2.put("DATA_TYPE", "Text");
                        map2.put("TIME_STAMP", ""+timsestamp);
                        map2.put("MSG", "" + edit_msg.getText().toString());
                        edit_msg.setText("");
                        edit_msg.requestFocus();
                        mag_root.updateChildren(map2);
                    }

            }
        });


        //==================     *\send image/*     ====================

        edit_msg.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(edit_msg)
        {
            @Override
            public boolean onDrawableClick()
            {
                askForCameraPermission();
                return true;
            }
        } );

//==================     *\status/*     ====================
//        edit_msg.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                DatabaseReference status_type = remove_node.child("STATUS");
////                Map<String,Object> map8 = new HashMap<String,Object>();
////                map8.put(""+get_token,"Online");
////                status_type.updateChildren(map8);
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                   // check_status_type();
////                DatabaseReference status_type = remove_node.child("STATUS");
////                Map<String,Object> map8 = new HashMap<String,Object>();
////                map8.put(""+get_token,"Typing");
////                status_type.updateChildren(map8);
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
////                DatabaseReference status_type = remove_node.child("STATUS");
////                Map<String,Object> map8 = new HashMap<String,Object>();
////                map8.put(""+get_token,"Online");
////                status_type.updateChildren(map8);
//            }
//        });
//==================     *\send image/*     ====================
    }

   public void test_typing(){
        checktyping_status.child(second_node_status).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String data = dataSnapshot.getValue().toString();
                status_type.setText(""+data);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String data = dataSnapshot.getValue().toString();
                status_type.setText(""+data);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void check_status_type() {
        checktyping_status.orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               // String status = (String) dataSnapshot.child(""+get_token).getValue();
                String status = (String) dataSnapshot.getKey();
                Log.e("jhbsv",""+status);
                if (status.equals(""+get_token)){

                }
                else{
                    second_node_status = status;
                }

//                chat_msg =  (String) dataSnapshot.child("MSG").getValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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

        private void get_chat_history() {
        root = FirebaseDatabase.getInstance().getReference().child("BUSY_NODES").child(check_node_key).child("CHATROOM");
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("who_is_calling","onChildAdded is calling");
                append_chat_convewrsation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e("who_is_calling","onChildChanged is calling");
                append_chat_convewrsation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot123","onmoved_working");
                //moveFirebaseRecord(move_node,Done_chats);
               // remove_node.removeValue();

                if (check_connect_another.equals("0")){
                Intent intent = new Intent(ChatScreen1.this,Home.class);
                intent.putExtra("chat_again_check","1");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();}

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("dataSnapshot123","onChildRemoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume(){
        check_status = "1";
        Log.e("Activity_lifecycle","onResume");
        super.onResume();
    }
    @Override
    public void onPause() {

        check_status = "0";
        super.onPause();
    }

    @Override
    public void onStop () {
        Log.e("Activity_lifecycle","onStop");
        super.onStop();
        check_status = "0";
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
                            if (check_connect_another.equals("0")){
                            remove_node.setValue(null);
                            Intent intent = new Intent(ChatScreen1.this,Home.class);
                            intent.putExtra("chat_again_check","1");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            System.out.println("Success");}
                            else{}
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

    public void onBackPressed() {
        open();
    }
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Leave Chat");
        alertDialogBuilder.setMessage("Are you sure, You wanted to leave ");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        moveFirebaseRecord(move_node,Done_chats);
                    }
                });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                finish();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    public void open_(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Leave Chat");
        alertDialogBuilder.setMessage("Are you sure want to connect with another user");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        check_connect_another = "1";
                        check_on_pause_working = "0";
                        moveFirebaseRecord(move_node,Done_chats);
                        remove_node.removeValue();

                        call_check();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//      finish();

            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void call_check() {

showpDialog();

        Log.e("working123456","dfghjkl");
        online_child.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i =dataSnapshot.getChildren().iterator();
                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }
                String check_online_node = set.toString();
                Log.e("check_online_node",""+check_online_node);
                if (set.toArray().length ==0&&check_2.equals("0")){
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put(""+get_token,"online");
                    online_child.updateChildren(map);
                    check_2 = "1";
                    hidepDialog();
                    Intent intent = new Intent(ChatScreen1.this,ChatScreen1.class);
                    startActivity(intent);
                }

                else{
                    if (check_2.equals("0")) {
                        check_2 = "1";
                        try {
                            JSONArray jsonArray = new JSONArray(check_online_node);
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
                            hidepDialog();
                            Intent intent = new Intent(ChatScreen1.this,ChatScreen1.class);
//                            intent.putExtra("me",""+get_token);
//                            intent.putExtra("you",""+get_node_2);
//                            intent.putExtra("check_node_key",temp_key);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void showNotification(Context context, String title, String message) {
        Intent notifyIntent = new Intent(context, ChatScreen1.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    public void onDestroy(){
        Log.e("Activity_lifecycle","onDestroy");
//        delete_chatroom.removeValue();
//        delete_node.removeValue();
//        Intent intent = new Intent(ChatScreen.this, Home.class);
//        intent.putExtra("chat_again_check","1");
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
        super.onDestroy();
    }

    private void initialization_ids() {
        edit_msg = (EditText)findViewById(R.id.edit_msg);
        ll_m = (LinearLayout)findViewById(R.id.l1_m);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        bef_connect = (LinearLayout)findViewById(R.id.bef_connect);
        btn_ok = (ImageButton)findViewById(R.id.btn_ok);
        report_abs = (ImageView)findViewById(R.id.report_abs);
        cancel = (ImageView)findViewById(R.id.cancel);
        status_type = (TextView)findViewById(R.id.status_type);
        chat_recycler = (RecyclerView)findViewById(R.id.chat_recycler);
        status_type.setText("Looking");
        scrollView.fullScroll(View.FOCUS_DOWN);
        preferences = getSharedPreferences(MyPRef,MODE_PRIVATE).edit();
        pref_ = getSharedPreferences(MyPRef,MODE_PRIVATE);
        if (pref_.contains("FCM_ON_OFF")){
            final_check_to_fcm_noti = pref_.getString("FCM_ON_OFF",null);
        }
        else{final_check_to_fcm_noti = "1";}


//        scrollView.scrollTo(0, scrollView.getBottom());
        chat_recycler.setNestedScrollingEnabled(false);
//        chat_recycler.setFocusable(true);
//        chat_recycler.requestFocus();

        layoutparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Looking for Stranger...");
        pDialog.setCancelable(false);

        layoutparams.setMargins(5,5,5,5);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        timsestamp = simpleDateFormat.format(new Date());

    }


    @SuppressLint("WrongConstant")
    private void append_chat_convewrsation(DataSnapshot dataSnapshot) {
        String get_key = dataSnapshot.getKey();
        ChatMessage tempMsg = null;
        Log.e("get_key",""+get_key);
        bef_connect.setVisibility(View.GONE);
        chat_username = (String) dataSnapshot.child("NAME").getValue();
        chat_msg =  (String) dataSnapshot.child("MSG").getValue();
        String data_type = (String) dataSnapshot.child("DATA_TYPE").getValue();
        chat_timestamp = (String) dataSnapshot.child("TIME_STAMP").getValue();
        note_count++;
        String [] temp_stamp;
        temp_stamp = chat_timestamp.split("-");
        tempMsg = new ChatMessage(chat_username, chat_msg, ""+temp_stamp[3]+":"+""+temp_stamp[4], data_type,get_token);
        chats.add(tempMsg);
        mAdapter = new ChatAdapter(ChatScreen1.this, chats,chat_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(true);
       chat_recycler.setLayoutManager(layoutManager);
       chat_recycler.setAdapter(mAdapter);
//        chat_recycler.setFocusable(true);
      //chat_recycler.requestFocus(View.FOCUS_DOWN);
//     scrollView.requestFocus(View.FOCUS_DOWN);

       mAdapter.notifyDataSetChanged();

       if (data_type.equals("Text")){
            notification_msg = ""+chat_msg;
       }

        else if (data_type.equals("Image")){
            notification_msg = "Sent You Image";
        }
        if (final_check_to_fcm_noti.equals("1")) {
            if (check_status.equals("0")) {
                showNotification(context, "Hey Stranger", "" + notification_msg);
            }
        }
    }
    private Target mTarget;

    //===================================== *image upload* ==============================================================
    public void askForCameraPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if ((! mallowPermission.checkPermissionForExternalStorage())|| (!mallowPermission.checkPermissionForCamera() )) {
                mallowPermission.requestPermissionForExternalStorage();
                mallowPermission.requestPermissionForCamera();

            }else {
                showDialog();}
        } else {
            showDialog();
        }
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(ChatScreen1.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog);
        dialog.show();
        LinearLayout camera_Roll = (LinearLayout) dialog.findViewById(R.id.takePic);
        camera_Roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
                dialog.dismiss();
            }
        });

        LinearLayout takePicture = (LinearLayout) dialog.findViewById(R.id.gallaryPic);
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
                dialog.dismiss();
            }
        });
        TextView textView_Cancel = (TextView) dialog.findViewById(R.id.canCel);
        textView_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = tsLong.toString();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+timestamp, null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String myfilePath = picturePath;
            Log.e("IMAGE_PATH",""+myfilePath);
            Bitmap bm = BitmapFactory.decodeFile(getRealPathFromUri(ChatScreen1.this,selectedImage));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            final String IMAGE_PATH  = Base64.encodeToString(b, Base64.DEFAULT);
            Map<String, Object> map = new HashMap<String, Object>();
            temp_key = root.push().getKey();
            root.updateChildren(map);
            DatabaseReference mag_root = root.child(temp_key);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("NAME", "" + get_token);
            map2.put("DATA_TYPE", "Image");
            map2.put("TIME_STAMP", ""+timsestamp);
            map2.put("MSG", "" +IMAGE_PATH);
            edit_msg.setText("");
            mag_root.updateChildren(map2);

        }

        else if (requestCode == 1 && resultCode == RESULT_OK)
            try {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    Log.e("IMAGE_PATH_ImaUri",""+selectedImageUri);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri uri = getImageUri(getApplicationContext(),photo);
                    Log.e("IMAGE_PATH_uri",""+uri);
                    String IMAGE_PATH_ = getRealPathFromUri(ChatScreen1.this,uri);
                    Log.e("IMAGE_PATH_",""+IMAGE_PATH_);
                    Bitmap bm = BitmapFactory.decodeFile(getRealPathFromUri(ChatScreen1.this,uri));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String IMAGE_PATH  = Base64.encodeToString(b, Base64.DEFAULT);

                    Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);
                    DatabaseReference mag_root = root.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("NAME", "" + get_token);
                    map2.put("DATA_TYPE", "Image");
                    map2.put("TIME_STAMP", ""+timsestamp);
                    map2.put("MSG", "" +IMAGE_PATH);
                    edit_msg.setText("");
                    mag_root.updateChildren(map2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        //===============================================================================================================

    }


    public static final String insertImage(ContentResolver cr,
                                           Bitmap source,
                                           String title,
                                           String description) {

        ContentValues values = new ContentValues();
        values.put(Images.Media.TITLE, title);
        values.put(Images.Media.DISPLAY_NAME, title);
        values.put(Images.Media.DESCRIPTION, description);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F,Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }
        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();
        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();
        matrix.setScale(scaleX, scaleY);
        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );
        ContentValues values = new ContentValues(4);
        values.put(Images.Thumbnails.KIND,kind);
        values.put(Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
    //====================================== */ image upload /* =========================================================

}

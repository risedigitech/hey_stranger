package com.houseofapps.heystranger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ExitService extends Service
{ @Nullable
    SharedPreferences preferences;
        DatabaseReference delete_chatroom ;
    public static final String MyPRef = "HeyStranger";
    String get_delete_node = "";
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    DatabaseReference move_node;
    DatabaseReference Done_chats;
    String check_which ="";
    Context context;
    public static final String check_exit_service = "1";

@Override public IBinder onBind(Intent intent) { return null; }

    @Override public int onStartCommand(Intent intent, int flags, int startId)
    { Log.d("MSG", "Service Started"); return START_NOT_STICKY; }

    @Override public void onDestroy() { super.onDestroy(); Log.d("MSG", "Service Destroyed"); }

    @Override public void onTaskRemoved(Intent rootIntent) {

        preferences = getApplicationContext().getSharedPreferences(MyPRef,MODE_PRIVATE);
        get_delete_node = preferences.getString("delete_chatroom",null);
        check_which = preferences.getString("check_which",null);
        Log.e("MSG", "END"+get_delete_node);

        if (check_which.equals("0")) {

            move_node = FirebaseDatabase.getInstance().getReference().getRoot().child("BUSY_NODES").child(get_delete_node);
            Done_chats = FirebaseDatabase.getInstance().getReference().getRoot().child("DONE_CHATS").child(get_delete_node);
            moveFirebaseRecord(move_node, Done_chats);
            delete_chatroom = FirebaseDatabase.getInstance().getReference().child("BUSY_NODES").child(get_delete_node);
            delete_chatroom.removeValue();
        }

        else if (check_which.equals("1")){
            delete_chatroom = FirebaseDatabase.getInstance().getReference().child("ONLINE_DEVICES").child(get_delete_node);
            delete_chatroom.removeValue();
        }

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
                            Log.e("jhbvfcvhjbash","sbvdhjbs");

                            Toast.makeText(ExitService.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                           System.out.println("Copy failed");
                        }
                        else
                        {

                            Log.e("jhbvfcvhjbash","561995151");
                          delete_chatroom.removeValue();
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

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
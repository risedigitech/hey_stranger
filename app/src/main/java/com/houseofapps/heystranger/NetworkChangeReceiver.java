package com.houseofapps.heystranger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by abc on 20/04/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(final Context context, final Intent intent) {
            this.context = context;
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){

                showDialog();

            }else{

            }

        }
    }


    public void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Network Loss");

        // Setting Dialog Message
        alertDialog.setMessage("No Network Connection available!!!");

        // Setting Icon to Dialog


        // Setting Positive "Yes" Button

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event

                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


}
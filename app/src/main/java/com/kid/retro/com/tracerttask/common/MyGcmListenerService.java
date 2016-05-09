package com.kid.retro.com.tracerttask.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.kid.retro.com.tracerttask.MainActivity;
import com.kid.retro.com.tracerttask.R;
import com.kid.retro.com.tracerttask.SplashActivity;

import java.util.Date;


public class MyGcmListenerService extends GcmListenerService {


    private static int id = 0;
    public static String latitude;
    public static String longitude;

    private static String TAG = MyGcmListenerService.class.getSimpleName();
    String message;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        if(data.containsKey("message")){
            message = data.getString("message");
        }

        if (message != null) {
            if (message.contains("Shared location with you")) {
                id = 1;
                latitude = data.getString("latitude");
                longitude = data.getString("longitude");
            } else {
                id = 2;
            }
            sendNotification(from, message);
        }


        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        // [END_EXCLUDE]
    }

    private void sendNotification(String message, String body) {
        /*Intent intent = new Intent(this, MainActivity_Chat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
        */


       /* Context context = getBaseContext();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)

                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)

                .setContentText(body);

        NotificationManager mNotificationManager = (NotificationManager) context

                .getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());*/
        Context context = getBaseContext();

        int icon = R.drawable.ring;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, body, when);
        String titleMsg;
        if (body != null && !body.equals("null")) {
            titleMsg = body.toString();
        } else {
            titleMsg = " ";
        }


        Date now = new Date();
        long uniqueId = now.getTime();
        long uniqueIdNew = now.getTime() + 1;

        Intent notificationIntent;
        if (AppSettings.getisLoggedIn(context)) {
            notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra("id", id);

            if (id == 1) {
                notificationIntent.putExtra("latitude", latitude);
                notificationIntent.putExtra("longitude", longitude);
            }
        } else {
            notificationIntent = new Intent(context, SplashActivity.class);
        }
        // notificationIntent.addFlags()

        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // updateMyActivity(context, "unique_name");
        SharedPreferences mpref = context.getSharedPreferences("My pref", 0);
        SharedPreferences.Editor edt = mpref.edit();
        edt.putString("NotificationMsg", body);
        edt.putBoolean("isNotificationRecieved", true);
        edt.commit();
       /* notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.setAction("com.root.gcmtask" + uniqueId);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ring)

                .setContentTitle(titleMsg).setContentText(body)
                .setAutoCancel(true).setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setColor(Color.BLUE)
                .setContentIntent(intent).setWhen(when);

        //notification.setLatestEventInfo(context, title, message, intent);
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
        mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify((int) uniqueIdNew, mBuilder.build());
    }

}
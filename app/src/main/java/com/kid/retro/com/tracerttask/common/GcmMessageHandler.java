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
    import android.util.Log;

    import com.google.android.gms.gcm.GcmListenerService;
    import com.kid.retro.com.tracerttask.MainActivity;
    import com.kid.retro.com.tracerttask.R;

    import java.util.Date;

    /**
 * Created by mayur on 17/11/15.
 */
public class GcmMessageHandler extends GcmListenerService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;



    @Override

    public void onMessageReceived(String from, Bundle data) {

        String message = data.getString("message");

        Log.e("Message####",""+data.toString());

        createNotification(from, message);



    }



    // Creates notification based on title and body received

    private void createNotification(String title, String body) {

        Context context = getBaseContext();

        int icon = R.drawable.ring;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, body, when);
        String titleMsg;
        if (body != null && !body.equals("null")) {
            titleMsg = body.toString();
        }else{
             titleMsg = " ";
        }


        Date now = new Date();
        long uniqueId = now.getTime();
        long uniqueIdNew = now.getTime() + 1;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        // updateMyActivity(context, "unique_name");
        SharedPreferences mpref = context.getSharedPreferences("My pref", 0);
        SharedPreferences.Editor edt = mpref.edit();
        edt.putString("NotificationMsg", body);
        edt.putBoolean("isNotificationRecieved", true);
        edt.commit();
       /* notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        notificationIntent.setAction("com.academic.pulse" + uniqueId);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ring)

                .setContentTitle(titleMsg).setContentText(body)
                .setAutoCancel(true).setLights(Color.RED, 3000, 3000)
                .setSound(alarmSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
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

                /*
                int icon = R.drawable.app_icon;
                long when = System.currentTimeMillis();
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(icon, message, when);
                String title = context.getString(R.string.app_name);

                Date now = new Date();
                long uniqueId = now.getTime();
                long uniqueIdNew = now.getTime() + 1;

                Intent notificationIntent = new Intent(context, MainActivity_Chat.class);
                // updateMyActivity(context, "unique_name");
                SharedPreferences mpref = context.getSharedPreferences("My pref", 0);
                Editor edt = mpref.edit();
                    edt.putString("NotificationMsg", message);
                            edt.putBoolean("isNotificationRecieved", true);
                            edt.commit();
                            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            notificationIntent.setAction("com.zaptech.photobug" + uniqueId);
                            Uri alarmSound = RingtoneManager
                            .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            PendingIntent intent = PendingIntent.getActivity(context, 0,
                            notificationIntent, 0);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            context).setSmallIcon(R.drawable.app_icon)

                            .setContentTitle(title).setContentText(message)
                            .setAutoCancel(true).setLights(Color.RED, 3000, 3000)
                            .setSound(alarmSound)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            .setColor(context.getResources().getColor(R.color.credit_color))
                            .setContentIntent(intent).setWhen(when);

                //notification.setLatestEventInfo(context, title, message, intent);
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                            notification.defaults |= Notification.DEFAULT_SOUND;
                            notification.defaults |= Notification.DEFAULT_LIGHTS;
                            notification.defaults |= Notification.FLAG_SHOW_LIGHTS;
                            mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

                            notificationManager.notify((int) uniqueIdNew, mBuilder.build());
                */

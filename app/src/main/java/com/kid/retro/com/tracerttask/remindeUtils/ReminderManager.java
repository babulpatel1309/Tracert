package com.kid.retro.com.tracerttask.remindeUtils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReminderManager {

    private Context mContext;
    private AlarmManager mAlarmManager;

    public ReminderManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(Long taskId, Calendar when) {

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long) taskId);

        final int _id = (int) System.currentTimeMillis();
        PendingIntent pi = PendingIntent.getBroadcast(mContext, _id, i, PendingIntent.FLAG_ONE_SHOT);

        mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);

        /*AlarmManager mgrAlarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

        //for(i = 0; i < 10; ++i)
        for (int j = 0; j < 10; j++) {
            Intent intent = new Intent(mContext, OnAlarmReceiver.class);
            // Loop counter `i` is used as a `requestCode`
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, j, intent,PendingIntent.FLAG_ONE_SHOT);
            // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
            mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 60000 * j,
                    pendingIntent);

            intentArray.add(pendingIntent);
        }*/
    }
}

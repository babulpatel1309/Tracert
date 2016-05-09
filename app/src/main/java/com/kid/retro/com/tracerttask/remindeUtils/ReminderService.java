package com.kid.retro.com.tracerttask.remindeUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.kid.retro.com.tracerttask.R;


public class ReminderService extends WakeReminderIntentService {

	public ReminderService() {
		super("ReminderService");
			}

	@SuppressWarnings("deprecation")
	@Override
	void doReminderWork(Intent intent) {
		Log.d("ReminderService", "Doing work.");
		Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

		NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(this, ReminderActivity.class);
		notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);

		PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

		/*Notification note=new Notification(R.drawable.icon, getString(R.string.notify_new_task_message), System.currentTimeMillis());
		note.setLatestEventInfo(this, getString(R.string.notify_new_task_title), getString(R.string.notify_new_task_message), pi);

		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags |= Notification.FLAG_AUTO_CANCEL;

		// An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
		// I highly doubt this will ever happen. But is good to note.
		int id = (int)((long)rowId);
		mgr.notify(id, note);*/


		Notification.Builder builder = new Notification.Builder(ReminderService.this);
		builder.setSmallIcon(R.drawable.tracert_eye)
				.setContentTitle(getString(R.string.notify_new_task_title))
		.setContentIntent(pi);

		Notification notification = builder.getNotification();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		int id = (int)((long)rowId);
		mgr.notify(id, notification);
		//mgr.notify(R.drawable.notification_template_icon_bg, notification);
	}
}

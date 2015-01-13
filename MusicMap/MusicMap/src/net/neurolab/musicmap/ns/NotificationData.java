package net.neurolab.musicmap.ns;

import net.neurolab.musicmap.MainActivity;
import net.neurolab.musicmap.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationData<PollTask> {

	public static NotificationManager mNotificationManager;
	public static int SIMPLE_NOTFICATION_ID;

	public NotificationData() {

	}

	public void clearNotification() {
		mNotificationManager.cancel(SIMPLE_NOTFICATION_ID);
	}

	public void showNotification(Context context) {
		System.out.println("shownotification");
		Log.i("context", context.toString());
		String title = context.getString(R.string.dbUpdate_title);
		String text = context.getString(R.string.dbUpdate_text);
		
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(text);
		mBuilder.setContentIntent(pi);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}

}

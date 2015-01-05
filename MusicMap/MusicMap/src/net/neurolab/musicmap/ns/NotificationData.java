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

	/*
	 * @SuppressWarnings("deprecation") public void SetNotification(int
	 * drawable, String msg, String action_string, Class cls) {
	 * mNotificationManager = (NotificationManager)
	 * _context.getSystemService(Context.NOTIFICATION_SERVICE); final
	 * Notification notifyDetails = new Notification(drawable, "Post Timer",
	 * System.currentTimeMillis()); long[] vibrate = { 100, 100, 200, 300 };
	 * notifyDetails.vibrate = vibrate; notifyDetails.ledARGB = 0xff00ff00;
	 * notifyDetails.ledOnMS = 300; notifyDetails.ledOffMS = 1000; //
	 * notifyDetails.number=4; notifyDetails.defaults =Notification.DEFAULT_ALL;
	 * Context context = _context; CharSequence contentTitle = msg; CharSequence
	 * contentText = action_string; Intent notifyIntent = new Intent(context,
	 * cls); Bundle bundle = new Bundle(); //
	 * bundle.putBoolean(AppConfig.IS_NOTIFICATION, true);
	 * notifyIntent.putExtras(bundle); PendingIntent intent =
	 * PendingIntent.getActivity(_context, 0,notifyIntent,
	 * android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
	 * notifyDetails.setLatestEventInfo(context, contentTitle, contentText,
	 * intent); mNotificationManager.notify(SIMPLE_NOTFICATION_ID,
	 * notifyDetails); }
	 */

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

/*
 * NotificationData notification; //create object notification = new
 * NotificationData(this); notification.SetNotification(R.drawable.notification,
 * "Notification Title", "Click to open", YourClassName.class);
 */
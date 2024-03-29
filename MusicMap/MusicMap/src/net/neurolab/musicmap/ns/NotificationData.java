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

/**
 * 
 * @author Ljiljana
 *
 * @param <PollTask>
 * 
 *            NotificationData is a class which is used to create and show
 *            Notification when database is updated.
 */

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

		/**
		 * Create an intent for a specific component. All other fields (action,
		 * data, type, class) are null, though they can be modified later with
		 * explicit calls. This provides a convenient way to create an intent
		 * that is intended to execute a hard-coded class name, rather than
		 * relying on the system to find an appropriate class for you; see
		 * setComponent for more information on the repercussions of this.
		 */
		Intent intent = new Intent(context, MainActivity.class);

		/**
		 * A description of an Intent and target action to perform with it.
		 * Instances of this class are created with getActivity, getActivities,
		 * getBroadcast, and getService; the returned object can be handed to
		 * other applications so that they can perform the action you described
		 * on your behalf at a later time.
		 * 
		 * By giving a PendingIntent to another application, you are granting it
		 * the right to perform the operation you have specified as if the other
		 * application was yourself (with the same permissions and identity). As
		 * such, you should be careful about how you build the PendingIntent:
		 * almost always, for example, the base Intent you supply should have
		 * the component name explicitly set to one of your own components, to
		 * ensure it is ultimately sent there and nowhere else.
		 * 
		 * A PendingIntent itself is simply a reference to a token maintained by
		 * the system describing the original data used to retrieve it. This
		 * means that, even if its owning application's process is killed, the
		 * PendingIntent itself will remain usable from other processes that
		 * have been given it. If the creating application later re-retrieves
		 * the same kind of PendingIntent (same operation, same Intent action,
		 * data, categories, and components, and same flags), it will receive a
		 * PendingIntent representing the same token if that is still valid, and
		 * can thus call cancel to remove it.
		 */
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

package net.neurolab.musicmap.ns;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * 
 * @author Ljiljana
 *
 *         BootReceiver is a class that extends BroadcastReceiver and is used to
 *         set alarm when BOOT_COMPLETED. In our case intent will always be
 *         BOOT_COMPLETED, so we can just set the alarm. BroadcastReceiver is
 *         *NOT* a Context. Thus, we can't use "this" whenever we need to pass a
 *         reference to the current context. But, Android will supply a valid
 *         Context as the first parameter.
 */
public class BootReceiver extends BroadcastReceiver {
	
	public void onReceive(Context context, Intent intent) {
		int minutes = 5 * 60;
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		Intent i = new Intent(context, NotificationService.class);
		PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
		am.cancel(pi);
		
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + minutes * 60 * 1000,
					minutes * 60 * 1000, pi);
		}
	}
}
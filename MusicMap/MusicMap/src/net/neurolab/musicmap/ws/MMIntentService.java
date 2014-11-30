package net.neurolab.musicmap.ws;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

public class MMIntentService extends IntentService {
	
	private final int STATUS_RUNNING = 0;
	private final int STATUS_FINISHED = 1;
	private final int STATUS_ERROR = -1;
	
	public MMIntentService() {
		super("MMWebServices");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Parcelable[] results = null;
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		String command = intent.getStringExtra("command");
		Bundle bundle = new Bundle();
		if (command.equals("addFbUser")) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			try {
				
				bundle.putParcelableArray("results", results);
				receiver.send(STATUS_FINISHED, bundle);
				
			}
			catch(Exception e) {
				bundle.putString(Intent.EXTRA_TEXT, e.toString());
				receiver.send(STATUS_ERROR, bundle);
			}
		}
		
		// TODO Auto-generated method stub
		
		/*
		idHash = String.valueOf(id.hashCode());
		idHash = idHash.replace("-", "0");
		MMAsyncTask mmTask = new MMAsyncTask();
		Object mmParams[] = new Object[]{"fbUser", "add", id, idHash};
		Log.i("presenter - hash", idHash);
		mmTask.execute(mmParams);
		*/
	}

	

}

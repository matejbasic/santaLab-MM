package net.neurolab.musicmap.ws;

/**
 * 
 * @author Ljiljana
 *
 *         MMAsyncResultHandler is an interface containing handleResult method
 *         used in MMAsyncTask onPostExecute method, and in DataLoaderMM and
 *         NotificationService to handle httpGetRequest result (JSON string)
 *         which means to save data to app db.
 */
public interface MMAsyncResultHandler {
	public void handleResult(String result, Boolean status);
}

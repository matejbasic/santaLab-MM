package net.neurolab.musicmap.ws;

/**
 * Interface for handler that manages
 * feedback from FlickrAsyncTask.
 * @author Basic
 *
 */
public interface FlickrAsyncResultHandler {
	public void handleResult(String imgUrl);	
}


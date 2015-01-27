package net.neurolab.musicmap.ws;

import java.util.HashMap;
/**
 * Interface for handler that manages
 * feedback from FacebookAsyncTask.
 * @author Basic
 *
 */
public interface FacebookResultHandler {
	
	public void handleResults(HashMap<String, String> userData);
}

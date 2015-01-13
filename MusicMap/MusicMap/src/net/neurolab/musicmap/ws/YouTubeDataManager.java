package net.neurolab.musicmap.ws;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.os.AsyncTask;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

public class YouTubeDataManager {	
	private static YouTube yt;
	private String apiKey = YouTubeDevKey.BROWSER_KEY;
	private YouTube.Search.List search;
	
	public class ytDataTask extends AsyncTask<Object, Void, Object[]> {

		@Override
		protected Object[] doInBackground(Object... params) {
			String query = params[0].toString();
			Object[] response = {(YouTubeDataResultHandler) params[1], ""};
			if ( query != null && !query.isEmpty()) {
				search.setQ(query);
	            
				SearchListResponse searchResponse = null;
	            List<SearchResult> searchResultList = null;
	            
				try {
					searchResponse = search.execute();
					searchResultList = searchResponse.getItems();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
	            
	            if (searchResultList != null) {
	            	Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
	            	while (iteratorSearchResults.hasNext()) {
	
	            		SearchResult singleVideo = iteratorSearchResults.next();
	                    ResourceId rId = singleVideo.getId();
	                    
	                    if (rId.getKind().equals("youtube#video")) {
	                    	response[1] = rId.getVideoId();
	                    	return response;
	                     }
	                 }
	            }
			}
			return null;
		}
		@Override
		protected void onPostExecute(Object[] result) {
			((YouTubeDataResultHandler) result[0]).handleResult(result[1].toString());	
		}
    	
    }
	
	public void setDataTask(String query, YouTubeDataResultHandler handler) {
		ytDataTask ytTask = new ytDataTask();
        ytTask.execute(new Object[]{query, handler});
	}
	
	public YouTubeDataManager() {

		try {		
			yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("net.neurolab.musicmap").build();
			
            search = yt.search().list("id,snippet");
            search.setKey(apiKey);
            search.setType("video");

            //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setFields("items(id/kind,id/videoId)");
            search.setMaxResults((long) 1);
            
        } 
		catch (GoogleJsonResponseException e) {
            System.err.println("service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } 
		catch (IOException e) {
            System.err.println("IO error: " + e.getCause() + " : " + e.getMessage());
        } 
		catch (Throwable t) {
            t.printStackTrace();
        }

	}

}

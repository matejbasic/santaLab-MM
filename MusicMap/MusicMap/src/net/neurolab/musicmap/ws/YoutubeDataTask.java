package net.neurolab.musicmap.ws;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;



public class YoutubeDataTask {	
	private static final String PROP_FILENAME = "youtube.properties";
	private static YouTube yt;
	
	public YoutubeDataTask(String query) {
		/*
		Properties props = new Properties();
		try {
			InputStream in = YoutubeDataTask.class.getResourceAsStream("/" + PROP_FILENAME);
			props.load(in);
		}
		catch(IOException e) {
			e.getStackTrace();
		}
		*/
		try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
			/*
            yt = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();
			*/
			
			yt = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("musicmap").build();
			
            // Prompt the user to enter a query term.
           
            // Define the API request for retrieving search results.
            final YouTube.Search.List search = yt.search().list("id,snippet");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://console.developers.google.com/
            
            //String apiKey = props.getProperty("youtube.apikey");
            String apiKey = YouTubeDevKey.DEVELOPER_KEY;
            search.setKey(apiKey);
            search.setQ(query);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            //search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setFields("items(id/kind,id/videoId)");
            search.setMaxResults((long) 1);
            class ytDataTask extends AsyncTask<Object, Void, Object[]> {

				@Override
				protected Object[] doInBackground(Object... params) {
					// Call the API and print results.
		            SearchListResponse searchResponse = null;
					try {
						searchResponse = search.execute();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            List<SearchResult> searchResultList = searchResponse.getItems();
		            if (searchResultList != null) {
		            	// prettyPrint(searchResultList.iterator(), query);
		            	//FROM PRETTY PRINT
		            	Iterator<SearchResult> iteratorSearchResults = searchResultList.iterator();
		            	while (iteratorSearchResults.hasNext()) {

		            		SearchResult singleVideo = iteratorSearchResults.next();
		                    ResourceId rId = singleVideo.getId();

		                    // Confirm that the result represents a video. Otherwise, the
		                    // item will not contain a video ID.
		                    if (rId.getKind().equals("youtube#video")) {
		                    	Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

		                        System.out.println(" Video Id" + rId.getVideoId());
		                        System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
		                        System.out.println(" Thumbnail: " + thumbnail.getUrl());
		                        System.out.println("\n-------------------------------------------------------------\n");
		                     }
		                 }

		            }
		            else {
		            	Log.i("ytData", "search results = null");
		            }
					return null;
				}
            	
            }
            
            ytDataTask ytTask = new ytDataTask();
            ytTask.execute(new Object[]{});
        } 
		catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } 
		catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } 
		catch (Throwable t) {
            t.printStackTrace();
        }

	}

}

package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.YouTubeFailureRecoveryActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class FragmentYTPlayer extends YouTubeFailureRecoveryActivity {
	private String defVideoId = "nCgQDjiotG0";
	
	@Override
	protected void onCreate(Bundle savedIS) {
		super.onCreate(savedIS);
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}
	
	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,
			boolean wasRestored) {
		if (!wasRestored) {
			player.cueVideo(defVideoId);
		}
		
	}

	@Override
	protected Provider getYouTubePlayerProvider() {
		//return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
		return null;
	}
	
}

package net.neurolab.musicmap.fragments;

import net.neurolab.musicmap.R;
import net.neurolab.musicmap.YouTubeFailureRecoveryActivity;
import net.neurolab.musicmap.ws.YouTubeDevKey;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public class FragmentYTPlayer extends YouTubeFailureRecoveryActivity {
	private String defVideoId = "nCgQDjiotG0";
	
	@Override
	protected void onCreate(Bundle savedIS) {
		// TODO Auto-generated method stub
		super.onCreate(savedIS);
		/*
		setContentView(R.layout.fragment_yt);
		
		YouTubePlayerFragment fPlayer = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
		fPlayer.initialize(ytDevKey.DEVELOPER_KEY, this);
		*/
	}
	
	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
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

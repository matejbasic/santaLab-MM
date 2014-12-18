package net.neurolab.musicmap.interfaces;

import java.util.List;

import net.neurolab.musicmap.db.Genre;
import android.content.Context;

public interface SetPreferencesPresenter {
	public Boolean checkUser();
	public void checkGenres();
	public void setLocation(String name, Context context);
	public void setPreferedGenres(List<Genre> genres);
}

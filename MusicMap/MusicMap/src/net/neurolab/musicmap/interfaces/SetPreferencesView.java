package net.neurolab.musicmap.interfaces;

import java.util.List;

import net.neurolab.musicmap.db.Genre;

public interface SetPreferencesView {
	void checkLocation(String location);
	void setPreferedGenres(List<Genre> genres);
	void navigateToLogin();
	void navigateToSetGenres();
	void navigateToHome();
	void setNoLocationError();
}

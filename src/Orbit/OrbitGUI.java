package Orbit;

import Utils.AudioPlayer;
import game.*;

public interface OrbitGUI {
	
	void setPlayer(int playerNo, String name);
	
	String getPlayerName(int playerNo);
	
	String readPlayerName(int playerNo);
	
	void setField(Field data);
	
	Field getField(int fieldNo);
	
	void SetDice(int left, int right);
	
	void SetDiceType(int sides);
	
	int getDieValue(boolean right);
	
	int getTotalDieValue();
	
	void movePlayer(int playerNo, int fieldNo);
	
	void resetPosition(int playerNo);
	
	int getPlayerPosition(int playerNo);
	
	void setPlayerScore(int playerNo, int score);
	
	int getPlayerScore(int playerNo);
	
	void setScreenText(String text);
	
	void setScreenDesc(String desc);
	
	void moveMouse(int x, int y, boolean clickAfterMove);
	
	int clickButton();
	
	void setButtonVisible(int buttonNo, boolean visible);
	
	AudioPlayer getAudio(final String path);
	
	void addLanguage(String lang);
	
	void changeLanguage(int optionNo, String newLang);
	
	boolean getDieType();
	
	String getLanguage();
	
	void setDieType(boolean isTwentySided);
	
	void setLanguage(int optionNo);
	
	void setOptionsVisible(boolean visible);
	
	void setPlayerLanguage(String title, String placeholder);
	
	void setOptionLanguage(int optionNo, String title);
	
	void setButtonLanguage(int buttonNo, String text);
	
	void setDieLanguage(String text);
	
	void setCounterLanguage(String text);
	
	void setLoaderVisible(boolean visible);
	
	boolean isOldGUI();
	
	void closeOldGUI();
	
	void playVideo(String filename);
	
	void stopVideo();
	
	boolean isVideoPlaying();
	
	void waitForVideoEnded();
	
}
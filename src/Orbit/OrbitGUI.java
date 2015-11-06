package Orbit;

import GameData.*;
import Utils.AudioPlayer;

public interface OrbitGUI {
	
	void setPlayer(int playerNo, String name);
	
	String getPlayerName(int playerNo);
	
	String readPlayerName(int playerNo);
	
	void setField(int fieldNo, Field data);
	
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
	
}
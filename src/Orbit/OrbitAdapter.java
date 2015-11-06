package Orbit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import GameData.Field;
import Utils.AudioPlayer;
import Utils.EventListener;
import Utils.FontManager;
import Utils.MouseController;

public class OrbitAdapter implements OrbitGUI {
	
	private static OrbitView window;
	private static JPanel panel;
	private OrbitTextField[] textfields = new OrbitTextField[12];
	private OrbitDice[] dice = new OrbitDice[2];
	private OrbitStatus[] counter = new OrbitStatus[12];
	private ImageIcon background;
	private int[] position = new int[12];
	private OrbitButton[] button = new OrbitButton[5];
	private OrbitOptionList options;
	private int width;
	private int height;
	private String screentext = "";
	private Font font;
	private int lastButtonClick;
	private OrbitBoard oboard;
	private Field[] board = new Field[40];
	private int boardsize = 40;
	
	public OrbitAdapter(int width, int height, int boardsize) {
		if (window == null) {
			this.width = width;
			this.height = height;
			this.boardsize = boardsize;
			board = new Field[boardsize];
			font = new FontManager("ProFontWindows.ttf", 48).get();
			window = new OrbitView();
			window.setSize(width, height);
			window.setVisible(true);
		}
	}
	
	public OrbitAdapter(int width, int height) {
		this(width, height, 40);
	}
	
	private class OrbitView extends JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public OrbitView() {
			initComponents();
		}
		
		private void initComponents() {
	        // we want a custom ProjectPanel, not a generic JPanel!
	        ProjectPanel jPanel = new ProjectPanel();
	        panel = jPanel;
	        
	        setResizable(false);

	        jPanel.setBackground(new java.awt.Color(0, 0, 0));
	        jPanel.setBorder(BorderFactory.createEmptyBorder());
	        jPanel.addMouseListener(new MouseAdapter() {
	            public void mousePressed(MouseEvent evt) {
	                MousePressed(evt);
	            }
	            public void mouseReleased(MouseEvent evt) {
	                //jPanel2MouseReleased(evt);
	            }
	        });

	        // add the component to the frame to see it!
	        this.setContentPane(jPanel);
	        // be nice to testers..
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        pack();
	    }
		
		private void MousePressed(MouseEvent evt) {
			boolean[] active = new boolean[12];
			boolean isActive = false;
			for (int i = 0; i < 12; i++) {
				if (textfields[i] != null && textfields[i].hasFocus()) {
					active[i] = true;
					isActive = true;
				}
				if (textfields[i] != null && isActive) {
					textfields[i].transferFocus();
				}
				if (textfields[i] != null && !active[i]) {
					textfields[i].focused = 0;
				}
			}
	    }
		
		private class ProjectPanel extends JPanel {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			
			ProjectPanel() {
				setPreferredSize(new Dimension(width, height));
				
				setLayout(null);
				
				MyThread mt = new MyThread();
		        new Thread(mt).start();
		        
		        background = new ImageIcon(getClass().getClassLoader().getResource("images/background.png"));
				
		        add(dice[0] = new OrbitDice(10, 10, "DICE 1", 1));
		        add(dice[1] = new OrbitDice(115, 10, "DICE 2", 1));
		        
		        add(options = new OrbitOptionList(10, 80).add("DICE 20-SIDES", 0).add("LANGUAGE", 2, new String[]{"English", "0"}));
		        
		        add(button[0] = new OrbitButton(10, height - 120, "ROLL DICE", 1, "dicecup.png"));
		        add(button[1] = new OrbitButton(10, height - 80, "RESET GAME", 2));
		        
		        add(button[2] = new OrbitButton(10, height - 180, "BUY FIELD", 0));
		        button[2].setVisible(false);
		        add(button[3] = new OrbitButton(10, height - 220, "PLACE HOUSE", 0));
		        button[3].setVisible(false);
		        add(button[4] = new OrbitButton(10, height - 260, "PLACE HOTEL", 0));
		        button[4].setVisible(false);
		        
		        oboard = new OrbitBoard(width / 2, height / 2, boardsize);
		        
		        for (int i = 0; i < button.length; i++) {
		        	button[i].setEnabled(false);
		        }
		        
			}
			
			@Override
		    public void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setBackground(new Color(0, 0, 0));
		        
		        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        
		        g2d.setColor(new Color(0, 0, 0));
				g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
				g2d.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), background.getImageObserver());
				
				oboard.paintComponent(g);
				
				boolean isHover = false;
				for (int i = 0; i < dice.length; i++) {
					dice[i].paintComponent(g);
				}
				
				for (int i = 0; i < counter.length; i++) {
					if (counter[i] != null) {
						counter[i].paintComponent(g);
					}
				}
				
				for (int i = 0; i < button.length; i++) {
					if (button[i] != null) {
						button[i].paintComponent(g);
						if (button[i].hovered) {
							isHover = true;
						}
					}
				}
				
				options.paintComponent(g);
				
				g2d.setFont(font);
				g2d.setColor(new Color(0, 0, 0, 100));
				g2d.drawString(screentext, width / 2 + 1 - (int)font.getStringBounds(screentext, g2d.getFontRenderContext()).getWidth() / 2, height / 2 + 2);
				g2d.setColor(new Color(35, 185, 185, (int)(150 + dice[0].getOpacity() * 105.0)));
		        g2d.drawString(screentext, width / 2 - (int)font.getStringBounds(screentext, g2d.getFontRenderContext()).getWidth() / 2, height / 2);
				
		        options.paintDropdown(g);
		        if (options.hovered) {
		        	isHover = true;
		        }
		        
				if (isHover) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					setCursor(Cursor.getDefaultCursor());
				}
				
			}
			
		}
		
		private class MyThread implements Runnable {

			@Override
			public void run() {
				while(true) {
					repaint();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	/**
	 * Sets or adds a player name TextField object. Use getPlayerName or readPlayerName to get the player's name.
	 * @param playerNo
	 * @param name
	 */
	@Override
	public void setPlayer(int playerNo, String name) {
		if (playerNo >= 0 && playerNo < 12) {
			if (textfields[playerNo] == null) {
				textfields[playerNo] = new OrbitTextField(width - 170, 10 + playerNo * 110, 30, "PLAYER " + (playerNo + 1), "Type username", 20, "form");
				textfields[playerNo].setEnabled(false);
				textfields[playerNo].setFocusTraversalKeysEnabled(false);
				counter[playerNo] = new OrbitStatus(width - 120, 45 + playerNo * 110, false, "CREDITS", 400);
				position[playerNo] = 0;
				oboard.setPlayer(playerNo, name);
				panel.add(textfields[playerNo]);
			}
			textfields[playerNo].setText(name);
		} else {
			System.err.println("Error: Limit is 12 players.");
		}
	}
	
	/**
	 * Gets a given player's name, either as Player number or their submitted name.
	 * @param playerNo
	 * @return the player's name.
	 */
	@Override
	public String getPlayerName(int playerNo) {
		if (playerNo >= 0 && playerNo < 12) {
			if (textfields[playerNo] == null) {
				if (textfields[playerNo].getText().length() == 0) {
					return "PLAYER " + (playerNo + 1);
				} else {
					return textfields[playerNo].getText();
				}
			} else {
				return "PLAYER " + (playerNo + 1);
			}
		} else {
			System.err.println("Error: Limit is 12 players.");
			return null;
		}
	}
	
	/**
	 * Works like the Scanner class read methods, yielding till the user inputs a value.
	 * The TextField is disabled unless this method is called.
	 * @param playerNo
	 * @return the player's new name.
	 */
	@Override
	public String readPlayerName(int playerNo) {
		if (playerNo >= 0 && playerNo < 12 && textfields[playerNo] != null) {
			textfields[playerNo].setEnabled(true);
			textfields[playerNo].addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {}

				@Override
				public void focusLost(FocusEvent e) {
					if (textfields[playerNo].isEnabled()) {
						textfields[playerNo].setEnabled(false);
					}
				}
				
			});
			textfields[playerNo].addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {}

				@Override
				public void keyPressed(KeyEvent e) {
					if (textfields[playerNo].isEnabled()) {
						if (e.getKeyCode() == 9 || e.getKeyCode() == 10) {
							textfields[playerNo].setEnabled(false);
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {}
				
			});
			while (textfields[playerNo].isEnabled()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			oboard.setPlayerName(playerNo, textfields[playerNo].getText());
			return textfields[playerNo].getText();
		} else {
			System.err.println("Error: TextField does not exist.");
			return null;
		}
	}
	
	/**
	 * Sets field data to a given field on the monopoly board.
	 * @param fieldNo
	 * @param data
	 */
	@Override
	public void setField(int fieldNo, Field data) {
		if (fieldNo >= 0 && fieldNo < boardsize) {
			board[fieldNo] = data;
		}
	}
	
	/**
	 * Gets field data given a field number. Gives an empty field object if no data is found.
	 * @param fieldNo
	 * @return field data.
	 */
	@Override
	public Field getField(int fieldNo) {
		if (fieldNo >= 0 && fieldNo < boardsize && board[fieldNo] != null) {
			return board[fieldNo];
		} else {
			return new Field();
		}
	}
	
	/**
	 * Updates the two dice values.
	 * @param left dice value
	 * @param right dice value
	 */
	@Override
	public void SetDice(int left, int right) {
		dice[0].setValue(left);
		dice[1].setValue(right);
	}
	
	/**
	 * Updates the two dice to have a given amount of sides (used to display values using eyes or raw numbers).
	 * @param sides
	 */
	@Override
	public void SetDiceType(int sides) {
		dice[0].setSides(sides);
		dice[1].setSides(sides);
	}
	
	/**
	 * Gets the value shown on either the left or the right die.
	 * @param right
	 * @return value of die.
	 */
	@Override
	public int getDieValue(boolean right) {
		return dice[right ? 1 : 0].getValue();
	}
	
	/**
	 * Gets the sum of all the dice values.
	 * @return sum of dice.
	 */
	@Override
	public int getTotalDieValue() {
		return getDieValue(false) + getDieValue(true);
	}
	
	/**
	 * Moves the given player to a given field.
	 * @param fieldNo
	 */
	@Override
	public void movePlayer(int playerNo, int fieldNo) {
		if (playerNo >= 0 && playerNo < 12 && textfields[playerNo] != null) {
			position[playerNo] = fieldNo % boardsize;
			oboard.setPlayerPos(playerNo, fieldNo);
		} else {
			System.err.println("Error: Player does not exist.");
		}
	}
	
	/**
	 * Resets the player's position, moving them away from the board.
	 * @param playerNo
	 */
	@Override
	public void resetPosition(int playerNo) {
		if (playerNo >= 0 && playerNo < boardsize) {
			oboard.resetPlayerPos(playerNo);
		} else {
			System.err.println("Error: Player does not exist.");
		}
	}
	
	/**
	 * Finds the field the player is currently located on.
	 * @param playerNo
	 * @return the field number the player is on.
	 */
	@Override
	public int getPlayerPosition(int playerNo) {
		return oboard.getPlayerPos(playerNo);
	}
	
	/**
	 * Sets a player's score. Usable for BOTH adding to score AND resetting score. *cough cough*
	 * @param playerNo
	 * @param score
	 */
	@Override
	public void setPlayerScore(int playerNo, int score) {
		if (playerNo >= 0 && playerNo < 12 && counter[playerNo] != null) {
			counter[playerNo].setValue(score);
		} else {
			System.err.println("Error: Player does not exist.");
		}
	}
	
	/**
	 * Gets a player's score. Default is 0.
	 * @param playerNo
	 * @return the player's score.
	 */
	@Override
	public int getPlayerScore(int playerNo) {
		if (playerNo >= 0 && playerNo < 12 && counter[playerNo] != null) {
			return counter[playerNo].getValue();
		}
		return 0;
	}
	
	/**
	 * Sets the text to display on the screen. Used for winning, use an empty string to clear.
	 * @param text
	 */
	@Override
	public void setScreenText(String text) {
		screentext = text;
	}
	
	/**
	 * Slowly moves the mouse to a given coordinate and optionally clicks after moving. Haha.
	 * @param x
	 * @param y
	 * @param click after move
	 */
	@Override
	public void moveMouse(int x, int y, boolean clickAfterMove) {
		new MouseController().set(x, y, clickAfterMove);
	}
	
	/**
	 * Yields the thread until a button is clicked. If 0 is returned, "Roll dice" is clicked.
	 * If 1 is returned, "Reset" is clicked. If 2 is returned, "Buy field" is clicked.
	 * If 3 is returned, "Place house" is clicked. If 4 is returned, "Place hotel" is clicked.
	 * @return the number of the button.
	 */
	@Override
	public int clickButton() {
		lastButtonClick = -1;
		for (int i = 0; i < button.length; i++) {
			button[i].setEnabled(true);
			button[i].onclick.addListener(new EventListener() {
				@Override
				public void event() {
					for (int i = 0; i < button.length; i++) {
						if (button[i].hovered) {
							lastButtonClick = i;
							for (int j = 0; j < button.length; j++) {
								button[j].setEnabled(false);
							}
							break;
						}
					}
				}
				@Override
				public void event(Object[] args) {}
			});
		}
		while (lastButtonClick == -1) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		return lastButtonClick;
	}

	/**
	 * Allows you to specify if a button should be visible.
	 * 0 = Roll dice
	 * 1 = Reset
	 * 2 = Buy field
	 * 3 = Place house
	 * 4 = Place hotel
	 * @param buttonNo
	 * @param visible
	 */
	@Override
	public void setButtonVisible(int buttonNo, boolean visible) {
		if (buttonNo >= 0 && buttonNo < 5) {
			button[buttonNo].setVisible(visible);
		} else {
			System.err.println("Error: Button does not exist.");
		}
	}
	
	/**
	 * Creates a new AudioPlayer, which is used for playing audio.
	 * @return an AudioPlayer object
	 */
	@Override
	public AudioPlayer getAudio(final String path) {
		return new AudioPlayer(path);
	}
	
	/**
	 * Adds the string to the dropdown menu specifying supported languages.
	 */
	@Override
	public void addLanguage(String lang) {
		options.addOption(1, lang);
	}
	
	/**
	 * Checks the option list for the player's option on whether to use a 6-sided die or 20-sided die.
	 * @return boolean indicating die type
	 */
	@Override
	public boolean getDieType() {
		return options.options[0][0] == "1";
	}
	
	/**
	 * Checks the currently selected language, defaults to English.
	 * @return language string from dropdown
	 */
	@Override
	public String getLanguage() {
		return options.options[1][0];
	}
	
	/**
	 * Overrides an existing dropdown option for the language dropdown menu. Does nothing if the option does not exist.
	 */
	@Override
	public void changeLanguage(int optionNo, String newLang) {
		if (optionNo >= 0 && optionNo < options.options[1].length - 2) {
			if (options.options[1][0].equals(options.options[1][optionNo + 2])) {
				options.options[1][0] = newLang;
			}
			options.options[1][optionNo + 2] = newLang;
		}
	}
	
	/**
	 * Overwrites the value for whether to use a 6-sided die or 20-sided die.
	 */
	@Override
	public void setDieType(boolean isTwentySided) {
		options.options[0][0] = isTwentySided ? "1" : "0";
	}
	
	/**
	 * If the option exists in the dropdown menu for languages, the new language is set.
	 */
	@Override
	public void setLanguage(int optionNo) {
		if (optionNo >= 0 && optionNo < options.options[1].length - 2) {
			options.options[1][0] = options.options[1][optionNo + 2];
		}
	}
	
	
	
}

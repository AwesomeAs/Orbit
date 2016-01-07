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
import javax.swing.WindowConstants;

import Utils.AudioPlayer;
import Utils.EventListener;
import Utils.FontManager;
import Utils.MediaPlayer;
import Utils.MouseController;
import desktop_resources.GUI;
import game.*;

public class OrbitAdapter implements OrbitGUI {
	
	private static OrbitView window;
	private static JPanel panel;
	private OrbitTextField[] textfields = new OrbitTextField[12];
	private OrbitDice[] dice = new OrbitDice[2];
	private OrbitStatus[] counter = new OrbitStatus[12];
	private OrbitLoader loader;
	private ImageIcon background;
	private int[] position = new int[12];
	private OrbitButton[] button = new OrbitButton[5];
	private OrbitButton skipbutton;
	private OrbitOptionList options;
	private int width;
	private int height;
	private String screentext = "";
	private String screendesc = "";
	private Font font;
	private Font descfont;
	private int lastButtonClick;
	private OrbitBoard oboard;
	private MediaPlayer mediaPlayer;
	private JFrame twindow;
	private float screendarken = 0f;
	private float screendarkengoal = 0f;
	private Field[] board = new Field[40];
	private int boardsize = 40;
	private String[] oldPlayerNames = new String[12];
	private int[] oldPlayerPos = new int[12];
	private int[] oldDiceValues = new int[]{1, 1};
	private int[] oldPlayerScore = new int[12];
	private boolean[] oldButtonVisible = new boolean[5];
	
	public OrbitAdapter(int width, int height, int boardsize, boolean useOldGUI, String[] boardicons) {
		if (!useOldGUI) {
			if (window == null) {
				this.width = width;
				this.height = height;
				this.boardsize = boardsize;
				board = new Field[boardsize];
				font = new FontManager("ProFontWindows.ttf", 48).get();
				descfont = new FontManager("ProFontWindows.ttf", 16).get();
				window = new OrbitView();
				window.setSize(width, height);
				window.setVisible(true);
				mediaPlayer = new MediaPlayer(window, skipbutton);
			}
		} else {
			oldButtonVisible[0] = true;
	        oldButtonVisible[1] = true;
	        desktop_fields.Field[] fields = new desktop_fields.Field[boardsize];
	        for (int i = 0; i < boardsize; i++) {
	        	if (i < boardicons.length && boardicons[i] != null) {
	        		switch (boardicons[i]) {
	        			case "Brewery":
	        				fields[i] = new desktop_fields.Brewery.Builder().build();
	        				break;
	        			case "Chance":
	        				fields[i] = new desktop_fields.Chance.Builder().build();
	        				fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(new Color(140, 140, 140)));
	        				break;
						case "Jail":
							fields[i] = new desktop_fields.Jail.Builder().build();
							fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(new Color(120, 120, 120)));
							break;
						case "Refuge":
	        				fields[i] = new desktop_fields.Refuge.Builder().build();
	        				fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(new Color(150, 150, 150)));
	        				break;
						case "Shipping":
	        				fields[i] = new desktop_fields.Shipping.Builder().build();
	        				fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(new Color(150, 150, 150)));
	        				break;
						case "Start":
							fields[i] = new desktop_fields.Start.Builder().build();
							fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(Color.getHSBColor(i * 0.18f, 0.75f, 0.8f)));
	        				break;
						case "Street":
							fields[i] = new desktop_fields.Street.Builder().build();
							fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(Color.getHSBColor(i * 0.18f, 0.75f, 0.8f)));
	        				break;
						case "Tax":
							fields[i] = new desktop_fields.Tax.Builder().build();
							fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(new Color(100, 100, 100)));
	        				break;
	        			default:
	        				fields[i] = new desktop_fields.Empty.Builder()
	        				.setBgColor(Color.getHSBColor(i * 0.18f, 0.75f, 1f)).build();
	        				fields[i].getPanel().setBorder(
	        						BorderFactory.createLineBorder(Color.getHSBColor(i * 0.18f, 0.75f, 0.8f)));
	        		}
	        	} else {
	        		fields[i] = new desktop_fields.Street.Builder()
	        				.setBgColor(Color.getHSBColor(i * 0.18f, 0.75f, 1f)).build();
	        		fields[i].getPanel().setBorder(BorderFactory.createLineBorder(Color.getHSBColor(i * 0.18f, 0.75f, 0.8f)));
	        	}
	        }
	        GUI.create(fields);
	        GUI.setSubText(1, "$$$");
	        GUI.setDescriptionText(1, "Much cash such wow");
		}
	}
	
	public OrbitAdapter(int width, int height, int boardsize, boolean useOldGUI) {
		this(width, height, boardsize, useOldGUI, new String[boardsize]);
	}
	
	public OrbitAdapter(int width, int height) {
		this(width, height, 40, false, new String[40]);
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
		        
		        add(loader = new OrbitLoader(width / 2 - 75, height / 2 - 40, 150, 0));
		        loader.setVisible(false);
		        
		        skipbutton = new OrbitButton(10, height - 80, "SKIP", 0);
		        skipbutton.setVisible(false);
		        
		        skipbutton.onclick.addListener(new EventListener() {

					@Override
					public void event() {
						skipbutton.setVisible(false);
						screendarkengoal = 0f;
						mediaPlayer.stop();
					}

					@Override
					public void event(Object[] args) {}
		        	
		        });
		        
		        add(button[0] = new OrbitButton(10, height - 120, "ROLL DICE", 1, "dicecup.png"));
		        add(button[1] = new OrbitButton(10, height - 80, "RESET GAME", 2));
		        
		        add(button[2] = new OrbitButton(10, height - 180, "BUY FIELD", 0));
		        button[2].setVisible(false);
		        add(button[3] = new OrbitButton(10, height - 220, "PLACE HOUSE", 0));
		        button[3].setVisible(false);
		        add(button[4] = new OrbitButton(10, height - 260, "PLACE HOTEL", 0));
		        button[4].setVisible(false);
		        
		        add(oboard = new OrbitBoard(width / 2, height / 2, boardsize));
		        
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
						button[i].setEnabled(mediaPlayer == null || !mediaPlayer.isPlaying());
						button[i].paintComponent(g);
						if (button[i].hovered) {
							isHover = true;
						}
					}
				}
				
				loader.paintComponent(g);
				loader.angle += 20;
				loader.angle = loader.angle % 360;
				
				options.setEnabled(mediaPlayer == null || !mediaPlayer.isPlaying());
				options.paintComponent(g);
				
				g2d.setFont(font);
				g2d.setColor(new Color(0, 0, 0, 50));
				for (int x = -1; x <= 2; x++) {
					for (int y = -1; y <= 2; y++) {
							g2d.drawString(screentext, width / 2 + 1 - (int)font.getStringBounds(screentext, g2d.getFontRenderContext()).getWidth() / 2 - 1 + x,
									height / 2 + (loader.isVisible() ? -59 : 1) + y);
					}
				}
				g2d.setColor(new Color(35, 185, 185, (int)(150 + dice[0].getOpacity() * 105.0)));
				g2d.drawString(screentext, width / 2 - (int)font.getStringBounds(screentext, g2d.getFontRenderContext()).getWidth() / 2, height / 2 +
		        		(loader.isVisible() ? -60 : 0));
		        
		        g2d.setFont(descfont);
				g2d.setColor(new Color(0, 0, 0, 50));
				for (int x = -1; x <= 2; x++) {
					for (int y = -1; y <= 2; y++) {
						int ly = 0;
						for (String line : screendesc.split("\n")) {
							g2d.drawString(line, width / 2 - (int)descfont.getStringBounds(line, g2d.getFontRenderContext()).getWidth() / 2, height - 98 +
									y * 24);
							g2d.drawString(screendesc, width / 2 + x - (int)descfont.getStringBounds(screendesc, g2d.getFontRenderContext()).getWidth() / 2,
									height - 100 + y + ly);
							ly++;
						}
					}
				}
				g2d.setColor(new Color(35, 185, 185, (int)(150 + dice[0].getOpacity() * 105.0)));
				int y = 0;
				for (String line : screendesc.split("\n")) {
					g2d.drawString(line, width / 2 - (int)descfont.getStringBounds(line, g2d.getFontRenderContext()).getWidth() / 2, height - 98 +
							y * 24);
					y++;
				}
				
				options.paintDropdown(g);
				if (options.hovered) {
					isHover = true;
				}
				
				screendarken += (screendarkengoal - screendarken) * 0.05f;
				g2d.setColor(new Color(0, 0, 0, screendarken));
				g2d.fillRect(0, 0, getWidth(), getHeight());
				
				if (mediaPlayer == null || mediaPlayer.hasEnded() || !mediaPlayer.isPlaying()) {
					if (isHover) {
					 	setCursor(new Cursor(Cursor.HAND_CURSOR));
					} else {
						setCursor(Cursor.getDefaultCursor());
					}
				}
				
				if (mediaPlayer != null && mediaPlayer.hasEnded()) {
					skipbutton.setVisible(false);
					screendarkengoal = 0f;
					mediaPlayer.stop();
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
		if (window != null) {
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
		} else {
			if (name.equals(null) || name.isEmpty()) {
				name = GUI.getUserString("Indtast navn for spiller " + (playerNo + 1));
			}
			int resName = 0;
			boolean found = true;
			while (found) {
				found = false;
				for (String val : oldPlayerNames) {
					if ((resName == 0 && name.equals(val)) || (resName > 0 && (name + " " + (resName + 1)).equals(val))) {
						resName++;
						found = true;
					}
				}
			}
			if (resName > 0) {
				name += (" " + (resName + 1));
			}
			if (oldPlayerNames[playerNo] == null) {
				GUI.addPlayer(name, 400);
			}
			oldPlayerNames[playerNo] = name;
			oldPlayerPos[playerNo] = 0;
			oldPlayerScore[playerNo] = 400;
		}
	}
	
	/**
	 * Gets a given player's name, either as Player number or their submitted name.
	 * @param playerNo
	 * @return the player's name.
	 */
	@Override
	public String getPlayerName(int playerNo) {
		if (window != null) {
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
		} else {
			if (playerNo >= 0 && playerNo < 12) {
				if (oldPlayerNames[playerNo] != null) {
					return oldPlayerNames[playerNo];
				} else {
					return "PLAYER " + (playerNo + 1);
				}
			} else {
				System.err.println("Error: Limit is 12 players.");
				return null;
			}
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
		if (window != null) {
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
		} else {
			return getPlayerName(playerNo); //GUI.getUserString("Indtast navn for spiller " + (playerNo + 1));
		}
	}
	
	/**
	 * Sets field data to a given field on the monopoly board.
	 * @param data
	 */
	@Override
	public void setField(Field data) {
		if (window != null) {
			if (data.getNumber() >= 0 && data.getNumber() < boardsize) {
				oboard.setField(data);
				board[data.getNumber()] = data;
			}
		} else {
			String desc = null;
			if (data instanceof Ownable) {
				if (((Ownable)data).getOwner() != null) {
					if (data instanceof Fleet) {
						desc = ((Ownable)data).getOwner().getName() + ", " +
								((Fleet)data).getRent();
					} else if (data instanceof LaborCamp) {
						desc = ((Ownable)data).getOwner().getName() + ", " +
								((LaborCamp)data).getRent();
					} else if (data instanceof Territory) {
						desc = ((Ownable)data).getOwner().getName() + ", " +
								((Territory)data).getRent();
					}
				} else {
					desc = ((Ownable)data).getPrice() + " $";
				}
			} else {
				if (data instanceof Tax) {
						desc = "-10% or " + ((Tax)data).getTax() + " $";
				} else {
					desc = "+" + ((Refuge)data).getBonus() + " $";
				}
			}
			//System.out.println("Setting field: " + data.getNumber());
			GUI.setTitleText(data.getNumber() + 1, data.getName());
			GUI.setSubText(data.getNumber() + 1, desc);
			GUI.setDescriptionText(data.getNumber() + 1, data.getName());
		}
	}
	
	/**
	 * Gets field data given a field number. Gives an empty field object if no data is found.
	 * @param fieldNo
	 * @return field data.
	 */
	@Override
	public Field getField(int fieldNo) {
		if (fieldNo >= 0 && fieldNo < boardsize) {
			return board[fieldNo];
		} else {
			return null;
		}
	}
	
	/**
	 * Updates the two dice values.
	 * @param left dice value
	 * @param right dice value
	 */
	@Override
	public void SetDice(int left, int right) {
		if (window != null) {
			dice[0].setValue(left);
			dice[1].setValue(right);
		} else {
			GUI.setDice(left, right);
			oldDiceValues[0] = left;
			oldDiceValues[1] = right;
		}
	}
	
	/**
	 * Updates the two dice to have a given amount of sides (used to display values using eyes or raw numbers).
	 * @param sides
	 */
	@Override
	public void SetDiceType(int sides) {
		if (window != null) {
			dice[0].setSides(sides);
			dice[1].setSides(sides);
		}
	}
	
	/**
	 * Gets the value shown on either the left or the right die.
	 * @param right
	 * @return value of die.
	 */
	@Override
	public int getDieValue(boolean right) {
		if (window != null) {
			return dice[right ? 1 : 0].getValue();
		} else {
			return oldDiceValues[right ? 1 : 0];
		}
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
		if (window != null) {
			if (playerNo >= 0 && playerNo < 12 && textfields[playerNo] != null) {
				position[playerNo] = fieldNo % boardsize;
				oboard.setPlayerPos(playerNo, fieldNo);
			} else {
				System.err.println("Error: Player does not exist.");
			}
		} else {
			GUI.setCar(fieldNo, getPlayerName(playerNo));
			oldPlayerPos[playerNo] = fieldNo;
		}
	}
	
	/**
	 * Resets the player's position, moving them away from the board.
	 * @param playerNo
	 */
	@Override
	public void resetPosition(int playerNo) {
		if (window != null) {
			if (playerNo >= 0 && playerNo < boardsize) {
				oboard.resetPlayerPos(playerNo);
			} else {
				System.err.println("Error: Player does not exist.");
			}
		} else {
			GUI.setCar(0, getPlayerName(playerNo));
			oldPlayerPos[playerNo] = 0;
		}
	}
	
	/**
	 * Finds the field the player is currently located on.
	 * @param playerNo
	 * @return the field number the player is on.
	 */
	@Override
	public int getPlayerPosition(int playerNo) {
		if (window != null) {
			return oboard.getPlayerPos(playerNo);
		} else {
			return oldPlayerPos[playerNo];
		}
	}
	
	/**
	 * Sets a player's score. Usable for BOTH adding to score AND resetting score. *cough cough*
	 * @param playerNo
	 * @param score
	 */
	@Override
	public void setPlayerScore(int playerNo, int score) {
		if (window != null) {
			if (playerNo >= 0 && playerNo < 12 && counter[playerNo] != null) {
				counter[playerNo].setValue(score);
			} else {
				System.err.println("Error: Player does not exist.");
			}
		} else {
			GUI.setBalance(getPlayerName(playerNo), score);
			oldPlayerScore[playerNo] = score;
		}
	}
	
	/**
	 * Gets a player's score. Default is 0.
	 * @param playerNo
	 * @return the player's score.
	 */
	@Override
	public int getPlayerScore(int playerNo) {
		if (window != null) {
			if (playerNo >= 0 && playerNo < 12 && counter[playerNo] != null) {
				return counter[playerNo].getValue();
			}
			return 0;
		} else {
			if (playerNo >= 0 && playerNo < 12) {
				return oldPlayerScore[playerNo];
			}
			return 0;
		}
	}
	
	/**
	 * Sets the text to display on the screen. Used for winning, use an empty string to clear.
	 * @param text
	 */
	@Override
	public void setScreenText(String text) {
		screentext = text;
		if (window == null) {
			GUI.showMessage(text);
		}
	}
	
	/**
	 * Sets the text to display a description on the screen. Use an empty string to clear.
	 * @param desc
	 */
	@Override
	public void setScreenDesc(String desc) {
		screendesc = desc;
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
		if (window != null) {
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
		} else {
			int size = 0;
			String[] buttons;
			for (int i = 0; i < oldButtonVisible.length; i++) {
				if (oldButtonVisible[i]) {
					size++;
				}
			}
			buttons = new String[size];
			size = 0;
			if (oldButtonVisible[0]) {
				buttons[size] = "Kast terninger";
				size++;
			}
			if (oldButtonVisible[1]) {
				buttons[size] = "Genstart";
				size++;
			}
			if (oldButtonVisible[2]) {
				buttons[size] = "Køb felt";
				size++;
			}
			if (oldButtonVisible[3]) {
				buttons[size] = "Placer hus";
				size++;
			}
			if (oldButtonVisible[4]) {
				buttons[size] = "Placer hotel";
				size++;
			}
			// if append to array if button 2, 3 or 4 is visible!
			String result = GUI.getUserButtonPressed("Vælg handling", buttons);
			switch (result) {
				case "Kast terninger":
					return 0;
				case "Genstart":
					return 1;
				case "Køb felt":
					return 2;
				case "Placer hus":
					return 3;
				default:
					return 4;
			}
		}
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
		if (window != null) {
			if (buttonNo >= 0 && buttonNo < 5) {
				button[buttonNo].setVisible(visible);
			} else {
				System.err.println("Error: Button does not exist.");
			}
		} else {
			if (buttonNo >= 0 && buttonNo < 5) {
				oldButtonVisible[buttonNo] = visible;
			} else {
				System.err.println("Error: Button does not exist.");
			}
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
		if (window != null) {
			options.addOption(1, lang);
		}
	}
	
	/**
	 * Checks the option list for the player's option on whether to use a 6-sided die or 20-sided die.
	 * @return boolean indicating die type
	 */
	@Override
	public boolean getDieType() {
		if (window != null) {
			return options.options[0][0] == "1";
		} else {
			return false;
		}
	}
	
	/**
	 * Checks the currently selected language, defaults to English.
	 * @return language string from dropdown
	 */
	@Override
	public String getLanguage() {
		if (window != null) {
			return options.options[1][0];
		} else {
			return "English";
		}
	}
	
	/**
	 * Overrides an existing dropdown option for the language dropdown menu. Does nothing if the option does not exist.
	 */
	@Override
	public void changeLanguage(int optionNo, String newLang) {
		if (window != null) {
			if (optionNo >= 0 && optionNo < options.options[1].length - 2) {
				if (options.options[1][0].equals(options.options[1][optionNo + 2])) {
					options.options[1][0] = newLang;
				}
				options.options[1][optionNo + 2] = newLang;
			}
		}
	}
	
	/**
	 * Overwrites the value for whether to use a 6-sided die or 20-sided die.
	 */
	@Override
	public void setDieType(boolean isTwentySided) {
		if (window != null) {
			options.options[0][0] = isTwentySided ? "1" : "0";
		}
	}
	
	/**
	 * If the option exists in the dropdown menu for languages, the new language is set.
	 */
	@Override
	public void setLanguage(int optionNo) {
		if (window != null) {
			if (optionNo >= 0 && optionNo < options.options[1].length - 2) {
				options.options[1][0] = options.options[1][optionNo + 2];
			}
		}
	}
	
	/**
	 * Sets if the visual widget for game setting should be visible.
	 */
	@Override
	public void setOptionsVisible(boolean visible) {
		if (window != null) {
			options.setVisible(visible);
		}
	}
	
	/**
	 * Sets the title and placeholder for all players' textfields.
	 */
	@Override
	public void setPlayerLanguage(String title, String placeholder) {
		if (window != null) {
			for (int i = 0; i < textfields.length; i++) {
				if (textfields[i] != null) {
					textfields[i].label = title + " " + (i + 1);
					textfields[i].desc = placeholder;
				}
			}
		}
	}
	
	/**
	 * Change text on the dice displays.
	 */
	@Override
	public void setDieLanguage(String text) {
		if (window != null) {
			dice[0].setDesc(text + " 1");
			dice[1].setDesc(text + " 2");
		}
	}
	
	
	@Override
	public void setCounterLanguage(String text) {
		if (window != null) {
			for (int i = 0; i < counter.length; i++) {
				if (counter[i] != null) {
					counter[i].setDesc(text);
				}
			}
		}
	}
	
	/**
	 * Sets the title for a given option.
	 */
	@Override
	public void setOptionLanguage(int optionNo, String title) {
		if (window != null) {
			if (optionNo >= 0 && optionNo < options.titles.length && options.titles[optionNo] != null) {
				options.titles[optionNo] = title;
			}
		}
	}
	
	/**
	 * Sets the text on a given button.
	 */
	@Override
	public void setButtonLanguage(int buttonNo, String text) {
		if (window != null) {
			if (buttonNo >= 0 && buttonNo < button.length && button[buttonNo] != null) {
				button[buttonNo].setText(text);
			}
		}
	}
	
	/**
	 * Sets if our loader widget should be visible in the center of the screen
	 */
	@Override
	public void setLoaderVisible(boolean visible) {
		if (window != null) {
			loader.setVisible(visible);
		}
	}
	
	/**
	 * Detect if we are using the new GUI or old GUI. The name is pretty self-explanatory
	 */
	@Override
	public boolean isOldGUI() {
		return window == null;
	}
	
	/**
	 * If we are using the old GUI design, close it. If we are using the new GUI design, do nothing
	 */
	@Override
	public void closeOldGUI() {
		if (window == null) {
			GUI.close();
			System.exit(0);
		}
	}
	
	/**
	 * If we are using the new GUI, play the video clip in the current window. If we are using the old GUI, do nothing
	 */
	@Override
	public void playVideo(String filename) {
		if (mediaPlayer != null) {
			screendarkengoal = 1f;
			mediaPlayer.load(filename);
			skipbutton.setVisible(true);
		} else {
			screendarkengoal = 1f;
			twindow = new JFrame();
			twindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			twindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
			twindow.setVisible(true);
			mediaPlayer = new MediaPlayer(twindow);
			mediaPlayer.load(filename);
		}
	}
	
	/**
	 * Stop the currently playing video clip, if one is playing
	 */
	@Override
	public void stopVideo() {
		if (mediaPlayer != null) {
			skipbutton.setVisible(false);
			screendarkengoal = 0f;
			mediaPlayer.stop();
			if (twindow != null) {
				twindow.dispose();
				twindow = null;
			}
		}
	}
	
	/**
	 * Checks if our current view is a video
	 */
	@Override
	public boolean isVideoPlaying() {
		return (mediaPlayer != null && mediaPlayer.isPlaying()) || (twindow != null && screendarkengoal == 1f);
	}
	
	/**
	 * Yields the current thread until the current view is no longer a video
	 */
	@Override
	public void waitForVideoEnded() {
		try {
			while ((mediaPlayer != null || twindow != null) && screendarkengoal == 1f) {
					Thread.sleep(100);
			}
		} catch (InterruptedException e) {}
	}
}

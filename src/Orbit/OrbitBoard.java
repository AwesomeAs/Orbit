package Orbit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import GameData.Field;
import Utils.FontManager;

/*
 * OrbitButton class by Andreas, 2015
 * 
 * This class is designed to display a round continuous board,
 * with different field data and players.
 * 
 * Java 1.7
 * 
 */

public class OrbitBoard extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon[] players;
	private Field[] fields;
	private int[] playerPos;
	private boolean[] onboard;
	private String[] names;
	private int size = 40;
	private int x;
	private int y;
	private int scale = 150;
	public boolean hovered = false;
	private Font font;
	private Font nametag;
	
	public OrbitBoard(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		fields = new Field[size];
		players = new ImageIcon[size];
		playerPos = new int[size];
		names = new String[size];
		onboard = new boolean[size];
		font = new FontManager("ProFontWindows.ttf", 42).get();
		nametag = new FontManager("ProFontWindows.ttf", 16).get();
	}
	
	
	
	public void setPlayer(int no, String name) {
		try {
			players[no] = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/spaceships/" + no + ".png")));
		} catch (IOException e) {}
		playerPos[no] = 0;
		if (name.length() == 0) {
			names[no] = "PLAYER " + (no + 1);
		} else {
			names[no] = name;
		}
	}
	
	public void setPlayerName(int no, String name) {
		if (name.length() == 0) {
			names[no] = "PLAYER " + (no + 1);
		} else {
			names[no] = name;
		}
	}
	
	public void setPlayerPos(int no, int fieldNo) {
		playerPos[no] = Math.floorMod(fieldNo, size);
		onboard[no] = true;
	}
	
	public void resetPlayerPos(int no) {
		playerPos[no] = 0;
		onboard[no] = false;
	}
	
	public int getPlayerPos(int no) {
		if (no >= 0 && no < 12) {
			return playerPos[no];
		} else {
			return 0;
		}
	}
	
	public void setField(int no, Field data) {
		fields[no] = data;
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g2d);
    }
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(new Color(35, 185, 185, 50));
		
        Ellipse2D.Double ellipse1 = new Ellipse2D.Double(
                x - scale * 3, y - (int)(scale * 1.5), 6 * scale, 3 * scale); 
        Ellipse2D.Double ellipse2 = new Ellipse2D.Double(
        		x - scale * 2, y - scale, 4 * scale, 2 * scale);
        Area circle = new Area(ellipse1);
        circle.subtract(new Area(ellipse2));
        
        Point point = MouseInfo.getPointerInfo().getLocation();
        
        hovered = circle.contains(point.x, point.y);
        
        g2d.fill(circle);
		
		g2d.setColor(new Color(35, 185, 185));
		g2d.setStroke(new BasicStroke(1));
		g2d.drawArc(x - scale * 2, y - scale, 4 * scale, 2 * scale, 0, 360);
		g2d.drawArc(x - scale * 3, y - (int)(scale * 1.5), 6 * scale, 3 * scale, 0, 360);
		g2d.setStroke(new BasicStroke(0.75f));
		
		for (int i = 0; i < size; i++) {
			g2d.drawLine(
					(int)(x + scale * 2.0 * Math.cos(((float)i / size) * Math.PI * 2)),
					(int)(y + scale * 1.0 * Math.sin(((float)i / size) * Math.PI * 2)),
					(int)(x + scale * 3.0 * Math.cos(((float)i / size) * Math.PI * 2)),
					(int)(y + scale * 1.5 * Math.sin(((float)i / size) * Math.PI * 2))
			);
		}
		
		g2d.setFont(font);
		for (int i = 0; i < size; i++) {
			if (fields[i] != null) {
				String text = "implement"; // fields[i].getText();
				int tx = (int)(x + scale * 2.5 * Math.cos(((i + 0.6f) / size) * Math.PI * 2));
				int ty = (int)(y + scale * 1.25 * Math.sin(((i + 0.6f) / size) * Math.PI * 2));
				g2d.drawString(text,
					tx - (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth() / 2,
					ty);
			}
		}
		
		int[][] ppos = new int[size][3];
		for (int i = 0; i < size; i++) {
			if (players[i] != null) {
				if (onboard[i]) {
					float pos = -playerPos[i] - ((i + 0.3f) % 0.6f - 0.2f);
					ppos[i] = new int[] {
							(int)(x + scale * 2.5 * Math.cos(((float)(pos - (5.0 / size)) / size) * Math.PI * 2)) - 55,
							(int)(y - scale * 1.25 * Math.sin(((float)(pos - (5.0 / size)) / size) * Math.PI * 2)) - 75,
							i
					};
				} else {
					ppos[i] = new int[] {
							(int)(x + scale * 0.75 * Math.cos(((float)(-i) / size) * Math.PI * 2)) - 55,
							(int)(y - scale * 0.375 * Math.sin(((float)(-i) / size) * Math.PI * 2)) - 75,
							i
					};
				}
				
			}
		}
		
		Arrays.sort(ppos, new Comparator<int[]>() {

			@Override
			public int compare(final int[] o1, final int[] o2) {
				return o1[1] > o2[1] ? 1 : (o1[1] < o2[1] ? -1 : 0);
			}
			
		});
		
		g2d.setFont(nametag);
		for (int j = 0; j < size; j++) {
			if (ppos[j] != null && players[ppos[j][2]] != null && (ppos[j][0] != 0 || ppos[j][1] != 0)) {
				int i = ppos[j][2];
				g2d.drawImage(players[i].getImage(),
					ppos[j][0],
					ppos[j][1],
					100, 100, players[i].getImageObserver());
				g2d.setColor(new Color(0, 0, 0, 100));
				g2d.drawString(names[i], ppos[j][0] + 51 - (int)nametag.getStringBounds(names[i], g2d.getFontRenderContext()).getWidth() / 2, ppos[j][1] + 12);
				g2d.setColor(new Color(35, 185, 185));
				g2d.drawString(names[i], ppos[j][0] + 50 - (int)nametag.getStringBounds(names[i], g2d.getFontRenderContext()).getWidth() / 2, ppos[j][1] + 10);
			}
		}
		
	}
	
}

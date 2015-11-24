package Orbit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Utils.FontManager;
import game.Field;

/*
 * OrbitBoard class by Andreas and Mickey, 2015
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
	private Font nametag;
	private Font fieldfont;
	
	public OrbitBoard(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		fields = new Field[size];
		players = new ImageIcon[size];
		playerPos = new int[size];
		names = new String[size];
		onboard = new boolean[size];
		nametag = new FontManager("ProFontWindows.ttf", 16).get();
		fieldfont = new FontManager("ProFontWindows.ttf", 20).get();
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
	
	public void setField(Field data) {
		if (data.getFieldNo() >= 0 && data.getFieldNo() < fields.length) {
			fields[data.getFieldNo()] = data;
		}
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
        Point loc = getLocationOnScreen();
        
        hovered = circle.contains(point.x - loc.x, point.y - loc.y);
        
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
		
		int hoverID = -1;
		if (hovered) {
			for (int i = 0; i < size; i++) {
				Arc2D.Double fieldarc = new Arc2D.Double(x - scale * 3.0, y - scale * 1.5,
						scale * 6.0, scale * 3.0, 360 - ((float)(i + 1) / size) * 360,
						(1f / size) * 360, Arc2D.PIE);
				Area field = new Area(fieldarc);
				field.intersect(circle);
				
				if (field.contains(point.x - loc.x, point.y - loc.y)) {
					hoverID = i;
					g2d.setColor(new Color(0, 1, 1, 0.75f));
					g2d.draw(field);
					g2d.setColor(new Color(0, 1, 1, 0.125f));
					g2d.fill(field);
				}
			}
		}
		
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
		
		if (hoverID >= 0 && hoverID < fields.length && fields[hoverID] != null) {
			int dx = (int)(x + scale * 2.5 * Math.cos(((float)(hoverID + 0.5f) / size) * Math.PI * 2));
			int dy = (int)(y + scale * 1.25 * Math.sin(((float)(hoverID + 0.5f) / size) * Math.PI * 2));
			g2d.setFont(fieldfont);
			g2d.setColor(new Color(0, 0, 0, 100));
			g2d.drawString(fields[hoverID].getName(), dx - (int)fieldfont.getStringBounds(fields[hoverID].getName(),
					g2d.getFontRenderContext()).getWidth() / 2 + 1, dy - 1);
			g2d.setFont(nametag);
			g2d.drawString(fields[hoverID].getValue() + " $", dx - (int)nametag.getStringBounds(fields[hoverID].getValue() + " $",
					g2d.getFontRenderContext()).getWidth() / 2 + 1, dy + 16);
			g2d.setFont(fieldfont);
			g2d.setColor(Color.WHITE);
			g2d.drawString(fields[hoverID].getName(), dx - (int)fieldfont.getStringBounds(fields[hoverID].getName(),
					g2d.getFontRenderContext()).getWidth() / 2, dy - 3);
			g2d.setFont(nametag);
			g2d.drawString(fields[hoverID].getValue() + " $", dx - (int)nametag.getStringBounds(fields[hoverID].getValue() + " $",
					g2d.getFontRenderContext()).getWidth() / 2, dy + 14);
		}
		
	}
	
}

package Orbit;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

import javax.swing.JPanel;

import Utils.FontManager;

/*
 * OrbitDice class by Andreas, 2015
 * 
 * This class is designed to display a die display, following
 * the theme of the Orbit UI layout.
 * The class is responsible for showing a reckognizable display
 * of dice eyes depending on number of die sides, and a numeric
 * representation if no suited easy representation.
 * 
 * Java 1.7
 * 
 */

public class OrbitDice extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int value = 1;
	private int sides = 6;
	private String desc = "ITEM";
	private Font font;
	private Font valuefont;
	private float opacity = 1;
	private boolean enabled = true;
	
	protected void loadFont() {
		font = new FontManager("ProFontWindows.ttf", 12).get();
		valuefont = new FontManager("ProFontWindows.ttf", 36).get();
		
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
		{
			public void eventDispatched(AWTEvent e)
		    {
		        if (e.getID() == 502) {
		        	
		        	Point point = MouseInfo.getPointerInfo().getLocation();
		    		Point loc = getLocationOnScreen();
		        	
		        	boolean hover = point.x - loc.x >= x && point.x - loc.x <= x + 100 && point.y - loc.y >= y &&
		        			point.y - loc.y <= y + 50;
		        	if (enabled && hover) {
		        		Roll();
		        	}
		        }
		    }
		}, eventMask);
	}
	
	public OrbitDice(int x, int y) {
		this.x = x;
		this.y = y;
		loadFont();
	}
	
	public OrbitDice(int x, int y, String desc, int value) {
		this.desc = desc.equals("") ? "ITEM" : desc;
		this.x = x;
		this.y = y;
		this.value = value;
		loadFont();
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setSides(int sides) {
		this.sides = sides;
	}
	
	public int getSides() {
		return this.sides;
	}
	
	public void setOpacity(float op) {
		this.opacity = op;
	}
	
	public float getOpacity() {
		return this.opacity;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public boolean getEnabled() {
		return this.enabled;
	}
	
	public void Roll() {
		value = 1 + (int)(Math.random() * sides);
		opacity = 0.1f;
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g2d);
    }
	
	protected int textWidth(Font font, String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(new GradientPaint(x, y, new Color(35, 185, 185, 45),
				x, y + 50, new Color(20, 125, 130, 35)));
		g2d.fillRect(x, y, 100, 65);
		g2d.setColor(new Color(35, 185, 185));
		g2d.fillRect(x, y, 2, 2);
		g2d.fillRect(x + 98, y, 2, 2);
		
		opacity += (1 - opacity) * 0.2;
		g2d.setColor(new Color(35, 185, 185, (int)(opacity * 255)));
		
		if (sides == 6) {
			switch (value) {
				case 1:
					g2d.fillArc(x + 47, y + 17, 6, 6, 0, 360);
					break;
				case 2:
					g2d.fillArc(x + 37, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 7, 6, 6, 0, 360);
					break;
				case 3:
					g2d.fillArc(x + 47, y + 17, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 7, 6, 6, 0, 360);
					break;
				case 4:
					g2d.fillArc(x + 37, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 7, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 7, 6, 6, 0, 360);
					break;
				case 5:
					g2d.fillArc(x + 47, y + 17, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 7, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 7, 6, 6, 0, 360);
					break;
				default:
					g2d.fillArc(x + 37, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 7, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 27, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 7, 6, 6, 0, 360);
					g2d.fillArc(x + 37, y + 17, 6, 6, 0, 360);
					g2d.fillArc(x + 57, y + 17, 6, 6, 0, 360);
			}
		} else {
			g2d.setFont(valuefont);
			g2d.drawString(value + "", x + 49 - textWidth(valuefont, value + "", g2d) / 2, y + 30);
		}
		
		g2d.setFont(font);
		g2d.drawString(desc, x + 49 - textWidth(font, desc, g2d) / 2, y + 50);
		g2d.setColor(new Color(35, 185, 185, 100));
		g2d.drawLine(x, y + 65, x + 100, y + 65);
	}
}

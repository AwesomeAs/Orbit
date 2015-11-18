package Orbit;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Utils.FontManager;

/*
 * OrbitStatus class by Andreas, 2015
 * 
 * This class is designed to display a numeric display, following
 * the theme of the Orbit UI layout.
 * The class is responsible for showing statistics with a given
 * name, and shorten the value shown.
 * 
 * Java 1.7
 * 
 */

public class OrbitStatus extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int value = 0;
	private String desc = "ITEM";
	private boolean small = false;
	private Font font;
	public Font valuefont;
	
	protected void loadFont() {
		font = new FontManager("Titillium-Regular.ttf", small ? 10 : 12).get();
		valuefont = new FontManager("ProFontWindows.ttf", small ? 16 : 32).get();
	}
	
	public OrbitStatus(int x, int y) {
		this.x = x;
		this.y = y;
		loadFont();
	}
	
	public OrbitStatus(int x, int y, boolean small, String desc, int value) {
		this.desc = desc.equals("") ? "ITEM" : desc;
		this.x = x;
		this.y = y;
		this.small = small;
		this.value = value;
		loadFont();
	}
	
	public void Tween(int goal, float p) {
		value += (int)((goal - value) * p);
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g2d);
    }
	
	protected String format(int num) {
		int t = (int)Math.floor(Math.log10(num) / 3);
		switch (t) {
			case 1:
				return (int)Math.floor(num / 1000) + "k";
			case 2:
				return (int)Math.floor(num / 1000000) + "m";
			case 3:
				return (int)Math.floor(num / 1000000000) + "b";
			default:
				return (int)Math.floor(num) + "";
		}
	}
	
	protected int textWidth(Font font, String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setPaint(new GradientPaint(x, y, new Color(35, 185, 185, 45),
				x, y + (small ? 35 : 50), new Color(20, 125, 130, 35)));
		g2d.fillRect(x, y, 100, small ? 45 : 65);
		g2d.setColor(new Color(35, 185, 185));
		g2d.fillRect(x, y, 2, 2);
		g2d.fillRect(x + 98, y, 2, 2);
		g2d.setFont(valuefont);
		g2d.drawString(Integer.toString(value), x + 49 - textWidth(valuefont, Integer.toString(value), g2d) / 2, y + (small ? 33 : 35));
		g2d.setFont(font);
		g2d.drawString(desc, x + 49 - textWidth(font, desc, g2d) / 2, y + (small ? 18 : 50));
		g2d.setColor(new Color(35, 185, 185, 100));
		g2d.drawLine(x, y + (small ? 45 : 65), x + 100, y + (small ? 45 : 65));
	}
}

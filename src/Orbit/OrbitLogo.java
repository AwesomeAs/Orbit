package Orbit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import Utils.FontManager;

/*
 * OrbitLogo class by Andreas, 2015
 * 
 * This class is designed to display the ORBIT logo, being
 * a planet with a moon circulating around the planet,
 * allowing for an arbitary title.
 * The class is responsible for defining at what angle the
 * moon is located around the planet.
 * 
 * Java 1.7
 * 
 */

public class OrbitLogo extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x;
	int y;
	int s;
	int textw = 0;
	String text;
	public float angle = 0;
	Font font = null;


	public OrbitLogo(int _x, int _y, int _s, String _text, int _textw) {
		x = _x;
		y = _y;
		s = _s;
		text = _text;
		textw = _textw / 2;
		font = new FontManager("Titillium-Regular.ttf", (int)(s * 0.32)).get();
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
		g2d.setPaint(new GradientPaint(x, y, new Color(30, 145, 140), x, y + s, new Color(35, 185, 185)));
		g2d.setStroke(new BasicStroke(3));
		g2d.drawArc(x, y, s, s, -25 - (int)angle, 305);
		g2d.fillArc(x + (int)(s * 0.25), y + (int)(s * 0.25), s / 2, s / 2, 0, 360);
		g2d.setPaint(new Color(255, 255, 255, 50));
		g2d.drawArc(x + 1 + (int)(s * 0.25), y + 1 + (int)(s * 0.25), s / 2 - 1, s / 2 - 1, 0, 360);
		g2d.setPaint(new GradientPaint(60, 60, new Color(30, 145, 140), 60, 160, new Color(35, 185, 185)));
		g2d.fillArc(
				x + (int)(s * 0.35) + (int)(Math.sin((double)(-(int)angle+38)/180*Math.PI) * (s / 2)),
				y + (int)(s * 0.35) + (int)(Math.cos((double)(-(int)angle+38)/180*Math.PI) * (s / 2)),
				(int)(s * 0.3), (int)(s * 0.3), 0, 360);
		g2d.setFont(font);
		
		g2d.drawString(text, x + s / 2 - (int)(textw / 100. * s), y + s + (int)(s * 0.4));
	}
}

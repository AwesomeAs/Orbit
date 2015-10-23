package Orbit;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/*
 * OrbitLoader class by Andreas, 2015
 * 
 * This class is designed to display a circle with an area
 * filled, showing how far a progress is from its goal, as
 * well as display multiple steps in a progress.
 * The class is responsible for tweening its values towards
 * new states.
 * 
 * Java 1.7
 * 
 */

public class OrbitLoader extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int s;
	private Color[] colors;
	private float[] dist;
	private int mode = 0;
	public float angle = 0;
	public float step = 0;
	public float end = 0;
	
	public OrbitLoader(int x, int y, int s, float angle) {
		this.x = x;
		this.y = y;
		this.s = s;
		this.angle = angle;
		this.step = 135;
		this.end = angle + 135;
		this.mode = 0;
	}
	
	public OrbitLoader(int x, int y, int s, float start, float end) {
		this(x, y, s, start);
		this.step = end - start;
		this.end = end;
		this.mode = 0;
	}
	
	public OrbitLoader(int x, int y, int s, float start, float step, float end) {
		this(x, y, s, start, end);
		this.step = step;
		this.mode = 1;
	}
	
	public OrbitLoader(int x, int y, int s, Color[] colors, float[] dist) {
		this(x, y, s, 0);
		this.colors = colors;
		this.dist = dist;
		this.mode = 2;
	}
	
	public void Tween(Color[] colors, float[] dist, float p) {
		for (int i = 0; i < this.colors.length; i++) {
			if (this.dist.length > i && colors.length > i && dist.length > i) {
				this.colors[i] = new Color(
					(int)(this.colors[i].getRed() + (colors[i].getRed() - this.colors[i].getRed()) * p),
					(int)(this.colors[i].getGreen() + (colors[i].getGreen() - this.colors[i].getGreen()) * p),
					(int)(this.colors[i].getBlue() + (colors[i].getBlue() - this.colors[i].getBlue()) * p),
					(int)(this.colors[i].getAlpha() + (colors[i].getAlpha() - this.colors[i].getAlpha()) * p)
				);
				this.dist[i] += (dist[i] - this.dist[i]) * p;
			}
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
		//g2d.setPaint(new GradientPaint(x, y, new Color(30, 145, 140), x, y + s, new Color(35, 185, 185)));
		g2d.setStroke(new BasicStroke(2));
		
		if (mode == 0) {
			end = angle + step;
			g2d.setColor(new Color(15, 125, 125, 100));
			g2d.drawArc(x, y, s, s, 96 - (int)angle, 347 - (int)(step));
			g2d.setColor(new Color(35, 185, 185));
			g2d.drawArc(x, y, s, s, 90 - (int)(angle), (int)(angle - end));
		} else if (mode == 1) {
			g2d.setColor(new Color(15, 125, 125, 100));
			g2d.drawArc(x, y, s, s, 96 - (int)angle, 347 - (int)(step + end));
			g2d.setColor(new Color(35, 185, 185));
			g2d.drawArc(x, y, s, s, 90 - (int)(angle), (int)(angle - step) - 1);
			g2d.setColor(new Color(35, 185, 185, 160));
			g2d.drawArc(x, y, s, s, 82 - (int)(angle + step), (int)(angle - end) + 8);
		} else {
			float empty = 0;
			for (int i = 0; i < colors.length; i++) {
				if (dist.length > i) {
					empty += dist[i];
					g2d.setColor(colors[i]);
					g2d.setStroke(new BasicStroke(2));
					g2d.drawArc(x, y, s, s, 90 - (int)empty, (int)dist[i] - 2);
					g2d.setStroke(new BasicStroke(1));
					g2d.drawArc(x + 4, y + 4, s - 8, s - 8, 90 - (int)empty, (int)dist[i] - 2);
				}
			}
			g2d.setColor(new Color(15, 125, 125, 100));
			g2d.setStroke(new BasicStroke(2));
			g2d.drawArc(x, y, s, s, 91, (int)(357 - empty));
			g2d.setStroke(new BasicStroke(1));
			g2d.drawArc(x + 4, y + 4, s - 8, s - 8, 91, (int)(357 - empty));
		}
	}
}

package Orbit;
import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JPanel;

import Utils.EventInitiater;
import Utils.FontManager;

/*
 * OrbitGraph class by Andreas, 2015
 * 
 * This class is designed to display a customizable graph,
 * following the theme of the Orbit UI layout.
 * Actions for clicking is customized to arbitary code, and
 * graph data varies with two-dimensional points in an xy-
 * coordinate system.
 * 
 * Java 1.7
 * 
 */

public class OrbitGraph extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Font font;
	int x;
	int y;
	int w = 300;
	int h = 200;
	protected double[][] data;
	protected double[][] secondData;
	double line = 0;
	protected String[] bottomLabels;
	int labelStart = 0;
	int labelInterval = 20;
	Rectangle view = new Rectangle(0, 0, 0, 0);
	protected int lastHover = -1;
	EventInitiater onclick = new EventInitiater();
	EventInitiater hoverpoint = new EventInitiater();
	
	protected void loadFont() {
		font = new FontManager("Titillium-Regular.ttf", 12).get();
		
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
		{
			public void eventDispatched(AWTEvent e)
		    {
		        if (e.getID() == 502) {
		        	onclick.fire();
		        }
		    }
		}, eventMask);
	}
	
	// KOPIERET FRA STACKOVERFLOOOOOOOOOOOOOOOW
	protected static <T> T[] append(T[] arr, T element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	protected static int[] append(int[] arr, int element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	protected static float[] append(float[] arr, float element) {
	    final int N = arr.length;
	    arr = Arrays.copyOf(arr, N + 1);
	    arr[N] = element;
	    return arr;
	}
	
	public OrbitGraph(int x, int y) {
		this.x = x;
		this.y = y;
		this.data = new double[0][0];
		this.secondData = new double[0][0];
		this.bottomLabels = new String[0];
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = new double[0][0];
		this.bottomLabels = new String[0];
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data, String[] bottomLabels) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = new double[0][0];
		this.bottomLabels = bottomLabels;
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data, String[] bottomLabels, int labelStart, int labelInterval) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = new double[0][0];
		this.bottomLabels = bottomLabels;
		this.labelStart = labelStart;
		this.labelInterval = labelInterval;
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data, double[][] secondData, double line) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = sortData(secondData);
		this.bottomLabels = new String[0];
		this.line = line;
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data, double[][] secondData, double line, String[] bottomLabels) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = sortData(secondData);
		this.line = line;
		this.bottomLabels = bottomLabels;
		loadFont();
	}
	
	public OrbitGraph(int x, int y, double[][] data, double[][] secondData, double line, String[] bottomLabels,
			int labelStart, int labelInterval) {
		this.x = x;
		this.y = y;
		this.data = sortData(data);
		this.secondData = sortData(secondData);
		this.line = line;
		this.bottomLabels = bottomLabels;
		this.labelStart = labelStart;
		this.labelInterval = labelInterval;
		loadFont();
	}
	
	public void add(double[] node) {
		data = sortData(append(data, node));
	}
	
	public void add(double x, double y) {
		data = sortData(append(data, new double[]{x, y}));
	}
	
	public void addSecond(double x, double y) {
		secondData = sortData(append(secondData, new double[]{x, y}));
	}
	
	public double[][] getData() {
		return data;
	}
	
	public double[] getPoint(int index) {
		if (index >= 0 && index < data.length) {
			return data[index];
		} else {
			return new double[]{0, 0};
		}
	}
	
	public int getPoint(double x, double y) {
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] == x && data[i][1] == y) {
				return i;
			}
		}
		return -1;
	}
	
	public double[][] getSecondData() {
		return secondData;
	}
	
	public String[] getLabels() {
		return bottomLabels;
	}
	
	public boolean change(int index, double x, double y) {
		return change(index, x, y, 1f);
	}
	
	public boolean change(int index, double x, double y, float p) {
		if (index >= 0 && index < data.length) {
			data[index][0] += (x - data[index][0]) * p;
			data[index][1] += (y - data[index][1]) * p;
			data = sortData(data);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean change(int index, double[] point, float p) {
		return change(index, point[0], point[1], p);
	}
	
	public boolean changeSecond(int index, double x, double y) {
		return changeSecond(index, x, y, 1f);
	}
	
	public boolean changeSecond(int index, double x, double y, float p) {
		if (index >= 0 && index < secondData.length) {
			secondData[index][0] += (x - secondData[index][0]) * p;
			secondData[index][1] += (y - secondData[index][1]) * p;
			secondData = sortData(secondData);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean changeSecond(int index, double[] point, float p) {
		return changeSecond(index, point[0], point[1], p);
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g2d);
    }
	
	protected int minY() {
		boolean set = false;
		double t = 0;
		for (int i = 0; i < data.length; i++) {
			if (!set || data[i][1] < t) {
				set = true;
				t = data[i][1];
			}
		}
		return (int)t;
	}
	
	protected int maxY() {
		boolean set = false;
		double t = 0;
		for (int i = 0; i < data.length; i++) {
			if (!set || data[i][1] > t) {
				set = true;
				t = data[i][1];
			}
		}
		return (int)t;
	}
	
	public double[][] sortData(double[][] tab) {
		double[][] t = tab.clone();
		
		Arrays.sort(t, new Comparator<double[]>() {
			
			@Override
			public int compare(final double[] e1, final double[] e2) {
				return Double.compare(e1[0], e2[0]);
			}
			
		});
		
		return t;
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
	
	protected int textWidth(String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		Point point = MouseInfo.getPointerInfo().getLocation();
		Point loc = getLocationOnScreen();
		
		g2d.setColor(new Color(20, 90, 95, 100));
		g2d.fillRect(x, y, w, h);
		g2d.setPaint(new GradientPaint(x, y + 25, new Color(20, 100, 95, 0), x, y + h - 35, new Color(35, 200, 180, 30)));
		g2d.fillRect(x, y + 25, w, h - 60);
		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(new Color(35, 185, 185, 60));
		g2d.drawLine(x, y + h, x + w, y + h);
		
		Rectangle tview = (view.width != 0 && view.height != 0) ? view :
			new Rectangle((int)data[0][0], minY() - 5,
					(int)data[data.length - 1][0] - (int)data[0][0], maxY() - minY() + 10);
		int steps = 2 + (int)((Math.log10(tview.height) - Math.floor(Math.log10(tview.height))) * 8) / 2;
		
		g2d.setClip(x, y, w, h - 35);
		g2d.setColor(new Color(35, 185, 185, 30));
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{2.0f}, 1.0f));
		for (int i = 1; i < steps; i++) {
			g2d.drawLine(x, y + (int)(((float)i / steps) * (h - 35)), x + w, y + (int)(((float)i / steps) * (h - 35)));
		}
		g2d.setColor(new Color(100, 120, 120, 150));
		int ly = y + h - 38 - (int)((double)((line - tview.y) / tview.height) * (h - 35));
		g2d.drawLine(x, ly, x + w - 37, ly);
		g2d.drawLine(x + w - 12, ly, x + w, ly);
		g2d.setFont(font);
		g2d.setColor(new Color(100, 10, 0, 20));
		for (int dx = -1; dx < 2; dx++) {
			for (int dy = -1; dy < 2; dy++) {
				g2d.drawString("AVG", x + w - 35 + dx, ly + 4 + dx);
			}
		}
		g2d.setColor(new Color(200, 20, 0, 240));
		g2d.drawString("AVG", x + w - 35, ly + 4);
		g2d.setColor(new Color(25, 185, 120, 150));
		g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{3.0f}, 2.0f));
		for (int i = 1; i < secondData.length; i++) {
				int tx = x + (int)((double)((secondData[i][0] - tview.x) / tview.width) * w);
				int ty = y + h - 35 - (int)((double)((secondData[i][1] - tview.y) / tview.height) * (h - 35));
				int px = x + (int)((double)((secondData[i - 1][0] - tview.x) / tview.width) * w);
				int py = y + h - 35 - (int)((double)((secondData[i - 1][1] - tview.y) / tview.height) * (h - 35));
				g2d.drawLine(px, py, tx, ty);
		}
		g2d.setStroke(new BasicStroke(1.5f));
		boolean hasHover = false;
		for (int i = 0; i < data.length; i++) {
			if (data[i][0] >= tview.x && data[i][0] <= tview.getMaxX()) {
				if (data[i][1] >= tview.y && data[i][1] <= (int)tview.getMaxY()) {
					int tx = x - 3 + (int)((double)((data[i][0] - tview.x) / tview.width) * w);
					int ty = y + h - 38 - (int)((double)((data[i][1] - tview.y) / tview.height) * (h - 35));
					boolean hover = Math.sqrt(Math.pow(point.x - loc.x - (tx + 3), 2) + Math.pow(point.y - loc.y - 
							(ty + 3), 2)) <= 6;
					if (hover) {
						hasHover = true;
						if (lastHover != i) {
							lastHover = i;
							hoverpoint.fire(i, data[i]);
						}
					}
					if (i > 0) {
						int px = x + (int)((double)((data[i - 1][0] - tview.x) / tview.width) * w);
						int py = y + h - 35 - (int)((double)((data[i - 1][1] - tview.y) / tview.height) * (h - 35));
						g2d.drawLine(px, py, tx + 3, ty + 3);
					}
					if (hover) {
						g2d.setColor(new Color(35, 185, 185));
						g2d.fillArc(tx, ty, 6, 6, 0, 360);
						g2d.drawArc(tx - 4, ty - 4, 13, 13, 0, 360);
						int tw = textWidth(Integer.toString((int)data[i][1]), g2d);
						boolean above = ty - y > 30;
						int side = tx - x - tw / 2 - 12 < 0 ? 1 : (tx - x + tw / 2 + 12 > w ? -1 : 0);
						g2d.setColor(new Color(0, 0, 0, 200));
						g2d.fillRoundRect(tx - tw / 2 - 8 + (side * (tw + 10)), side != 0 ? ty - 8 : (above ? ty - 28 : ty + 13),
								tw + 16, 19, 4, 4);
						g2d.setPaint(new GradientPaint(tx, side != 0 ? ty - 6 : (above ? ty - 25 : ty + 16),
								new Color(25, 90, 95, 140), tx,
								side != 0 ? ty + 6 : (above ? ty - 12 : ty + 29), new Color(30, 135, 137, 160)));
						g2d.fillRoundRect(tx - tw / 2 - 8 + (side * (tw + 10)), side != 0 ? ty - 8 : (above ? ty - 28 : ty + 13),
								tw + 16, 19, 4, 4);
						g2d.setColor(new Color(35, 185, 185));
						g2d.drawString(Integer.toString((int)data[i][1]), tx - tw / 2 + (side * (tw + 10)),
							side != 0 ? ty + 5 : (above ? ty - 15 : ty + 26));
						g2d.setColor(new Color(35, 185, 185, 200));
					} else {
						g2d.setColor(new Color(35, 185, 185, 200));
						g2d.drawArc(tx, ty, 6, 6, 0, 360);
					}
				}
			}
		}
		if (!hasHover) lastHover = -1;
		g2d.setColor(new Color(35, 200, 120, 200));
		g2d.setClip(null);
		g2d.setColor(new Color(0, 0, 0, 200));
		g2d.fillRect(x, y, 30, h - 35);
		g2d.setPaint(new GradientPaint(x, y, new Color(35, 175, 185, 45), x, y + h - 35, new Color(25, 150, 145, 100)));
		g2d.fillRect(x, y, 30, h - 35);
		g2d.setColor(new Color(35, 185, 185));
		for (int i = 1; i < steps; i++) {
			float perc = ((float)i / steps);
			g2d.drawString(format((int)(tview.y + perc * tview.height)), x + 9, y + h - 31 - (int)(perc * (h - 35)));
		}
		g2d.setClip(x, y + h - 35, w, 35);
		for (int i = 0; i < bottomLabels.length; i++) {
			float perc = (float)(((double)i * labelInterval) + labelStart - tview.x) / (tview.width);
			if (perc > -0.2 && perc < 1.2) {
				if (perc > 0.9) {
					g2d.setColor(new Color(35, 185, 185, 255 - (int)((perc - 0.9) * 10 * 200)));
				} else if (perc < 0.1) {
					g2d.setColor(new Color(35, 185, 185, 55 + (int)((perc) * 10 * 200)));
				} else {
					g2d.setColor(new Color(35, 185, 185, 255));
				}
				g2d.drawString(bottomLabels[i], x + perc * w - textWidth(bottomLabels[i], g2d) / 2, y + h - 12);
			}
		}
		g2d.setClip(null);
	}
}

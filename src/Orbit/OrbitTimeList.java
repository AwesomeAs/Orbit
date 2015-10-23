package Orbit;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JPanel;

import Utils.FontManager;

/*
 * OrbitTimeList class by Andreas, 2015
 * 
 * This class is designed to display a list
 * with items showing a title, timestamp and type.
 * The class is responsible formatting timestamps,
 * adding items and removing items from its list.
 * 
 * Java 1.7
 * 
 */

public class OrbitTimeList extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font;
	private Font mfont;
	private int x;
	private int y;
	private int w = 200;
	private int h = 6; // 30px per item
	private float hoffset = 0;
	public boolean hovered = false;
	public String hoverTitle = "";
	public boolean enabled = true;
	protected String[] titles = {};
	protected Date[] times = {};
	protected int[] dots = {};
	protected float[] hoverf = {};
	protected SimpleDateFormat daytime = new SimpleDateFormat("hh:mm a");
	protected SimpleDateFormat datetime = new SimpleDateFormat("dd. MMM");
	
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
	
	protected void loadFont() {
		font = new FontManager("Titillium-Regular.ttf", 14).get();
		mfont = new FontManager("Titillium-Regular.ttf", 10).get();
	}
	
	public void add(String title, Date time, int dot) {
		titles = append(titles, title);
		times = append(times, time);
		dots = append(dots, dot);
		hoverf = append(hoverf, 0);
		hoffset = titles.length > h ? 0 : 1f;
	}
	
	public void remove(int index) {
		System.arraycopy(titles, index + 1, titles, index, titles.length - 1 - index);
	    System.arraycopy(times, index + 1, times, index, times.length - 1 - index);
	    System.arraycopy(dots, index + 1, dots, index, dots.length - 1 - index);
	    titles = Arrays.copyOf(titles, titles.length - 1);
	    times = Arrays.copyOf(times, times.length - 1);
	    dots = Arrays.copyOf(dots, dots.length - 1);
	}
	
	public OrbitTimeList(int x, int y) {
		this.x = x;
		this.y = y;
		loadFont();
	}
	
	public OrbitTimeList(int x, int y, String[] titles, Date[] times, int[] dots) {
		this(x, y);
		this.titles = titles;
		this.times = times;
		this.dots = dots;
		hoverf = new float[titles.length];
		for (int i = 0; i < titles.length; i++) {
			hoverf[i] = 0;
		}
		loadFont();
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
		
		Point point = MouseInfo.getPointerInfo().getLocation();
		Point loc = getLocationOnScreen();
		
		hoffset = titles.length > h ? ((hoffset + 2) * 0.9f - 2) : hoffset * 0.9f;
		if (titles.length >= h + 2 && hoffset <= -1.99f) {
			while (titles.length > h) {
				remove(0);
			}
			hoffset = 0;
		}
		
		hovered = false;
		hoverTitle = "";
		g2d.setClip(x, y, w, h * 30);
		int offset = (int)(hoffset * 30);
		for (int i = 0; i < titles.length; i++) {
			if (i < times.length && i < dots.length) {
				boolean hover = enabled ? (point.x - loc.x >= x && point.x - loc.x <= x + w && point.y - loc.y + offset >= y + i * 30
						&& point.y - loc.y + offset <= y + i * 30 + 29) : false;
				hovered = hovered || hover;
				hoverf[i] += ((hover ? 1 : 0) - hoverf[i]) * 0.1;
				if (hover) {
					hoverTitle = titles[i];
				}
				g2d.setPaint(
						new GradientPaint(x, y, new Color(30, 145, 140, 75 + (int)(hoverf[i] * 50)),
								x + w, y, new Color(35, 185, 185, 30 + (int)(hoverf[i] * 30))));
				g2d.fillRect(x, y + i * 30 + offset, w, 28);
				g2d.setPaint(
						new GradientPaint(x, y, new Color(35, 185, 185, 100), x + w, y, new Color(35, 185, 185, 50)));
				g2d.drawLine(x, y + i * 30 + 28 + offset, x + w, y + i * 30 + 28 + offset);
				g2d.setPaint(new GradientPaint(x, y, new Color(0, 0, 0, 100), x + w - 10, y, new Color(0, 0, 0, 0)));
				g2d.drawLine(x, y + i * 30 + 27 + offset, x + w, y + i * 30 + 27 + offset);
				g2d.setColor(new Color(40, 225, 220));
				g2d.setFont(font);
				g2d.drawString(titles[i], x + 58, y + i * 30 + 18 + offset);
				g2d.setFont(mfont);
				if (times[i].after(new Date(System.currentTimeMillis() - 60000))) {
					g2d.drawString("now", x + 5, y + i * 30 + 17 + offset);
				} else if (times[i].after(new Date(System.currentTimeMillis() - (60 * 60 * 24) * 1000))) {
					g2d.drawString(
							daytime.format(times[i]), x + 5, y + i * 30 + 17 + offset);
				} else {
					g2d.drawString(
							datetime.format(times[i]), x + 5, y + i * 30 + 17 + offset);
				}
				switch (dots[i]) {
					case 1:
						g2d.setColor(new Color(100, 255, 150));
						break;
					case 2:
						g2d.setColor(new Color(240, 40, 40));
						break;
					case 3:
						g2d.setColor(new Color(40, 210, 255));
						break;
					default:
						g2d.setColor(new Color(0, 0, 0, 0));
				}
				g2d.fillArc(x + w - 15, y + i * 30 + 11 + offset, 6, 6, 0, 360);
				
			}
		}
		g2d.setClip(null);
	}
	
}

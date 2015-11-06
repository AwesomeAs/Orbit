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
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.util.Arrays;

import javax.swing.JPanel;

import Utils.EventInitiater;
import Utils.FontManager;

/*
 * OrbitOptionList class by Andreas, 2015
 * 
 * This class is designed to display a multi-purpose
 * list of toggles / dropdown elements to be interacted
 * with.
 * The class is responsible for handling detection of
 * hovering, updating item states and add new items.
 * 
 * Java 1.7
 * 
 */

public class OrbitOptionList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font;
	private int x;
	private int y;
	public int w = 200;
	public boolean hovered = false;
	public String hoverTitle = "";
	public boolean visible = true;
	protected String[] titles;
	protected int[] types;
	protected String[][] options;
	protected float[] hoverf;
	protected int lastHover = -1;
	public EventInitiater onclick = new EventInitiater();
	public EventInitiater onhover = new EventInitiater();
	
	protected void loadFont() {
		font = new FontManager("Titillium-Regular.ttf", 10).get();
		hoverf = new float[titles.length];
		for (int i = 0; i < titles.length; i++) {
			hoverf[i] = 0;
		}
		
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
		{
			public void eventDispatched(AWTEvent e)
		    {
		        if (e.getID() == 502) {
		        	click();
		        }
		    }
		}, eventMask);
	}
	
	protected void click() {
		if (hovered) {
			Point point = MouseInfo.getPointerInfo().getLocation();
			Point loc = getLocationOnScreen();
			
			boolean isDropdownActive = false;
			for (int i = 0; i < titles.length; i++) {
				if (i < types.length && i < options.length && !isDropdownActive) {
					boolean active;
					boolean hover;
					switch (types[i]) {
						case 1:
							active = options[i][0] == "1";
							hover = Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 17), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9 ||
									Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 27), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9 ||
									(point.x - loc.x >= x + w - 27 && point.x - loc.x <= x + w - 17 &&
									point.y - loc.y >= y + i * 45 + 9 && point.y - loc.y <= y + i * 45 + 27);
							if (hover) {
								options[i][0] = active ? "0" : "1";
								onclick.fire(i, !active);
							}
							break;
						case 2:
							active = options[i][1] == "1";
							hover = point.x - loc.x >= x + w / 2 &&
								point.x - loc.x <= x + w && point.y - loc.y >= y + i * 45 &&
								point.y - loc.y <= y + i * 45 + 35;
							boolean didFire = false;
							if (active && !hover) {
								isDropdownActive = true;
								for (int j = 2; j < options[i].length; j++) {
									int ty = y + i * 45 + 7 + (j - 2) * 30;
									hover = point.x - loc.x >= x - 8 && point.x - loc.x <= x + w - 8 &&
											point.y - loc.y >= ty + 32 && point.y - loc.y <= ty + 28 + 31;
									if (hover) {
										options[i][0] = options[i][j];
										didFire = true;
										onclick.fire(i, false, options[i][0]);
									}
								}
								hover = true;
							}
							if (hover) {
								options[i][1] = active ? "0" : "1";
								if (!didFire) {
									onclick.fire(i, !active);
								}
							}
							break;
						default:
							active = options[i][0] == "1";
							hover = Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 17), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9;
							if (hover) {
								options[i][0] = active ? "0" : "1";
								onclick.fire(i, !active);
							}
					}
				} else if (isDropdownActive) {
					break;
				}
			}
		}
	}
	
	public String getSelected(int index) {
		if (titles.length > index) {
			String selected = options[index][0];
			String ret = "";
			for (int i = 2; i < options[index].length; i++) {
				if (options[index][i] == selected) ret = options[index][i];
			}
			return ret;
		} else {
			return "";
		}
	}
	
	public boolean getState(int index) {
		if (titles.length > index) {
			return options[index][types[index] == 2 ? 1 : 0] == "1";
		} else {
			return false;
		}
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
	
	public OrbitOptionList add(String title, int type) {
		titles = append(titles, title);
		types = append(types, type);
		options = append(options, new String[]{"0"});
		hoverf = append(hoverf, 0);
		return this;
	}
	
	public OrbitOptionList add(String title, int type, boolean option) {
		titles = append(titles, title);
		types = append(types, type);
		options = append(options, new String[]{option ? "1" : "0"});
		hoverf = append(hoverf, 0);
		return this;
	}
	
	public OrbitOptionList add(String title, int type, String[] toptions) {
		titles = append(titles, title);
		types = append(types, type);
		options = append(options, toptions);
		hoverf = append(hoverf, 0);
		return this;
	}
	
	public OrbitOptionList addOption(int index, String value) {
		if (index >= 0 && index < options.length) {
			options[index] = append(options[index], value);
		}
		return this;
	}
	
	public OrbitOptionList(int x, int y) {
		this.x = x;
		this.y = y;
		this.titles = new String[0];
		this.types = new int[0];
		this.options = new String[0][0];
		loadFont();
	}
	
	public OrbitOptionList(int x, int y, String[] titles, int[] types) {
		this.x = x;
		this.y = y;
		this.titles = titles;
		this.types = types;
		this.options = new String[titles.length][];
		for (int i = 0; i < titles.length; i++) {
			this.options[i] = new String[]{"0"};
		}
		loadFont();
	}
	
	public OrbitOptionList(int x, int y, String[] titles, int[] types, String[][] options) {
		this.x = x;
		this.y = y;
		this.titles = titles;
		this.types = types;
		this.options = options;
		loadFont();
	}
	
	protected int textWidth(String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
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
		
		hovered = false;
		hoverTitle = "";
		if (visible) {
			for (int i = 0; i < titles.length; i++) {
				if (i < types.length && i < options.length) {
					g2d.setColor(new Color(0, 0, 0, 200));
					g2d.fillRect(x, y + i * 45, w, 35);
					g2d.setPaint(
							new GradientPaint(x, y, new Color(30, 145, 140, 75),
									x + w, y, new Color(35, 185, 185, 30)));
					g2d.fillRect(x, y + i * 45, w, 35);
					g2d.setPaint(
							new GradientPaint(x, y, new Color(35, 185, 185, 100), x + w, y, new Color(35, 185, 185, 50)));
					g2d.drawLine(x, y + i * 45 + 35, x + w, y + i * 45 + 35);
					g2d.setColor(new Color(40, 225, 220));
					g2d.setFont(font);
					g2d.drawString(titles[i], x + 7, y + i * 45 + 22);
					
					g2d.setStroke(new BasicStroke(1.2f));
					boolean active;
					boolean hover;
					switch (types[i]) {
						case 1:
							active = options[i][0] == "1";
							hover = Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 17), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9 ||
									Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 27), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9 ||
									(point.x - loc.x >= x + w - 27 && point.x - loc.x <= x + w - 17 &&
									point.y - loc.y >= y + i * 45 + 9 && point.y - loc.y <= y + i * 45 + 27);
							hovered = hovered || hover;
							if (hover) {
								hoverTitle = titles[i];
								if (lastHover != i) {
									onhover.fire(i);
								}
								lastHover = i;
							}
							hoverf[i] += ((active ? 1 : 0) - hoverf[i]) * 0.2;
							g2d.setPaint(new GradientPaint(
								x, y + i * 45, new Color(
									(int)(30 * hoverf[i]),
									(int)(160 * hoverf[i]),
									(int)(100 * hoverf[i]),
									100),
								x, y + i * 45 + 35, new Color(
									(int)(60 * hoverf[i]),
									(int)(200 * hoverf[i]),
									(int)(90 * hoverf[i]),
									(int)(100 + 50 * hoverf[i]))));
							g2d.fillRoundRect(x + w - 33, y + i * 45 + 10, 25, 15, 15, 15);
							g2d.setColor(new Color(
									35 + (int)(65 * hoverf[i]),
									185 + (int)(45 * hoverf[i]),
									185 - (int)(65 * hoverf[i]),
									150 + (int)(50 * hoverf[i])));
							g2d.drawRoundRect(x + w - 35, y + i * 45 + 8, 28, 18, 18, 18);
							g2d.setColor(new Color(0, 0, 0, 100));
							g2d.fillArc(x + w - (34 - (int)(hoverf[i] * 11)), y + i * 45 + 9, 17, 17, 0, 360);
							g2d.setColor(new Color(
									35 + (int)(65 * hoverf[i]),
									185 + (int)(45 * hoverf[i]),
									185 - (int)(65 * hoverf[i]),
									100 + (int)(155 * hoverf[i])));
							g2d.fillArc(x + w - (33 - (int)(hoverf[i] * 11)), y + i * 45 + 10, 15, 15, 0, 360);
							break;
						case 2:
							if (options[i].length > 1) {
								g2d.setColor(new Color(40, 225, 220, 200));
								g2d.drawString(options[i][0], x + w - textWidth(options[i][0], g2d) - 25, y + i * 45 + 21);
								g2d.setStroke(new BasicStroke(2));
								active = options[i][1] == "1";
								if (active) {
									g2d.drawLine(x + w - 15, y + i * 45 + 19, x + w - 11, y + i * 45 + 15);
									g2d.drawLine(x + w - 19, y + i * 45 + 15, x + w - 15, y + i * 45 + 19);
								} else {
									g2d.drawLine(x + w - 16, y + i * 45 + 13, x + w - 12, y + i * 45 + 17);
									g2d.drawLine(x + w - 16, y + i * 45 + 21, x + w - 12, y + i * 45 + 17);
								}
								hover = point.x - loc.x >= x + w / 2 &&
										point.x - loc.x <= x + w && point.y - loc.y >= y + i * 45 &&
										point.y - loc.y <= y + i * 45 + 35;
								hovered = hovered || hover;
								if (hover) {
									if (lastHover != i) {
										onhover.fire(i);
									}
									lastHover = i;
								}
							} else {
								g2d.setColor(new Color(40, 225, 220, 100));
								g2d.drawString(options[i][0], x + w - textWidth(options[i][0], g2d) - 25, y + i * 45 + 21);
							}
							break;
						default:
							active = options[i][0] == "1";
							hover = Math.sqrt(
									Math.pow(point.x - loc.x - (x + w - 17), 2.) +
									Math.pow(point.y - loc.y - (y + i * 45 + 18), 2.)) <= 9;
							hovered = hovered || hover;
							if (hovered) hoverTitle = titles[i];
							hoverf[i] += ((active ? 1 : 0) - hoverf[i]) * 0.2;
							g2d.setPaint(new GradientPaint(
								x, y + i * 45, new Color(
									(int)(30 * hoverf[i]),
									(int)(160 * hoverf[i]),
									(int)(100 * hoverf[i]),
									100),
								x, y + i * 45 + 35, new Color(
									(int)(60 * hoverf[i]),
									(int)(200 * hoverf[i]),
									(int)(90 * hoverf[i]),
									(int)(100 + 50 * hoverf[i]))));
							g2d.fillArc(x + w - 23, y + i * 45 + 11, 15, 15, 0, 360);
							g2d.setColor(new Color(
									35 + (int)(65 * hoverf[i]),
									185 + (int)(45 * hoverf[i]),
									185 - (int)(65 * hoverf[i]),
									150 + (int)(50 * hoverf[i])));
							g2d.drawArc(x + w - 25, y + i * 45 + 9, 18, 18, 0, 360);
							g2d.setStroke(new BasicStroke(2));
							if (active) {
								g2d.drawLine(x + w - 13, y + i * 45 + 16, x + w - 18, y + i * 45 + 21);
								g2d.drawLine(x + w - 18, y + i * 45 + 21, x + w - 20, y + i * 45 + 19);
							}
							g2d.setStroke(new BasicStroke(1));
							if (hover) {
								if (lastHover != i) {
									onhover.fire(i);
								}
								lastHover = i;
							}
					}
				}
			}
		}
	}
	
	public void paintDropdown(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		Point point = MouseInfo.getPointerInfo().getLocation();
		Point loc = getLocationOnScreen();
		
		if (visible) {
			g2d.setStroke(new BasicStroke(1));
			
			g2d.setFont(font);
			
			for (int i = 0; i < titles.length; i++) {
				if (i < types.length && i < options.length && options[i].length > 2 && options[i][1] == "1") {
					
					for (int j = 2; j < options[i].length; j++) {
						int ty = y + i * 45 + 37 + (j - 2) * 30;
						boolean hover = point.x - loc.x >= x && point.x - loc.x <= x + w && point.y - loc.y >= ty &&
								point.y - loc.y <= ty + 28;
						hovered = hovered || hover;
						g2d.setColor(new Color(0, 0, 0, 220));
						g2d.fillRect(x, ty, w, 29);
						g2d.setPaint(new GradientPaint(x, ty, new Color(25, 150, 150, hover ? 90 : 30),
								x, ty + 30, new Color(35, 185, 185, hover ? 120 : 50)));
						g2d.fillRect(x, ty, w, 28);
						g2d.setColor(new Color(35, 185, 185, 90));
						g2d.drawLine(x, ty, x + w, ty);
						g2d.drawLine(x, ty + 28, x + w, ty + 28);
						g2d.setColor(new Color(35, 185, 185));
						g2d.drawString(options[i][j], x + 10, ty + 20);
					}
				}
			}
		}
	}
}
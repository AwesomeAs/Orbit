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
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import Utils.EventInitiater;
import Utils.FontManager;

/*
 * OrbitButton class by Andreas, 2015
 * 
 * This class is designed to display a clickable button following
 * the theme of the Orbit UI layout.
 * Actions for clicking is customized to arbitary code, and
 * colors of the button varies with green, blue and red.
 * The class is responsible for displaying the button with
 * button text and optional button icon, and handle hover.
 * 
 * Java 1.7
 * 
 */

public class OrbitButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	private int w = 150;
	private String value = "BUTTON";
	private int type = 0;
	protected float hoverf = 0;
	protected ImageIcon icon;
	public boolean hovered = false;
	private Font font;
	public EventInitiater onclick = new EventInitiater();
	
	protected void loadFont() {
		font = new FontManager("ProFontWindows.ttf", 12).get();
		
		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener()
		{
			public void eventDispatched(AWTEvent e)
		    {
		        if (e.getID() == 502 && hovered) {
		        	onclick.fire();
		        }
		    }
		}, eventMask);
	}
	
	public OrbitButton(int x, int y) {
		this.x = x;
		this.y = y;
		loadFont();
	}
	
	public OrbitButton(int x, int y, String value, int type) {
		this(x, y);
		this.value = value;
		this.type = type;
	}
	
	public OrbitButton(int x, int y, int w, String value, int type) {
		this(x, y, value, type);
		this.w = w;
	}
	
	public OrbitButton(int x, int y, String value, int type, String icon) {
		this(x, y, value, type);
		try {
			this.icon = new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/" + icon)));
		} catch (IOException e) {}
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paint(g2d);
    }
	
	protected int textWidth(String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if (isVisible()) {
			Point point = MouseInfo.getPointerInfo().getLocation();
			Point loc = getLocationOnScreen();
			
			hovered = isEnabled() ? point.x - loc.x >= x && point.x - loc.x <= x + w && point.y - loc.y >= y && point.y - loc.y <= y + 35 : false;
			hoverf += ((hovered ? 1 : 0) - hoverf) * 0.1;
			
			switch (type) {
				case 1:
					g2d.setColor(new Color(30, 160, 100, 200));
					break;
				case 2:
					g2d.setColor(new Color(240, 20, 0, 200));
					break;
				default:
					g2d.setColor(new Color(35, 185, 185, 200));
			}
			g2d.fillRoundRect(x, y, w, 35, 5, 5);
			switch (type) {
				case 1:
					g2d.setPaint(new GradientPaint(x, y, new Color(3, 10 + (int)(hoverf * 30), 6 + (int)(hoverf * 20), 200),
							x, y + 35, new Color(2, 20 + (int)(hoverf * 50), 5 + (int)(hoverf * 45), 200)));
					break;
				case 2:
					g2d.setPaint(new GradientPaint(x, y, new Color(20 + (int)(hoverf * 40), 2 + (int)(hoverf * 10), 0, 200),
							x, y + 35, new Color(30 + (int)(hoverf * 70), 5 + (int)(hoverf * 25), 0, 200)));
					break;
				default:
					g2d.setPaint(new GradientPaint(x, y, new Color(5, 10 + (int)(hoverf * 30), 15 + (int)(hoverf * 35), 200),
							x, y + 35, new Color(10, 15 + (int)(hoverf * 65), 20 + (int)(hoverf * 70), 200)));
			}
			g2d.fillRoundRect(x + 1, y + 1, w - 2, 33, 4, 4);
			switch (type) {
				case 1:
					g2d.setColor(new Color(30, 160, 100));
					break;
				case 2:
					g2d.setColor(new Color(240, 20, 0));
					break;
				default:
					g2d.setColor(new Color(35, 185, 185));
			}
			g2d.setFont(font);
			g2d.drawString(value, x + w / 2 - textWidth(value, g2d) / 2 + (icon != null ? 10 : 0), y + 22);
			
			if (icon != null) {
				g2d.drawImage(icon.getImage(), x + w / 2 - textWidth(value, g2d) / 2 - 25, y + 3, 30, 30, icon.getImageObserver());
			}
		} else {
			hovered = false;
		}
	}
}

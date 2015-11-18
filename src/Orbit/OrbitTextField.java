package Orbit;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import Utils.FontManager;

/*
 * OrbitTextField class by Andreas, 2015
 * 
 * This class is designed to style a JTextField to
 * follow the Orbit UI layout.
 * The class is responsible for detecting focus,
 * limiting the length of the input and updating
 * the graphics of the element.
 * 
 * Java 1.7
 * 
 */

public class OrbitTextField extends JTextField implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x;
	public int y;
	private int w = 150;
	private int h = 30;
	public String label;
	public String desc;
	private int mode = 0;
	private int maxLength = 10;
	private Font font;
	private float hover = 0;
	public boolean isHover = false;
	public float focused = 0;
	
	protected void loadFont() {
		font = new FontManager("ProFontWindows.ttf", (int)(h * 0.4)).get();
		setFont(font);
		addMouseListener(this);
	}
	
	public OrbitTextField(int x, int y, int s, String label, String desc) {
		this.x = x;
		this.y = y;
		this.h = s;
		this.label = label;
		this.desc = desc;
		loadFont();
		setBounds(x, y, w, h);
		setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
	}
	
	public OrbitTextField(int x, int y, int w, int s, String label, String desc) {
		this(x, y, s, label, desc);
		this.w = w;
		setBounds(x, y, w, h);
		setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
	}

	public OrbitTextField(int x, int y, int s, String label, String desc, int maxLength, String type) {
		this(x, y, s, label, desc);
		this.maxLength = maxLength;
		switch (type) {
			case "right":
				mode = 1;
				break;
			case "form":
				mode = 2;
				break;
		}
		setBounds(x, y, w, h);
		setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
	}
	
	public OrbitTextField(int x, int y, int w, int s, String label, String desc, int maxLength, String type) {
		this(x, y, w, s, label, desc);
		this.maxLength = maxLength;
		switch (type) {
		case "right":
			mode = 1;
			break;
		case "form":
			mode = 2;
			break;
		}
		setBounds(x, y, w, h);
		setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		isHover = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		isHover = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPlaceholder(String placeholder) {
		desc = placeholder;
	}
	
	public void setTitle(String title) {
		label = title;
	}
	
	@Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(0f));
        super.paint(g2d);
        g2d.dispose();
    }
	
	public int textWidth(String text, Graphics2D g2d) {
		return (int)font.getStringBounds(text, g2d.getFontRenderContext()).getWidth();
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        // We need this because we've taken over the painting of the component
		hover += (((isHover && this.isEnabled()) ? 1 : 0) - hover) * 0.1;
		focused += ((this.hasFocus() ? 1 : 0) - focused) * 0.1;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        g2d.setColor(new Color(40, 225, 220));
        if (this.getText().length() > maxLength) this.setText(this.getText().substring(0, maxLength));
        //g2d.setFont(font);
        if (mode == 2) {
        	g2d.drawString(label, 2, 19 - (int)(focused * 13));
        } else {
        	g2d.drawString(label, 2, 19);
        }
        int width = textWidth(label, g2d);
        int cpos = textWidth(this.getText().substring(0, this.getCaretPosition()), g2d);
        g2d.setColor(new Color(40, 225, 220));
        if (mode == 2) {
        	g2d.drawString(this.getText(), 2 + (int)((width + 5.) * (1. - focused)), 19);
	        if (this.getText().length() == 0) {
	        	g2d.setColor(new Color(40, 225, 220, 90 + (int)(hover * 50)));
	        	g2d.drawString(desc, 2 + (int)((width + 5.) * (1. - focused)), 19);
	        }
	        g2d.setColor(new Color(40, 225, 220, 120));
	        g2d.drawString(this.getCaret().isVisible() ? "|" : "",
	        		cpos + (int)((width + 5.) * (1. - focused)), 19);
        } else if (mode == 1) {
        	g2d.drawString(this.getText(), w - textWidth(this.getText(), g2d) - 1, 19);
        	if (this.getText().length() == 0) {
        		g2d.setColor(new Color(40, 225, 220, 90 + (int)(hover * 50)));
        		g2d.drawString(desc, w - textWidth(desc, g2d) - 2, 19);
        	}
        	g2d.setColor(new Color(40, 225, 220, 120));
            g2d.drawString(this.getCaret().isVisible() ? "|" : "", w - textWidth(this.getText(), g2d) + cpos - 3, 19);
        } else {
        	g2d.drawString(this.getText(), 7 + width, 19);
	        if (this.getText().length() == 0) {
	        	g2d.setColor(new Color(40, 225, 220, 90 + (int)(hover * 50)));
	        	g2d.drawString(desc, 7 + width, 19);
	        }
	        g2d.setColor(new Color(40, 225, 220, 120));
	        g2d.drawString(this.getCaret().isVisible() ? "|" : "", cpos + width + 5, 19);
        }
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(new Color(35, 185, 185, 100 + (int)(hover * 100)));
        g2d.drawLine(0, h - 1, w, h - 1);
        g2d.dispose();
        super.paintComponent(g);
    }
}

package Orbit;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JSlider;

/*
 * OrbitSlider class by Andreas, 2015
 * 
 * This class is designed to style a JSlider object
 * to follow the standards of the Orbit UI layout.
 * 
 * Java 1.7
 * 
 */

public class OrbitSlider extends JSlider {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x;
	int y;
	int w;
	int mode;

	public OrbitSlider(int _x, int _y, int _w, String _mode) {
        // Important, we taking over the filling of the
        // component...
        setOpaque(false);
        setBackground(Color.GREEN);
        x = _x;
        y = _y;
        w = _w;
        setBounds(x, y, w, 16);
        switch (_mode) {
            case "simple":
            	mode = 0;
            	break;
            case "system":
            	setEnabled(false);
            	mode = 1;
            	break;
            case "range":
            	mode = 2;
            	getModel().setRangeProperties(100, 200, getMinimum(), getMaximum(), getValueIsAdjusting());
            	
            	break;
            default:
            	mode = 0;
            	break;
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.SrcOver.derive(0f));
        super.paint(g2d);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // We need this because we've taken over the painting of the component
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
        g2d.setColor(new Color(30, 150, 155, 80));
        if (mode != 1)
        	g2d.fillRect(7, 6, w - 14, 4);
        else
        	g2d.fillRect((int)(super.getValue() * (((double)w - 12.) / 100.) + 4.), 6,
        			w - (int)(super.getValue() * (((double)w - 12.) / 100.) + 11.), 4);
        g2d.setPaint(new GradientPaint(7, 0, new Color(35, 185, 185), w - 7, 0, new Color(55, 242, 95)));
        g2d.fillRect(7, 6, (int)(super.getValue() * (((double)w - 12.) / 100.) - 5.), 4);
        if (isEnabled()) {
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillArc(5 + (int)(super.getValue() * (((double)w - 12.) / 100.) - 5.), 2, 12, 12, 0, 360);
            g2d.setPaint(new GradientPaint(0, 2, new Color(230, 240, 255), 0, 8, new Color(210, 230, 255)));
            g2d.fillArc(6 + (int)(super.getValue() * (((double)w - 12.) / 100.) - 5.), 3, 10, 10, 0, 360);
        }
        g2d.dispose();

        super.paintComponent(g);
    }

}
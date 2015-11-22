package Utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.util.Date;

import Orbit.OrbitButton;
import javafx.embed.swing.JFXPanel;

public class ExtJFXPanel extends JFXPanel {
	private static final long serialVersionUID = 1L;
	private final OrbitButton button;
	private long timestamp = new Date().getTime();
	private float countdown = 10f;
	
	public ExtJFXPanel(OrbitButton button) {
		this.button = button;
		this.setBackground(Color.BLACK);
	}
	
	public void setCountdown(int countdown) {
		this.countdown = (float)countdown;
		this.timestamp = new Date().getTime();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (button != null && button.isVisible()) {
			if (countdown > 0) {
				int t = Math.max(0, (int)(countdown - (new Date().getTime() - timestamp) / 1000.0));
				button.setText(t > 0 ? "SKIP IN " + t + " SECOND" + (t == 1 ? "" : "S") : "SKIP");
				button.setEnabled(t <= 0);
			}
			button.paintComponent(g);
			if (button.hovered) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}
	
}

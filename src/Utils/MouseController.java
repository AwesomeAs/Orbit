package Utils;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

/*
 * 
 * Utility class for controlling the current device's mouse.
 * 
 * Java 1.7
 * 
 */

public class MouseController {
	
	int desiredX = -1;
	int desiredY = -1;
	float damp = 0.05f;
	boolean clickOnFinish = false;
	protected EventListener listener;
	Robot robot;
	
	public MouseController() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyThread mt = new MyThread();
        new Thread(mt).start();
	}
	
	public void set(int x, int y) {
		desiredX = x;
		desiredY = y;
	}
	
	public void set(int x, int y, boolean click) {
		set(x, y);
		clickOnFinish = click;
	}
	
	public void set(int x, int y, boolean click, EventListener callback) {
		set(x, y, click);
		listener = callback;
	}
	
	public double dist(int x, int y) {
		Point point = MouseInfo.getPointerInfo().getLocation();
		return Math.sqrt(Math.pow(point.x - x + 2, 2) + Math.pow(point.y - y - 7, 2));
	}
	
	class MyThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				if (desiredX != -1 || desiredY != -1) {
					if (dist(desiredX, desiredY) < 15) {
						System.out.println("Reached");
						robot.mouseMove(desiredX, desiredY);
						if (listener != null) {
							listener.event(new Object[]{desiredX, desiredY, clickOnFinish});
						}
						if (clickOnFinish) {
							robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
							robot.waitForIdle();
							robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
						}
						desiredX = -1;
						desiredY = -1;
						clickOnFinish = false;
					} else {
						Point point = MouseInfo.getPointerInfo().getLocation();
						robot.mouseMove((int)(point.x + (desiredX - point.x + 10) * damp),
								(int)(point.y + (desiredY - point.y) * damp));
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

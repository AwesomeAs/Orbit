package Utils;

import java.awt.Graphics;
import javax.swing.JFrame;

import Orbit.OrbitButton;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MediaPlayer {
	
	private final ExtJFXPanel webPanel;
	private final JFrame window;
	private WebView webComponent;
	private OrbitButton button;
	private WebEngine engine = null;
	
	public MediaPlayer(JFrame window, OrbitButton button) {
		webPanel = new ExtJFXPanel(button);
		this.window = window;
		window.add(webPanel);
		this.button = button;
		if (button != null) {
			webPanel.add(button);
		}
	}
	
	public MediaPlayer(JFrame window) {
		this(window, null);
	}
	
	public void load(final String filename) {
		webPanel.setBounds(0, 0, window.getWidth(), window.getHeight());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				BorderPane borderPane = new BorderPane();
				webComponent = new WebView();
				engine = webComponent.getEngine();
				engine.setJavaScriptEnabled(true);
				System.out.println(getClass().getClassLoader().getResource("media/" + filename + ".mp4").toExternalForm());
				engine.loadContent("<!DOCTYPE html><head><title id=\"title\">running</title><script " +
				"type=\"text/javascript\">function onend() { document.getElementById('title').innerHTML = \"ended\"; " +
						"}</script></head><html style=\"background-color:black\">" +
						"<body style=\"padding:0; margin:0; width:100%; height:100%; overflow:hidden; " +
						"background-color:black\"><video style=\"width:100%; height:100%\" id=\"video\" autoplay " +
						"preload=\"auto\" onended=\"onend()\"><source src=\"" + getClass().getClassLoader().getResource("media/" +
						filename + ".mp4").toExternalForm() + "\" type=\"video/mp4\"></video></body></html>");
				borderPane.setCenter(webComponent);
				Scene scene = new Scene(borderPane, window.getWidth(), window.getHeight());
				webPanel.setScene(scene);
				if (button != null) {
					button.setVisible(true);
					webPanel.setCountdown(10);
				}
			}
		});
	}
	
	public boolean isPlaying() {
		if (engine != null && webPanel.getScene() != null) {
			return engine.getTitle() != null && engine.getTitle().equals("running");
		} else {
			return false;
		}
	}
	
	public boolean hasEnded() {
		if (engine != null && webPanel.getScene() != null) {
			if (engine.getTitle() != null && engine.getTitle().equals("ended")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void stop() {
		if (engine != null && webPanel.getScene() != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					engine.loadContent("");
				}
			});
		}
		webPanel.setCursor(null);
		webPanel.setScene(null);
		if (button != null) {
			button.setVisible(false);
		}
	}
	
	public Graphics getGraphics() {
		return webPanel.getGraphics();
	}
	
}

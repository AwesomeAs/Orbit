package Tester;

import java.util.Scanner;

import Networking.Matchmaker;
import Orbit.OrbitAdapter;
import Utils.AudioPlayer;
import game.*;

public class Startup {
	
	public static void main(String[] args) {
		System.out.println("Startup!");
		Scanner sc = new Scanner(System.in);
		System.out.println("Want to use the new GUI? Write 'y' or 'n'.");
		boolean isNew = false;
		while (true) {
			String got = sc.next();
			if (got.equals("y")) {
				isNew = true;
				break;
			} else if (got.equals("n")) {
				break;
			}
		}
		sc.close();
		OrbitAdapter adapter = new OrbitAdapter(1200, 800, 21, !isNew, new String[]{
				"Start",
				null,
				"Brewery",
				"Shipping",
				null,
				null,
				null,
				"Refuge",
				"Chance",
				"Jail",
				"Tax"
		});
		try {
			/*Matchmaker matcher =*/ new Matchmaker("matchmaker_keys");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(matcher.allUsers());
		
		adapter.setField(new Fleet("Cool spot", 2, 200));
		adapter.setField(new Refuge("Expensive", 6, 5000));
		adapter.setField(new LaborCamp("Free?", 12, 0, 5));
		
		adapter.setLoaderVisible(true);
		
		//matcher.clearCallback("talk");
		
		AudioPlayer aplay = adapter.getAudio("bgmusic.wav");
		aplay.play(true);
		
		adapter.setPlayer(0, "");
		adapter.setPlayer(1, "");
		adapter.setPlayer(2, "");
		adapter.setPlayerScore(0, 600);
		adapter.setPlayerScore(1, 600);
		adapter.setPlayerScore(2, 600);
		adapter.movePlayer(0, 1);
		adapter.movePlayer(1, 10);
		
		adapter.addLanguage("English");
		adapter.addLanguage("Danish");
		
		System.out.println(adapter.readPlayerName(0));
		System.out.println(adapter.getPlayerScore(0));
		System.out.println(adapter.getDieType());
		System.out.println(adapter.getLanguage());
		
		aplay.stop();
		
		aplay = adapter.getAudio("bgmusic_milkyway.wav");
		aplay.play(true);
		
		adapter.setScreenText("HELLO WORLD");
		adapter.setScreenDesc("A very very very\r\nvery very very very very very\nvery very very very very very long description.");
		adapter.stopVideo();
		while (true) {
			if (adapter.clickButton() == 0) {
				aplay.stop();
				adapter.playVideo("WantYouGone");
				adapter.waitForVideoEnded();
				aplay.play(true);
				System.out.println("Video stopped!");
				//adapter.moveMouse(1195, 5, true);
				adapter.setDieType(true);
				adapter.setLanguage((int)Math.floor(Math.random() * 2));
				System.out.println("Rolling dice!");
				System.out.println(adapter.getField(45));
			} else {
				System.out.println("Hit reset game");
				adapter.closeOldGUI();
			}
		}
		
	}
	
}

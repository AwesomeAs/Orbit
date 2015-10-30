package Tester;

import Orbit.OrbitAdapter;

public class Startup {
	
	public static void main(String[] args) {
		System.out.println("Startup!");
		OrbitAdapter adapter = new OrbitAdapter(1200, 800, 12);
		
		adapter.setPlayer(0, "");
		adapter.setPlayer(1, "");
		adapter.setPlayer(2, "");
		adapter.setPlayerScore(0, 600);
		adapter.setPlayerScore(1, 600);
		adapter.setPlayerScore(2, 600);
		adapter.movePlayer(0, 1);
		adapter.movePlayer(1, 1);
		
		System.out.println(adapter.readPlayerName(0));
		System.out.println(adapter.getPlayerScore(0));
		
		adapter.setScreenText("HELLO WORLD");
		
		while (true) {
			if (adapter.clickButton() == 0) {
				System.out.println("Rolling dice!");
				System.out.println(adapter.getField(45));
			} else {
				System.out.println("Hit reset game");
			}
		}
		
	}
	
}

package Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer {
	
	private Clip clip;
	
	/**
	 * Loads an audio from the audio resource folder.
	 * @param path
	 */
	public AudioPlayer(final String path) {
		try {
		    AudioInputStream audioInputStream =
		        AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("audio/" + path));
		    clip = AudioSystem.getClip();
		    clip.open(audioInputStream);
		} catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	 * Plays the loaded audio, optionally looped indefinitely.
	 * @param looped
	 */
	public void play(boolean looped) {
		if (looped) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.start();
		}
	}
	
	/**
	 * Stops the currently playing audio.
	 */
	public void stop() {
		if (clip.isRunning()) {
			clip.stop();
		}
	}
	
}

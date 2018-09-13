import java.lang.Object;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;

public class PlaySong{

	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		// TODO Auto-generated method stub
	}
	public static void play(String name) throws LineUnavailableException, IOException, UnsupportedAudioFileException, InterruptedException
	{
		File song = new File(name);
		AudioInputStream player = AudioSystem.getAudioInputStream(song);
		Clip current = AudioSystem.getClip();
		current.open(player);
		current.start();
		int time = current.getFrameLength();
		while(current.getFramePosition() < time)
		{
			Thread.sleep(10);
		}
		Thread.currentThread().interrupt();
		current.stop();
	}

}

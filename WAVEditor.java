import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * @author Vincent Vu
 * @since 10-07-2018
 *
 */
public class WAVEditor {
 public static final String EXT = ".wav";
 static AudioFormat format;
 static long frameLength;

 public static void main(String[] args) {
   try {
  byte[] data = WAVtoByte("Yellow Submarine_The Beatles_Yellow Submarine");
  Clip player = null;
  player = playSong(data, player);
  player.start();
   Thread.sleep(10000);
  int pos = pauseSong(player);
  Thread.sleep(5000);
  player.setFramePosition(pos);
  player.start();
  Thread.sleep(5000);
  stopSong(player);
  } catch (InterruptedException ex) {

  }
 }

 public static byte[] WAVtoByte(String fileName) {
  try {
   String path = fileName + EXT;
   File song = new File(path);
   AudioInputStream player = AudioSystem.getAudioInputStream(song);
   format = player.getFormat();
   frameLength = player.getFrameLength();

   int bytesAmt = player.available();
   System.out.println(bytesAmt);
   byte[] songData = new byte[bytesAmt];
   player.read(songData);
   player.close();

   // Changing array size to 1024 (Standard)
   // Change second 0 into 44 if the header becomes a problem in reading
   // 44 header size for WAV file
   // Doesn't work
   /*
    * byte[] buf = new byte[1024]; System.arraycopy(songData, 0, buf, 0, bytesAmt);
    */
   return songData;
  } catch (IOException | UnsupportedAudioFileException e1) {
   System.err.println("Audio file could not be converted");
  }

  return null;
 }

 public static Clip playSong(byte[] buf,Clip current) {
  if ((current == null) || (current != null && (!(current.isActive())))) {// checks if song is either not started
                    // or if the song was paused
   try {
    InputStream song = new ByteArrayInputStream(buf);
    AudioInputStream player = new AudioInputStream(song, format, frameLength);
    current = AudioSystem.getClip();
    current.open(player);
    return current;
   } catch (LineUnavailableException | IOException e) {
    // TODO Auto-generated catch block
    System.err.println("Error with sending clip");
   }
  }
  return null;
 }

 public static int pauseSong(Clip current) {
  if (current != null) {
   if (current.isActive()) {
    int pos = current.getFramePosition();
    current.stop();
    return pos;
   }
  }
  return 0;
 }
 public static void stopSong(Clip current) {
   if(current != null)
     {
      if(current.isActive())
      {
       current.stop();
      }
      current.flush();
      current.close();
      
     }
 }
}

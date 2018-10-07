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

public class WAVEditor {
 public static final String EXT = ".wav";
 static AudioFormat format;
 static long frameLength;
 static Clip current;
 
 public static void main(String[] args) {
   byte[] data = WAVtoByte("Yellow Submarine_The Beatles_Yellow Submarine");
   playSong(data);
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
  
  //Changing array size to 1024 (Standard)
  //Change second 0 into 44 if the header becomes a problem in reading
  //44 header size for WAV file
  /*byte[] buf = new byte[1024];
  System.arraycopy(songData, 0, buf, 0, bytesAmt); */
  return songData;
  } catch (IOException | UnsupportedAudioFileException e1) {
   System.err.println("Audio file could not be converted");
  }
  
  return null;
 }
 
 public static void playSong(byte[] buf) {
  try {
  InputStream song = new ByteArrayInputStream(buf);
  AudioInputStream player = new AudioInputStream(song,format, frameLength);
  current = AudioSystem.getClip();
  current.open(player);
  current.start();
  System.out.println("Song is playing");
  } catch (LineUnavailableException | IOException e) {
   // TODO Auto-generated catch block
   System.err.println("Error with playing song");
  }
 }
}

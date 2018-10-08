import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
/**
 * @author Luciano Vega
   Playbutton handles songs and adds the features to play and pause a song.
 */
public class PlayButton {
	static String uName = "";//used for the username
	static String song = "";//used for song being played
	static String playlist = "";//used for the name of playlist

	public static void main(String[] args) {//used to test this frame on its own.
		// TODO Auto-generated method stub
		// construct the frame
		JFrame frame = new PlayFrame("Nirvana_All Apologies_In Utero", "Cool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	static class PlayFrame extends JFrame{

		/**
		 * Playframe constructor that takes in song name, username, and playlist name
		 * @param s
		 * @param u
		 * @param pL
		 */
		PlayFrame(String s, String u, String pL)
		{
			uName = u;
			playlist = pL;
			song = s;
			setTitle("Play");
			setSize(500, 200);
			JPanel p = new PlayPanel(u, pL);
			this.add(p);
		}
		/**
		 * Play frame that takes in a song name and username
		 * @param s
		 * @param u
		 */
		PlayFrame(String s, String u)
		{
			uName = u;
			playlist = "";
			song = s;
			setTitle("Play");
			setSize(500, 200);
			JPanel p = new PlayPanel(u);
			this.add(p);
		}
	}
	static class PlayPanel extends JPanel{
		
		JButton play = new JButton("\u25B6");//play button
		JButton back = new JButton("Back");//back button
		JButton pause = new JButton("\u25F8");//pause button
		PlayPanel(String u)//panel for playing a song from search menu
		{
			play.setFont(new Font("Dialog", Font.PLAIN, 30));
			ActionListener a = new YActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			p1.add(back, BorderLayout.WEST);
			p1.add(pause, BorderLayout.EAST);
			p1.add(play, BorderLayout.CENTER);
			pause.addActionListener(a);
			play.addActionListener(a);
			back.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.CENTER);
		}
		PlayPanel(String u, String p)//panel for playing song from playlist
		{
			play.setFont(new Font("Dialog", Font.PLAIN, 30));
			ActionListener a = new XActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			//p1.add(title, BorderLayout.PAGE_START);
			p1.add(back, BorderLayout.WEST);
			p1.add(pause, BorderLayout.EAST);
			p1.add(play, BorderLayout.CENTER);
			play.addActionListener(a);
			pause.addActionListener(a);
			back.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.CENTER);
		}
		
		class YActionListener implements ActionListener{//Action listener for songs played from search menu
			private PlayPanel panel;
			Clip current;
			int pos = 0;
			

			public YActionListener(PlayPanel b)
			{
				this.panel = b;
			}
			/**
			 * Implement ActionListener
			 * @param e ActionEvent
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object o = e.getSource();
				if(o == panel.play )
				{
					if((current == null) || (current!=null && (!(current.isActive()))))//checks if song is either not started or if the song was paused
					{
						try {
							System.out.println(song + ".wav");
							File file = new File(song + ".wav");
							AudioInputStream player = AudioSystem.getAudioInputStream(file);
							current = AudioSystem.getClip();
							current.open(player);
							current.setFramePosition(pos);
							current.start();
						} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else if(o == panel.back)//stops song if still playing and goes back to search menu
				{
					if(current != null)
					{
						if(current.isActive())
						{
							current.stop();
						}
						current.flush();
						current.close();
						
					}
					JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
					pFrame.dispose();
//					new SearchMenuFrame(uName).setVisible(true);
				}
				else if(o == panel.pause)//pauses song if song is active and saves frame position to resume in play
				{
					if(current != null)
					{
						if(current.isActive())
						{
							pos = current.getFramePosition();
							current.stop();
						}
					}
				}
			}
		}
		class XActionListener implements ActionListener{//
			private PlayPanel panel;
			Clip current;
			int pos = 0;
			
			String username = uName;
			ArrayList<String> songList = new ArrayList<String>();
			
			public void updatePlaylist() 
			{
				
				try (InputStream input = new FileInputStream(username+".json"))
				{
				
					JSONObject obj = new JSONObject(new JSONTokener(input));								// turn into JSON object
				    
				    JSONArray listOfSongs = obj.getJSONArray(playlist);										// grabs JSON array of songs by mapping the playlist name
				    
				    for(int j = 0; j < listOfSongs.length(); j++)											// adds all songs from array into JList
				    {
				    	String temp = listOfSongs.getString(j);
				    	String[] transferSong = SearchMenuPanel.search(temp);
						
						// get the list of .wav files and separate by song, artist, and album
						String[] column = { "Song Title", "Artist", "Album" };
						DefaultTableModel model = new DefaultTableModel(null, column);
						model.setRowCount(0);
						for (int i = 0; i < transferSong.length; i++) {
							model.addRow(transferSong[i].split("_"));
						};
						
						// get selected song variables
						String songTitle = model.getValueAt(0, 0).toString();
						String artist = model.getValueAt(0, 1).toString();
						String album = model.getValueAt(0, 2).toString();
						
						//change text on labels in homepage
				    	songList.add(songTitle + "_" + artist + "_" + album);
				    }

				}
				catch (Exception e) 																		// catch exception
				{
					e.printStackTrace();
					
				}
			}

			public XActionListener(PlayPanel b)
			{
				this.panel = b;
			}
			/**
			 * Implement ActionListener
			 * @param e ActionEvent
			 */
			public void actionPerformed(ActionEvent e) {//action listener for songs played from playlist
				
				// TODO Auto-generated method stub
				Object o = e.getSource();
				if(o == panel.play )
				{
					if((current == null) || (current!=null && (!(current.isActive()))))//checks if song has not started playing or if it is paused
					{
						try {
							System.out.println(song + ".wav");
							File file = new File(song + ".wav");
							AudioInputStream player = AudioSystem.getAudioInputStream(file);
							current = AudioSystem.getClip();
							current.open(player);
							current.setFramePosition(pos);
							current.start();
						} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else if(o == panel.back)//stops song if it is playing and returns to playlist
				{
					if(current != null)
					{
						if(current.isActive())
						{
							current.stop();
						}
						current.setFramePosition(0);
						current.flush();
						current.close();
					}
					JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
					pFrame.dispose();
					if(playlist.isEmpty())
					{
//						new SearchMenuFrame(uName).setVisible(true);
					}
					else 
					{
//						new PlaylistFrame(uName, playlist).setVisible(true);
					}
				}
				else if(o == panel.pause)//pauses song is song is playing
				{
					if(current!= null)
					{
						if(current.isActive())
						{
							pos = current.getFramePosition();
							current.stop();
						}
					}
					
				}
			}
		}
	}

}

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * PlaylistPanel.java is the JPanel that acts as the GUI for the playlist page 
 * 
 * @author Tam Tran
 * @since 09-05-2018
 */
@SuppressWarnings("serial")
public class PlaylistPanel extends JPanel
{
	// declaring variables
	public JLabel playlistName, playlistAuthor;
	
	public JButton selectSongButton,
				   addSongButton,
				   deleteSongButton,
				   backToProfileButton;
	
	public JList<String> songList;
	
	private PlaylistActionListener listener;
	
	String username,
		   playlist,
		   songName;
	
	/**
	 * Constructor for GUI playlist panel with components added 
	 * @param username
	 * @param playlist
	 */
	public PlaylistPanel(String username, String playlist)
	{
		// initialize variables
		this.username = username;
		this.playlist = playlist;
		
		// initialize  variable that adds functionality to the buttons
		listener = new PlaylistActionListener(this);

		// based on absolute layout to organize the components in the frame (https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html)
		this.setLayout(null);
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		this.setSize(new Dimension(805,453));
		
		this.setName(this.playlist);			// set name of playlistPanel as the playlist name
		
//		// creating back to profile button
//		backToProfileButton = new JButton("Back to Profile");
//		backToProfileButton.setSize(backToProfileButton.getPreferredSize());
//		backToProfileButton.setLocation(10, 10);
//		backToProfileButton.addActionListener(listener);
//		this.add(backToProfileButton);
		
		// creating playlist name label
		playlistName = new JLabel("Playlist Name: " + playlist);
		playlistName.setSize(playlistName.getPreferredSize());
		playlistName.setLocation(10, 10);
		this.add(playlistName);
		
		playlistAuthor = new JLabel("Author: " + this.username);
		playlistAuthor.setSize(playlistName.getPreferredSize());
		playlistAuthor.setLocation(10, 30);
		this.add(playlistAuthor);
		
		// creating select song button
		selectSongButton = new JButton("Select Song");
		selectSongButton.setSize(selectSongButton.getPreferredSize());
		selectSongButton.setLocation(270, 55);
		selectSongButton.addActionListener(listener);
		this.add(selectSongButton);
	
		// creating delete song button
		deleteSongButton = new JButton("Delete Song");
		deleteSongButton.setSize(deleteSongButton.getPreferredSize());
		deleteSongButton.setLocation(270, 85);
		deleteSongButton.addActionListener(listener);
		this.add(deleteSongButton);
		
		// creating list of songs in the playlist
		songList = new JList<String>();
		songList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		DefaultListModel<String> DLM = new DefaultListModel<String>();
		songList.setModel(DLM);
		songList.setSize(250,200);
		songList.setLocation(10, 55);
		this.add(songList);		
		
		// updates the list of songs in the playlist
		updatePlaylist();
	}
	
	/**
	 * method that updates the JList in the PlaylistPanel
	 */
	public void updatePlaylist() 
	{
		
		try (InputStream input = new FileInputStream(username+".json")) 							// read JSON file
		{
			DefaultListModel<String> model = (DefaultListModel<String>) this.songList.getModel();	// grabs model for JList
			
			model.removeAllElements();																// clears JList
			
		    JSONObject obj = new JSONObject(new JSONTokener(input));								// turn into JSON object
		    
		    JSONArray listOfSongs = obj.getJSONArray(playlist);										// grabs JSON array of songs by mapping the playlist name
		    
		    for(int i = 0; i < listOfSongs.length(); i++)											// adds all songs from array into JList
		    {
		    	model.addElement(listOfSongs.getString(i));
		    }

		}
		catch (Exception e) 																		// catch exception
		{
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Gets the PlaylistActionListener variable 'listener'
	 * @return listener, a PlaylistActionListener
	 */
	public PlaylistActionListener getListener() { return listener; }

}

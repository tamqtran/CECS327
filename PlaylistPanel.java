import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PlaylistPanel extends JPanel
{
	public JLabel playlistName;
	
	public JButton selectSongButton,
				   addSongButton,
				   deleteSongButton,
				   backToProfileButton;
	
	public JList songList;
	
	String username,
		   playlist;
	// based on absolute layout to organize the components in the frame (https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html)
	
	public PlaylistPanel(String username, String playlist)
	{
		this.username = username;
		this.playlist = playlist;
		
		
		ActionListener listener = new PlaylistActionListener(this);
		
		this.setLayout(null);
		
		// back to profile button
		backToProfileButton = new JButton("Back to Profile");
		backToProfileButton.setSize(backToProfileButton.getPreferredSize());
		backToProfileButton.setLocation(10, 10);
		backToProfileButton.addActionListener(listener);
		this.add(backToProfileButton);
		
		// playlist name label
		playlistName = new JLabel("Playlist Name: " + playlist);
		playlistName.setSize(playlistName.getPreferredSize());
		playlistName.setLocation(50, 100);
		this.add(playlistName);
		
		// select song button
		selectSongButton = new JButton("Select Song");
		selectSongButton.setSize(selectSongButton.getPreferredSize());
		selectSongButton.setLocation(350, 500);
		selectSongButton.addActionListener(listener);
		this.add(selectSongButton);
		
		// add song button 
		addSongButton = new JButton("Add Song");
		addSongButton.setSize(addSongButton.getPreferredSize());
		addSongButton.setLocation(457, 500);
		addSongButton.addActionListener(listener);
		this.add(addSongButton);
		
		// delete song button
		deleteSongButton = new JButton("Delete Song");
		deleteSongButton.setSize(deleteSongButton.getPreferredSize());
		deleteSongButton.setLocation(550, 500);
		deleteSongButton.addActionListener(listener);
		this.add(deleteSongButton);
		
		// list of songs in the playlist
		songList = new JList();
		songList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		DefaultListModel DM = new DefaultListModel();
		songList.setModel(DM);
		songList.setSize(300,380);
		songList.setLocation(350, 100);
		this.add(songList);		
		
		// add content to songList
					
		
		try (InputStream input = new FileInputStream(username+".json")) 
		{
		    JSONObject obj = new JSONObject(new JSONTokener(input));
		    
		    JSONArray listOfSongs = obj.getJSONArray(playlist);
		    
		    for(int i = 0; i < listOfSongs.length(); i++)
		    {
		    	DM.addElement(listOfSongs.getString(i));
		    }
		    //currentList.remove(currentList.toList().indexOf(playlist)); 
		    
		    
		}catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}

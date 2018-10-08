import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * ActionListener that provides functionality to PlaylistPanel
 * 
 * @author Tam Tran
 * @since 09-09-2018
 */
public class PlaylistActionListener implements ActionListener
{
	// declaring variables
	private PlaylistPanel panel;
	private String songTitle, artist, album;
	private JLabel titleLabel, artistLabel, albumLabel;
	
	/**
	 * Constructor of PlaylistActionListener
	 * @param p - PlaylistPane
	 */
	public PlaylistActionListener(PlaylistPanel p) 
	{ 
		// initializing variables
		this.panel = p;
	}
	
	/**
	 * Adding functionality to buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		// back to profile button will close JFrame of PlaylistFrame and open JFrame of Profile Page of current user
		if (source == panel.backToProfileButton)
		{
			JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
			pFrame.dispose();
			new Profile(panel.username).setVisible(true);
		}
		
		// select song button will close JFrame of PLaylistFram and open JFrame for Play Page with selected song
		else if(source == panel.selectSongButton)
		{
			// search for the song title in the project folder
			String[] transferSong = SearchMenuPanel.search(panel.songList.getSelectedValue().toString());
		
			// get the list of .wav files and separate by song, artist, and album
			String[] column = { "Song Title", "Artist", "Album" };
			DefaultTableModel model = new DefaultTableModel(null, column);
			model.setRowCount(0);
			for (int i = 0; i < transferSong.length; i++) {
				model.addRow(transferSong[i].split("_"));
			};
			
			// get selected song variables
			songTitle = model.getValueAt(0, 0).toString();
			artist = model.getValueAt(0, 1).toString();
			album = model.getValueAt(0, 2).toString();
			
			//change text on labels in homepage
			titleLabel.setText(songTitle); titleLabel.setVisible(true);
			artistLabel.setText(artist); artistLabel.setVisible(true);
			albumLabel.setText(album);	albumLabel.setVisible(true);
			

			// open new JFrame
			new PlayButton.PlayFrame(songTitle + "_" + artist + "_" + album, panel.username, panel.playlist).setVisible(true);
		}
		
		// delete song button will delete song from JSON file and update JList in Playlist JPanel
		else if(source == panel.deleteSongButton)
		{
			// initialize variables to refer to JSON file
			String username = panel.username;
			String playlistName = panel.playlist;
			String songName = panel.songList.getSelectedValue().toString();
			
			
			try (InputStream input = new FileInputStream(username+".json")) 	// read JSON file
			{
			    JSONObject JSONfile = new JSONObject(new JSONTokener(input));	// turn into JSON object
			    
			    JSONArray pJSON = JSONfile.getJSONArray(playlistName);			// grabs JSON array of songs by mapping the playlist name
			    
			    pJSON.remove(pJSON.toList().indexOf(songName));					// remove selected song
			 
			    FileWriter fileWriter = new FileWriter(username+".json");		// update JSON file 
				fileWriter.write(JSONfile.toString());
				fileWriter.flush();
				fileWriter.close();
			    
			}
			catch (Exception f) 												// catch exception
			{
				f.printStackTrace();
			}
			
			panel.updatePlaylist();												// update JList in the Playlist Panel
			
		}
	}
	
	/**
	 * Sets variables to these labels in order to change their text whenever the song changes
	 * @param label - a JLabel from Homepage
	 */
	public void setLabel(JLabel label) 
	{
		switch (label.getName()) 			// the three labels are named via label.setName(labelName)
		{
		case "title": titleLabel = label; break;
		case "artist(s)": artistLabel = label; break;
		case "album": albumLabel = label; break;
		}
	}
	
	/**
	 * Set buttons to active
	 * @param button - a JButton from Homepage
	 */
	public void setButtonOn(JButton button) 
	{
		button.setEnabled(true);
	}
	
}

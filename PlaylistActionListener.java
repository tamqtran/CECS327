import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * PlaylistActionListener.java adds functionality to the buttons in PlaylistPanel.java
 * 
 * @author Tam Tran, Austin Tao
 * @since 09-09-2018
 */
public class PlaylistActionListener implements ActionListener, MouseListener {
	// declaring variables
	private PlaylistPanel panel;
	private String song, songTitle, artist, album;
	private JLabel titleLabel, artistLabel, albumLabel;
	
	/**
	 * Constructors that adds functionality to the playlist panel
	 * @param p - playlist panel
	 */
	public PlaylistActionListener(PlaylistPanel p) { 
		// initializing variables
		this.panel = p;
	}
		
	/**
	 * Adding functionality to buttons
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		// back to profile button will close JFrame of PlaylistFrame and open JFrame of Profile Page of current user
		if (source == panel.backToProfileButton) {
			JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
			pFrame.dispose();
			new Profile(panel.username).setVisible(true);
		}
		
		// select song button will close JFrame of PLaylistFram and open JFrame for Play Page with selected song
		else if(source == panel.selectSongButton) {			
			new PlayButton.PlayFrame(song, panel.username, panel.playlist).setVisible(true);
		}
		
		// delete song button will delete song from JSON file and update JList in Playlist JPanel
		else if(source == panel.deleteSongButton) {
			// initialize variables to refer to JSON file
			String username = panel.username;
			String playlistName = panel.playlist;
			String songName = panel.songList.getSelectedValue().toString();
			
			try (InputStream input = new FileInputStream(username+".json")) {	// read JSON file
			    JSONObject JSONfile = new JSONObject(new JSONTokener(input));	// turn into JSON object
			    
			    JSONArray pJSON = JSONfile.getJSONArray(playlistName);			// grabs JSON array of songs by mapping the playlist name
			    
			    pJSON.remove(pJSON.toList().indexOf(songName));					// remove selected song
			 
			    FileWriter fileWriter = new FileWriter(username+".json");		// update JSON file 
				fileWriter.write(JSONfile.toString());
				fileWriter.flush();
				fileWriter.close();
			    
			} catch (Exception f) { 												// catch exception
				f.printStackTrace();
			}
			
			panel.updatePlaylist();												// update JList in the Playlist Panel
		}
	}
	
	/**
	 * The JLabel variables from Homepage (title_, artist_, album_) are set to these labels in order to change their text whenever the song changes
	 * @param label: a JLabel from Homepage
	 */
	public void setLabel(JLabel label) {
		switch (label.getName()) {			// the three labels are named via label.setName(labelName)
		case "title": 		titleLabel = label;  break;
		case "artist(s)": 	artistLabel = label; break;
		case "album": 		albumLabel = label;  break;
		}
	}
	
	/**
	 * The JButtons variables from Homepage (previousSong_, playPause_, nextSong_) are set to active
	 * @param button: a JButton from Homepage
	 */
	public void setButtonOn(JButton button) {
		button.setEnabled(true);
		button.updateUI();
	}
	
	/**
	 * A get method that returns the currently selected song.
	 * @return String - the title, artist(s), and album of the currently selected song
	 */
	public String getSong() {
		System.out.println("PlaylistPanel " + panel.getName() + " - song chosen: " + song);
		return song;
	}

	/**
	 * A private method that creates a table model of a selected song. Uses the search() method from SearchMenuPanel.
	 * @param list: the selected song, taken from a mouse event
	 * @return a DefaultTAbleModel, containing data pertaining to the selected song
	 */
	private DefaultTableModel createModel( JList<String> list) {
		
		System.out.println("PlaylistPanel -> SearchMenuPanel");
		String[] transferSong = SearchMenuPanel.search(list.getSelectedValue().toString(), panel.aSocket, panel.serverPort); 
		System.out.println("SearchMenuPanel -> PlaylistPanel");
		
		DefaultTableModel model = new DefaultTableModel(null, new String[]{"Song Title", "Artist", "Album"});		
		model.setRowCount(0);
		for (int i = 0; i < transferSong.length; i++)
			model.addRow(transferSong[i].split("_"));
		
		return model;
	}
	
	/**
	 * An overridden MouseEvent method that detects what song has been clicked on.
	 * @param e: a MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		JList<String> list = (JList<String>)e.getSource();
		Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (r != null && r.contains(e.getPoint())) {
				DefaultTableModel model = this.createModel(list);
				
				// get selected song variables
				songTitle = model.getValueAt(0, 0).toString();
				artist = model.getValueAt(0, 1).toString();
				album = model.getValueAt(0, 2).toString();
				
				System.out.println("PlaylistPanel " + panel.getName() +" -- '" + songTitle + "_" + artist + "_" + album + "' clicked");
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		JList<String> list = (JList<String>)e.getSource();
		Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());
		
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getClickCount() == 2 && r != null && r.contains(e.getPoint())) {// search for the song title in the project folder
				DefaultTableModel model = this.createModel(list);
				
				// get selected song variables
				songTitle = model.getValueAt(0, 0).toString();
				artist = model.getValueAt(0, 1).toString();
				album = model.getValueAt(0, 2).toString();

				//change text on labels in homepage
				titleLabel.setText(songTitle); titleLabel.setSize(new Dimension(titleLabel.getPreferredSize())); titleLabel.setVisible(true);
				artistLabel.setText(artist); artistLabel.setSize(new Dimension(artistLabel.getPreferredSize())); artistLabel.setVisible(true);
				albumLabel.setText(album); albumLabel.setSize(new Dimension(albumLabel.getPreferredSize())); albumLabel.setVisible(true);

				song = songTitle + "_" + artist + "_" + album;
				System.out.println("PlaylistPanel " + panel.getName() +" -- '" + song + "' selected");
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}	
}

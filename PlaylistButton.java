import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class PlaylistButton extends JPanel
{
	public JLabel playlistName;
	
	public JButton playButton,
				   addSongButton,
				   deleteSongButton,
				   backToProfileButton;
	
	public JList songList;
	
	// based on absolute layout to organize the components in the frame (https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html)
	
	public PlaylistButton()
	{
		this.setLayout(null);
		
		playlistName = new JLabel("Playlist Name: ");
		playlistName.setSize(playlistName.getPreferredSize());
		playlistName.setLocation(50, 100);
		this.add(playlistName);
		
		addSongButton = new JButton("Add Song");
		addSongButton.setSize(addSongButton.getPreferredSize());
		addSongButton.setLocation(450, 500);
		this.add(addSongButton);
		
		deleteSongButton = new JButton("Delete Song");
		deleteSongButton.setSize(deleteSongButton.getPreferredSize());
		deleteSongButton.setLocation(550, 500);
		this.add(deleteSongButton);
		
		backToProfileButton = new JButton("Back to Profile");
		backToProfileButton.setSize(backToProfileButton.getPreferredSize());
		backToProfileButton.setLocation(5, 5);
		this.add(backToProfileButton);
		
		//play button
		playButton = new JButton("Play Song");
		playButton.setSize(playButton.getPreferredSize());
		playButton.setLocation(350, 500);
		this.add(playButton);
		
		//Jlist
		songList = new JList();
		songList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		
		
		
		
	}
}

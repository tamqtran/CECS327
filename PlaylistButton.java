import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlaylistButton extends JPanel
{
	public JLabel playlistName;
	
	public JButton addSongButton,
				   deleteSongButton,
				   backToProfileButton;
	
	public PlaylistButton()
	{
		//this.setLayout(new BorderLayout());
		
		playlistName = new JLabel("Playlist Name: ");
		this.add(playlistName, BorderLayout.EAST);
		
		addSongButton = new JButton("Add Song");
		// add action listener
		this.add(addSongButton, BorderLayout.WEST);
		
		deleteSongButton = new JButton("Delete Song");
		// add action listener
		this.add(deleteSongButton, BorderLayout.WEST);
		
		backToProfileButton = new JButton("Back to Profile");
		// add action listener
		this.add(backToProfileButton, BorderLayout.EAST);
	}
}

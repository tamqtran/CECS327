import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class PlaylistPanel extends JPanel
{
	public JLabel playlistName;
	
	public JButton selectSongButton,
				   addSongButton,
				   deleteSongButton,
				   backToProfileButton;
	
	public JList songList;
	
	// based on absolute layout to organize the components in the frame (https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html)
	
	public PlaylistPanel()
	{
		this.setLayout(null);
		
		// back to profile button
		backToProfileButton = new JButton("Back to Profile");
		backToProfileButton.setSize(backToProfileButton.getPreferredSize());
		backToProfileButton.setLocation(10, 10);
		this.add(backToProfileButton);
		
		// playlist name label
		playlistName = new JLabel("Playlist Name: ");
		playlistName.setSize(playlistName.getPreferredSize());
		playlistName.setLocation(50, 100);
		this.add(playlistName);
		
		// select song button
		selectSongButton = new JButton("Select Song");
		selectSongButton.setSize(selectSongButton.getPreferredSize());
		selectSongButton.setLocation(350, 500);
		this.add(selectSongButton);
		
		// add song button 
		addSongButton = new JButton("Add Song");
		addSongButton.setSize(addSongButton.getPreferredSize());
		addSongButton.setLocation(457, 500);
		this.add(addSongButton);
		
		// delete song button
		deleteSongButton = new JButton("Delete Song");
		deleteSongButton.setSize(deleteSongButton.getPreferredSize());
		deleteSongButton.setLocation(550, 500);
		this.add(deleteSongButton);
		
		// list of songs in the playlist
		songList = new JList();
		songList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		songList.setSize(300,380);
		songList.setLocation(350, 100);
		this.add(songList);
		
		backToProfileButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				//frame.remove();
				new Login().setVisible(true); 
			}
		});
												
	}

}

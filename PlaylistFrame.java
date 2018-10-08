
import javax.swing.JFrame;

/**
 * PlaylistFrame.java is the JFrame used for PlaylistPanel.java
 * 
 * @author Tam Tran
 * @since 09-05-2018
 */
@SuppressWarnings("serial")
public class PlaylistFrame extends JFrame
{
	// declaring variables
	private PlaylistPanel panel;
	
	/**
	 * Constructor for the playlist Jframe and initialize a playlist JPanel inside 
	 * @param username - identifying variable for account's username 
	 * @param playlist - identifying variable for selectedplaylist
	 */
	public PlaylistFrame(String username, String playlist)
	{
		setTitle("Playlist Page");
		setResizable(false);
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel = new PlaylistPanel(username, playlist);
		this.add(panel);
		this.setVisible(true);
	}
	
}

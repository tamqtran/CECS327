import javax.swing.JFrame;

public class PlaylistFrame extends JFrame
{
	//initialize variables
	private PlaylistPanel panel;
	
	// create constructor
	public PlaylistFrame(String username, String playlist)
	{
		setTitle("Playlist Page");
		setResizable(false);
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.panel = new PlaylistPanel(username, playlist);
		this.add(panel);
		this.setVisible(true);
	}
	
}

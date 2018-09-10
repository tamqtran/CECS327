import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlaylistFrame extends JFrame
{
	public PlaylistFrame()
	{
		setTitle("Playlist Page");
		
		setResizable(false);
		setSize(700, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new PlaylistPanel();
		this.add(panel);
	}

}

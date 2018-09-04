import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlaylistFrame extends JFrame
{
	public PlaylistFrame()
	{
		setTitle("PLaylist Page");
		setSize(700, 700);
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new PlaylistButton();
		this.add(panel);
	}
}

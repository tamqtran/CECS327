import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class PlaylistPage extends JFrame implements ActionListener
{
	
	public static void main(String[] args) 
	{
		// constructing the playlist frame
		JFrame playlistFrame = new PlaylistFrame();
		playlistFrame.setVisible(true);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

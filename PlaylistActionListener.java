import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaylistActionListener implements ActionListener
{
	private PlaylistPanel panel;
	
	public PlaylistActionListener(PlaylistPanel p)
	{
		this.panel = p;
	}
		
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		
		if (source == panel.backToProfileButton)
		{
			
		}
		else if(source == panel.selectSongButton)
		{
			
		}
		else if(source == panel.addSongButton)
		{
			
		}
		else if(source == panel.deleteSongButton)
		{
			
		}
	}

}
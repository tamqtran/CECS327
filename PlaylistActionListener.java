import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
			JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
			pFrame.dispose();
			new Profile(panel.username).setVisible(true);
		}
		else if(source == panel.selectSongButton)
		{
			JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
			pFrame.dispose();
			new PlayButton.PlayFrame(panel.songList.getSelectedValue().toString(), panel.username, panel.playlist).setVisible(true);
		}
		else if(source == panel.deleteSongButton)
		{
			
			String username = panel.username;
			String playlistName = panel.playlist;
			String songName = panel.songList.getSelectedValue().toString();
			
			
			try (InputStream input = new FileInputStream(username+".json")) 
			{
				// read as JSON file
			    JSONObject JSONfile = new JSONObject(new JSONTokener(input));
			    
			    // get correct playlist from JSON file
			    JSONArray pJSON = JSONfile.getJSONArray(playlistName);//play
			    
			    pJSON.remove(pJSON.toList().indexOf(songName));
			 
			    FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(JSONfile.toString());
				fileWriter.flush();
				fileWriter.close();
			    
			}
			catch (Exception f) 
			{
				f.printStackTrace();
			}
			
			// update Jlist on Frame
			panel.updatePlaylist();
			
		}
	}
}

import java.io.FileWriter;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistPage
{
	
	public static void main(String[] args) 
	{
		
		

			//Writing to JSON
			
			//Create JSON object and add value
			JSONObject obj = new JSONObject();
			obj.put("name", "allan");
			obj.put("password", "forever");
			
			JSONArray playlists = new JSONArray();
			playlists.put("king");
			playlists.put("queen");
			obj.put("Playlist", playlists);
			
			JSONArray kingSongLists = new JSONArray();
			JSONArray queenSongLists = new JSONArray();
			kingSongLists.put("song1");
			kingSongLists.put("song4");
			kingSongLists.put("song7");
			queenSongLists.put("song2");
			
			obj.put("king", kingSongLists);
			obj.put("queen", queenSongLists);
			
			
			
			
			//Write all to json file
			try {
				FileWriter fileWriter = new FileWriter("allan.json");
				fileWriter.write(obj.toString());
				fileWriter.flush();
				fileWriter.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(obj);
			//System.out.print(obj.getJSONArray("king").toString());
			
			// constructing the playlist frame
			JFrame playlistFrame = new PlaylistFrame("allan", "king");
		
	}
	
}

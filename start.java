import java.io.FileWriter;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

public class start {

	public static void main(String[] args) {

		

		//Writing to JSON
		
		//Create JSON object and add value
		JSONObject obj = new JSONObject();
		obj.put("username", "allan");
		obj.put("password", "forever");
		
		JSONArray playlists = new JSONArray();
		playlists.put("king");
		playlists.put("queen");
		obj.put("playlists", playlists);
		
		JSONArray kingSongLists = new JSONArray();
		JSONArray queenSongLists = new JSONArray();
		kingSongLists.put("Imagine");
		kingSongLists.put("Money");
		kingSongLists.put("So_Serious");
		queenSongLists.put("Yellow_Submarine");
		queenSongLists.put("Sgt._Pepper's_Lonely_Hearts_Club_Band");
		
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
		//JFrame playlistFrame = new PlaylistFrame("allan", "king");
		Login lFrame = new Login();
	}

}

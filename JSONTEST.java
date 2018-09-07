import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONTEST {

	public static void main(String[] args) {
		//Writing to JSON
		
		//Create JSON object and add value
		JSONObject obj = new JSONObject();
		obj.put("name", "allan");
		obj.put("password", "forever");
		
		//Create JSON array and add value
		JSONArray playlists = new JSONArray();
		playlists.put("playlist1");
		playlists.put("playlist2");
		playlists.put("playlist3");
		
		obj.put("playlists", playlists);
		
		JSONArray songlists = new JSONArray();
		songlists.put("song1");
		songlists.put("song2");
		songlists.put("song3");
		
		obj.put("playlist1", songlists);
		obj.put("playlist2", songlists);
		obj.put("playlist3", songlists);
		
		//Write all to json file
		try {
			FileWriter fileWriter = new FileWriter("allan.json");
			fileWriter.write(obj.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//Reading from JSON
		JSONObject obj1;
		String pathname = "allan.json";
		try (InputStream input = new FileInputStream(pathname)) {
		    obj1 = new JSONObject(new JSONTokener(input));
		    //read name + password
		    String name = obj1.get("name").toString();
		    String password = obj1.get("password").toString();
		    System.out.println("Name: "+ name+"\nPassword: "+password);
		    
		    System.out.println();
		    
		    //read playlists
		    String playlist = obj1.get("playlists").toString();
		    String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
		    System.out.print("Playlists: ");
		    for(int i = 0; i < playlistArray.length; i++)
		    	System.out.print(playlistArray[i] + " ");
		    
		    System.out.println();
		    
		    //songs of each playlist
		    for(int i = 0; i < playlistArray.length; i++) {
		    	String songlist = obj1.get(playlistArray[i]).toString();
		    	String[] songlistArray = songlist.substring(2,songlist.length()-2).split("\",\"");
		    	System.out.println();
		    	
		    	System.out.print(playlistArray[i] + ": ");
		    	for(int j  = 0; j < songlistArray.length; j++) 
		    		System.out.print(songlistArray[j] + " ");
		    }
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
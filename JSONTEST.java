import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		    JSONArray playlistJsonArray = obj1.getJSONArray("playlists");
		    System.out.print("Playlists: ");
		    for(int i=0; i<playlistJsonArray.length();i++) {
		    	System.out.print(playlistJsonArray.get(i)+" ");
		    }
		    
		    System.out.println();
		    
		    //songs of each playlist
		    List<JSONArray> songlistArray = new ArrayList<JSONArray>();
		    for(int i = 0; i < playlistJsonArray.length(); i++) {
		    	JSONArray songlistJsonArray = obj1.getJSONArray((String) playlistJsonArray.get(i));
		    	songlistArray.add(songlistJsonArray);
		    	System.out.print(playlistJsonArray.get(i)+": ");
		    	for(int j=0; j<songlistJsonArray.length();j++) {
			    	System.out.print(songlistJsonArray.get(j)+" ");
			    }
		    	System.out.println();
		    }
		    //Add new song to playlist x
		    System.out.println("Adding new song...");
		    JSONArray songlistToAdd = obj1.getJSONArray("playlist1");
		    if(!songlistToAdd.toList().contains("song5")) {
		    	songlistToAdd.put("song5");
		    }
		    
		    //Remove song from playlist x
		    System.out.println("Removing song...");
		    JSONArray songlistToRemove = obj1.getJSONArray("playlist1");
		    songlistToRemove.remove(songlistToRemove.toList().indexOf("song5"));
		    
		    //Add new playlist
		    System.out.println("Adding new playlist...");
		    JSONArray playlistToAdd = obj1.getJSONArray("playlists");
		    if(!playlistToAdd.toList().contains("playlist8")) {
		    	playlistToAdd.put("playlist8");
		    	obj1.put("playlist8", new JSONArray());
		    }
		    
		    //Remove playlist
		    System.out.println("Removing playlist....");
		    JSONArray playlistToRemove = obj1.getJSONArray("playlists");
		    playlistToRemove.remove(playlistToRemove.toList().indexOf("playlist8"));
		    obj1.remove("playlist8");
		    
		  //Write all to json file
			try {
				FileWriter fileWriter = new FileWriter("allan.json");
				fileWriter.write(obj1.toString());
				fileWriter.flush();
				fileWriter.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			//removePlaylist("playlist5", "allan");
		    for(int i = 0; i < playlistJsonArray.length(); i++) {
		    	JSONArray songlistJsonArray = obj1.getJSONArray((String) playlistJsonArray.get(i));
		    	songlistArray.add(songlistJsonArray);
		    	System.out.print(playlistJsonArray.get(i)+": ");
		    	for(int j=0; j<songlistJsonArray.length();j++) {
			    	System.out.print(songlistJsonArray.get(j)+" ");
			    }
		    	System.out.println();
		    }
		 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add playlist to JSON FiLE
	 * @param playlist playlist to be added
	 * @param username current login user
	 */
	static void addPlaylist(String playlist, String username) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentList = obj1.getJSONArray("playlists");
		    if(!currentList.toList().contains(playlist)) {
		    	currentList.put(playlist);
		    	obj1.put(playlist, new JSONArray()); //empty song list for this array
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Remove playlist from JSON FiLE
	 * @param playlist playlist to be removed
	 * @param username current login user
	 */
	static void removePlaylist(String playlist, String username) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentList = obj1.getJSONArray("playlists");
		    currentList.remove(currentList.toList().indexOf(playlist)); 
		    obj1.remove(playlist); // also need to remove songs from playlist
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Add a song to a playlist
	 * @param playlist playlist to add the song
	 * @param song song to be added to playlist
	 * @param username current login user
	 */
	static void removeSong(String playlist, String song, String username) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) {
		    	JSONArray currentList = obj1.getJSONArray(playlist);
		    	currentList.remove(currentList.toList().indexOf(song));
		    }
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Remove a song from a playlist
	 * @param playlist playlist that song is in
	 * @param song song to be removed
	 * @param username current login user
	 */
	static void addSong(String playlist, String song, String username) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) {
		    	 JSONArray currentList = obj1.getJSONArray(playlist);
				    if(!currentList.toList().contains(song)) {
				    	currentList.put(song);
				    }
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
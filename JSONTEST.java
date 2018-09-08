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
		/*JSONObject obj = new JSONObject();
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
		}*/
		
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
		    
		    System.out.println("Adding new song...");
		    //int index = playlistJsonArray.toList().indexOf("playlist2");
		    //System.out.println(index);
		    //JSONArray newSonglist = songlistArray.get(index);
		    //newSonglist.put("song4");
		    //songlistArray.remove(index);
		    //songlistArray.add(index,newSonglist);
		    
		    obj1.append("playlist1", "song5");
		    
		  //Write all to json file
			try {
				FileWriter fileWriter = new FileWriter("allan.json");
				fileWriter.write(obj1.toString());
				fileWriter.flush();
				fileWriter.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
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
	 * Update json file with new information
	 * @param oper operation to perform: "addsong", "removesong", "addplaylist", "removeplaylist"
	 * @param ele element to be changed
	 * @param play playlist if updating songs else should be empty string;
	 */
	void updateJson(String oper, String play, String ele, String username) {
		//read data from current json file
		JSONObject obj1;
		String pathname = username+".json";
		String name;
		String password;
		JSONArray playlistJsonArray = null;
		List<JSONArray> songlistArray = null;
		try (InputStream input = new FileInputStream(pathname)) {
		    obj1 = new JSONObject(new JSONTokener(input));
		    //read name + password
		    name = obj1.get("name").toString();
		    password = obj1.get("password").toString();
		 
		    //read playlists
		    playlistJsonArray = obj1.getJSONArray("playlists");
		    		    
		    //songs of each playlist
		    songlistArray = new ArrayList<JSONArray>();
		    for(int i = 0; i < playlistJsonArray.length(); i++) {
		    	JSONArray songlistJsonArray = obj1.getJSONArray((String) playlistJsonArray.get(i));
		    	for(int j=0; j<songlistJsonArray.length();j++) {
		    		songlistArray.add(songlistJsonArray);
			    }
		    }
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(oper.equals("addsong")) {
			int index = playlistJsonArray.toList().indexOf(play);
			JSONArray temp = songlistArray.get(index);
			
			
		}else if(oper.equals("removesong")) {
			
		}else if(oper.equals("addplaylist")) {
			
		}else if(oper.equals("removeplaylist")) {
			
		}else {
			System.out.println("Invalid operation when using method updateJson");
			return;
		}
	}

}
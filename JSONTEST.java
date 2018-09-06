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
		
		//Profile Object
		JSONObject profile = new JSONObject();
		profile.put("name", "Allan");
		profile.put("password", "Forever");
		
		//Playlist Object
		JSONObject play = new JSONObject();
		JSONArray playlist = new JSONArray();
		
		JSONObject song = new JSONObject();
		JSONArray songList = new JSONArray();
		
		songList.put("song1");
		songList.put("song2");
		songList.put("song3");
		
		
		playlist.put("playlist1");
		playlist.put("playlist2");
		playlist.put("playlist3");
		
		song.put("songs", songList);
		play.put("playlists", song);
		
		
		//Write all to json file
		try {
			FileWriter fileWriter = new FileWriter("user.json");
			fileWriter.write(profile.toString());
			fileWriter.write(playlist.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//Reading from JSON
		/*JSONObject obj;
		String pathname = "Allan.json";
		try (InputStream input = new FileInputStream(pathname)) {
		    obj = new JSONObject(new JSONTokener(input));
		    String name = obj.get("playlists").toString();
		    //String pageName = obj.getJSONObject("pageInfo").getString("pageName");
		    System.out.println(name);
		   // System.out.println(pageName);
		    
		}catch (Exception e) {
			e.printStackTrace();
		}*/
	
	}

}

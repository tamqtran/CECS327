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
		/*
		//Create JSON object and add value
		JSONObject obj = new JSONObject();
		obj.put("name", "Allan");
		obj.put("password", "Forever");
		
		//Create JSON array and add value
		JSONArray list = new JSONArray();
		list.put("playlist1");
		list.put("playlist2");
		list.put("playlist3");
		
		obj.put("playlists", list);
		
		//Write all to json file
		try {
			FileWriter fileWriter = new FileWriter("Allan.json");
			fileWriter.write(obj.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) {
			e.printStackTrace();
		}*/
		
		//Reading from JSON
		JSONObject obj;
		String pathname = "practice.json";
		try (InputStream input = new FileInputStream(pathname)) {
		    obj = new JSONObject(new JSONTokener(input));
		    String name = obj.get("playlist").toString();
		    System.out.println(name);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
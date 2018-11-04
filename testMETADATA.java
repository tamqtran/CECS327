import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.JSONObject;

public class testMETADATA {

	public static void main(String[] args) throws IOException {
//		System.out.println(Paths.get("METADATA.txt"));
		Metadata meta= new Metadata();
		for(int i = 0; i < meta.fileList.size(); i++){
			File f = meta.getFile(i);
			
			System.out.println(f.getFileName());				
		}
		/*
		String song = "Yellow Submarine";
		String artist = "The Beatles";
		String album = "Yellow Submarine";
		
		String content = song + artist + album;
		System.out.println(content);
		System.out.println(content.hashCode());
		
		String txt = "artistIndex.txt";
		
		String json = txt.substring(0, txt.length()-4) +  ".json";
		
		System.out.println(json);
		*/
		String[] content = {"Yellow Submarine", "The Beatles", "Yellow Submarine"};
		
		Object obj = parser.parse(new FileReader(Paths.get("artistIndex.json")));

        JSONObject jsonObject =  (JSONObject) obj;

        String name = (String) jsonObject.get("name");
	}
}
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
		//content is {song, artist, album}
		String[] content = {"Hello Goodbye", "The Beatles", "Magical Mystery Tour"};
		meta.append("songIndex.txt", content);
		
		String[] content2 = {"Money", "Pink Floyd", "The Dark Side of the Moon"};
		meta.append("songIndex.txt", content2);
		
		String[] content3 = {"Shadow", "Jilian Aversa", "Origins"};
		meta.append("songIndex.txt", content3);
		
		String[] content4 = {"So Serious", "Electric Light Orchestra", "Balance Of Power"};
		meta.append("songIndex.txt", content4);
		
		String[] content5= {"Yellow Submarine", "The Beatles", "Yellow Submarine"};
		meta.append("songIndex.txt", content5);
		
		
	     
	      
        //String name = (String) jsonObject.get("name");
	}
}
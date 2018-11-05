import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
public class File {
	private String fileName;
	private String jsonFile;
	private List<Chunk> chunks;
	
	// constructor for File
	public File(String file){
		fileName = file;
		jsonFile = file.substring(0, file.length()-4) +  ".json";
		chunks = new ArrayList<Chunk>();
		chunks.add(new Chunk("a","i"));
		chunks.add(new Chunk("j","q"));
		chunks.add(new Chunk("r","z"));
	}


	// currently content is {song, title, album} example:{Yellow Submarine, The Beatles, Yellow Submarine}
	public void append(String[] content) {
		/*
		// split content into sub-contents based on size limit 
		// and sequentially load sub-contents into chunks
		// where each chunk's guid is the hash of the sub-contents meant for that chunk
		
		int guid;
		byte[][] subcontents; //split content into a 2D array of bytes called subcontents
		
		
		// and then...
		for (byte[] sb_row : subcontents ) {
			guid = hash(sb_row);  // message digest goes here?
			Chunk c = new Chunk(guid);
			chunks.add(c);
			// peer[0].put(guid, content);
		}
		*/
		
		// combine content to get string to use for constant guid for that song contents
		String temp = null;
		for (String s: content) {
			temp = temp + s;
		}
		int guid = temp.hashCode();
		
		// all index files names without extension
		String[] nameOfFiles = {"songIndex","artistIndex","albumIndex"};
		
		JSONObject song = new JSONObject();
		song.put("guid", guid);
		song.put("songFile", content[0]+"_"+content[1]+"_"+content[2]+ ".wav");
		
		// do for each index files
		for(String S: nameOfFiles) {
			// add changes to txt files
			
			// get index
			int i = 0;
			if(S.compareTo("songIndex") == 0)
				i = 0;
			else if(S.compareTo("artistIndex") == 0)
				i = 1;
			else if(S.compareTo("albumIndex") == 0)
				i = 2;
			
			// get correct index json file
			JSONObject obj = null;
			try (InputStream input = new FileInputStream(S +".json")) 
			{
				obj = new JSONObject(new JSONTokener(input));
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			// get array of chunks
			JSONArray chunks = 	(JSONArray) obj.get("chunks");
			
			JSONObject specifiedChunk = null;
			JSONArray chunkList = null;
			
			// add changes to json files
			Character c = Character.toUpperCase(content[i].charAt(0));
			if(c >= 'A' && c <= 'I') {
				// put {"guid":"0","songFile":"something.wav"} into the JSONArray of the specified chunk
				// chunkA_I
				specifiedChunk = (JSONObject) chunks.get(0);
				chunkList = (JSONArray) specifiedChunk.get("chunkA_I");
			}
			else if(c >= 'J' && c <= 'Q') {
				// chunkJ_Q
				specifiedChunk = (JSONObject) chunks.get(1);
				chunkList = (JSONArray) specifiedChunk.get("chunkJ_Q");
			}
			else if(c >= 'R' && c <= 'Z') {
				// chunkR_Z
				specifiedChunk = (JSONObject) chunks.get(2);
				chunkList = (JSONArray) specifiedChunk.get("chunkR_Z");
			}
			
			// add song to json file
			chunkList.put(song);
			
			// overwrite JSON file
			try 											// Write into json file
				{
					FileWriter fileWriter = new FileWriter(S + ".json");
					fileWriter.write(obj.toString());
					fileWriter.flush();
					fileWriter.close();
				}catch (Exception e) 							// catch exception
				{
					e.printStackTrace();
				}
		}
	}
	
	// getter methods
	public String getFileName() {return fileName;}
	public List<Chunk> getChunks() {return chunks;}
	public Chunk getChunk(int i) {return chunks.get(i);}
}

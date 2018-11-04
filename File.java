import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
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
		
		// add to all files
		String[] nameOfFiles = {"songIndex","artistIndex","albumIndex"};
		
		JSONObject song = new JSONObject();
		song.put("guid", guid);
		song.put("songFile", content[0]+"_"+content[1]+"_"+content[2]);
		
		for(String S: nameOfFiles) {
			// add changes to txt files
			
			// add changes to json files
			if(Character.toUpperCase(content[0].charAt(0)) >= 'A' && Character.toUpperCase(content[0].charAt(0)) <= 'I') {
				// put {"guid":"0","songFile":"something.wav"} into the JSONArray of the specified chunk
			}
			else if(Character.toUpperCase(content[0].charAt(0)) >= 'J' && Character.toUpperCase(content[0].charAt(0)) <= 'Q') {
				
			}
			else if(Character.toUpperCase(content[0].charAt(0)) >= 'R' && Character.toUpperCase(content[0].charAt(0)) <= 'Z') {
				
			}
		}
		if(fileName.substring(0, fileName.length()-4).compareTo("songIndex") == 0) {
			
		}
		else if(fileName.substring(0, fileName.length()-4).compareTo("artistIndex") == 0) {
			// if last letter of artist name goes between two letters to specify which chunk
			
		}
		else if(fileName.substring(0, fileName.length()-4).compareTo("albumIndex") == 0) {
			
		}
	}
	
	// getter methods
	public String getFileName() {return fileName;}
	public List<Chunk> getChunks() {return chunks;}
	public Chunk getChunk(int i) {return chunks.get(i);}
}

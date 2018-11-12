import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.tomp2p.peers.Number160;
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
		/*NOt using anymore, using tomp2p hash
		 * String temp = null;
		for (String s: content) {
			temp = temp + s;
		}*/
		Number160 guid = Number160.createHash(content[0]+"_"+content[1]+"_"+content[2]+ ".wav");
		
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
	
	public void appends(String content, String first, String last) {
		int guid = content.hashCode();
		Chunk c = new Chunk(guid, first, last);
		chunks.add(c);
		
		//INSERT LINE HERE TO PUT CONTENT INTO PEER
		//PEER[].PUT(GUID,CONTENT.getBytes(Charset.forName("UTF-8")))
		//Important to put the byte version of content, not content itself.
	}
	
	// getter methods
	public String getFileName() {return fileName;}
	
	public byte[] getChunkData(int i) {
		Chunk[] chunksArray = (Chunk[]) chunks.toArray();
		Chunk main = chunksArray[i];
		String first = main.getFirstLetter();
		String last = main.getLastLetter();
		
		Scanner scan = null;
		try {
			scan = new Scanner(Paths.get(fileName + ".txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean correct = false;
		
		String data = null;
		char LastChar = last.charAt(0);
		LastChar = (char) (LastChar + 1);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.substring(0,1).equals(first)) {
				correct = true;
			}
			if (correct) {
				data = data + line + "_";
			}
			if (line.charAt(0) == LastChar) {
				correct = false;
			}
			if (correct == false) {
				break;
			}
		}
		return data.getBytes(Charset.forName("UTF-8"));
		
		//May not be this complicated
		//Probably get method with guid
		//Im pretty sure it's a get method with peer, but we need peer in this file
		//Chunk c = getChunk(i);
		//peer.get(c.getGUID());
		//However this will be useful in the initialization file
		
		//IMPORTANT. IF YOU EVER NEED TO RECONVERT BACK TO STRING
		//use this String str = new String(bytes, StandardCharsets.UTF_8);
	}
	public int getSize() {
		return chunks.size();
	}
	//For 
	public boolean equals(File f) {
		if (this.getFileName().equals(f.getFileName())) {
			return true;
		} else {
			return false;
		}
	}
	
	//I need this
	public Chunk getChunk(int i) {return chunks.get(i);}
}

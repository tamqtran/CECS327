import java.util.ArrayList;
import java.util.List;

public class File {
	private String fileName;
	private List<Chunk> chunks;
	
	// constructor for File
	public File(String file){
		fileName = file;
		chunks = new ArrayList<Chunk>();
	}

	// appending content into File's Chunk list
	// maybe change type of CONTENT to byte[] since CONTENT is the song data
	public void append(byte[] content) {
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
	}
	
	// getter methods
	public String getFileName() {return fileName;}
	public List<Chunk> getChunks() {return chunks;}
	public Chunk getChunk(int i) {return chunks.get(i);}
}

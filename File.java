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
	// maybe change type of CONTENT to byte[]
	public void append(String content) {
		// split content into sub-contents based on size limit 
		// and sequentially load sub-contents into chunks
	}
	
	public String getFileName() {return fileName;}
	public List<Chunk> getChunks() {return chunks;}
	public Chunk getChunk(int i) {return chunks.get(i);}
}

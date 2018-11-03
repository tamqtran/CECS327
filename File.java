import java.util.ArrayList;
import java.util.List;

public class File {
	protected String fileName;
	private List<Chunk> chunks;
	
	// constructor for File
	public File(String file){
		fileName = file;
		chunks = new ArrayList<Chunk>();
	}

	// appending content
	// maybe change type of CONTENT to byte[]
	public void append(String content) {
		// split content into sub-contents based on size limit 
		// and sequentially load sub-contents into chunks
	}
	
	public Chunk getChunk(int i) {return chunks.get(i);}
}

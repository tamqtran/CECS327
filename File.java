import java.util.ArrayList;
import java.util.List;

public class File {
	String fileName;
	List<Chunk> chunks;
	
	// constructor for File
	public File(String file){
		fileName = file;
		chunks = new ArrayList<>();
	}

	// appending content
	// maybe change  type of CONTENT
	public void append(String content) {
		
	}
}

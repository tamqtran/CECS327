import java.util.ArrayList;
import java.util.List;

class Chunk {
	int guid;
	String first, last;
	byte[] content; // ????
	public Chunk(int guid) {this.guid = guid;}
	
	//ls, touch, ))
}

class File {
	String filename;
	List<Chunk> chunks;
	
	public byte[] getChunk(int i) {
		Chunk c = chunks.get(i);
		return c.content;
	}
	
	public File(String fn) {
		filename = fn;
	}
	
	public String getFilename() {return filename;}
	
	public void append(byte[] content) {
		int guid = getHash(content); //change this to whatever we need
		Chunk c = new Chunk(guid);
		chunks.add(c);
		peer[0].put(guid, content);
		
		metadata.append("inverted_index", content);
	}
}

public class Metadata {
	List<File> fileList;
	
	public Metadata() {}
	
	public void append(String filename, byte[] content) {
		File f;
		f = matchFile(filename);

		if (f == null)
			f = new File(filename);
		
		f.append(content);
	}
	
	private File matchFile(String filename) {
		File F = null;
		for (File f : fileList) {
			if (filename.equals(f.getFilename()))
				F = f;
		}
		return F;
	}
	
	// searchSongs(String filter)
	// read metadata
	// let f be the inverted index of the songs
	// search in f the chunk that contains filter
	// let c be the chunk that contains filter
	//** Chunk c = chunk.getFilename().contains(filter) ?
	// Data = peer.get(c.guid)
	// for each row in Data
	//  if filter is in row:
	//   include row in reply
}


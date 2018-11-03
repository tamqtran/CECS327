import java.util.ArrayList;
import java.util.List;



//class File {
//	String filename;
//	List<Chunk> chunks;
//	
//	public byte[] getChunk(int i) {
//		Chunk c = chunks.get(i);
//		return c.content;
//	}
//	
//	public File(String fn) {
//		filename = fn;
//	}
//	
//	public String getFilename() {return filename;}
//	
//	public void append(byte[] content) {
//		int guid = getHash(content); //change this to whatever we need
//		Chunk c = new Chunk(guid);
//		chunks.add(c);
//		peer[0].put(guid, content);
//		
//		metadata.append("inverted_index", content);
//	}
//}

public class Metadata {
	
	public List<File> fileList;
	
	// constructor for metadata
	public Metadata() {
		fileList = new ArrayList<>();
	}
	
	// maybe byte[] content instead
	// append an inverted index file by add content at the end of filename. if filename does not exists, it creates it and adds the content
	public void append(String fileName, String content) {
		
		// get correct file
		File f = new File(null);
		for(int i = 0; i < fileList.size(); i++) {
			if(fileList.get(i).fileName.equals(fileName))
				f = fileList.get(i);
		}
		
		// no fileName found, make a new one
		if (f.fileName == null)
			f = new File(fileName);
		
		f.append(content);
	}

	// delete the filefrom the DFS
	public void delete(String filename) {
		
	}
	

	// read the i-chunk of filename and returns an array of btyes
	public void read(String filename, int i) {
		
	}
	
	// returns the list of files in DFS
	public List<File> Is(){
		return fileList;
	}
	/*
	// match file 
	private File matchFile(String filename) {
		File F = null;
		for (File f : fileList) {
			if (filename.equals(f.getFilename()))
				F = f;
		}
		return F;
	}
	*/
	
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
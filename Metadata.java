import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
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
	
	protected List<File> fileList;
	static PeerDHT peer;
	
	// constructor for metadata
	public Metadata() throws IOException {
		fileList = new ArrayList<File>();
		
		Scanner sc = new Scanner(Paths.get("METADATA.txt"));
		while (sc.hasNextLine()) {
		     fileList.add(new File(sc.nextLine()));
		}
		sc.close();
	}
	
	
	// constructor for metadata with peer
		public Metadata(PeerDHT peer) throws IOException {
			this.peer = peer;
			fileList = new ArrayList<File>();
			
			Scanner sc = new Scanner(Paths.get("METADATA.txt"));
			while (sc.hasNextLine()) {
			     fileList.add(new File(sc.nextLine()));
			}
			sc.close();
		}
	// maybe byte[] instead of string for content
	// append an inverted index file by add content at the end of filename. 
	// if filename does not exists, it creates it and adds the content
	public void append(String filename, String[] content) {
		
		// get correct file
		File f = null;		
		for (File f_ : fileList) {
			if (f_.getFileName().equals(filename)) {
				f = f_;	break;
			} // will continue otherwise
		}
		
		// no fileName found, make a new one
		if (f == null) {
			System.out.println(filename + " is new; creating new file...");
			f = new File(filename);
			fileList.add(f); // add file to fileList
		}
		
		f.append(content);
	}

	public void appends(String fileName, String content, String first, String last) {
		File f = null;
		//Need to replace fileName + .txt
		//Wont work
		if (fileList.contains(new File(fileName))) {
			int index = fileList.indexOf(new File(fileName));
			f = new File (fileName);
		} else {
			f = new File(fileName);
		}
		f.appends(content, first, last);
	}
	// delete the filefrom the DFS
	public void delete(String filename) {
		
		for (File f_ : fileList) {
			if (f_.getFileName().equals(filename)) {
				fileList.remove(f_);
				System.out.println(filename + " found and removed.");
				break;
			} // for loop will continue otherwise
		}
		
		// nothing happens if the break condition never happens
	}
	

	// read the i-chunk of filename and returns an array of bytes
	public byte[] read(String filename, int i) {

		for (File f_ : fileList) { 
			if (f_.getFileName().equals(filename)) {
				return f_.getChunk(i).getContent();
			}
		}
		
		System.out.println(filename + " not found.");
		return null; // no bytes; will only get here if no such file was found
	}
	
	// returns the list of files in DFS
	public List<File> Ls(){	return fileList; }
	
	// get a particular File from fileList
	public File getFile(int i) {return fileList.get(i);}
	
	
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
	public String[] search(String filter, String index) {
		int location = fileList.indexOf(new File(index));
		File f = fileList.get(location);
		char sFirstLetter = filter.charAt(0);
		boolean found = false;
		Chunk c;
		for (int i = 0; i < f.getSize(); i++) {
			c = f.getChunk(i);
			if (sFirstLetter >= c.getFirstLetter().charAt(0) && sFirstLetter <= c.getLastLetter().charAt(0)) {
				found = true;
				break;
			}
		}
		Number160 guid = new Number160(c.getGUID());
		byte[] data = get(peer, guid);
		String str = new String(data, StandardCharsets.UTF_8);
		String[] songs = str.split("_");
		ArrayList<String> result = new ArrayList<String>();
		int sizeFilter = filter.length();
		for (int i = 0; i < songs.length; i++) {
			if (filter.equals(songs[i].substring(0, sizeFilter))) {
				result.add(songs[i]);
			}
		}
		
		return (String[]) result.toArray();
	}
	
	private static Object get(final PeerDHT peer, final Number160 guid) throws ClassNotFoundException, IOException {
        
    	FutureGet futureGet = peer.get(guid).start();
        futureGet.awaitUninterruptibly();
        System.out.println("peer " + peer.peerID() + " got: \"" + futureGet.data() + "\" for the key " + guid);
        return futureGet.data().object();
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
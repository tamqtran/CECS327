import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
<<<<<<< HEAD

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
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



/**
 * The class handles the metadata for the music streaming project
 * @author Tam Tran, Vincent Vu
 *
 */
public class Metadata {
	
	protected List<File> fileList;
	
	/**
	 * Default Contructor for Metadata
	 */
	public Metadata() throws IOException {
		fileList = new ArrayList<File>();
		
		Scanner sc = new Scanner(Paths.get("METADATA.txt"));
		while (sc.hasNextLine()) {
		     fileList.add(new File(sc.nextLine()));
		}
		sc.close();
	}
	
	/** 
	 * Constructor for metadata with peer
	 * @param peer
	 * @throws IOException
	 */
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
		/**
		 * Append filename with fileName and song content
		 * @param filename - filename of the index used
		 * @param content - song content
		 */
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

	/**
	 * Append filename with fileName, song content. first letter of chunk, last letter of chunk
	 * @param fileName - index file
	 * @param content - song content
	 * @param first - first letter of chunk
	 * @param last - last letter of chunk
	 */
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

	/**
	 * Delete the file from the DFS
	 * @param filename - index file
	 */
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
	
	/*
	// read the i-chunk of filename and returns an array of bytes
	public byte[] read(String filename, int i) {

		for (File f_ : fileList) { 
			if (f_.getFileName().equals(filename)) {
				return f_.getChunk(i).getChunkData();
			}
		}
		
		System.out.println(filename + " not found.");
		return null; // no bytes; will only get here if no such file was found
	}*/
	
	/**Returns the list of files in DFS
	 * @return list of files in DFS
	 */
	public List<File> Ls(){	return fileList; }
	
	/**Get a particular File from fileList
	 * 
	 * @param i - ith File
	 * @return ith File
	 */
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

	/**
	 * Search song using filter
	 * @param filter - text to search song
	 * @param index - which index file to search in 
	 * @return song results
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String[] search(String filter, String index) throws ClassNotFoundException, IOException {
		Scanner sc = new Scanner(Paths.get(index));
		ArrayList<String> songsList = new ArrayList<String>();
		while (sc.hasNextLine()) {
		     songsList.add(sc.nextLine());
		}
		sc.close();
		String[] songList = (String[]) songsList.toArray();
		char sFirstLetter = filter.charAt(0);
		boolean found = false;
		ArrayList<String> result = new ArrayList<String>();
		Chunk c;
		for (int i = 0; i < songList.length; i++) {
			if (filter.equals(songList[i].substring(0, filter.length()))) {
				result.add(songList[i]);
			}
		}
		ArrayList<String> moddedSearch = new ArrayList<String>();
		if (index.equals("Artist")) {
			for (int i = 0; i < result.size(); i++) {
				String[] temp = result.get(i).split(";");
				String temp2 = temp[1] + "_" + temp[0] +"_" + temp[2];
				moddedSearch.add(temp2);
			}
		}
		if (index.equals("Song")) {
			for (int i = 0; i < result.size(); i++) {
				String[] temp = result.get(i).split(";");
				String temp2 = temp[1] + "_" + temp[0] + "_" + temp[2];
				moddedSearch.add(temp2);
			}
		}
		if (index.equals("Album")) {
			for (int i = 0; i < result.size(); i++) {
				String[] temp = result.get(i).split(";");
				String temp2 = temp[1] + "_" + temp[2] + "_" + temp[0];
				moddedSearch.add(temp2);
			}
		}
		
		return (String[]) moddedSearch.toArray();
	}
	
	/**
	 * Get data from peer by using guid
	 * @param peer - peer to get data from
	 * @param guid - guid to map data
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
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
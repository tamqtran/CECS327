import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * The class handles the metadata for the music streaming project
 * @author Tam Tran, Vincent Vu
 *
 */
public class Metadata {
	
	protected List<Meta_File> fileList;
	
	/**
	 * Default Contructor for Metadata
	 */
	public Metadata() throws IOException {
		fileList = new ArrayList<Meta_File>();
		
		Scanner sc = new Scanner(Paths.get("METADATA.txt"));
		while (sc.hasNextLine()) {
		     fileList.add(new Meta_File(sc.nextLine()));
		}
		sc.close();
	}
	
	/**
	 * Contructor for Metadata with specified file
	 */
	public Metadata(String file) throws IOException {
		fileList = new ArrayList<Meta_File>();
		
		Scanner sc = new Scanner(Paths.get(file));
		while (sc.hasNextLine()) {
		     fileList.add(new Meta_File(sc.nextLine()));
		}
		sc.close();
	}

	// append an inverted index file by add content at the end of filename. 
	// if filename does not exists, it creates it and adds the content
		/**
		 * Append filename with fileName and song content
		 * @param filename - filename of the index used
		 * @param content - song content
		 */
		public void append(String filename, String[] content) {
		
		// get correct file
		Meta_File f = null;		
		for (Meta_File f_ : fileList) {
			if (f_.getFileName().equals(filename)) {
				f = f_;	break;
			} // will continue otherwise
		}
		
		// no fileName found, make a new one
		if (f == null) {
			System.out.println(filename + " is new; creating new file...");
			f = new Meta_File(filename);
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
		Meta_File f = null;
		//Need to replace fileName + .txt
		//Wont work
		if (fileList.contains(new Meta_File(fileName))) {
			int index = fileList.indexOf(new Meta_File(fileName));
			f = new Meta_File (fileName);
		} else {
			f = new Meta_File(fileName);
		}
		f.appends(content, first, last);
	}

	/**
	 * Delete the file from the DFS
	 * @param filename - index file
	 */
	public void delete(String filename) {
		
		for (Meta_File f_ : fileList) {
			if (f_.getFileName().equals(filename)) {
				fileList.remove(f_);
				System.out.println(filename + " found and removed.");
				break;
			} // for loop will continue otherwise
		}
		
		// nothing happens if the break condition never happens
	}
		
	/**Returns the list of files in DFS
	 * @return list of files in DFS
	 */
	public List<Meta_File> Ls(){	return fileList; }
	
	/**Get a particular File from fileList
	 * 
	 * @param i - ith File
	 * @return ith File
	 */
	public Meta_File getFile(int i) {return fileList.get(i);}
		
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
		Scanner sc = new Scanner(Paths.get(index.toLowerCase() + "index.txt"));
		List<String> songsList = new ArrayList<String>();
		while (sc.hasNextLine()) {
		     songsList.add(sc.nextLine());
		}
		sc.close();
		System.out.println("\nSearching for " + filter + ":");
		for (int i = 0; i < songsList.size(); i++) {
			System.out.println(songsList.get(i));
		}
		
		List<String> result = new ArrayList<String>();
		//System.out.println(songsList.get(0).substring(0, filter.length()-1));
		System.out.println("\n-^-\n" + songsList.get(0).split(";")[0].toLowerCase());
		// i starts at 1 to skip the headers in index 0
		for (int i = 1; i < songsList.size(); i++) {
			//if (filter.equalsIgnoreCase(songsList.get(i).substring(0, filter.length()))) {
			if (songsList.get(i).split(";")[0].toLowerCase().contains(filter.toLowerCase())) {
				result.add(songsList.get(i));
			}
		}
		System.out.println("-O-\nResults:");
		for (int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i));
		}
		List<String> moddedSearch = new ArrayList<String>();
		
		int a = 0, b = 1, c = 2; // 'Song' configuration by default; song = 0, artist = 1, album = 2
		switch(index) {
		case "Song": 	a = 0; b = 1; c = 2; break;
		case "Artist": 	a = 1; b = 0; c = 2; break;
		case "Album": 	a = 2; b = 0; c = 1; break;
		default: 		break;
		}
		
		for (int i = 0; i < result.size(); i++) {
			String[] temp = result.get(i).split(";");
			String temp2 = temp[a] + "_" + temp[b] +"_" + temp[c];
			moddedSearch.add(temp2);
		}
		
		System.out.println("\n-v-\nFinal list:");
		String[] array = moddedSearch.toArray(new String[moddedSearch.size()]);
		for (int i = 0; i < moddedSearch.size(); i++) {
			System.out.println(moddedSearch.get(i));
		}
		return array;
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
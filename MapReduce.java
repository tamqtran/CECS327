import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;

public class MapReduce {
	TreeMap<String, List<String>> mappingTreeSong = new TreeMap();
	TreeMap<String, List<String>> mappingTreeArtist = new TreeMap();
	TreeMap<String, List<String>> mappingTreeAlbum = new TreeMap();
	TreeMap reduceTree = new TreeMap();
	
	/**
	 * Initailizes the map reduce using a peer
	 *  @param file - metafile txt file (contains all the index files)
	 *  @return returns nothing
	 *  @throws IOException 
	 */
	public void runMapReduce(PeerDHT[] peers, String file) throws IOException {
		MapCounter mapCounter = new MapCounter();
		ReduceCounter reduceCounter = new ReduceCounter();
		CompletedCounter completedCounter = new CompletedCounter();
		
		Mapper mapper = new Mapper();
		Reducer reducer = new Reducer();
		
		
		
		// get metadata file
		Metadata meta= new Metadata(file);

		// for each index file in the metadata.txt
		for(int i = 0; i < 3; i++){
			//System.out.println(meta.getFile(i).getFileName());
			mapCounter.add(meta.getFile(i).getFileName());
			
			// let peer be the process responsible for storing page
			mapContext(peers, meta.getFile(i).getFileName(), mapper, mapCounter); 
			
			
		}
		
		// Printing to make sure it has the right mapping
		//System.out.println(mappingTreeSong);
		//System.out.println(mappingTreeArtist);
		//System.out.println(mappingTreeAlbum);
		// map phase
		// locate metafile.file
		
		// ...
		
		/* Pseudo code from assignment 4 
		 * 
		>>^ mapCounter = new Counter();
		>>^ reduceCounter = new Counter();
		>>^ completedCounter = new Counter();
		>>^ mapper = new MapInterface(); 
		>>^ reducer = new ReduceInterface(); 
		// map Phases 
		for each page in metafile.file 
			mapCounter.add(page); 
			let peer be the process responsible for storing page
			peer.mapContext(page, mapper, mapCounter) 
			wait until mapCounter.hasCompleted() = true 
			// reduce phase 
			reduceContext(guid, reducer, reduceCounter); 
			wait until reduceCounter.hasCompleted() = true; 
			completed(guid, completedCounter); 
			wait until completedCounter.hasCompleted() = true;
		*/
	}
	
	public void emitReduce(PeerDHT peer,String key, String value, Counter counter) {
		
	}
	
	public void emitMap(PeerDHT peer, String key, String value, Counter counter) {
		
	}
	
	public void mapContext(PeerDHT[] peers, String page, MapInterface mapper, MapCounter mapCounter) throws IOException {
		
		// read the file and put in list
		Scanner sc = new Scanner(Paths.get(page));
		
		// skip first row of index file
		sc.nextLine();
		
		int n = 0; // number of rows
		while (sc.hasNextLine()) {
			String row = sc.nextLine();

			String[] songInfo = row.split(";");
			
			// before the first ';' in the index file
			String key = songInfo[0];
			
			// after the first ';' in the index file
			String value= songInfo[1] + ";" + songInfo[2]+ ";" + songInfo[3];
		
			n++;
			
			// depending on page, use corresponding treemaps
			switch(page) {
				case "songIndex.txt":
					mapper.map(key, value, mappingTreeSong, peers);
					break;
				case "artistIndex.txt":
					mapper.map(key, value, mappingTreeArtist, peers);
					break;
				case "albumIndex.txt":
					mapper.map(key, value, mappingTreeAlbum, peers);
					break;
				default:
					System.out.println("No Index");
					break;
			}
		}
		sc.close();
		
		mapCounter.increment(page, n);
		
		//TODO create new thread to avoid blocking?
	}
	
	public void reduceContext(PeerDHT peer, Integer source, ReduceInterface reducer, Counter counter) {
		
	}
	
	public void completed(PeerDHT peer, Integer source, Counter counter) {
		
	}
	
	
}

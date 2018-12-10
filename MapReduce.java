import java.io.IOException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;

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

		// MAPPING PHASE
		do {
			// for each index file in the metadata.txt
			for(int i = 0; i < 3; i++){
				//System.out.println(meta.getFile(i).getFileName());
				mapCounter.add(meta.getFile(i).getFileName());
				
				// let peer be the process responsible for storing page
				mapContext(peers, meta.getFile(i).getFileName(), mapper, mapCounter); 
			}
		}while(!mapCounter.hasCompleted());
		
		
		System.out.println(mapCounter.hasCompleted());
		//System.out.println(mapCounter.counter);
		// printing to test out if treemap has the correct values
		//System.out.println(mappingTreeSong);
		
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
	
	public static void emitMap(PeerDHT peer, String key, String value, MapCounter mapCounter, TreeMap<String, List<String>> map) throws RemoteException {
		// get content of peer
		List<Number160> contentOfPeer = (List<Number160>) peer.storageLayer().findContentForResponsiblePeerID(peer.peerID());
		
		// get guid of song from value to match the guid of song in the peer
		Number160 guid = new Number160(value.split(";")[2]);
		
		// go through each peer
		for(Number160 p: contentOfPeer)
		{
			// checking if peer has the song guid
			if(p.equals(new Number160(value.split(";")[2]))) {
				
				// if there is not a key for peer, make a new key with a List of String
				if(!map.containsKey(key)){
					
					// list and add value
					List<String> valueList = new ArrayList<String>();
					valueList.add(value);
					map.put(key, valueList);
					
					
				}
				// if there is a key, add on to its List of String
				else {
					// add value to existing list and put it back inside the tree map
					List<String> newValueList = map.get(key);
					newValueList.add(value);
					map.replace(key, newValueList);
				}
				
				// decrement counter
				mapCounter.decrement();
			}
			else
				System.out.println("Song not in peer");
		}
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
		
			// depending on page, use corresponding treemaps
			switch(page) {
				case "songIndex.txt":
					mapper.map(key, value, mappingTreeSong, mapCounter, peers);
					break;
				case "artistIndex.txt":
					mapper.map(key, value, mappingTreeArtist, mapCounter, peers);
					break;
				case "albumIndex.txt":
					mapper.map(key, value, mappingTreeAlbum, mapCounter, peers);
					break;
				default:
					System.out.println("No Index");
					break;
			}
			n++;
		}
		sc.close();
		
		// increment counter
		mapCounter.increment(page, n);
		
		
		//TODO create new thread to avoid blocking?
	}
	
	public void reduceContext(PeerDHT peer, Integer source, ReduceInterface reducer, Counter counter) {
		
	}
	
	public void completed(PeerDHT peer, Integer source, Counter counter) {
		
	}
	
	
}

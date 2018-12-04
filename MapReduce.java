import java.io.IOException;

import net.tomp2p.dht.PeerDHT;

public class MapReduce {
	
	/**
	 * Initailizes the map reduce using a peer
	 *  @param file - metafile txt file (contains all the index files)
	 *  @return returns nothing
	 *  @throws IOException 
	 */
	public static void runMapReduce(PeerDHT[] peers, String file) throws IOException {
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
	
	public static void emitReduce(PeerDHT peer,String key, String value, Counter counter) {
		
	}
	
	public static void emitMap(PeerDHT peer, String key, String value, Counter counter) {
		
	}
	
	public static void mapContext(PeerDHT[] peer, String page, MapInterface mapper, MapCounter mapCounter) {
		
	}
	
	public static void reduceContext(PeerDHT peer, Integer source, ReduceInterface reducer, Counter counter) {
		
	}
	
	public static void completed(PeerDHT peer, Integer source, Counter counter) {
		
	}
	
	
}

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;

public class MapReduce {
	TreeMap mappingTree = new TreeMap();
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
	
	public void mapContext(PeerDHT[] peer, String page, MapInterface mapper, MapCounter mapCounter) throws IOException {
		
		// read the file and put in list
		Scanner sc = new Scanner(Paths.get(page));
		
		// skip first row of index file
		sc.nextLine();
		
		int n = 0; // number of rows
		while (sc.hasNextLine()) {
			String row = sc.nextLine();

			String[] songInfo = row.split(";");
			String key = null;
			String[] value = new String[4];
			
			switch (page) {
	            case "songIndex.txt": 
	            	key = songInfo[0];
	            	value[0] = songInfo[0];
	            	value[1] = songInfo[1];
	            	value[2] = songInfo[2];
	            	value[3] = songInfo[3];
	            	System.out.println(value[0] + "_" + value[1] + "_" + value[2] + "_" + value[3]);
	            	break;
	            case "artistIndex.txt":
	            	key = songInfo[0];
	            	value[0] = songInfo[1];
	            	value[1] = songInfo[0];
	            	value[2] = songInfo[2];
	            	value[3] = songInfo[3];
	            	System.out.println(value[0] + "_" + value[1] + "_" + value[2] + "_" + value[3]);
	            	break;
	            case "albumIndex.txt":
	            	key = songInfo[0];
	            	value[0] = songInfo[1];
	            	value[1] = songInfo[2];
	            	value[2] = songInfo[0];
	            	value[3] = songInfo[3];
	            	System.out.println(value[0] + "_" + value[1] + "_" + value[2] + "_" + value[3]);
	            	break;
	            default: 
	            	System.out.println("No index");
                	break;
			 }
			n++;
			
			mapper.map(key, value, mappingTree);
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

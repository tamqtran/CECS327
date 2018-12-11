import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;

/**
 * This class holds all the main methods for the Map-Reduce features 
 * @author Duong Pham, Tam Tran, Vincent Vu
 *
 */
public class MapReduce {
	TreeMap<String, List<String>> mappingTreeSong = new TreeMap<String, List<String>>();
	TreeMap<String, List<String>> mappingTreeArtist = new TreeMap<String, List<String>>();
	TreeMap<String, List<String>> mappingTreeAlbum = new TreeMap<String, List<String>>();
	TreeMap<String, String> reduceTreeSong = new TreeMap<String, String>();
	TreeMap<String, String> reduceTreeArtist = new TreeMap<String, String>();
	TreeMap<String, String> reduceTreeAlbum = new TreeMap<String, String>();

	/**
	 * Initializes the map reduce using a peer
	 * 
	 * @param file - metafile txt file (contains all the index files)
	 * @return returns nothing
	 * @throws IOException
	 */
	public void runMapReduce(PeerDHT[] peers, String file) throws IOException {
		MapCounter mapCounter = new MapCounter();
		ReduceCounter reduceCounter = new ReduceCounter();
		CompletedCounter completedCounter = new CompletedCounter();

		Mapper mapper = new Mapper();
		Reducer reducer = new Reducer();

		// get index files from metadata file
		Metadata meta = new Metadata(file);

		// MAPPING PHASE
		do {
			// for each index file in the metadata.txt
			for (int i = 0; i < 3; i++) {
				
				mapCounter.add(meta.getFile(i).getFileName());

				// let peer be the process responsible for storing page
				mapContext(peers, meta.getFile(i).getFileName(), mapper, mapCounter);
			}
		} while (!mapCounter.hasCompleted());
		
		// indicate mapping phase has completed
		System.out.println("Mapping Phase is done");
		System.out.println(mappingTreeSong);
		System.out.println(mappingTreeArtist);
		System.out.println(mappingTreeAlbum);
		
		// REDUCE PHASE
		do {
			for (int i = 0; i < 3; i++) {
				
				reduceCounter.add(meta.getFile(i).getFileName());
				
				// reduce for each index file
				reduceContext(meta.getFile(i).getFileName(), reducer, reduceCounter);
			}
		} while(!reduceCounter.hasCompleted());
		

		// indicate reduce phase has completed
		System.out.println("Reduce Phase is done");
		System.out.println(reduceTreeSong);
		System.out.println(reduceTreeArtist);
		System.out.println(reduceTreeAlbum);
		
		completed(completedCounter);

		System.out.println(mapCounter.hasCompleted());
	}



	/**
	 * Emit mapping on the specified peer
	 * @param peer - the peer that is being mapped
	 * @param key - the key that is being checked
	 * @param value - the value that is being added to the tree map
	 * @param mapCounter - the map counter
	 * @param map - the tree map of map phase
	 * @throws RemoteException
	 */
	public static void emitMap(PeerDHT peer, String key, String value, MapCounter mapCounter,
			TreeMap<String, List<String>> map) throws RemoteException {
		// get content of peer
		List<Number160> contentOfPeer = (List<Number160>) peer.storageLayer()
				.findContentForResponsiblePeerID(peer.peerID());

		// get guid of song from value to match the guid of song in the peer
		Number160 guid = new Number160(value.split(";")[2]);
		
		System.out.println(contentOfPeer);

		// go through each key in peer
		for (Number160 p : contentOfPeer) {
			// checking if peer has the song guid
			if (p.equals(new Number160(value.split(";")[2]))) {
				System.out.println("Song is in peer");

				// decrement counter
				System.out.println(mapCounter.counter);
				mapCounter.decrement();
				System.out.println(mapCounter.counter);
				
				
				// if there is not a key for peer, make a new key with a List of String
				if (!map.containsKey(key)) {

					// list and add value
					List<String> valueList = new ArrayList<String>();
					valueList.add(value);
					map.put(key, valueList);

					
				}
				// if there is a key, add on to its List of String
				// this is where duplicates can appear
				else {
					// add value to existing list and put it back inside the tree map
					List<String> newValueList = map.get(key);
					newValueList.add(value);
					map.replace(key, newValueList);
				}

				
			} else
				System.out.println("Song not in peer");
		}
	}

	/**
	 * Begins the map phase by mapping the infomation provided by page
	 * @param peers - the list of peers to map
	 * @param page - the name of the file
	 * @param mapper - the mapper that maps the peers
	 * @param mapCounter - the map counter
	 * @throws IOException
	 */
	public void mapContext(PeerDHT[] peers, String page, MapInterface mapper, MapCounter mapCounter)
			throws IOException {

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
			String value = songInfo[1] + ";" + songInfo[2] + ";" + songInfo[3];

			// depending on page, use corresponding treemaps
			switch (page) {
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

		// TODO create new thread to avoid blocking?
	}

	/**
	 * Begins the reduce phase by reducing the tree map of the map phase in to a simpler tree map on the provided file
	 * @param file - the name of the file
	 * @param reducer - the reducer that is used to reduce the tree map of the map phase
	 * @param counter - the reduce counter
	 * @throws IOException
	 */
	public void reduceContext(String file, ReduceInterface reducer, ReduceCounter counter) throws IOException {
		
		TreeMap<String, List<String>> source = new TreeMap<String, List<String>>();
		TreeMap<String, String> dest = new TreeMap<String, String>();
		
		// depending on page, use corresponding treemaps
		switch (file) {
		case "songIndex.txt":
			source = mappingTreeSong;
			dest = reduceTreeSong;
			break;
		case "artistIndex.txt":
			source = mappingTreeArtist;
			dest = reduceTreeArtist;
			break;
		case "albumIndex.txt":
			source = mappingTreeAlbum;
			dest = reduceTreeAlbum;
			break;
		default:
			System.out.println("No Index");
			break;
		}
		int n = 0;
		for (Map.Entry<String, List<String>> entry : source.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			reducer.reduce(key, value, dest, counter);
			n++;
		}
		
		counter.increment(file, n);
	}

	/**
	 * When Map Phase and Reduce Phase have been completed then write into new sort files
	 * @param counter -the completed counter
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void completed(CompletedCounter counter) throws UnsupportedEncodingException, IOException {
		
		if(counter.hasCompleted()) {
			String[] output = { "sortedSongIndex.txt", "sortedArtistIndex.txt", "sortedAlbumIndex.txt" };
			for (String i : output) {
				switch (i) {
				case "sortedSongIndex.txt":
					tree2File(reduceTreeSong, i);
					break;
				case "sortedArtistIndex.txt":
					tree2File(reduceTreeArtist, i);
					break;
				case "sortedAlbumIndex.txt":
					tree2File(reduceTreeAlbum, i);
					break;
				default:
					System.out.println("Did i make a spelling error somewhere");
					break;
				}
			}
		}
	}

	/**
	 * Writes tree map into file
	 * @param map - tree map that using to write
	 * @param newFile - name of the new file that is being created
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public void tree2File(TreeMap<String, String> map, String newFile)
			throws IOException, UnsupportedEncodingException {
		PrintWriter file = new PrintWriter(newFile, "UTF-8");

		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			// In case of values that have multiple keys
			// meaning example. same song title, different artist/album
			String[] multValues = value.split("_");
			if (multValues.length == 1) {
				file.println(key + ";" + value);
			} else {
				for (String i : multValues) {
					file.println(key + ";" + i);
				}
			}
		}
		file.close();
	}

}

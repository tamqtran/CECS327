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

public class MapReduce {
	TreeMap<String, List<String>> mappingTreeSong = new TreeMap<String, List<String>>();
	TreeMap<String, List<String>> mappingTreeArtist = new TreeMap<String, List<String>>();
	TreeMap<String, List<String>> mappingTreeAlbum = new TreeMap<String, List<String>>();
	TreeMap<String, String> reduceTreeSong = new TreeMap<String, String>();
	TreeMap<String, String> reduceTreeArtist = new TreeMap<String, String>();
	TreeMap<String, String> reduceTreeAlbum = new TreeMap<String, String>();

	/**
	 * Initailizes the map reduce using a peer
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
				// System.out.println(meta.getFile(i).getFileName());
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
		for (int i = 0; i < 3; i++) {
			
			// reduce for each index file
			reduceContext(meta.getFile(i).getFileName(), reducer, reduceCounter);
		}

		// indicate reduce phase has completed
		System.out.println("Reduce Phase is done");
		System.out.println(reduceTreeSong);
		System.out.println(reduceTreeArtist);
		System.out.println(reduceTreeAlbum);
		
		completed(completedCounter);

		System.out.println(mapCounter.hasCompleted());
	}

	public void emitReduce(PeerDHT peer, String key, String value, Counter counter) {

	}

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

	// Modeled after mapContext
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
		
		for (Map.Entry<String, List<String>> entry : source.entrySet()) {
			String key = entry.getKey();
			List<String> value = entry.getValue();
			reducer.reduce(key, value, dest);
		}

		// I have no idea how to use counter
	}

	public void completed(CompletedCounter counter) throws UnsupportedEncodingException, IOException {
		// Again got no idea what counter is for
		/**
		 * Code for if we want to empty and override //CAREFUL WHEN TESTING //SAVE A
		 * COPY OF INDEX FILES
		 * 
		 * //empties content of file new PrintWriter(source).close();
		 * 
		 * //Writes to file by appending PrintWriter output = new PrintWriter(new
		 * FileWriter(source, true));
		 */

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;

/**
 * This class is used for the Map Phase
 * @author Austin Tao, Tam Tran
 *
 */
public class Mapper implements MapInterface{

	
	@Override
	/** This map phase calls emitMap so that it can map each peer
	 * @param key - the string key to check for the 
	 * @param value - the string value
	 * @param map- the tree map of the map phase
	 * @param counter - the map counter
	 * @param peers - the peers in the system
	 */
	public void map(String key, String value, TreeMap<String, List<String>> map, MapCounter counter, PeerDHT[] peers) throws IOException {
		// for each peer
		for(int i = 0; i < peers.length; i++ )
		{
			// emit map
			MapReduce.emitMap(peers[i], key, value, counter, map);
		}
	}

	
}

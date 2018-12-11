import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;

/**
 * This interface is used to for the Map Phase
 * @author Austin Tao, Tam Tran
 *
 */
public interface MapInterface {
	/** This map phase calls emitMap so that it can map each peer
	 * @param key - the string key to check for the 
	 * @param value - the string value
	 * @param map- the tree map of the map phase
	 * @param counter - the map counter
	 * @param peers - the peers in the system
	 */
	public void map(String key, String value, TreeMap<String, List<String>> map, MapCounter counter,  PeerDHT[] peers) throws IOException;
}

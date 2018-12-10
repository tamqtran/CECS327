import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;

public interface MapInterface {
	public void map(String key, String value, TreeMap<String, List<String>> map, MapCounter counter,  PeerDHT[] peers) throws IOException;
}

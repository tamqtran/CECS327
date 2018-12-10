import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;
public class Mapper implements MapInterface{

	@Override
	public void map(String key, String value, TreeMap<String, List<String>> map, MapCounter counter, PeerDHT[] peers) throws IOException {
		// for each peer
		for(int i = 0; i < peers.length; i++ )
		{
			// emit map
			MapReduce.emitMap(peers[i], key, value, counter, map);
		}
	}

	
}

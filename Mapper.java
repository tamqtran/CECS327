import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;
public class Mapper implements MapInterface{

	@Override
	public void map(String key, String value, TreeMap<String, List<String>> map, PeerDHT[] peers) throws IOException {
		// TODO Auto-generated method stub
		// for each word in value
		// emit (md5(mode), word + ":" + 1)
		
		
		for(int i = 0; i < 3; i++ )
		{
			// get content of peer
			List<Number160> contentOfPeer = (List<Number160>) peers[i].storageLayer().findContentForResponsiblePeerID(peers[i].peerID());
			
			// get guid of song from value to match the guid of song in the peer
			Number160 guid = new Number160(value.split(";")[2]);
			
			// go through each peer
			for(Number160 p: contentOfPeer)
			{
				// checking if peer has the song guid
				if(p.equals(new Number160(value.split(";")[2]))) {
					
					// if there is not a key, make a new key with a List of String
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
				}
				else
					System.out.println("Song not in peer");
				
				// printing to test out if treemap has the correct values
				System.out.println(map);
			}
		}
	}

	
}

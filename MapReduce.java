import net.tomp2p.dht.PeerDHT;

public class MapReduce {
	public static void emitReduce(PeerDHT peer,String key, String value, Counter counter) {
		
	}
	
	public static void emitMap(PeerDHT peer, String key, String value, Counter counter){
		
	}
	
	public static void mapContext(PeerDHT peer, Integer page, MapInterface mapper, Counter counter){
		
	}
	
	public static void reduceContext(PeerDHT peer, Integer source, ReduceInterface reducer, Counter counter){
		
	}
	
	public static void completed(PeerDHT peer, Integer source, Counter counter) {
		
	}
}

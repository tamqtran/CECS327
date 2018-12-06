import java.io.IOException;
import java.util.TreeMap;

public class Mapper implements MapInterface{

	@Override
	public void map(String key, String[] value, TreeMap map) throws IOException {
		// TODO Auto-generated method stub
		// for each word in value
		// emit (md5(mode), word + ":" + 1)
		
	}

	
}

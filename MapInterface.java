import java.io.IOException;
import java.util.TreeMap;

public interface MapInterface {
	public void map(String key, String[] value, TreeMap map) throws IOException;
}

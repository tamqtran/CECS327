import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

/**
 * This interface is used to for the Map Phase
 * @author Austin Tao, Vincent Vu
 *
 */
public interface ReduceInterface {
	/**
	 * This reduce phase calls reduces the tree map of the map phase
	 * @param key - the string key
	 * @param values - the string values (may hold dupicates)
	 * @param map - the tree map of the reduce phase
	 * @param counter - the reduce counter
	 * @throws IOException
	 */
	public void reduce(String key, List<String> values, TreeMap<String,String> map, ReduceCounter counter) throws IOException;
}

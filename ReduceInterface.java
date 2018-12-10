import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

public interface ReduceInterface {
	public void reduce(String key, List<String> values, TreeMap<String,String> map, ReduceCounter counter) throws IOException;
}

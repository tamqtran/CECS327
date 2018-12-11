import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * This Reducer reduces the tree map for the reduce phase
 * @author Duong Pham, Vicent Vu
 *
 */
public class Reducer implements ReduceInterface {

	@Override
	/**
	 * Reduce the tree map for the reduce phase
	 * @param key - the string key that is being used to check in the tree maps
	 * @param values - the values that will be placed into the tree map of the reduce phase
	 * @param map - the tree map of the reduce phase
	 * @param counter - the reduce counter
	 */
	public void reduce(String key, List<String> values, TreeMap<String, String> map, ReduceCounter counter) throws IOException {
		// TODO Auto-generated method stub

		// Does not allow duplicates
		List<String> noDupes = new ArrayList<>(new HashSet<String>(values));

		// compares the list of values from the map phase
		// Sorts the values using comparator
		Collections.sort(noDupes, new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				// Standard compare function for String
				// negative if less than (alphabetically)
				// 0 if equal
				// positive if greater than (alphabetically)
				return a.compareTo(b);
			}
		});

		// String variable to hold condensed format of values
		// grabs first value of List
		String newValue = noDupes.get(0);
		counter.decrement();
		
		// iterates through sorted list
		// adds each element onto list
		for (int i = 1; i < noDupes.size(); i++) {
			newValue = newValue + "_" + (String) noDupes.get(i);
			counter.decrement();
		}

		// adds resulting string to reduceTree
		map.put(key, newValue);
		
	}

}

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

/**
 * This interface is used to make counters for the Map-Reduce feature
 * @author Duong Pham
 *
 */
public class CompletedCounter implements CounterInterface {

	long counter = 0;
	Set<String> set = new HashSet<String>();

	@Override
	/**
	 * Add the string key to the counter
	 * @param key - the filename
	 */
	public void add(String key) {
		set.add(key);
	}

	@Override
	/**
	 * Check if counter is at 0 or lower, if so then it has been completed
	 */
	public boolean hasCompleted() {
		if( counter == 0 && set.isEmpty() )
			return true;
		return false;
	}

	/**
	 * Increment so that a certain number is being kept tracked
	 * @param key - the filename
	 * @param n - the number to increment
	 */
	@Override
	public void increment(String key, int n) throws RemoteException {
		set.remove(key);
		counter += n;
	}

	/**
	 * Decrement so that counter is closer to completion
	 */
	@Override
	public void decrement() throws RemoteException {
		counter--;
	}

}

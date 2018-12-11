import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

/**
 * This counter is used to track if the Reduce Phase is completed
 * @author Duong Pham
 *
 */
public class ReduceCounter implements CounterInterface{

	long counter = 0;
	Set<String> set  = new HashSet<String>();

	/**
	 * Add the string key to the counter
	 * @param key - the filename
	 */
	@Override
	public void add(String key) {
		set.add(key);
	}

	/**
	 * Check if counter is at 0 or lower, if so then it has been completed
	 */
	@Override
	public boolean hasCompleted() {
		if( counter <= 0 && set.isEmpty() )
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

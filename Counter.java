import java.rmi.RemoteException;
import java.util.Set;

/**
 * This interface is used to make counters for the Map-Reduce feature
 * @author Austin Tao
 *
 */
public class Counter implements CounterInterface {
	long counter = 0;
	Set<String> set;
	
	/**
	 * Add the string key to the counter
	 * @param key - the filename
	 */
	@Override
	public void add(String key) {
		// TODO Auto-generated method stub
		set.add(key);
	}

	/**
	 * Check if counter is at 0 or lower, if so then it has been completed
	 */
	@Override
	public boolean hasCompleted() {
		// TODO Auto-generated method stub
		if (counter <= 0 && set.isEmpty())
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
		// TODO Auto-generated method stub
		set.remove(key);
		counter += n;
	}

	/**
	 * Decrement so that counter is closer to completion
	 */
	@Override
	public void decrement() throws RemoteException {
		// TODO Auto-generated method stub
		counter--;
	}

}

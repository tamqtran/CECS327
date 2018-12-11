import java.rmi.RemoteException;

/**
 * This interface is used to make counters for the Map-Reduce feature
 * @author Austin Tao
 *
 */
public interface CounterInterface {
	/**
	 * Add the string key to the counter
	 */
	public void add(String key);
	
	/**
	 * Check if counter is at 0 or lower, if so then it has been completed
	 */
	public boolean hasCompleted();
	
	/**
	 * Increment so that a certain number is being kept tracked
	 */
	public void increment(String key, int n) throws RemoteException;
	
	/**
	 * Decrement so that counter is closer to completion
	 */
	public void decrement() throws RemoteException;
}

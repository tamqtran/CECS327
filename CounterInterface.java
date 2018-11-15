import java.rmi.RemoteException;

public interface CounterInterface {
	public void add(String key);
	public boolean hasCompleted();
	public void increment(String key, long n) throws RemoteException;
	public void decrement() throws RemoteException;
}

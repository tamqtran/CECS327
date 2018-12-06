import java.rmi.RemoteException;

public interface CounterInterface {
	public void add(String key);
	public boolean hasCompleted();
	public void increment(String key, int n) throws RemoteException;
	public void decrement() throws RemoteException;
	//void increment(String key, int n) throws RemoteException;
}

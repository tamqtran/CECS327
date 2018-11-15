import java.rmi.RemoteException;
import java.util.Set;

public class Counter implements CounterInterface {
	long counter = 0;
	Set<String> set;
	
	@Override
	public void add(String key) {
		// TODO Auto-generated method stub
		set.add(key);
	}

	@Override
	public boolean hasCompleted() {
		// TODO Auto-generated method stub
		if (counter == 0 && set.isEmpty())
			return true;
		return false;
	}

	@Override
	public void increment(String key, long n) throws RemoteException {
		// TODO Auto-generated method stub
		set.remove(key);
		counter += n;
	}

	@Override
	public void decrement() throws RemoteException {
		// TODO Auto-generated method stub
		counter--;
	}

}

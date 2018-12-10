import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class ReduceCounter implements CounterInterface{

	long counter = 0;
	Set<String> set  = new HashSet<String>();

	@Override
	public void add(String key) {
		set.add(key);
	}

	@Override
	public boolean hasCompleted() {
		if( counter <= 0 && set.isEmpty() )
			return true;
		return false;
	}

	@Override
	public void increment(String key, int n) throws RemoteException {
		set.remove(key);
		counter += n;
	}

	@Override
	public void decrement() throws RemoteException {
		counter--;
	}

}

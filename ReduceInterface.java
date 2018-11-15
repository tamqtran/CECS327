import java.io.IOException;

public interface ReduceInterface {
	public void reduce(String key, String[] values) throws IOException;
}

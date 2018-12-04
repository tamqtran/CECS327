import java.io.IOException;

public class Mapper implements MapInterface{

	@Override
	public void map(String key, String value) throws IOException {
		// TODO Auto-generated method stub
		// for each word in value
		// emit (md5(mode), word + ":" + 1)

	}

	
}

import java.io.IOException;

public class Mapper implements MapInterface, ReduceInterface{

	@Override
	public void reduce(String key, String[] values) throws IOException {
		// TODO Auto-generated method stub
		
		//String word = values[0].split(":")[0];
		//emit(key, word + ":" + values.length);
	}

	@Override
	public void map(String key, String value) throws IOException {
		// TODO Auto-generated method stub
		
		// for each word in value
		//    emit(md5(word), word + ":" + 1);
	}

}

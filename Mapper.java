import java.io.IOException;


// The commented-out processes below count the number of words
public class Mapper implements MapInterface, ReduceInterface{

	@Override
	public void map(String key, String value) throws IOException {
		// TODO Auto-generated method stub
		
		// String words[] = value.split(" ");
		// for (String word : words)
		//    emit(md5(word), word + ":" + 1);
	}
	
	@Override
	public void reduce(String key, String[] values) throws IOException {
		// TODO Auto-generated method stub
		
		//String word = values[0].split(":")[0];
		//emit(key, word + ":" + values.length);
	}

}

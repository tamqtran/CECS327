import org.json.JSONArray;
import org.json.JSONObject;

public class Chunk {
	protected int guid;
	private String firstLetter, lastLetter;
	protected byte[] content = null;
	
	@SuppressWarnings("null")
	public Chunk(String firstLetter, String lastLetter) {		
		//this.guid = (Integer) null;
		this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
	}
	/*class Chunk {
		int guid;
		String first, last;
		byte[] content; // ????
		public Chunk(int guid) {this.guid = guid;}
		
		//ls, touch, ))
	}*/
	
	protected void writeIn(byte[] content) {
		this.content = content;
	}
	
	// getter methods
	public int getGUID() {return guid;}
	public String getFirstLetter() {return firstLetter;}
	public String getLastLetter() {return lastLetter;}
	public byte[] getContent() {return content;}
}

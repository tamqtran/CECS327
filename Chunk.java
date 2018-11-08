import org.json.JSONArray;
import org.json.JSONObject;

public class Chunk {
	protected int guid;
	private String firstLetter, lastLetter;
	
	@SuppressWarnings("null")
	public Chunk(String firstLetter, String lastLetter) {		
		//this.guid = (Integer) null;
		this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
	}
	
	//Overloaded Constructor
	public Chunk(int guid, String firstLetter, String lastLetter) {
    this.guid = guid;
    this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
  }

  //Default constructor
  public Chunk(int guid) {
    this.guid = guid;
    this.firstLetter = null;
		this.lastLetter = null;
  }
	
	// getter methods
	public int getGUID() {return guid;}
	public String getFirstLetter() {return firstLetter;}
	public String getLastLetter() {return lastLetter;}
}

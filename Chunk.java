/**
 * This class helps represent a Chunk in metadata
 * @author Tam Tran, Vincent Vu
 *
 */
public class Chunk {
	protected int guid;
	private String firstLetter, lastLetter;

	/**
	 * Basic Construtor for Chunk 
	 * @param firstLetter - first letter in chunk
	 * @param lastLetter - last letter in chunk
	 */
	@SuppressWarnings("null")
	public Chunk(String firstLetter, String lastLetter) {		
		//this.guid = (Integer) null;
		this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
	}

	/**Overloaded Constructor for Chunk
	 * @param guid - guid for the chunk
	 * @param firstLetter - first letter in chunk
	 * @param lastLetter - last letter in chunk
	 */
	public Chunk(int guid, String firstLetter, String lastLetter) {
		this.guid = guid;
		this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
	}

	/**Default Constructor for Chunk
	 * @param guid - guid for chunk
	 */
	public Chunk(int guid) {
		this.guid = guid;
		this.firstLetter = null;
		this.lastLetter = null;
	}

	/**
	 * Get guid
	 * @return guid
	 */
	public int getGUID() {return guid;}

	/**
	 * Get firstLetter
	 * @return firstLetter
	 */
	public String getFirstLetter() {return firstLetter;}

	/**
	 * Get lastLetter
	 * @return lastLetter
	 */
	public String getLastLetter() {return lastLetter;}
}

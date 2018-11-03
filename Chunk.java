public class Chunk {
	protected int guid;
	private String firstLetter, lastLetter;
	protected byte[] content;
	
	public Chunk(int guid, String firstLetter, String lastLetter, byte[] content) {		
		this.guid = guid;
		this.firstLetter = firstLetter;
		this.lastLetter = lastLetter;
		this.content = content;
	}
	/*class Chunk {
		int guid;
		String first, last;
		byte[] content; // ????
		public Chunk(int guid) {this.guid = guid;}
		
		//ls, touch, ))
	}*/
	
	public byte[] getContent() {return content;}
}

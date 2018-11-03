public class Chunk {
	int guid;
	String firstLetter, lastLetter;
	
	public Chunk(int guid, String firstLetter, String lastLetter) {		
		this.guid = guid;
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
}

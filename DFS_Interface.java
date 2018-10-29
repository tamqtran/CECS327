/**
 * An interface meant for the distributed file system, or DFS, in order to keep track of all the inverted data files.
 * @author Austin Tao
 * @since 10/25/18
 */
public interface DFS_Interface {
	/**
	 * An abstract append method. It would append content to the file named filename... if the system can find it.
	 * @param filename - the name of the file
	 * @param content - the data to be added to file
	 */
	abstract void append(String filename, byte[] content);
	
	/**
	 * An abstract delete method. It would remove the file named filename from the DFS... if the system can find it.
	 * @param filename - the name of the file
	 */
	abstract void delete(String filename);
	
	/**
	 * An abstract read method. It would get chunk i from the file named filename and return the i-chunk contents as a byte array.
	 * This is, if the system can find the file.
	 * @param filename - the name of the file
	 * @param i - the chunk number to be checked out
	 * @return - a byte array containing the contents of the i-chunk from the file named filename
	 */
	abstract byte[] read(String filename, int i);
	
	/**
	 * An abstract ls method. It would print out all available files within the DFS.
	 */
	abstract void ls();
}

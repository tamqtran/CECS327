import java.net.*;

/**
 * This file is the file that initializes the testing JSON files to start
 * This file starts the program
 * @author Duong Pham
 * @since 10-04-2018
 */
public class start 
{
	public static void main(String[] args) 
	{
		/*Start Client*/
		int serverPort = 6733;
		DatagramSocket aSocket = null;
		
		try 
		{
			aSocket = new DatagramSocket();
			System.out.println("Client started on port: "+aSocket.getPort());
		}
		catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		}
		
		/*End Client*/	
		
		// call initial Frame to start the program
		new Login(aSocket,serverPort);
	}
}

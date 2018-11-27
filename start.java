import java.net.DatagramSocket;
import java.net.SocketException;

import java.util.Scanner;

/**
 * This file is the file that initializes the testing JSON files to start
 * This file starts the program
 * @author Tam Tran
 *
 */
public class start {
	static Scanner sc;
	
	public static void main(String[] args) {
		/*Start Client*/
		int serverPort = 6733;
		DatagramSocket aSocket = null;
		sc = new Scanner(System.in);
		
		try {
			aSocket = new DatagramSocket();
			System.out.println("Client started on port: " + aSocket.getPort());
		}
		catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		}
		
		/*End Client*/	
		
		// call initial Frame to start the program
		new Login(aSocket,serverPort, null);
	}

}

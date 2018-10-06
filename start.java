import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.UUID;

import org.json.JSONTokener;
import org.json.JSONException;


/**
 * This file is the file that initializes the testing JSON files to start
 * This file starts the program
 * @author Tam Tran
 *
 */
public class start 
{
	public static void main(String[] args) 
	{
		/*Start Client*/
		int serverPort = 6733;
		DatagramSocket aSocket = null;
		Scanner sc = new Scanner(System.in);
		
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
		Login begin = new Login(aSocket,serverPort);
	}
	

}

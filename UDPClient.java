//package Client;

import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class UDPClient
{

	public static void main(String args[])
	{
	
		// args give message contents and server hostname
	
		DatagramSocket aSocket = null;
		Scanner sc = new Scanner(System.in);
	
		// loop for client to send messages in
		// INFINITE LOOP FOR NOW
		
		try 
		{
			aSocket = new DatagramSocket();
			System.out.println("Client started on port: "+aSocket.getPort());
			
			byte [] m;
			
			System.out.println("Type a message to send or x to exit.");
			String cont = sc.nextLine();
			while(!cont.equals("x")) 
			{
				m = cont.getBytes();
			
				
				// opening client side
				//Login user1 = new Login();
				
				InetAddress aHost = InetAddress.getByName("localhost");
				
				int serverPort = 6733;
				
				//Request
				String [] arguments = {"allan","new"};
				m = JSONRequest("addPlaylist",arguments).toString().getBytes("utf-8");
				DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
				aSocket.send(request);
				
				//Reply
				byte[] buffer = new byte[1000];
				
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				
				aSocket.receive(reply);
	
				System.out.println("Reply: " + new String(reply.getData()));
				System.out.println("Type a message to send or x to exit.");
				cont = sc.nextLine();
			}
		}
		catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		finally
		{
			if(aSocket != null) 
				aSocket.close();
		}	
	}
	/**
	 * Format request into JSON Object
	 * @param method call method
	 * @param args argument of the method
	 * @return return json object
	 * @throws JSONException
	 */
	static JSONObject JSONRequest(String method, Object[] args) throws JSONException
	{
	        //Arguments
	        JSONArray jsonArgs = new JSONArray();
	        for (int i=0; i<args.length; i++)
	        {
	        	jsonArgs.put(args[i]);
	        }
	
	        //Json Object
	        JSONObject jsonRequest = new JSONObject();
	        try 
	        {
	                jsonRequest.put("id", UUID.randomUUID().hashCode());
	                jsonRequest.put("method", method);
	                jsonRequest.put("arguments", jsonArgs);
	        }
	        catch (JSONException e)
	        {
	                System.out.println(e);
	        }
	        return jsonRequest;
	}

}
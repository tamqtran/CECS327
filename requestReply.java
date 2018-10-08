import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class holds the methods that handles the request and replies on the Client side of the Client-Server interaction
 * @author Duong Pham, Tam Tran
 * @since 10-05-2018
 */
public class requestReply 
{
	/**
	 * Handles the UDP request and reply on the Client side 
	 * @param method - the method to call
	 * @param param - arguments for the method
	 * @return JSONObject reply from server
	 */
	static JSONObject UDPRequestReply(String method,String[] param, DatagramSocket aSocket, int serverPort) 
	{
		JSONObject JsonReply = null;
		try 
		{
			byte [] m;
						
			// opening client side
			//Login user1 = new Login();
				
			InetAddress aHost = InetAddress.getByName("localhost");
				
			//Request
			String [] arguments = param;
			m = JSONRequestObject(method,arguments).toString().getBytes("utf-8");
			DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
			aSocket.send(request);
				
			//Reply
			byte[] buffer = new byte[1000];
			
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
				
			aSocket.receive(reply);
			
			//Format datagram reply into JSONObject
			JsonReply=new JSONObject(new String(reply.getData()));
			
			System.out.println("Reply: " + new String(reply.getData()));
		}
		catch (SocketException e)
		{
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		return JsonReply;
	}
	
	/**
	 * Formats the request to send to the Server into a JSON Object
	 * @param method - the method to call
	 * @param args - argument of the method
	 * @return JSONObject request for server
	 * @throws JSONException
	 */
	static JSONObject JSONRequestObject(String method, Object[] args) throws JSONException
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

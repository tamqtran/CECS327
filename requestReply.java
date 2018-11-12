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
 * This class holds methods to create messages for UDP interaction
 * @author Duong Pham, Tam Tran
 *
 */
public class requestReply 
{
	/**
	 * This method handles the UDP request and reply on the Client side 
	 * @param method method to call
	 * @param param arguments for the method
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
			
			String r = new String(reply.getData(), "utf-8");
			// System.out.println(r);
			
			//Format datagram reply into JSONObject
			JsonReply=new JSONObject(r);
			
			System.out.println("Reply: " + r.trim());	
			// trim() is to remove the buffer space in the console
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
		return JsonReply;
	}
	
	
	/**
	 * This method formats the request to send to the Server into a JSON Object
	 * @param method call method
	 * @param args argument of the method
	 * @return return JSON object
	 * @throws JSONException
	 */
	static JSONObject JSONRequestObject(String method, Object[] args) throws JSONException {
	       
	        JSONArray jsonArgs = new JSONArray(); //Arguments
	        for (int i=0; i<args.length; i++) {
	        	jsonArgs.put(args[i]);
	        }
	
	        
	        JSONObject jsonRequest = new JSONObject(); //Json Object
	        try {
	                jsonRequest.put("id", UUID.randomUUID().hashCode());
	                jsonRequest.put("method", method);
	                jsonRequest.put("arguments", jsonArgs);
	        } catch (JSONException e) {
	                System.out.println(e);
	        }
	        return jsonRequest;
	}
	
}

//package Server;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;

/**
 * This class is for the UDP relationship to work between server and client
 * @author Duong Pham
 *
 */
public class UDPServer
{
	public class client{
		int clientPort; // client port
		int serverPort; // server port for client
		public client(int cport, int sPort) {
			clientPort = cport;
			serverPort = sPort;
		}
	}
	static Method json;
	
	public static void main(String args[]) throws IOException, ClassNotFoundException
	{
		List<client> clients = new ArrayList<client>();
		
		/*final ServerSocket serverSocket = new ServerSocket(6778);
		Socket clientSocket = null;
		byte[] rep = null;
		try {
			//request
			clientSocket  = new Socket("localhost", 6777);
			ObjectOutputStream request = new ObjectOutputStream(clientSocket.getOutputStream());
			String[] args1 = {"So Serious_Electric Light Orchestra_Balance Of Power.wav"};
			request.writeObject(JSONRequestObject("getSong",args1).toString());
			System.out.println(JSONRequestObject("getSong",args1).toString());
			
			//reply
			Socket socket = serverSocket.accept();
			DataInputStream reply = new DataInputStream(socket.getInputStream());
			rep = new byte[reply.readInt()];
			reply.readFully(rep, 0, rep.length);;
			System.out.println("Reply: "+rep.toString());
			
			request.close();
			clientSocket.close();
			socket.close();
			reply.close();
			serverSocket.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Clip current = null;
		ByteArrayInputStream myInputStream = new ByteArrayInputStream(rep);
    	try {
   
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(myInputStream);
			current = AudioSystem.getClip();
			current.open(audioIn);
			myInputStream.close();
			//current.start();
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		
		json = new Method();
	
	//Removable Testing
	/*
	String meth = "getName";
	String [] args1 = {"allan","playlist"};
	Class<?>[] paramTypes = new Class<?>[args1.length];
	for(int i = 0; i<paramTypes.length; i++) {
		paramTypes[i] = String.class;
	}
	
    try {
       json.getClass().getMethod(meth,paramTypes).invoke(json, args1);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    */
		int port = 6733;
		final DatagramSocket aSocket = new DatagramSocket(port); // server port used for client login
		
		try
		{
			//aSocket = new DatagramSocket(6733);
			System.out.println("Server started on port: "+port);
			byte[] buffer = new byte[1000];
		
			while(true)
			{
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				//System.out.println("Waiting for request...");
				aSocket.receive(request);
				//System.out.println("Request from Port: "+ request.getPort());
				//System.out.println("Request: "+new String(request.getData()).trim());
			
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						JSONObject JsonRequest=new JSONObject(new String(request.getData()));
			
						//Request Data
						@SuppressWarnings("unused")
						int ID = JsonRequest.getInt("id");
						String method = JsonRequest.get("method").toString();
						String param = JsonRequest.get("arguments").toString();
						String [] arguments = param.substring(2, param.length() - 2).split("\",\"");
			
			
						//Printing out request
						/*System.out.println("ID: "+ID);
						System.out.println("Method: "+method);
						System.out.println("Arguments: "+param);
						 */
			
						Class<?>[] argTypes = new Class<?>[arguments.length];
						for(int i = 0; i<arguments.length; i++) 
						{
							argTypes[i] = String.class;
						}
						Object result = null;
			
						//Method call
						try 
						{						
							result = json.getClass().getMethod(method,argTypes).invoke(json, arguments);
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
			
						//Reply
						byte[] rep = (byte[]) result;
						try 
						{
							//rep = JSONReply(method,arguments,result).toString().getBytes("utf-8");
							DatagramPacket reply = new DatagramPacket(rep, rep.length, request.getAddress(), request.getPort());
							//Just for testing
							//System.out.println("Reply to port: "+ request.getPort());
							//System.out.println("Reply: "+new String(reply.getData()).trim());
							
							aSocket.send(reply);
						} 
						catch (UnsupportedEncodingException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						catch (JSONException e1) 
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						catch (IOException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
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
	 * This method creates the reply for the Server to send to the Client.
	 * @param method
	 * @param args
	 * @param reply
	 * @return JSON message
	 * @throws JSONException
	 */
	static JSONObject JSONReply(String method, Object[] args, Object reply) throws JSONException
	{
	        //Arguments
	        JSONArray jsonArgs = new JSONArray();
	        for (int i=0; i<args.length; i++)
	        {
	        	jsonArgs.put(args[i]);
	        }
	
	        //Json Object
	        JSONObject jsonReply = new JSONObject();
	        try 
	        {
	        	jsonReply.put("id", UUID.randomUUID().hashCode());
	        	jsonReply.put("method", method);
	        	jsonReply.put("arguments", jsonArgs);
	        	jsonReply.put("result", reply);
	        }
	        catch (JSONException e)
	        {
	                System.out.println(e);
	        }
	        return jsonReply;
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

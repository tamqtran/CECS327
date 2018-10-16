//package Server;
import java.net.*;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;

public class UDPServer
{
	static Method json;
	
	public static void main(String args[]) throws SocketException
	{
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
		final DatagramSocket aSocket = new DatagramSocket(port);
	
		try
		{
			//aSocket = new DatagramSocket(6733);
			System.out.println("Server started on port: "+port);
			byte[] buffer = new byte[1000];
		
			while(true)
			{
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				System.out.println("Waiting for request...");
				aSocket.receive(request);
				System.out.println("Request from Port: "+ request.getPort());
				System.out.println("Request: "+new String(request.getData()).trim());
			
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
							System.out.println("Reply to port: "+ request.getPort());
							System.out.println("Reply: "+new String(reply.getData()).trim());
							
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
}

//package Server;

//import java.util.UUID;
//
//import javax.swing.DefaultListModel;
//
//import java.net.*;
//import java.util.Scanner;
import java.util.UUID;

import javax.swing.DefaultListModel;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

/**
 * Class that holds methods to handle messages between client and server
 * @author Duong Pham, Vincent Vu, Tam Tran
 *
 */
public class Method 
{
	byte[] bytes;
	int size = 0;
	public Method() {}
	
	/**
	 * Get user's name
	 * @param username - user making request
	 * @param password - password user used
	 * @return user's name 
	 * @throws JSONException - catches errors concerning JSON files 
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] checkLogin(String username,String password) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username,password};
		String sPassword = null;
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			sPassword = obj1.get("password").toString();
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		if(password.equals(sPassword))
			return JSONReply("checkLogin",arg,true);
		return JSONReply("checkLogin",arg,false);
	}
	
	/**
	 * This method changes the loggedIn variable of the account JSON once user logs out
	 * @param username - user making request
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] logIn(String username) throws JSONException, UnsupportedEncodingException
	{
		String[] arg = {username};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			
			boolean loggedIn = (boolean) obj1.get("loggedIn");
			
			// checking if another user has logged in
			// true if someone logged in, false if someone is not
			if(loggedIn == true)	
			{
				System.out.println("Error: Someone is already logged into this account");
				return JSONReply("logIn",arg,false);
			}
			else
			{
				// change variable so multiple users cant use it
				obj1.put("loggedIn", true);
				
				FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(obj1.toString());
				fileWriter.flush();
				fileWriter.close();
			}
			
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("logIn",arg,true);
	}
	
	/**
	 * This method changes the loggedIn variable of the account JSON once user logs out
	 * @param username - user making request
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] loggedOut(String username) throws JSONException, UnsupportedEncodingException
	{
		String[] arg = {username};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			
			boolean loggedIn = (boolean) obj1.get("loggedIn");
			
			if(loggedIn == true)	
			{
				obj1.put("loggedIn", false);
				
				FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(obj1.toString());
				fileWriter.flush();
				fileWriter.close();
				System.out.println("Error: Someone is already logged into this account");
				return JSONReply("loggedOut",arg,true);
			}
				
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("loggedOut",arg,false);
	}
	
	/**
	 * Get user's name
	 * @param username - user making request
	 * @return user's name 
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getName(String username) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username};
		String name = null;
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			name = obj1.get("name").toString();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
			return JSONReply("getName",arg,name);
	}
	
	/**
	 * 
	 * @param username - user making request
	 * @return list of songs
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSonglist(String username) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username};
		JSONObject obj1;
		JSONArray songlist = null;
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    songlist = obj1.getJSONArray("songlist");
		    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("getSonglist",arg,songlist);
	}
	
	/**
	 * Get playlist
	 * @param username - user making request
	 * @return a JSONArray of playlist
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getPlaylists(String username) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username};
		JSONObject obj1;
		JSONArray playlist = null;
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    playlist = obj1.getJSONArray("playlists");
		    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("getPlaylists",arg,playlist);
	}
	
	/**
	 * Add playlist to JSON FiLE
	 * @param username - user making request
	 * @param playlist - playlist to be added
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding  
	 */
	 public byte[] addPlaylist(String username, String playlist) throws JSONException, UnsupportedEncodingException 
	 {
		String [] arg = {username,playlist};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentList = obj1.getJSONArray("playlists");
		    if(!currentList.toList().contains(playlist)) 
		    {
		    	currentList.put(playlist);
		    	obj1.put(playlist, new JSONArray()); //empty song list for this array
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("addPlaylist",arg,true);
	}
	 
	 /**Remove playlist
	 * @param username - user making request
	  * @param playlist - playlist user has chosen to remove
	  * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	  */
	public byte[] removePlaylist(String username, String playlist) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username,playlist};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
				    
			JSONArray currentList = obj1.getJSONArray("playlists");
			currentList.remove(currentList.toList().indexOf(playlist)); 
			obj1.remove(playlist); // also need to remove songs from playlist
				    
			FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return JSONReply("removePlaylist",arg,true);
	}
	
 	/**
	 * Remove a song from a playlist
	 * @param username - user making request
	 * @param playlist - playlist to add the song
	 * @param song - song to be added to playlist
	 * @return true upon success
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] removeSong(String username, String playlist, String song) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username,playlist,song};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) 
		    {
		    	JSONArray currentList = obj1.getJSONArray(playlist);
		    	currentList.remove(currentList.toList().indexOf(song));
		    }
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("removeSong",arg,true);
	}
		
	/**
	 * Add a song to a playlist
	 * @param username - user making request
	 * @param playlist - playlist song is in
	 * @param song - song to add
	 * @return true upon success
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] addSong(String username, String playlist, String song) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username,playlist,song};
		try (InputStream input = new FileInputStream(username+".json")) 
		{
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) 
		    {
		    	 JSONArray currentList = obj1.getJSONArray(playlist);
				    if(!currentList.toList().contains(song)) 
				    {
				    	currentList.put(song);
				    }
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("addSong",arg,true);
	}
		
	/**
	 * Get songs from a playlist
	 * @param username - user making request
	 * @param playlist - playlist to get song from
	 * @return return JSONArray of songs
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 * */
	public byte[] getSongs(String username, String playlist) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {username,playlist};
		JSONObject obj1;
		JSONArray songs = null;
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
			songs = obj1.getJSONArray(playlist);
		  
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return JSONReply("getSongs",arg,songs);
	}

	/**
	 * Return search results
	 * @param search - string the user wanted to search
	 * @return search results
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSearch(String search, String filter) throws JSONException,IOException, UnsupportedEncodingException 
	{
		String [] arg = {search, filter};
		
		final ServerSocket serverSocket = new ServerSocket(6778);
		Socket clientSocket = null;
		try {
			//request
			clientSocket  = new Socket("localhost", 6777);
			ObjectOutputStream request = new ObjectOutputStream(clientSocket.getOutputStream());
			request.writeObject(JSONRequestObject("search",arg).toString());
			System.out.println(JSONRequestObject("search",arg).toString());
			
			//reply 3 seperations metadata.append
			Socket socket = serverSocket.accept();
			DataInputStream reply = new DataInputStream(socket.getInputStream());
			size = reply.readInt();
			bytes = new byte[size];
			reply.readFully(bytes, 0, bytes.length);;
			System.out.println("Reply: "+bytes.toString());
			
			request.close();
			clientSocket.close();
			socket.close();
			reply.close();
			serverSocket.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return JSONReply("getSearch",arg,new String(bytes, StandardCharsets.UTF_8));
	}
	
	/**
	 * Get size of song in byte
	 * @param song - song chosen by user
	 * @param count - the byte value at which the song is at
	 * @return parts of the song in byte form
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws IOException 
	 */
	public byte[] playSong(String song, String count) throws JSONException, IOException 
	{
		String [] arg = {song, count};
		//File file = new File(song);
		if(count.equals("-1")) 
		{
			final ServerSocket serverSocket = new ServerSocket(6778);
			Socket clientSocket = null;
			try {
				//request
				clientSocket  = new Socket("localhost", 6777);
				ObjectOutputStream request = new ObjectOutputStream(clientSocket.getOutputStream());
				String[] args1 = {song};
				request.writeObject(JSONRequestObject("getSong",args1).toString());
				System.out.println(JSONRequestObject("getSong",args1).toString());
				
				//reply 3 seperations metadata.append
				Socket socket = serverSocket.accept();
				DataInputStream reply = new DataInputStream(socket.getInputStream());
				size = reply.readInt();
				bytes = new byte[size];
				reply.readFully(bytes, 0, bytes.length);;
				System.out.println("Reply: "+bytes.toString());
				
				request.close();
				clientSocket.close();
				socket.close();
				reply.close();
				serverSocket.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			/*bytes = new byte[size];
			try 
			{
				BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
				buf.read(bytes, 0, bytes.length);
				buf.close();
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}*/
			return JSONReply("playSong",arg,size);
		}
		else 
		{
			
			byte[] bytePacket = new byte[64000];
			
			if(size - Integer.parseInt(count) > 64000) 
			{
				//System.out.println(i);
				System.arraycopy(bytes, Integer.parseInt(count), bytePacket, 0, 64000);
			}
			else
			{
				System.arraycopy(bytes, Integer.parseInt(count), bytePacket, 0, size - Integer.parseInt(count));
			}
			
			return bytePacket;
		}
	}
	
	/**
	 * Get size of song in byte
	 * @param song - song chosen by user
	 * @param count - the byte value at which the song is at
	 * @return parts of the song in byte form
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSong(String song) throws JSONException, UnsupportedEncodingException 
	{
		
		java.io.File file = new java.io.File(song);
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		try 
		{
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
			
			return bytes;
		
	}
	
	
	/**
	 * This method creates the reply for the Server to send to the Client.
	 * @param method - method wanted to use
	 * @param args - arguments for method
	 * @param reply - reply result
	 * @return JSON message
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	static byte[] JSONReply(String method, Object[] args, Object reply) throws JSONException, UnsupportedEncodingException
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
	        return jsonReply.toString().getBytes("utf-8");
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

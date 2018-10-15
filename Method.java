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
import java.util.Scanner;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

public class Method 
{
	public Method() {}
	
	/**
	 * Get user's name
	 * @param username user making request
	 * @return user's name 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @return user's name 
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @return a JSONArray of playlist
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @param playlist playlist to be added
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @param playlist playlist to add the song
	 * @param song song to be added to playlist
	 * @return true upon success
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @param playlist playlist song is in
	 * @param song song to add
	 * @return true upon success
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @param username user making request
	 * @param playlist to get song from
	 * @return return JSONArray of songs
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
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
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 */
	public byte[] getSearch(String search) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {search};
		return JSONReply("getSearch",arg,SearchMenuPanel.search(search));
	}
	
	/**
	 * Get size of song in byte
	 * @param song 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws JSONException 
	 */
	public byte[] playSong(String song, String count) throws JSONException, UnsupportedEncodingException 
	{
		String [] arg = {song, count};
		File file = new File(song);
		int size = (int) file.length();
		if(count.equals("-1")) 
		{
			return JSONReply("playSong",arg,size);
		}
		else 
		{
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
	 * This method creates the reply for the Server to send to the Client.
	 * @param method
	 * @param args
	 * @param reply
	 * @return JSON message
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
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
}

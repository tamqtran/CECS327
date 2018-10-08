//package Server;

import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * This class holds the methods that reads messages from the Client-Server interaction and also invokes the method according to the message 
 * @author Duong Pham
 * @since 10-01-2018
 *
 */
public class Method 
{
	public Method() {}
	
	/**
	 * Get user's name
	 * @param username - user that is making the request
	 * @return user's name 
	 */
	public Boolean checkLogin(String username,String password) 
	{
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
			return true;
		return false;
	}
	
	/**
	 * Changes the loggedIn variable of the account JSON once user logs in
	 * @param username - user that is making the request
	 * @return boolean depending if there is someone currently using the account
	 */
	public Boolean logIn(String username)
	{
		try (InputStream input = new FileInputStream(username+".json")) 
		{
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			
			boolean loggedIn = (boolean) obj1.get("loggedIn");
			
			// checking if another user has logged in
			// true if someone logged in, false if someone is not
			if(loggedIn == true)	
			{
				System.out.println("Error: Someone is already logged into this account");
				return false;
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
		return true;
	}
	
	/**
	 * Changes the loggedIn variable of the account JSON once user logs out
	 * @param username - user that is making the request
	 * @return boolean that identifies that the user has successfully logged out
	 */
	public Boolean loggedOut(String username)
	{
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
				return true;
			}
				
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get user's name
	 * @param username - user that is making the request
	 * @return user's name 
	 */
	public String getName(String username) 
	{
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
			return name;
	}
	
	/**
	 * Get the song list
	 * @param username - user that is making the request
	 * @return in a JSONARRAY song list
	 */
	public JSONArray getSonglist(String username) 
	{
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
		return songlist;
	}
	
	/**
	 * Get the list of playlists
	 * @param username - user that is making the request
	 * @return a JSONArray of playlist
	 */
	public JSONArray getPlaylists(String username) 
	{
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
		return playlist;
	}
	
	/**
	 * Add playlist to JSON FiLE
	 * @param username - user that is making the request
	 * @param playlist - playlist to be added
	 */
	 public boolean addPlaylist(String username, String playlist) 
	 {
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
		return true;
	}
	 
	 /**
		 * Remove playlist from JSON FiLE
		 * @param username - user that is making the request
		 * @param playlist - playlist to be removed
		 */
	public boolean removePlaylist(String username, String playlist) 
	{
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
			return true;
	}
	
	/**
	 * Remove a song from a playlist
	 * @param username - user that is making the request
	 * @param playlist - playlist to add the song
	 * @param song - song to be added to playlist
	 * @return true upon success; false if unsuccessful
	 */
	public boolean removeSong(String username, String playlist, String song) 
	{
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
		return false;
	}
	/**
	 * Add a song to a playlist
	 * @param username - user that is making the request
	 * @param playlist - playlist song is in
	 * @param song - song to add
	 * @return true upon success; false if unsuccessful
	 */
	public boolean addSong(String username, String playlist, String song) 
	{
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
		return true;
	}
	/**
	 * Get songs from a playlist
	 * @param username - user that is making the request
	 * @param playlist - playlist to get song from
	 * @return return JSONArray of songs
	 * */
	public JSONArray getSongs(String username, String playlist) 
	{
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
		return songs;
	}
	
	public String[] getSearch(String search) {
		return SearchMenuPanel.search(search);
	}
}

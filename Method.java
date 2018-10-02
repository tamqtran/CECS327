//package Server;

import java.util.UUID;

import javax.swing.DefaultListModel;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;

public class Method {
	
	public Method() {
		
	}
	
	/**
	 * Get user's name
	 * @param username user making request
	 * @return user's name 
	 */
	public Boolean checkLogin(String username,String password) {
		String sPassword = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			sPassword = obj1.get("password").toString();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(password.equals(sPassword))
			return true;
		return false;
	}
	
	/**
	 * Get user's name
	 * @param username user making request
	 * @return user's name 
	 */
	public String getName(String username) {
		String name = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			name = obj1.get("name").toString();
		}catch (Exception e) {
			e.printStackTrace();
		}
			return name;
	}
	
	public JSONArray getSonglist(String username) {
		JSONObject obj1;
		JSONArray songlist = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    songlist = obj1.getJSONArray("songlist");
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return songlist;
	}
	
	/**
	 * Get playlist
	 * @param username user making request
	 * @return a JSONArray of playlist
	 */
	public JSONArray getPlaylists(String username) {
		JSONObject obj1;
		JSONArray playlist = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    playlist = obj1.getJSONArray("playlists");
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return playlist;
	}
	
	/**
	 * Add playlist to JSON FiLE
	 * @param username user making request
	 * @param playlist playlist to be added
	 */
	 public boolean addPlaylist(String username, String playlist) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentList = obj1.getJSONArray("playlists");
		    if(!currentList.toList().contains(playlist)) {
		    	currentList.put(playlist);
		    	obj1.put(playlist, new JSONArray()); //empty song list for this array
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	 
	 /**
		 * Remove playlist from JSON FiLE
		 * @param username user making request
		 * @param playlist playlistto be removed
		 */
	public boolean removePlaylist(String username, String playlist) {
			try (InputStream input = new FileInputStream(username+".json")) {
			    JSONObject obj1 = new JSONObject(new JSONTokener(input));
			    
			    JSONArray currentList = obj1.getJSONArray("playlists");
			    currentList.remove(currentList.toList().indexOf(playlist)); 
			    obj1.remove(playlist); // also need to remove songs from playlist
			    
			    FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(obj1.toString());
				fileWriter.flush();
				fileWriter.close();
			    
			}catch (Exception e) {
				e.printStackTrace();
			}
			return true;
	}
	
	/**
	 * Remove a song from a playlist
	 * @param username user making request
	 * @param playlist playlist to add the song
	 * @param song song to be added to playlist
	 * @return true upon success
	 */
	public boolean removeSong(String username, String playlist, String song) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) {
		    	JSONArray currentList = obj1.getJSONArray(playlist);
		    	currentList.remove(currentList.toList().indexOf(song));
		    }
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * Add a song to a playlist
	 * @param username user making request
	 * @param playlist playlist song is in
	 * @param song song to add
	 * @return true upon success
	 */
	public boolean addSong(String username, String playlist, String song) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    JSONArray currentPlaylist = obj1.getJSONArray("playlists");
		    if(currentPlaylist.toList().contains(playlist)) {
		    	 JSONArray currentList = obj1.getJSONArray(playlist);
				    if(!currentList.toList().contains(song)) {
				    	currentList.put(song);
				    }
		    }
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**
	 * Get songs from a playlist
	 * @param username user making request
	 * @param playlist to get song from
	 * @return return JSONArray of songs
	 * */
	public JSONArray getSongs(String username, String playlist) {
		JSONObject obj1;
		JSONArray songs = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
			songs = obj1.getJSONArray(playlist);
		  
		}catch (Exception e) {
			e.printStackTrace();
		}
		return songs;
	}
	
}

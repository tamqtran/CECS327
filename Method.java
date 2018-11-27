import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONException;


/**
 * Class that holds methods to handle messages between client and server
 * @author Duong Pham, Vincent Vu, Tam Tran
 *
 */
public class Method {
	byte[] bytes;
	int size = 0;
	public Method() {}

	/**
	 * Check user's username and password inputs for account verification.
	 * @param username - user making request
	 * @param password - password user used
	 * @return user's name 
	 * @throws JSONException - catches errors concerning JSON files 
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] checkLogin(String username, String password) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,password};
		String sPassword = null;
		
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			sPassword = in_obj.get("password").toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (password.equals(sPassword))
			return JSONReply("checkLogin",arg,true);
		
		return JSONReply("checkLogin",arg,false);
	}

	/**
	 * This method changes the loggedIn variable of the account JSON once user logs in.
	 * @param username - user making request
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] logIn(String username) throws JSONException, UnsupportedEncodingException {
		String[] arg = {username};
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			boolean loggedIn = (boolean) in_obj.get("loggedIn");

			// checking if another user has logged in
			// true if someone logged in, false if someone is not
			if (loggedIn == true) {
				System.out.println("Error: Someone is already logged into this account");
				return JSONReply("logIn",arg,false);
			} else {
				// change variable so multiple users cant use it
				in_obj.put("loggedIn", true);

				FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(in_obj.toString());
				fileWriter.flush();
				fileWriter.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("logIn",arg,true);
	}

	/**
	 * This method changes the loggedIn variable of the account JSON once user logs out.
	 * @param username - user making request
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] loggedOut(String username) throws JSONException, UnsupportedEncodingException {
		String[] arg = {username};
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			boolean loggedIn = (boolean) in_obj.get("loggedIn");

			if (loggedIn == true) {
				in_obj.put("loggedIn", false);

				FileWriter fileWriter = new FileWriter(username+".json");
				fileWriter.write(in_obj.toString());
				fileWriter.flush();
				fileWriter.close();
				System.out.println("User " + username + " has logged out.");
				return JSONReply("loggedOut",arg,true);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("loggedOut",arg,false);
	}

	/**
	 * Get user's name.
	 * @param username - user making request
	 * @return user's name 
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getName(String username) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username};
		String name = null;
		
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			name = in_obj.get("name").toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("getName",arg,name);
	}

	/**
	 * Get a user's playlist.
	 * @param username - user making request
	 * @return list of songs
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSonglist(String username) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username};
		JSONObject in_obj;
		JSONArray songlist = null;
		
		try (InputStream input = new FileInputStream(username+".json")) {
			in_obj = new JSONObject(new JSONTokener(input));
			songlist = in_obj.getJSONArray("songlist"); //read songlist
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("getSonglist",arg,songlist);
	}

	/**
	 * Get a user's list of playlists.
	 * @param username - user making request
	 * @return a JSONArray of playlist
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getPlaylists(String username) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username};
		JSONObject in_obj;
		JSONArray playlist = null;
		
		try (InputStream input = new FileInputStream(username+".json")) {
			in_obj = new JSONObject(new JSONTokener(input));
			playlist = in_obj.getJSONArray("playlists"); //read playlists 
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("getPlaylists",arg,playlist);
	}

	/**
	 * Add a playlist to a JSON file.
	 * @param username - user making request
	 * @param playlist - playlist to be added
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding  
	 */
	public byte[] addPlaylist(String username, String playlist) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,playlist};
		
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			JSONArray currentList = in_obj.getJSONArray("playlists");
			
			if (!currentList.toList().contains(playlist)) {
				currentList.put(playlist);
				in_obj.put(playlist, new JSONArray()); //empty song list for this array
			}
			
			FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(in_obj.toString());
			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("addPlaylist",arg,true);
	}

	/**
	 * Remove a playlist from a JSON file.
	 * @param username - user making request
	 * @param playlist - playlist user has chosen to remove
	 * @return byte with boolean
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] removePlaylist(String username, String playlist) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,playlist};
		
		try (InputStream input = new FileInputStream(username+".json"))	{
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			JSONArray currentList = in_obj.getJSONArray("playlists");
			
			currentList.remove(currentList.toList().indexOf(playlist));
			in_obj.remove(playlist); // also need to remove songs from playlist

			FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(in_obj.toString());
			fileWriter.flush();
			fileWriter.close();    
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return JSONReply("removePlaylist",arg,true);
	}

	/**
	 * Remove a song from a playlist.
	 * @param username - user making request
	 * @param playlist - playlist to add the song
	 * @param song - song to be added to playlist
	 * @return true upon success
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] removeSong(String username, String playlist, String song) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,playlist,song};
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));

			JSONArray currentPlaylist = in_obj.getJSONArray("playlists");
			if (currentPlaylist.toList().contains(playlist)) {
				JSONArray currentList = in_obj.getJSONArray(playlist);
				currentList.remove(currentList.toList().indexOf(song));
			}
			
			FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(in_obj.toString());
			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("removeSong",arg,true);
	}

	/**
	 * Add a song to a playlist.
	 * @param username - user making request
	 * @param playlist - playlist song is in
	 * @param song - song to add
	 * @return true upon success
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] addSong(String username, String playlist, String song) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,playlist,song};
		
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject in_obj = new JSONObject(new JSONTokener(input));
			JSONArray currentPlaylist = in_obj.getJSONArray("playlists");
			
			if(currentPlaylist.toList().contains(playlist)) {
				JSONArray currentList = in_obj.getJSONArray(playlist);
				if (!currentList.toList().contains(song))
					currentList.put(song);
			}
			FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(in_obj.toString());
			fileWriter.flush();
			fileWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("addSong",arg,true);
	}

	/**
	 * Get songs from a playlist.
	 * @param username - user making request
	 * @param playlist - playlist to get song from
	 * @return return JSONArray of songs
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 * */
	public byte[] getSongs(String username, String playlist) throws JSONException, UnsupportedEncodingException {
		String [] arg = {username,playlist};
		JSONObject in_obj;
		JSONArray songs = null;
		try (InputStream input = new FileInputStream(username+".json")) {
			in_obj = new JSONObject(new JSONTokener(input));
			songs = in_obj.getJSONArray(playlist); //read playlist
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSONReply("getSongs",arg,songs);
	}

	/**
	 * Return search results.
	 * @param search - string the user wanted to search
	 * @return search results
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSearch(String search, String filter) throws JSONException,IOException, UnsupportedEncodingException {
		String [] arg = {search, filter}, result = null;
		final ServerSocket serverSocket = new ServerSocket(6778);
		Socket clientSocket = null;

		try { //request
			clientSocket  = new Socket("localhost", 6777);
			ObjectOutputStream request = new ObjectOutputStream(clientSocket.getOutputStream());

			String search_obj = JSONRequestObject("search", arg).toString();
			request.writeObject(search_obj);
			System.out.println(search_obj);

			//reply 3 separations metadata.append

			Socket socket = serverSocket.accept();
			ObjectInputStream reply = new ObjectInputStream(socket.getInputStream());
			try {
				result = (String[]) reply.readObject();
			} 
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			clientSocket.close();
			socket.close();
			reply.close();
			serverSocket.close();
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}
		
		return JSONReply("getSearch",arg,result);
	}

	/**
	 * Get a song in bytes, or a particular set of byte packets for a song.
	 * @param song - song chosen by user
	 * @param count - the byte value at which the song is at
	 * @return parts of the song in byte form
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws IOException 
	 */
	public byte[] playSong(String song, String count) throws JSONException, IOException {
		String [] arg = {song, count};
		//File file = new File(song);

		if (count.equals("-1")) {
			final ServerSocket serverSocket = new ServerSocket(6778);
			Socket clientSocket = null;
			try { //request
				clientSocket  = new Socket("localhost", 6777);
				ObjectOutputStream request = new ObjectOutputStream(clientSocket.getOutputStream());
				String[] args1 = {song}; 
				String song_obj = JSONRequestObject("getSong",args1).toString();
				request.writeObject(song_obj);
				System.out.println(song_obj);

				//reply 3 seperations metadata.append

				Socket socket = serverSocket.accept();
				DataInputStream reply = new DataInputStream(socket.getInputStream());
				size = reply.readInt();
				bytes = new byte[size];
				reply.readFully(bytes, 0, bytes.length);
				System.out.println("Reply: "+bytes.toString());

				request.close();
				clientSocket.close();
				socket.close();
				reply.close();
				serverSocket.close();
			} 
			catch (IOException e2) {
				e2.printStackTrace();
			}

			/**
			 * bytes = new byte[size];
			 * try {
			 * 	BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			 * 	buf.read(bytes, 0, bytes.length);
			 * 	buf.close();
			 * } 
			 * catch (FileNotFoundException e) {
			 * 	e.printStackTrace();
			 * } 
			 * catch (IOException e) {
			 * 	e.printStackTrace();
			 * }
			 */

			return JSONReply("playSong",arg,size);
		} else { // return a particular group of byte packets of the song
			byte[] bytePacket = new byte[64000];

			if (size - Integer.parseInt(count) > 64000) {
				//System.out.println(i);
				System.arraycopy(bytes, Integer.parseInt(count), bytePacket, 0, 64000);
			} else {
				System.arraycopy(bytes, Integer.parseInt(count), bytePacket, 0, size - Integer.parseInt(count));
			}

			return bytePacket;
		}
	}

	/**
	 * Get size of song in bytes.
	 * @param song - song chosen by user
	 * @param count - the byte value at which the song is at
	 * @return parts of the song in byte form
	 * @throws JSONException - catches errors concerning JSON files
	 * @throws UnsupportedEncodingException - catches errors concerning encoding 
	 */
	public byte[] getSong(String song) throws JSONException, UnsupportedEncodingException {
		File file = new File(song);
		int size = (int) file.length();
		byte[] bytes = new byte[size];

		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
			buf.read(bytes, 0, bytes.length);
			buf.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
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
	static byte[] JSONReply(String method, Object[] args, Object reply) throws JSONException, UnsupportedEncodingException {
		JSONArray jsonArgs = new JSONArray(); //Arguments
		for (int i=0; i<args.length; i++) {
			jsonArgs.put(args[i]);
		}

		JSONObject jsonReply = new JSONObject(); //JSON Object
		try {
			jsonReply.put("id", UUID.randomUUID().hashCode());
			jsonReply.put("method", method);
			jsonReply.put("arguments", jsonArgs);
			jsonReply.put("result", reply);
		}
		catch (JSONException e) {
			System.out.println(e);
		}

		return jsonReply.toString().getBytes("utf-8");
	}

	/**
	 * This method formats the request to send to the Server into a JSON Object.
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

		JSONObject jsonRequest = new JSONObject(); //JSON Object
		try {
			jsonRequest.put("id", UUID.randomUUID().hashCode());
			jsonRequest.put("method", method);
			jsonRequest.put("arguments", jsonArgs);
		} 
		catch (JSONException e) {
			System.out.println(e);
		}

		return jsonRequest;
	}
}

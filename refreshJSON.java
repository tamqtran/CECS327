import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * This file is the file that initializes the testing JSON files to start
 * This file starts the program
 * @author Tam Tran
 * @since 10-05-2018
 */
public class refreshJSON 
{
	public static void main(String[] args) 
	{
		// 1st Account
		//Create JSON object for account 'allan'
		JSONObject allan = new JSONObject();
		allan.put("username", "allan");					// add username
		allan.put("password", "forever");				// add password
		
		JSONArray playlists = new JSONArray();			// add list of playlists
		playlists.put("king");
		playlists.put("queen");
		playlists.put("ace");
		playlists.put("jack");
		allan.put("playlists", playlists);
		
		JSONArray kingSongLists = new JSONArray();		// add individual playlists with their own songs
		JSONArray queenSongLists = new JSONArray();
		kingSongLists.put("Money");
		kingSongLists.put("So Serious");
		kingSongLists.put("Hello Goodbye");
		queenSongLists.put("Shadows");
		queenSongLists.put("Yellow Submarine");
		allan.put("king", kingSongLists);
		allan.put("queen", queenSongLists);
		allan.put("ace", new JSONArray());
		allan.put("jack", new JSONArray());
		allan.put("loggedIn", false);

		
		try 											//Write into json file
		{
			FileWriter fileWriter = new FileWriter("allan.json");
			fileWriter.write(allan.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) 							// catch exception
		{
			e.printStackTrace();
		}
		
		// 2nd Account
		// Create another JSON object for account 'bill' to test out login into different accounts
		JSONObject bill = new JSONObject();
		bill.put("username", "bill");					// add username
		bill.put("password", "pokemon");				// add password
		
		JSONArray playlists2 = new JSONArray();			// add list of playlists
		playlists2.put("Long Beach");
		bill.put("playlists", playlists2);
		
		JSONArray longBeachLists = new JSONArray();		// add individual playlists with their own songs
		longBeachLists.put("Yellow Submarine");
		bill.put("Long Beach", longBeachLists);
		
		bill.put("loggedIn", false);
		
		try 											// Write into json file
		{
			FileWriter fileWriter = new FileWriter("bill.json");
			fileWriter.write(bill.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) 							// catch exception
		{
			e.printStackTrace();
		}
		
		
		// 3rd Account
		// Create another JSON object for account 'bill' to test out login into different accounts
		JSONObject tony = new JSONObject();
		tony.put("username", "tony");					// add username
		tony.put("password", "stark");				// add password
				
		JSONArray playlists3 = new JSONArray();			// add list of playlists
		playlists3.put("Marvel");
		tony.put("playlists", playlists3);
				
		JSONArray marvel = new JSONArray();		// add individual playlists with their own songs
		marvel.put("Hello Goodbye");
		tony.put("Marvel", marvel);
				
		tony.put("loggedIn", false);
				
		try 											// Write into json file
		{
			FileWriter fileWriter = new FileWriter("tony.json");
			fileWriter.write(tony.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) 							// catch exception
		{
			e.printStackTrace();
		}
		
		System.out.println(allan);
		System.out.println(bill);
		System.out.println(tony);
	}
	

}

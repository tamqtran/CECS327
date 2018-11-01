import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;


/**
 * This file is the file that initializes the testing JSON files to start
 * This file starts the program
 * @author Tam Tran
 *
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
	
		
		// testing METADATA
		JSONObject METADATA = new JSONObject();
		JSONArray chunks = new JSONArray();
		
		JSONObject chunk1 = new JSONObject();	// songtitle that starts from letter a to i
		JSONObject chunk2 = new JSONObject();	// songtitle that starts from letter j to q
		JSONObject chunk3 = new JSONObject();	// songtitle that starts from letter r to z
		
		JSONArray chunkList1 = new JSONArray();
		JSONArray chunkList2 = new JSONArray();
		JSONArray chunkList3 = new JSONArray();
		
		JSONObject song1 = new JSONObject();
		song1.put("filename","Hello Goodbye_The Beatles_Magical Mystery Tour.wav");
		song1.put("guid", "1");
		
		JSONObject song2 = new JSONObject();
		song2.put("filename","Money_Pink Floyd_The Dark Side of the Moon.wav");
		song2.put("guid", "2");
		
		JSONObject song3 = new JSONObject();
		song3.put("filename","Shadows_Jilian Aversa_Origins.wav");
		song3.put("guid", "3");
		
		JSONObject song4 = new JSONObject();
		song4.put("filename","So Serious_Electric Light Orchestra_Balance of Power.wav");
		song4.put("guid", "4");
		
		JSONObject song5 = new JSONObject();
		song5.put("filename","Yellow Submarine_The Beatles_Yellow Submarine.wav");
		song5.put("guid", "5");
		
		chunkList1.put(song1);
		chunkList2.put(song2);
		chunkList3.put(song3);
		chunkList3.put(song4);
		chunkList3.put(song5);
		
		chunk1.put("chunk", chunkList1);
		chunk2.put("chunk", chunkList2);
		chunk3.put("chunk", chunkList3);
		
		chunks.put(chunk1);
		chunks.put(chunk2);
		chunks.put(chunk3);
		
		METADATA.put("chunks", chunks);
		
		
		try 											// Write into json file
		{
			FileWriter fileWriter = new FileWriter("METADATA.json");
			fileWriter.write(METADATA.toString());
			fileWriter.flush();
			fileWriter.close();
		}catch (Exception e) 							// catch exception
		{
			e.printStackTrace();
		}
		
		System.out.println(METADATA);
	}

}

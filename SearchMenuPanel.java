import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.DatagramSocket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Search Menu Class that deals with searching for songs
 * @author Vincent Vu, Austin Tao
 *
 */
@SuppressWarnings("serial")
public class SearchMenuPanel extends JPanel implements ActionListener {
	
	//Declaring variables
	private JPanel 		panel;
	private JTable 		results;
	private JScrollPane resultsJPS;
	private JButton 	playButton, addButton;
	private JLabel 		playlistLabel, responseLabel, searchLabel, filterLabel,
				   		titleLabel, artistLabel, albumLabel;
	private JComboBox<String> playList;
	private String 		username, songName, songTitle, artist, album, 
						filter = "song", u_search = ""; //by default, the search filter is 'by title'
	private int 		searchIndex = 0;
	private DefaultTableModel model;
	private final String[] columns = { "Song", "Artist", "Album" };
	private boolean 	flipOrder = false;
	
	/**
	 * Constructor for SearchMenuPanel
	 * @param username - username of the user
	 * @param search - string that contains the song artist name, album name, and song title name
	 * @param userSearch - the desired search from the user
	 */
	public SearchMenuPanel(String username, String[] search, String userSearch) {
		this.username = username;
		
		System.out.println(username + " has searched: " + userSearch);
		
		// Custom Layout
		this.setLayout(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				
		//Response Label. Displays whether action is successful or not
		responseLabel = new JLabel("");
		responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
		responseLabel.setLocation(200, 17);
		responseLabel.setName("response");
		this.add(responseLabel);

		searchLabel = new JLabel("Searching for: '" + u_search + "'");
		searchLabel.setSize(new Dimension(searchLabel.getPreferredSize()));
		searchLabel.setLocation(10, 10);
		searchLabel.setName("search entry");
		this.add(searchLabel);
		
		filterLabel = new JLabel("Currently sorted by: " + filter);
		filterLabel.setSize(new Dimension(filterLabel.getPreferredSize()));
		filterLabel.setLocation(10, 25);
		filterLabel.setName("filter");
		this.add(filterLabel);
		
		model = null;
		updateSearch(search, userSearch);
		System.out.println("Filter by " + filter + " (Initial)");
		
		// Creates a table to display the search results
		results = new JTable(model);
		results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		results.getTableHeader().setReorderingAllowed(false);
		results.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				int col = results.columnAtPoint(e.getPoint());
				filter = results.getColumnName(col).toLowerCase();
				
				System.out.println("Filter by " + filter);							// system call
				System.out.println("searchIndex " + searchIndex + " / col " + col); // system call
				
				//	if the same column is pressed as the prior action,
				//	then if it's already inverted,
				// 	show inverted for filter; nothing otherwise,
				//	or flipOrder is true; false otherwise
				
				filter  +=  (searchIndex == col) ? ((flipOrder == true) ?   ""  : " (inverted)") : ""   ; 
				flipOrder = (searchIndex == col) ? ((flipOrder == true) ? false :      true    ) : false; 
				
				// for flipOrder --- false: A-Z; true: Z-A
				
				System.out.println("flipOrder is now " + flipOrder);				// system call

				searchIndex = col;			// set searchIndex to col
				tableChangedFilter();		// change the table
				
				filterLabel.setText("Currently sorted for: " + filter);
				filterLabel.setSize(new Dimension(filterLabel.getPreferredSize()));
			}
		});
		
		resultsJPS = new JScrollPane(results);
		resultsJPS.setSize(new Dimension(400, 250));
		resultsJPS.setLocation(10, 50);
		resultsJPS.setName("resultsJPS");
		results.addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int row = results.getSelectedRow();

					songTitle = results.getModel().getValueAt(row, 0).toString();
					artist = results.getModel().getValueAt(row, 1).toString();
					album = results.getModel().getValueAt(row, 2).toString();

					//change text on labels in Homepage
					titleLabel.setText(songTitle); titleLabel.setSize(new Dimension(titleLabel.getPreferredSize())); titleLabel.setVisible(true);
					artistLabel.setText(artist); artistLabel.setSize(new Dimension(artistLabel.getPreferredSize())); artistLabel.setVisible(true);
					albumLabel.setText(album); albumLabel.setSize(new Dimension(albumLabel.getPreferredSize())); albumLabel.setVisible(true);

					songName = songTitle + "_" + artist + "_" + album;	
					System.out.println("SearchMenuPanel "+ panel.getName() + ": '" + songName + "' selected");
				}
			}	
		});
			
		// News JScrollPane else column names wont show up
		this.add(resultsJPS);

		// A drop-down menu for the user to select which playlist to add song to
		// pulls playlist from json file
		playList = new JComboBox<String>(grabPlaylists());
		playList.setSize(new Dimension(110, 30));
		playList.setLocation(280, 315);
		playList.setName("playlist_dd");		
		this.add(playList);

		// Another display label
		playlistLabel = new JLabel("<html>Select a playlist to<br/> add a chosen song to:</html>");
		playlistLabel.setSize(playlistLabel.getPreferredSize());
		playlistLabel.setLocation(140, 315);
		playlistLabel.setName("playlistLabel");
		this.add(playlistLabel);

		// Add Button. Allows user to add a song to the selected playlist
		// Cannot add a song if song already exists in playlist
		// Cannot add if no song is selected
		addButton = new JButton("Add Song");
		addButton.setSize(addButton.getPreferredSize());
		addButton.setName("addButton");
		addButton.setLocation(10, 315);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Checks if user selected a song
				if (results.getSelectedRow() != -1) {
					String selectedPL = playList.getSelectedItem().toString();
					int row = results.getSelectedRow();
					String selectedSong = results.getModel().getValueAt(row, 0).toString();

					// Reading from JSON
					JSONObject obj1;
					String pathname = username + ".json";
					
					try (InputStream input = new FileInputStream(pathname)) {
						obj1 = new JSONObject(new JSONTokener(input));

						// Add new song to playlist x
						System.out.println("Adding new song...");
						JSONArray songlistToAdd = obj1.getJSONArray(selectedPL);
						
						// Checks if the song is legal (songList json contains the name)
						if (!songlistToAdd.toList().contains(selectedSong)) {
							songlistToAdd.put(selectedSong);
							responseLabel.setText("Success! This song was added to that playlist.");
							responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
							responseLabel.setForeground(Color.BLUE);
							System.out.println("Song '" + selectedSong + "' has been added to playlist '" + selectedPL + "'");
						} else {
							responseLabel.setText("Warning! This song already exists in that playlist");
							responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
							responseLabel.setForeground(Color.RED);
							System.out.println("Song '" + selectedSong + "' already exists in playlist '" + selectedPL + "'");
						}
						
						// Changes written to the json file
						 FileWriter fileWriter = new FileWriter(pathname);
						 fileWriter.write(obj1.toString());
						 fileWriter.flush();
						 fileWriter.close();
							
					} catch (Exception c) {
						c.printStackTrace();
					}
				} else {
					responseLabel.setText("No song is being selected");
					responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
					responseLabel.setForeground(Color.RED);
				}
			}
		});
		this.add(addButton);
		
		panel = this;
	}
	
	/**
	 * Changes results based on the search array input
	 * @param search - an array of song strings related to userSearch
	 * @param userSearch - the user input
	 */
	public void changeSearch(String [] search, String userSearch) {
		updateSearch(search, userSearch);
		results.setModel(model);
		results.updateUI();
		
		panel = this;
	}
	
	/**
	 * Update the search based on the search.
	 * @param search
	 * @param userSearch
	 */
	private void updateSearch(String [] search, String userSearch) {
		u_search = userSearch;
		model = new DefaultTableModel(null, columns) {
			public boolean isCellEditable(int row, int column) {
				return false; // This causes all cells to be not editable
			}
		};
		if (search != null) {
			responseLabel.setText("");
			responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
			
			search = sortSearch(search);	// sort the search
			
			// create the model
			for (int i = 0; i < search.length; i++) {
				  model.addRow(search[i].split("_"));
			}
		} else { // create the error label
			responseLabel.setText("No results found for '" + userSearch + "'");
			responseLabel.setSize(new Dimension(responseLabel.getPreferredSize()));
			responseLabel.setForeground(Color.RED);
		}			
		
		searchLabel.setText("Searching for: '" + u_search + "'");
		searchLabel.setSize(new Dimension(searchLabel.getPreferredSize()));
	}
		
	/**
	 * Void method. Sorts the input array search and return a 'sorted' array
	 * @param search: a string array
	 * @return a sorted version of search
	 */
	private String[] sortSearch(String [] search) {
		// sort by current filter
		String[] sorted = quickSort(search, 0, search.length-1);
		// pass back sorted array
		return sorted;
	}
	
	/**
	 * Void method used only when the table headers is interacted with.
	 */
	public void tableChangedFilter() {
		// lineData is an array of null strings
		String[] lineData = new String[results.getRowCount()]; 

		for (int r = 0; r < results.getRowCount(); r++) {
			// lineData[r] gets replaced from null
			lineData[r] = results.getValueAt(r, 0).toString() + "_"; 
			for (int c = 1; c < results.getColumnCount(); c++) {
				lineData[r] += results.getValueAt(r, c).toString();
				if (c < results.getColumnCount()-1)
					lineData[r] += "_";
			}
		}
		// change the table data to lineData
		changeSearch(lineData, u_search); 
	}
	
	/**
	 * Driver method of quick sort. Sorts an array using quick sort.
	 * @param array - the array to be sorted
	 * @param low - the lowest point of the switch zone (usually index 0)
	 * @param high - the highest point of the switch zone (usually the last element of the array)
	 * @return the sorted array
	 */
	private String[] quickSort(String[] array, int low, int high) {
		String[] sorted = array;
		if (low < high) {
			int p = qs_Partition(sorted, low, high);
			quickSort(sorted, low, p-1);
			quickSort(sorted, p+1, high);
		}
		return sorted;
	}
	
	/**
	 * Submethod of quick sort. Determines the pivot; switches every element around it based on the comparison
	 * @param array - the array to be sorted
	 * @param low - the low point of the switch zone
	 * @param high - the high point of the switch zone
	 * @return - the next pivot
	 */
	private int qs_Partition(String[] array, int low, int high) {
		String pivotElement = array[high].split("_")[searchIndex];
		int i = (low - 1);
		for (int j = low; j < high; j++) {
			int compare = array[j].split("_")[searchIndex].compareTo(pivotElement);
			if (flipOrder ? compare >= 0 : compare <= 0) {
				i++; qs_Swap(array, i, j);
			}
		}
		qs_Swap(array, i+1, high);
		return i+1;
	}
	
	/**
	 * Submethod for quicksort. Swaps two elements in an array.
	 * @param array - the array to be sorted
	 * @param a - the index of the first element
	 * @param b - the index of the second element
	 */
	private void qs_Swap(String[] array, int a, int b) {
		String temp = array[a];
		array[a] = array[b];
		array[b] = temp;
	}
	
	/**
	 * System-prints the elements in an array.
	 * @param array - a String array
	 */
	private void printArray(String [] array) {
		for (String s : array) {
			System.out.println("Printing: " + s);
		}
	}
	
	/**
	 * Search method to search for the user's desired song and display relevant results
	 * @param text: what the user wants to search for
	 * @return String array of the search results
	 */
	public static String[] search(String text, DatagramSocket aSocket, int serverPort) {
		
		System.out.println("SearchMenuPanel call");
		
		String [] args = {text.toLowerCase(), "Song"}; // uses "Song" for the filter bc of how playlists are shown
		JSONObject obj = requestReply.UDPRequestReply("getSearch", args, aSocket, serverPort);
		// uses aSocket and serverPort from panel, which it got from its related Homepage
		String results = obj.get("result").toString(); 
		
		if (results.length() < 3) return null;
		
		results = results.substring(2, results.length() - 2).replace("\",\"", "\n");
		System.out.println("These are the results:\n" + results);
		
		return results.split("\n");
	}
	
	
	

	/**
	 * Method for grabbing the current user's playlists
	 * @return String array of all the user's playlists
	 */
	public String[] grabPlaylists() {
		// Reading from JSON
		JSONObject obj1;
		String pathname = username + ".json";
		try (InputStream input = new FileInputStream(pathname)) {
			obj1 = new JSONObject(new JSONTokener(input));

			try {// read playlists
				String playlist = obj1.get("playlists").toString();
				String[] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
				return playlistArray;
			} catch(StringIndexOutOfBoundsException e) {
				System.out.println("There are no available playlists for this user.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String [] none = {};
		return none;
	}

	// Action Listener for buttons
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		//If the user clicks play, play the selected song.
		// Display success message if successful
		//If no song is selected, display Error message
		if (source == this.playButton) {
			int row = this.results.getSelectedRow();
			if (row != -1) {
				new PlayButton.PlayFrame(songName, username).setVisible(true);
			} else {
				responseLabel.setText("No song is being selected");
				responseLabel.setForeground(Color.RED);
			}
		}
	}
	
	/**
	 * A set method for labels. Doing so allows for manipulation with the local label to affect the input label
	 * @param label: a JLabel, either title_, artist_, or album_ from Homepage.java
	 */
	public void setLabel(JLabel label) {
		switch (label.getName()) { 			// the three labels are named via label.setName(labelName)
		case "title": 		titleLabel = label;  break;
		case "artist(s)": 	artistLabel = label; break;
		case "album": 		albumLabel = label;  break;
		}
	}

	/**
	 * A get method. Returns the currently selected song.
	 * @return String - the title, artist(s), and album of the currently selected song
	 */
	public String getSong() {
		System.out.println("SearchMenuPanel " + this.getName() + " - song chosen: " + songName);
		return songName;
	}
}

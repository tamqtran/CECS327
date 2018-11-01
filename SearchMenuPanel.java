import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.*;
import java.awt.event.*;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.table.*;

@SuppressWarnings("serial")
public class SearchMenuPanel extends JPanel implements ActionListener, MouseListener {
	
	//Declaring variables

	private JTable results;

	private JScrollPane resultsJPS;

	private JButton playButton, addButton;

	private JLabel playlistLabel, responseLabel, errorLabel,
				   titleLabel, artistLabel, albumLabel;

	private final String[] searchTypes = {"By Title", "by Artist(s)", "by Album"};
	private JComboBox<String> searchFilter;
	private JComboBox<String> playList;

	private String username, songName, songTitle, artist, album, 
					searchType = "By Title"; //by default, the search filter is 'by title'

	/**
	 * Constructor for SearchMenuPanel
	 * @param username - username of the user
	 * @param search - string that contains the song artist name, album name, and song title name
	 * @param userSearch - the desired search from the user
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SearchMenuPanel(String username, String[] search, String userSearch) {
		this.username = username;

		// Custom Layout
		this.setLayout(null);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				
		//Response Label. Displays whether action is successful or not
		responseLabel = new JLabel("");
		responseLabel.setSize(new Dimension(320, 50));
		responseLabel.setLocation(130, 355);
		responseLabel.setName("response");
		this.add(responseLabel);

		// the search filter is here
		searchFilter = new JComboBox(searchTypes);
		searchFilter.setName("searchFilter");
		searchFilter.setEditable(false);
		searchFilter.setSize(new Dimension(110, 30));
		searchFilter.setSelectedIndex(0);
		searchFilter.setLocation(10,10);
		this.add(searchFilter);
		
		searchFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				searchType = searchFilter.getSelectedItem().toString(); //sets searchType to whichever option is selected.
				System.out.println(searchType);
			}
		});
		System.out.println(searchType);
		
//		// Testing. Dummy values to store in JTable
//		String data[][] = { {}, {}, {} };
		String[] columns = { "Song Title", "Artist", "Album" };
		DefaultTableModel model = null;
		if (search != null) {
			model = new DefaultTableModel(null, columns)
			 {
			    public boolean isCellEditable(int row, int column)
			    {
			      return false;//This causes all cells to be not editable
			    }
			  };
			for (int i = 0; i < search.length; i++) {
				   model.addRow(search[i].split("_"));
			}
		} else {
			errorLabel = new JLabel("No results found for '" + userSearch + "'");
			errorLabel.setSize(errorLabel.getPreferredSize());
			errorLabel.setLocation(130, 415);
			this.add(errorLabel);
		};
		
		// Creates a table to display the search results
		results = new JTable(model);
		results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsJPS = new JScrollPane(results);
		resultsJPS.setSize(new Dimension(400, 250));
		resultsJPS.setLocation(10, 50);
		resultsJPS.setName("resultsJPS");
		results.addMouseListener(this);
		
		// News JScrollPane else column names wont show up
		this.add(resultsJPS);

		// A dropdown menu for the user to select which playlist to add song to
		// pulls playlist from json file
		playList = new JComboBox(grabPlaylists());
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
							responseLabel.setForeground(Color.BLUE);
							System.out.println("Song '" + selectedSong + "' has been added to playlist '" + selectedPL + "'");
						} else {
							responseLabel.setText("Warning! This song already exists in that playlist");
							responseLabel.setForeground(Color.RED);
							System.out.println("Song '" + selectedSong + "' already exists in playlist '" + selectedPL + "'");
						}
						
						// Changes written to the json file
						 FileWriter fileWriter = new FileWriter(username+".json");
						 fileWriter.write(obj1.toString());
						 fileWriter.flush();
						 fileWriter.close();
							
					} catch (Exception c) {
						c.printStackTrace();
					}
				} else {
					responseLabel.setText("No song is being selected");
					responseLabel.setForeground(Color.RED);
				}
			}
		});
		this.add(addButton);
	}

	/**
	 * Search method to search for the user's desired song and display relevant results
	 * @param text: what the user wants to search for
	 * @return String array of the search results
	 */
	public static String[] search(String text) {
		// Desired Music extension
		final String EXT = ".wav";

		// System command to grab current working directory
		String currentFolderPath = System.getProperty("user.dir"); // C:\Users\Austin\Eclipse_OxyMain\MusicStreaming 
		File currentFolder = new File(currentFolderPath);

		// list() vs listFiles()
		// List- string array
		// listFiles - files array
		// Doing list() reduces unnecessary code
		String[] FilesInFolder = currentFolder.list();
		// Storage for results
		ArrayList<String> searchResults = new ArrayList<String>();

		for (int i = 0; i < FilesInFolder.length; i++) {
			if (FilesInFolder[i].endsWith(EXT) && FilesInFolder[i].toLowerCase().contains(text.toLowerCase()))
				searchResults.add(FilesInFolder[i].replace(EXT, ""));
		}

		return searchResults.toArray(new String[searchResults.size()]);
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

			// read playlists
			String playlist = obj1.get("playlists").toString();
			String[] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
			return playlistArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	public void setLabel(JLabel label) 
	{
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
	
	/**
	 * An overridden MouseEvent method that detects what song has been clicked on.
	 * @param e: a MouseEvent
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row = this.results.getSelectedRow();
		
		songTitle = results.getModel().getValueAt(row, 0).toString();
		artist = results.getModel().getValueAt(row, 1).toString();
		album = results.getModel().getValueAt(row, 2).toString();

		System.out.println("SearchMenuPanel "+ this.getName() + " -- '" + songTitle + "_" + artist + "_" + album + "' clicked");

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getClickCount() == 2) {
			// TODO Auto-generated method stub
			int row = this.results.getSelectedRow();
			
			songTitle = results.getModel().getValueAt(row, 0).toString();
			artist = results.getModel().getValueAt(row, 1).toString();
			album = results.getModel().getValueAt(row, 2).toString();
			
			//change text on labels in homepage
			titleLabel.setText(songTitle); titleLabel.setVisible(true);
			artistLabel.setText(artist); artistLabel.setVisible(true);
			albumLabel.setText(album);	albumLabel.setVisible(true);
			
			songName = songTitle + "_" + artist + "_" + album;	
			System.out.println("SearchMenuPanel "+ this.getName() + ": '" + songName + "' selected");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

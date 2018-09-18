import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.Document;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.table.*;

public class SearchMenuPanel extends JPanel implements DocumentListener, ActionListener {
	
	//Declaring variables
	private JTextField searchTextField;

	private JTable results;

	private JScrollPane resultsJPS;

	private JButton backButton, playButton, addButton;

	private JLabel playlistLabel, userLabel, responseLabel;

	private JComboBox playList;

	private String username;

	/**
	 * Constructor for SearchMenuPanel
	 * @param username: username of the user
	 */
	public SearchMenuPanel(String username) {
		this.username = username;

		// Custom Layout
		this.setLayout(null);

		//Displays Username on Panel
		userLabel = new JLabel("User: " + username);
		userLabel.setSize(new Dimension(100, 30));
		userLabel.setLocation(100, 8);
		this.add(userLabel);

		//Response Label. Displays whether action is successful or not
		responseLabel = new JLabel("");
		responseLabel.setSize(new Dimension(220, 50));
		responseLabel.setLocation(245, 460);
		this.add(responseLabel);

		// Back button. Tries to match the playlist page visually
		// Main functionality is to go back to Profile Frame
		backButton = new JButton("Back");
		backButton.setSize(backButton.getPreferredSize());
		backButton.setLocation(20, 10);
		backButton.addActionListener(this);
		this.add(backButton);

		// Play Button. Redirects user to PlaySong frame
		playButton = new JButton("Play Song");
		playButton.setSize(playButton.getPreferredSize());
		playButton.setLocation(270, 500);
		playButton.addActionListener(this);
		this.add(playButton);

		// Search textfield where user can type in to search for desired song
		searchTextField = new JTextField("Search for song title, album, or artist", 20);
		// Wipes textfield once you click it
		searchTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				searchTextField.setText("");

				// Clears the Jtable
				DefaultTableModel model = (DefaultTableModel) results.getModel();
				model.setRowCount(0);
			}
		});
		// Detects changes within the textfield
		searchTextField.getDocument().addDocumentListener(this);
		searchTextField.setSize(new Dimension(400, 20));
		searchTextField.setLocation(150, 50);
		this.add(searchTextField);

		// Testing. Dummy values to store in JTable
		String data[][] = { {}, {}, {} };
		String[] columns = { "Song Title", "Artist", "Album" };

		DefaultTableModel model = new DefaultTableModel(null, columns);

		// Creates a table to display the search results
		results = new JTable(model);
		results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsJPS = new JScrollPane(results);
		resultsJPS.setSize(new Dimension(400, 250));
		resultsJPS.setLocation(150, 100);
		// News JScrollPane else column names wont show up
		this.add(resultsJPS);

		// A dropdown menu for the user to select which playlist to add song to
		// pulls playlist from json file
		playList = new JComboBox(grabPlaylists());
		playList.setSize(new Dimension(110, 30));
		playList.setLocation(350, 380);
		this.add(playList);

		// Another display label
		playlistLabel = new JLabel("Select a playlist");
		playlistLabel.setSize(playlistLabel.getPreferredSize());
		playlistLabel.setLocation(255, 386);
		this.add(playlistLabel);

		// Add Button. Allows user to add a song to the selected playlist
		// Cannot add a song if song already exists in playlist
		// Cannot add if no song is selected
		addButton = new JButton("Add Song");
		addButton.setSize(addButton.getPreferredSize());
		addButton.setLocation(360, 425);
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
							responseLabel.setText("Success! Song was added to playlist");
							responseLabel.setForeground(Color.GREEN);
						} else {
							responseLabel.setText("Invalid! Song is alreadly in the playlist");
							responseLabel.setForeground(Color.RED);
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

	// Updates search results when user types additional characters
	public void insertUpdate(DocumentEvent e) {
		String[] searchResults = search(searchTextField.getText());

		// Clears the Jtable
		DefaultTableModel model = (DefaultTableModel) results.getModel();
		model.setRowCount(0);

		for (int i = 0; i < searchResults.length; i++) {
			model.addRow(searchResults[i].split("_"));
		}
		;

	}

	// Updates search results when user deletes additional characters
	// For example, backspacing
	public void removeUpdate(DocumentEvent e) {
		String[] searchResults = search(searchTextField.getText());

		// Clears the Jtable
		DefaultTableModel model = (DefaultTableModel) results.getModel();
		model.setRowCount(0);

		for (int i = 0; i < searchResults.length; i++) {
			model.addRow(searchResults[i].split("_"));
		};

	}

	public void changedUpdate(DocumentEvent e) {
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
		String currentFolderPath = System.getProperty("user.dir");
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
		;

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

		// If the user clicks back, take the user to Profile Frame
		if (source == this.backButton) {
			JFrame sFrame = (JFrame) this.getTopLevelAncestor();
			sFrame.dispose();
			new Profile(this.username).setVisible(true);
		}
		
		//If the user clicks play, play the selected song.
		// Display success message if successful
		//If no song is selected, display Error message
		if (source == this.playButton) {
			int row = this.results.getSelectedRow();
			if (row != -1) {
				String songTitle = results.getModel().getValueAt(row, 0).toString();
				String artist = results.getModel().getValueAt(row, 1).toString();
				String album = results.getModel().getValueAt(row, 2).toString();
				JFrame sFrame = (JFrame) this.getTopLevelAncestor();
				sFrame.dispose();
				new PlayButton.PlayFrame(songTitle + "_" + artist + "_" + album, username).setVisible(true);
			} else {
				responseLabel.setText("No song is being selected");
				responseLabel.setForeground(Color.RED);
			}
		}
	}
}

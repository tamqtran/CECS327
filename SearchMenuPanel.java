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
	private JTextField searchTextField;

	private JTable results;

	private JScrollPane resultsJPS;

	private JButton backButton, playButton, addButton;

	private JLabel playlistLabel, userLabel, responseLabel;

	private JComboBox playList;

	private String username;

	public SearchMenuPanel(String username) {
		this.username = username;

		// Custom Layout
		this.setLayout(null);

		userLabel = new JLabel("User: " + username);
		userLabel.setSize(new Dimension(100, 30));
		userLabel.setLocation(100, 8);
		this.add(userLabel);

		responseLabel = new JLabel("");
		responseLabel.setSize(new Dimension(220, 50));
		responseLabel.setLocation(245, 460);
		this.add(responseLabel);

		// Back button. Tries to match the playlist page visually
		backButton = new JButton("Back");
		backButton.setSize(backButton.getPreferredSize());
		backButton.setLocation(20, 10);
		backButton.addActionListener(this);
		this.add(backButton);

		playButton = new JButton("Play Song");
		playButton.setSize(playButton.getPreferredSize());
		playButton.setLocation(270, 500);
		playButton.addActionListener(this);
		this.add(playButton);

		searchTextField = new JTextField("Search for song title, album, or artist", 20);
		// Wipe textfield once you click it
		searchTextField.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				searchTextField.setText("");

				// Clears the Jtable
				DefaultTableModel model = (DefaultTableModel) results.getModel();
				model.setRowCount(0);
			}
		});

		searchTextField.getDocument().addDocumentListener(this);
		searchTextField.setSize(new Dimension(400, 20));
		searchTextField.setLocation(150, 50);
		this.add(searchTextField);

		// Testing. Dummy values to store in JTable
		String data[][] = { {}, {}, {} };
		String[] columns = { "Song Title", "Artist", "Album" };

		DefaultTableModel model = new DefaultTableModel(null, columns);

		results = new JTable(model);
		results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		resultsJPS = new JScrollPane(results);
		resultsJPS.setSize(new Dimension(400, 250));
		resultsJPS.setLocation(150, 100);
		// News JScrollPane else column names wont show up
		this.add(resultsJPS);

		playList = new JComboBox(grabPlaylists());
		playList.setSize(new Dimension(110, 30));
		playList.setLocation(350, 380);
		this.add(playList);

		playlistLabel = new JLabel("Select a playlist");
		playlistLabel.setSize(playlistLabel.getPreferredSize());
		playlistLabel.setLocation(255, 386);
		this.add(playlistLabel);

		addButton = new JButton("Add Song");
		addButton.setSize(addButton.getPreferredSize());
		addButton.setLocation(360, 425);
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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

						if (!songlistToAdd.toList().contains(selectedSong)) {
							songlistToAdd.put(selectedSong);
							responseLabel.setText("Success! Song was added to playlist");
							responseLabel.setForeground(Color.GREEN);
						} else {
							responseLabel.setText("Invalid! Song is alreadly in the playlist");
							responseLabel.setForeground(Color.RED);
						}
						
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

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == this.backButton) {
			JFrame sFrame = (JFrame) this.getTopLevelAncestor();
			sFrame.dispose();
			new Profile(this.username).setVisible(true);
		}
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

// This file has been created by Austin Tao on 10/2/2018.
// Inspired by Oracle's CustomDialog.java example.
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CreatePlaylistDialog extends JDialog implements ActionListener, PropertyChangeListener {

	private String typedText = null;
	private JTextField textField;
	private JOptionPane optionPane;
	private DefaultListModel dlm;
	
	private String specials = "[!@#$%&*()+=|<>?{}\\[\\]~-]";
	private String username_;
	private String button1 = "Enter", button2 = "Cancel";
	
	private JSONArray currentList;
	
	public CreatePlaylistDialog(Frame homeFrame, String user, DefaultListModel dm) {
		super(homeFrame, true);
		username_ = user;									// assign locally the user's username
		dlm = dm;											// assign locally the defaultlistmodel, still references dm (in Homepage)
		
		textField = new JTextField(15);								// initialize text field
		String msgString = "Name this new playlist:"; 				// initialize given message string
		
		Object[] array = {msgString, textField}; 					// initialize object arrays with objects and strings
		Object[] options = {button1, button2};
		
		optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE, 	// initialize optionPane using
				JOptionPane.YES_NO_OPTION, null, options, options[0]); 	// object arrays
		
		setContentPane(optionPane); 									// set the content pane to optionPane
		setTitle("Playlist Creation"); 									// set the title of the pane
				
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); 					// handles when the window actually closes
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
		}	});
		
		addComponentListener(new ComponentAdapter() {					// handles the focus to the text field on initialization
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
		}	});
		
		textField.addActionListener(this);							// assigns the action listener defined in this class to the text field
		optionPane.addPropertyChangeListener(this);				// assigns the property change listener defined in this class to the option pane
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		String prop = e.getPropertyName();
		if (isVisible() && (e.getSource() == optionPane) && ( JOptionPane.VALUE_PROPERTY.equals(prop) || 
				 JOptionPane.INPUT_VALUE_PROPERTY.equals(prop) )) { 					// if the pane's property was changed in any way...
			Object value = optionPane.getValue();										// ...then find out what the user did
			
			if (value == JOptionPane.UNINITIALIZED_VALUE) return;						// this case means the user hasn't done anything yet
			
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE); 						// reset of value; allows for repeatable tries
			
			if (button1.equals(value)) {												// if the user hit the enter button...
				typedText = textField.getText();										// ... get what the user said
				if (!codeDenial(typedText)) {											// ... and run checks (safety check)
					textField.selectAll();
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, "This (" + typedText +
							") has unallowed special characters", "Try again", JOptionPane.ERROR_MESSAGE);
					System.out.println("Special characters found. Attempting again..."); 	// this will proc if there are special characters 
					typedText = null;													//  found in typedText(refer to 'specials' for full list)
					textField.requestFocusInWindow();										// resets typedText, re-focuses on text field
				} else if (checkPlaylistNameUniqueness(typedText, username_)) {
					addPlaylist(typedText, username_);					// this will proc if typedText is unique to the user's list
					getPlaylists(dlm);											// the playlist is added to the user's json file, 
					System.out.println("The new playlist --" +					// then the gui updates with this change
					typedText + "-- has been created. Returning...");
					typedText = null;										// typedText is nulled out
					clearAndHide();											// the window is cleared and hidden away (not deleted)
				} else {
					textField.selectAll();							// this will proc if typedText matches an existing playlist name
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, 
						"This ("+ typedText + ") is not unique", "Try again", JOptionPane.ERROR_MESSAGE);
					System.out.println("This ("+ typedText + ") is not unique");
					typedText = null;												// typedText is reset, re-focuses on text field
					textField.requestFocusInWindow();
			}	} else { 																	// procs if the window is exited out
				clearAndHide();															// clears out the dialog and hides itself
				System.out.println("Creation subwindow has been hidden away...");
		}	}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		optionPane.setValue(button1);		// this sets the action of the text field to the enter button
	}
	
	/** ORIGIN: Profile.java
	 * Add playlist to JSON FILE
	 * @param playlist playlist to be added
	 * @param username current login user
	 */
	 void addPlaylist(String playlist, String username) {
		try (InputStream input = new FileInputStream(username+".json")) {
		    JSONObject obj1 = new JSONObject(new JSONTokener(input));
		    
		    currentList = obj1.getJSONArray("playlists");
		    
		    currentList.put(playlist);
		    obj1.put(playlist, new JSONArray()); //empty song list for this array
		    
		    FileWriter fileWriter = new FileWriter(username+".json");
			fileWriter.write(obj1.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	 /**
	  * This method checks the user's json file for their playlists' names against the variable 'playlist'.
	  * This method returns true if none of the playlist names match 'playlist'.
	  * @param playlist the name of the new playlist
	  * @param username the username of the account this playlist name is to go into
	  * @return boolean
	  */
	 private boolean checkPlaylistNameUniqueness(String playlist, String username) {
		// define name against all other playlist names in the user's account
		// true if name is unique, false otherwise
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			currentList = obj1.getJSONArray("playlists");
			if (!currentList.toList().contains(playlist)) return true;
		} catch (Exception e) {
			e.printStackTrace();
		} return false;
	}
	
	public void clearAndHide() {
		textField.setText(null);
		setVisible(false);
	}
	
	/** ORIGIN: Profile.java
	 * Read playlists array from json file and add to gui list
	 * @param dm defaultlistModel
	 */
	void getPlaylists(DefaultListModel dm) {
		dm.clear(); //clear list 
		JSONObject obj1;
		String pathname = username_ + ".json";
		try (InputStream input = new FileInputStream(pathname)) {
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    String playlist = obj1.get("playlists").toString();		    
		    String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
		    //add playlist to default list
		    for(int i = 0; i < playlistArray.length; i++) dm.addElement(playlistArray[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** ORIGIN: Login.java
	 * A boolean method that abhors code injections.
	 * @param input: A string, either username or password
	 * @return false if any special characters are found in input; true otherwise
	 */
	private boolean codeDenial (String input) {
		//denies if input has the following: "[!@#$%&*()+=|<>?{}\\[\\]~-]" (code injection prevention)
		for (char c : input.toCharArray()) {
			for (char s : specials.toCharArray())
				if (s == c) return false;
		} return true;
	}
}

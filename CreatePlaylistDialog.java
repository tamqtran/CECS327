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
		username_ = user;
		dlm = dm;
		
		textField = new JTextField(15);
		String msgString = "Name this new playlist:";
		
		Object[] array = {msgString, textField};
		Object[] options = {button1, button2};
		
		optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE, 
				JOptionPane.YES_NO_OPTION, null, options, options[0]);
		
		setContentPane(optionPane);
		
		setTitle("Playlist Creation");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
			}
		});
		
		textField.addActionListener(this);
		optionPane.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		String prop = e.getPropertyName();
		if (isVisible() && (e.getSource() == optionPane) && 
				(JOptionPane.VALUE_PROPERTY.equals(prop) || 
				 JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();
			
			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				return;
			}
			
			if (button1.equals(value)) {
				typedText = textField.getText();
				if (!codeDenial(typedText)) {
					textField.selectAll();
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, "This (" + typedText +
							") has unallowed special characters", "Try again", JOptionPane.ERROR_MESSAGE);
					System.out.println("");
					typedText = null;
					textField.requestFocusInWindow();
				} else if (checkPlaylistNameUniqueness(typedText, username_)) {
					addPlaylist(typedText, username_); 
					getPlaylists(dlm);
					System.out.println("The new playlist --" + typedText + "-- has been created. Returning...");
					typedText = null;
					clearAndHide();
				} else {
					textField.selectAll();
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, 
						"This ("+ typedText + ") is not unique", "Try again", JOptionPane.ERROR_MESSAGE);
					System.out.println("This ("+ typedText + ") is not unique"); 
					typedText = null;
					textField.requestFocusInWindow();
					
				}
			} else {
				typedText = null;
				clearAndHide();
				System.out.println("Creation subwindow has been hidden away...");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		optionPane.setValue(button1);
	}
	
	/**
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
	
	private boolean checkPlaylistNameUniqueness(String playlist, String username) {
		// define name against all other playlist names in the user's account
		// true if name is unique, false otherwise
		try (InputStream input = new FileInputStream(username+".json")) {
			JSONObject obj1 = new JSONObject(new JSONTokener(input));
			currentList = obj1.getJSONArray("playlists");
			if (!currentList.toList().contains(playlist)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void clearAndHide() {
		textField.setText(null);
		setVisible(false);
	}
	
	/**
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
		    
		    System.out.println(playlist);
		    
		    String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
		    //add playlist to default list
		    for(int i = 0; i < playlistArray.length; i++)
		    	dm.addElement(playlistArray[i]);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
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

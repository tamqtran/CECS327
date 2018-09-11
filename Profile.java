import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Profile {

	String username = "allan";
	private JFrame frame;
	private JTextField txtSearchMyPlaylists;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profile window = new Profile("allan");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Profile(String user){
		username = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("'MusicService' Profile");
		frame.setBounds(100, 100, 659, 427);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//MODEL
		DefaultListModel dm = new DefaultListModel();
		
		JList list_1 = new JList();
		list_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		list_1.setBounds(339, 81, 274, 279);
		frame.getContentPane().add(list_1);
		
		//BUTTONS
		JButton btnAddPlaylist = new JButton("Add");
		btnAddPlaylist.setBounds(221, 221, 97, 33);
		frame.getContentPane().add(btnAddPlaylist);
		
		JButton btnRemovePlaylist = new JButton("Remove");
		btnRemovePlaylist.setBounds(221, 309, 97, 33);
		frame.getContentPane().add(btnRemovePlaylist);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 11, 79, 23);
		frame.getContentPane().add(btnLogout);
		
		JButton btnExplorePlaylist = new JButton("Explore");
		btnExplorePlaylist.setBounds(221, 265, 97, 33);
		frame.getContentPane().add(btnExplorePlaylist);
		
		txtSearchMyPlaylists = new JTextField();
		
		
		txtSearchMyPlaylists.setText("Search my playlists");
		txtSearchMyPlaylists.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtSearchMyPlaylists.setBounds(339, 47, 274, 23);
		frame.getContentPane().add(txtSearchMyPlaylists);
		txtSearchMyPlaylists.setColumns(10);
		
		JLabel lblName = new JLabel("");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName.setBounds(25, 67, 293, 33);
		frame.getContentPane().add(lblName);
		lblName.setText("Name: "+getName(username));
		
		JLabel lblMyPlaylists = new JLabel("My Playlists");
		lblMyPlaylists.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblMyPlaylists.setBounds(339, 15, 129, 19);
		frame.getContentPane().add(lblMyPlaylists);
		
		//Playlist FROM json file
		list_1.setModel(dm);
		getPlaylists(dm);
		
		
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
		        new Login().setVisible(true); 
			}
		});
		
		btnExplorePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list_1.getSelectedIndex()==-1) {
					txtSearchMyPlaylists.setText("Select a playlist to explore");
					txtSearchMyPlaylists.setForeground(Color.RED);;
				}else {
					frame.dispose();
					new PlaylistFrame(username,list_1.getSelectedValue().toString()).setVisible(true); 
				}
			}
		});
		
		btnAddPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtSearchMyPlaylists.getText().equals("Search my playlists") || txtSearchMyPlaylists.getText().equals("Select a playlist to explore") || txtSearchMyPlaylists.getText().equals("Select a playlist to remove") || txtSearchMyPlaylists.getText().equals("Enter a name for the new playlist")) {
					txtSearchMyPlaylists.setText("Enter a name for the new playlist");
					txtSearchMyPlaylists.setForeground(Color.RED);;
				}else if(!dm.contains(txtSearchMyPlaylists.getText())){
					addPlaylist(txtSearchMyPlaylists.getText(), username); //add to playlist to json
					getPlaylists(dm); //update list gui
				}
				
			}
		});
		
		btnRemovePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(list_1.getSelectedIndex()!=-1) {
					removePlaylist(list_1.getSelectedValue().toString(), username); //add to playlist to json
					getPlaylists(dm); //update list gui
				}
				else {
					txtSearchMyPlaylists.setText("Select a playlist to remove");
					txtSearchMyPlaylists.setForeground(Color.RED);;
				}
			}
		});
		
		txtSearchMyPlaylists.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//enter pressed for search playlist
				if(dm.contains(txtSearchMyPlaylists.getText())) { 
					dm.clear();
					dm.addElement(txtSearchMyPlaylists.getText());
				}else {
					dm.clear();
				}
				
			}
		});
		txtSearchMyPlaylists.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtSearchMyPlaylists.setText("");
				txtSearchMyPlaylists.setForeground(Color.BLACK);
				getPlaylists(dm);
			}
		});
		txtSearchMyPlaylists.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtSearchMyPlaylists.getText().equals("")) {
					txtSearchMyPlaylists.setText("Search my playlists");
				}
			}
		});
		txtSearchMyPlaylists.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(txtSearchMyPlaylists.getText().equals(""))
					getPlaylists(dm);
			}
		});
	}
	
	/**
	 * Read playlists array from json file and add to gui list
	 * @param dm defaultlistModel
	 */
	void getPlaylists(DefaultListModel dm) {
		dm.clear(); //clear list 
		JSONObject obj1;
		String pathname = "allan.json";
		try (InputStream input = new FileInputStream(pathname)) {
			obj1 = new JSONObject(new JSONTokener(input));
		    //read playlists
		    String playlist = obj1.get("playlists").toString();
		    String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
		    //add playlist to default list
		    for(int i = 0; i < playlistArray.length; i++)
		    	dm.addElement(playlistArray[i]);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove playlist from JSON FiLE
	 * @param playlist playlist to be removed
	 * @param username current login user
	 */
	 void removePlaylist(String playlist, String username) {
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
	}
	
	/**
	 * Add playlist to JSON FiLE
	 * @param playlist playlist to be added
	 * @param username current login user
	 */
	 void addPlaylist(String playlist, String username) {
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
	}
	 /**
	  * Get current login user's name
	  * @param username 
	 * @return 
	  */
	 String getName(String username) {
		 String name = null;
		 try (InputStream input = new FileInputStream(username+".json")) {
			    JSONObject obj1 = new JSONObject(new JSONTokener(input));
			    name = obj1.get("name").toString();
		 }catch (Exception e) {
				e.printStackTrace();
			}
		 return name;
	 }

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		frame.setVisible(b);
	}
	
}

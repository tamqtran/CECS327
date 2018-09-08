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

	private JFrame frame;
	private JTextField txtSearchMyPlaylists;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Profile window = new Profile();
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
	public Profile() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
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
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(221, 265, 97, 33);
		frame.getContentPane().add(btnEdit);
		
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
		lblName.setText("Name: ");
		
		JLabel lblMyPlaylists = new JLabel("My Playlists");
		lblMyPlaylists.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblMyPlaylists.setBounds(339, 15, 129, 19);
		frame.getContentPane().add(lblMyPlaylists);
		
		//Playlist FROM json file
		list_1.setModel(dm);
		getPlaylists(dm);
		
		btnAddPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txtSearchMyPlaylists.getText().equals("Search my playlists")) {
					txtSearchMyPlaylists.setText("Enter name of the new playlist");
					txtSearchMyPlaylists.setForeground(Color.RED);;
				}else if(!dm.contains(txtSearchMyPlaylists.getText())){
					dm.addElement(txtSearchMyPlaylists.getText()); //add to playlist
					//add to playlist to json
					
				}
				
			}
		});
		
		btnRemovePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.removeElementAt(list_1.getSelectedIndex());
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
	 * Add new songs with 
	 * @param dm defaultlistModel
	 */
	void addPlaylist(DefaultListModel dm) {
		
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
}

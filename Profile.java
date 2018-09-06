import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class Profile {

	private JFrame frame;

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
		list_1.setBounds(328, 29, 285, 325);
		frame.getContentPane().add(list_1);
		
		//BUTTONS
		JButton btnAddPlaylist = new JButton("Add Playlist");
		btnAddPlaylist.setBounds(211, 249, 107, 40);
		frame.getContentPane().add(btnAddPlaylist);
		
		JButton btnRemovePlaylist = new JButton("Remove Playlist");
		btnRemovePlaylist.setBounds(211, 314, 107, 40);
		frame.getContentPane().add(btnRemovePlaylist);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.setBounds(10, 11, 89, 23);
		frame.getContentPane().add(btnLogout);
		
		//Panel name from json
		JLabel label = new JLabel("");
		label.setBounds(10, 45, 303, 33);
		frame.getContentPane().add(label);
		label.setText("Nick");
		//Playlist List FROM json
		list_1.setModel(dm);
		dm.addElement("Playlist1");
		dm.addElement("Playlist2");
		dm.addElement("Playlist3");
		btnAddPlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Moveto playlist panel
				list_1.setModel(dm);
				dm.addElement("Happy");
			}
		});
		
		btnRemovePlaylist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dm.removeElementAt(list_1.getSelectedIndex());
			}
		});
		
	}
}

/*
 * This file was created by Austin Tao on 9/20/2018.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Homepage {
	private String 			userName;
	private JFrame 			frame;
	protected JTextField 	searchField;
	private JPanel 			HIGH_panel, 
							Playlist_Panel, 
							 PlaylistTitle,
							 UserSavedPanel, 
							 PlaylistOptions,
							Explore_Panel, 
							 TopPanel, _SearchPanel, _HistoryPanel, _ProfilePanel,
							 ShiftingPanel,
				  			LOW_panel, 
				   			 Description_Panel, 
				   			 Song_Panel, SongOptions;
	private JButton 		createPlaylist_, removePlaylist_, 
							searchQuery_, logout_, 
							previousSong_, playPause_, nextSong_; 
	private JLabel 			playlist_, username_, 
							title_, artist_, album_;
	
	
	private boolean isSongPlaying = false; //starts false
	
	public static void main(String[] args) {
		new Homepage("allan");
	}
	
	public Homepage(String user) {
		userName = user;
		frame = new JFrame("MusicService -- " + userName);
		frame.setMinimumSize(new Dimension(675,400));
		frame.setSize(new Dimension(1000,600));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentsToHome(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}
	
	public void addComponentsToHome(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		
		HIGH_panel = new JPanel();
		HIGH_panel.setLayout(new BoxLayout(HIGH_panel, BoxLayout.X_AXIS));
		
		Playlist_Panel = new JPanel();
		Playlist_Panel.setLayout(new BoxLayout(Playlist_Panel, BoxLayout.Y_AXIS));
		Playlist_Panel.setMaximumSize(new Dimension(150,500));
		
		PlaylistTitle = new JPanel();
		PlaylistTitle.add(Box.createRigidArea(new Dimension(5,10)));
		playlist_ = new JLabel("Your Playlists");
		PlaylistTitle.add(playlist_);
		
//		inspiration for borders/box.glue: http://www.java2s.com/Code/Java/Swing-JFC/BoxLayoutGlueSample.htm
		
		UserSavedPanel = new JPanel();
		// the JList goes here //jscrollpane?
		
		PlaylistOptions = new JPanel();
		PlaylistOptions.setLayout(new BoxLayout(PlaylistOptions, BoxLayout.X_AXIS));

		createPlaylist_ = new JButton("Create");
		createPlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.print("Open subwindow displaying instructions for name of new playlist...");
			}
		});
		removePlaylist_ = new JButton("Remove");
		removePlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Displaying options for which one to delete...");	
			}
		});
		
		PlaylistOptions.add(Box.createRigidArea(new Dimension(10,10)));
		PlaylistOptions.add(createPlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,5)));
		PlaylistOptions.add(removePlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(10,10)));
		
		Playlist_Panel.add(PlaylistTitle);
		Playlist_Panel.add(UserSavedPanel);
		Playlist_Panel.add(PlaylistOptions);
		
		Playlist_Panel.setBorder(BorderFactory.createLineBorder(Color.RED));
		
		HIGH_panel.add(Playlist_Panel);
		
//		large song album cover (e_CoverPanel) can go here later
		
//		Explore_Panel
		Explore_Panel = new JPanel();
		Explore_Panel.setLayout(new BoxLayout(Explore_Panel, BoxLayout.Y_AXIS));
		
		TopPanel = new JPanel();
//		TopPanel.setLayout(new BorderLayout());
		TopPanel.setLayout(new BoxLayout(TopPanel, BoxLayout.X_AXIS));
		
		
		_SearchPanel = new JPanel();
		_SearchPanel.setLayout(new FlowLayout());
		searchField = new JTextField();
		searchField.setText("Search for...	");
		searchField.setSize(searchField.getPreferredSize()); 
		searchQuery_ = new JButton("Search");
		
		_SearchPanel.add(searchField);
		_SearchPanel.add(searchQuery_);
		_SearchPanel.setMaximumSize(new Dimension(100,40));
		_SearchPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		
//		_HistoryPanel (worry about this later)
		_HistoryPanel = new JPanel();
		
		_ProfilePanel = new JPanel();
		_ProfilePanel.setLayout(new FlowLayout());
		username_ = new JLabel("User: " + userName);
		logout_ = new JButton("Logout");
		logout_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
		        new Login().setVisible(true); 
			}
		});
		
		_ProfilePanel.add(username_);
		_ProfilePanel.add(logout_);
		_ProfilePanel.setMaximumSize(new Dimension(100,40));
		_ProfilePanel.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
		
		TopPanel.add(_SearchPanel);
		TopPanel.add(Box.createHorizontalGlue());
		TopPanel.add(_ProfilePanel);
		
		TopPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
//		ShiftingPanel (the one that keeps changing) //jscrollpane?
		ShiftingPanel = new JPanel();
		
		ShiftingPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		
		Explore_Panel.add(TopPanel);
		Explore_Panel.add(ShiftingPanel);
		
		HIGH_panel.add(Explore_Panel);
		
//		---------------------------
		
		LOW_panel = new JPanel();
		LOW_panel.setLayout(new BoxLayout(LOW_panel, BoxLayout.X_AXIS));
		LOW_panel.setPreferredSize(new Dimension(20,20)); //need to change this to keep structure dynamically
		LOW_panel.add(Box.createHorizontalStrut(12));
		
		// small song album cover (m_CoverPanel) can go here later
		
		Description_Panel = new JPanel();
		Description_Panel.setLayout(new BoxLayout(Description_Panel, BoxLayout.Y_AXIS));
		title_ = new JLabel("Title");
		title_.setSize(title_.getPreferredSize());
		Description_Panel.add(title_);
		artist_ = new JLabel("Artist(s)"); 
		artist_.setSize(artist_.getPreferredSize());
		Description_Panel.add(artist_);
		album_ = new JLabel("Album"); 
		album_.setSize(album_.getPreferredSize()); 
		Description_Panel.add(album_);
						
		Song_Panel = new JPanel();
		Song_Panel.setLayout(new BoxLayout(Song_Panel, BoxLayout.Y_AXIS));
		
		// song progress bar goes here
		
		SongOptions = new JPanel();
		SongOptions.setLayout(new FlowLayout());
		previousSong_ = new JButton("Previous Song");
		previousSong_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Moving to previous song...");
				// do things here
			}
		});
		playPause_ = new JButton("Paused");
		playPause_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				// initializes as unclickable 
				// 'if there is a song chosen,' it will unlock and play automatically
				isSongPlaying = !isSongPlaying;
				if (isSongPlaying) { 
					playPause_.setText("Playing");
					System.out.println("Song is playing");
					// do things here
				} else {
					playPause_.setText("Paused");
					System.out.println("Song is paused");
					// do things here
				}
			}
		});
		nextSong_ = new JButton("Next Song");
		nextSong_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Moving to next song...");
				//do things here
			}
		});
	
		SongOptions.add(previousSong_);
		SongOptions.add(playPause_);
		SongOptions.add(nextSong_);
		
		Song_Panel.add(SongOptions);
		
		// song current time/ total time goes here
		
		LOW_panel.add(Description_Panel);
		LOW_panel.add(Song_Panel);
		LOW_panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));		
//		------------------------------------
		
		pane.add(HIGH_panel);
		pane.add(LOW_panel);	
	}

}

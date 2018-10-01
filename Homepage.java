/*
 * This file was created by Austin Tao on 9/20/2018.
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class Homepage {
	
	private String specials = "[!@#$%&*()_+=|<>?{}\\[\\]~-]";
	
	private String 				userName;
	private JFrame 				frame;
	protected JTextField 		searchField;
	private JPanel 				HIGH_panel, 
								Playlist_Panel, 
								 PlaylistTitle, PlaylistOptions,
								Explore_Panel, 
								 TopPanel, _SearchPanel, _HistoryPanel, _ProfilePanel,
				  				LOW_panel, 
				   				 Description_Panel, 
				   				 Song_Panel, _SongBar, _SongButtons;
	private JButton 			createPlaylist_, removePlaylist_, 
								searchQuery_, logout_, 
								previousSong_, playPause_, nextSong_; 
	private JLabel 				playlist_, username_, 
								title_, artist_, album_,
								currentTime_, songTime_;
	@SuppressWarnings("rawtypes")	private DefaultListModel 	dm;
	@SuppressWarnings("rawtypes")	private JList				playlist_List;
	private JScrollPane			UserSavedPanel, ShiftingPanel;
	private JSlider				timedSlider;
	
	private boolean isSongPlaying = false, //starts false
					isThereASong = false;
	
	public static void main(String[] args) {
		new Homepage("allan");
	}
	
	public Homepage(String user) {
		userName = user;
		initialize();
	}
	
	private void initialize() {
		frame = new JFrame("MusicService -- " + userName);
		frame.setMinimumSize(new Dimension(675,400));		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addHighComponentsToHome(frame.getContentPane());
		addLowComponentsToHome(frame.getContentPane());
		
		frame.pack(); 
		
		frame.setSize(new Dimension(1000,600)); 
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addHighComponentsToHome(Container pane) {
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(Box.createRigidArea(new Dimension(1,1)));
		
		HIGH_panel = new JPanel();
		HIGH_panel.setLayout(new BoxLayout(HIGH_panel, BoxLayout.X_AXIS));
		
		Playlist_Panel = new JPanel();
		Playlist_Panel.setLayout(new BoxLayout(Playlist_Panel, BoxLayout.Y_AXIS));
		Playlist_Panel.setMaximumSize(new Dimension(200,10000));
		
		PlaylistTitle = new JPanel();
		PlaylistTitle.setMaximumSize(new Dimension(100,10));
		playlist_ = new JLabel("My Playlists"); playlist_.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PlaylistTitle.add(playlist_);
		
//		inspiration for borders/box.glue: http://www.java2s.com/Code/Java/Swing-JFC/BoxLayoutGlueSample.htm
		
		UserSavedPanel = new JScrollPane();
		dm = new DefaultListModel();
		playlist_List = new JList();
		playlist_List.setModel(dm);
//		getPlaylist(dm);
		UserSavedPanel.add(playlist_List);
	
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
		
		PlaylistOptions.add(Box.createRigidArea(new Dimension(6,6)));
		PlaylistOptions.add(createPlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,5)));
		PlaylistOptions.add(removePlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(10,10)));
		
		Playlist_Panel.add(Box.createRigidArea(new Dimension(3,3)));
		Playlist_Panel.add(PlaylistTitle);
		Playlist_Panel.add(Box.createRigidArea(new Dimension(4,4)));
		Playlist_Panel.add(UserSavedPanel);
		Playlist_Panel.add(Box.createRigidArea(new Dimension(3,3)));
		Playlist_Panel.add(PlaylistOptions);
		Playlist_Panel.add(Box.createRigidArea(new Dimension(3,3)));
		
		Playlist_Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
//		large song album cover (e_CoverPanel) can go here later
		
//		Explore_Panel
		Explore_Panel = new JPanel();
		Explore_Panel.setLayout(new BoxLayout(Explore_Panel, BoxLayout.Y_AXIS));
		
		TopPanel = new JPanel();
		TopPanel.setLayout(new BoxLayout(TopPanel, BoxLayout.X_AXIS));
		
		_SearchPanel = new JPanel();
		_SearchPanel.setLayout(new FlowLayout());
		searchField = new JTextField();
		searchField.setText("Search for...	");
		searchField.setColumns(15);
		searchQuery_ = new JButton("Search");
		
		
		//CONTAINS CODE THAT IS DEBUG ONLY; DOES NOT REFLECT FINAL PRODUCT
		searchQuery_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				//DEBUG ONLY; DOES NOT REFLECT FINAL PRODUCT
				isThereASong = !isThereASong; 								//
				System.out.println("is there a song? " + isThereASong);		//
				if (isThereASong) {											//
					previousSong_.setText("\u2758" + "\u23F4");				//
					playPause_.setText("\u2758" + "\u2758");				//
					nextSong_.setText("\u23F5" + "\u2758");					//
				} else {													//
					previousSong_.setText("\u274C");						//
					playPause_.setText("\u274C");							//
					nextSong_.setText("\u274C");							//
				}															//
				previousSong_.setEnabled(isThereASong);						//
				playPause_.setEnabled(isThereASong);						//
				nextSong_.setEnabled(isThereASong);							//
				currentTime_.setVisible(isThereASong);						//
				timedSlider.setEnabled(isThereASong);						//
				songTime_.setVisible(isThereASong);							//
				//DEBUG ONLY; DOES NOT REFLECT FINAL PRODUCT
			}
		});
		//CONTAINS CODE THAT IS DEBUG ONLY; DOES NOT REFLECT FINAL PRODUCT
		
		_SearchPanel.add(searchField);
		_SearchPanel.add(searchQuery_);
		_SearchPanel.setMaximumSize(new Dimension(200,40));
		
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
		        System.out.println("Logging out...");
			}
		});
		
		_ProfilePanel.add(username_);
		_ProfilePanel.add(logout_);
		_ProfilePanel.setMaximumSize(new Dimension(100,40));
		
		TopPanel.add(_SearchPanel);
//		a horizontal box, then _HistoryPanel would go here
		TopPanel.add(Box.createHorizontalGlue());
		TopPanel.add(_ProfilePanel);
		TopPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
//		ShiftingPanel (the one that keeps changing)
		ShiftingPanel = new JScrollPane();
		ShiftingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
//		Explore_Panel.add(Box.createHorizontalStrut(1)); 
		Explore_Panel.add(TopPanel); Explore_Panel.add(ShiftingPanel);
		
		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1)));
		HIGH_panel.add(Playlist_Panel);
		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1)));
		HIGH_panel.add(Explore_Panel);
		
		pane.add(HIGH_panel);
	}
	
	private void addLowComponentsToHome(Container pane) {
		LOW_panel = new JPanel();
		LOW_panel.setLayout(new BoxLayout(LOW_panel, BoxLayout.X_AXIS));
		LOW_panel.setMaximumSize(new Dimension(15000,100));
		LOW_panel.add(Box.createHorizontalStrut(4));
		
		// small song album cover (m_CoverPanel) can go here later
		
		Description_Panel = new JPanel();
		Description_Panel.setLayout(new BoxLayout(Description_Panel, BoxLayout.Y_AXIS));
		title_ = new JLabel(); title_.setMaximumSize(new Dimension(150,20)); title_.setText("Title: song title song title song title song title song title");
		artist_ = new JLabel(); artist_.setMaximumSize(new Dimension(150,20)); artist_.setText("Artist(s): list of artists");
		album_ = new JLabel(); album_.setMaximumSize(new Dimension(150,20));  album_.setText("Album: song album");
		
//		new Dimension(10,10)

		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		Description_Panel.add(title_);
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		Description_Panel.add(artist_);
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		Description_Panel.add(album_);
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		
		Description_Panel.setMinimumSize(new Dimension(175,200));
		Description_Panel.setPreferredSize(new Dimension(175,60));
		Description_Panel.setMaximumSize(new Dimension(175,200));						
		
		Song_Panel = new JPanel();
		Song_Panel.setLayout(new BoxLayout(Song_Panel, BoxLayout.Y_AXIS));
		
//		SongBar goes here
		_SongBar = new JPanel();
		_SongBar.setLayout(new FlowLayout());
		currentTime_ = new JLabel("current");
		timedSlider = new JSlider(0,100,0);
		// un-movable on initialization, movable when there is a song 'queued' (is that the word for it?)
		songTime_ = new JLabel("total");
		
		currentTime_.setVisible(isThereASong);
		timedSlider.setEnabled(isThereASong);
		songTime_.setVisible(isThereASong);
		
		_SongBar.add(currentTime_);
		_SongBar.add(timedSlider);
		_SongBar.add(songTime_);
							
		_SongButtons = new JPanel();
		_SongButtons.setLayout(new FlowLayout());
		
		previousSong_ = new JButton("\u2758" + "\u23F4");
		previousSong_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Moving to previous song...");
				// do things here
				
				playPause_.setText("\u2758" + "\u2758");
				isSongPlaying = true;		// the song is playing when it's all over
			}
		});
		playPause_ = new JButton("\u274C");
		playPause_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				isSongPlaying = !isSongPlaying;
				if (isSongPlaying) {
					System.out.println("Song is playing");
					// do things here
					
					playPause_.setText("\u2758" + "\u2758");		
				} else {
					System.out.println("Song is paused");
					// do things here
					
					playPause_.setText("\u25B6");
				}
			}
		});
		nextSong_ = new JButton("\u23F5" + "\u2758");
		nextSong_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {			
				System.out.println("Moving to next song...");
				//do things here
				
				playPause_.setText("\u2758" + "\u2758");
				isSongPlaying = true;		// the song is playing when it's all over			
			}
		});
	
		// all three buttons initialize as un-clickables
		previousSong_.setEnabled(isThereASong);
		playPause_.setEnabled(isThereASong);
		nextSong_.setEnabled(isThereASong);
		
		// if there is a song, it will unlock and play automatically
		_SongButtons.add(previousSong_); 	_SongButtons.add(playPause_); 	_SongButtons.add(nextSong_);
		
		Song_Panel.add(_SongBar);
		Song_Panel.add(_SongButtons);
		
//		Song_Panel.setBorder(BorderFactory.createLineBorder(Color.RED));
				
		LOW_panel.add(Box.createRigidArea(new Dimension(1,1)));
		LOW_panel.add(Description_Panel);
		LOW_panel.add(Box.createRigidArea(new Dimension(2,2)));
		LOW_panel.add(Song_Panel);
		LOW_panel.add(Box.createRigidArea(new Dimension(1,1)));
		
		LOW_panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));				
		
		pane.add(LOW_panel);
	}

	
	
	/**
	 * A boolean method that abhors code injections.
	 * @param input: A string, either username or password
	 * @return false if any special characters are found in input; true otherwise
	 */
	private boolean codeDenial (String input) {
		//denies if input has the following: "[!@#$%&*()_+=|<>?{}\\[\\]~-]" (code injection prevention)
		for (char c : input.toCharArray()) {
			for (char s : specials.toCharArray())
				if (s == c) return false;
		} return true;
	}
	
	/**
	 * A void method that can make a frame visible.
	 * @param b boolean (true/false) that determines visibility of the frame (where true makes it visible, and false makes it not visible
	 */
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		frame.setVisible(b);
	}
}

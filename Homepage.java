import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * The Homepage class creates a frame that houses the main piece of this music streaming application.
 * @author Austin Tao
 * @since 9/20/2018
 */
public class Homepage 
{

	
	private volatile static Clip current;
	private int					songIndex;
	private ArrayList<String> 	songList = new ArrayList<String>();
	
	private String 				userName, playlist;
	
	private final String 		specials = "[!@#$%&*()+=|<>?{}\\[\\]~-]", 
								SHIFT_PANEL = "Shifting", 
								SEARCH_PANEL = "Search",
								homeCard;
	private final Font			Tahoma_Plain_FourZero = new Font("Tahoma", Font.PLAIN, 40);
	
	private JFrame 				frame;
	
	private JPanel 				HIGH_panel, 
								Playlist_Panel, 
								 PlaylistTitle, PlaylistOptions,
								Explore_Panel, 
								 TopPanel, _SearchPanel, _HistoryPanel, _ProfilePanel,
								 HomePanel, CorePanel,
				  				LOW_panel, 
				   				 Description_Panel, 
				   				 Song_Panel, 
				   				  _SongBar, _SongButtons;
	private JButton 			createPlaylist_, removePlaylist_, home_,
								searchQuery_, logout_, previousHistory_, nextHistory_,
								previousSong_, playPause_, nextSong_
								//, addSong_, removeSong_					// add these to _SongButtons
								;	// addSong_ adds to a chosen playlist (or a new one), removeSong_ removes the song from that playlist ONLY
	private JLabel 				playlist_, username_, 
								title_, artist_, album_,
								currentTime_, songTime_, // song timers
								titleLabel;
	private DefaultListModel<String> 	dm;
	private JList<?>			playlist_List;
	private JScrollPane			UserSavedPanel;
	private ShiftingPanel       ShiftingPanel; 		// ShiftingPanel is the big one
	private SearchMenuPanel		SearchPanel; 
	private JSlider				timedSlider;			// song manipulator
	
	protected JComboBox<String>	searchField;
	
	private CreatePlaylistDialog playlistCreation;
	
	private boolean isSongPlaying = false 				// starts false; changes depending on changes occurring in frame
//					, isThereASong = false
	;
	
	DatagramSocket aSocket;								//For server connection
	int serverPort;
	static volatile int packet = 0;
	static int clipFrame = 0;
	static boolean clipSyn = true;
	static volatile byte[] byteSong;
	static InputStream myInputStream;
	Dimension shift;
	Thread play;
	boolean pause = false;
	String currentSong = "";
	
	/**
	 * Test driver for this class.
	 * @param args: a list of arguments. If none, then it will act as an empty array.
	 */
	public static void main(String[] args) { // test
		new Homepage("allan",null, 6733, null);
	}
	
	/**
	 * Main constructor.
	 * @param user: the name of the user
	 * @param aSocket: a socket
	 * @param serverPort: the server port
	 */
	public Homepage(String user, DatagramSocket aSocket, int serverPort, Frame base) {
		userName = user; 											// takes the username from input
		this.aSocket = aSocket;										// takes the socket from input
		this.serverPort = serverPort;								// takes the serverPort from input
		homeCard = "'MusicService' -  " + userName;
		initialize(base);												// initializes the frame
	}
	
	/**
	 * The main initialization method. Initializes the frame and sets off everything else into motion.
	 */
	private void initialize(Frame base) { 									
		frame = new JFrame("MusicService -- " + userName);			// initialize the frame itself
		frame.setMinimumSize(new Dimension(775,500));				// and set minimum dimensions
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// and the default close operation for the frame
		
		addHighComponentsToHome(frame.getContentPane()); 			// initializes and loads the upper half of frame components into frame
		addLowComponentsToHome(frame.getContentPane()); 			// initializes and loads the lower half of frame components into frame
		
		frame.pack(); 												// packs the frame together
		
		frame.setSize(new Dimension(1000,600)); 					// sets the size of the frame
		frame.setLocationRelativeTo(base); 							// sets frame location
		
		playlistCreation = new CreatePlaylistDialog(frame, userName, dm, aSocket, serverPort); 	// create playlist creation dialog
		playlistCreation.pack();																// pack playlist creation dialog
		
		frame.getRootPane().setDefaultButton(searchQuery_);			// set enter key functionality to searchQuery_
		
		frame.addComponentListener(new ComponentAdapter() {			// add a component listener between the frame and ShiftingPanel
			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
				Dimension f = frame.getSize(), shift = new Dimension(196,147); // an insanely precise Dimension object
				
				ShiftingPanel.setSize((int)(f.getSize().getWidth() - shift.getWidth()), 
										(int)(f.getSize().getHeight()- shift.getHeight()));
				ShiftingPanel.updateUI(); 							// update the UI of ShiftingPanel after changing the size
			}
		});
		
		frame.setVisible(true); 									// make the frame visible
	}
	
	/**
	 * This initialization method handles the upper half of the frame.
	 * @param pane: the frame's content pane
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addHighComponentsToHome(Container pane) 
	{
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));		// set up pane layout
		pane.add(Box.createRigidArea(new Dimension(1,1)));			// add one rigid area to pane
		
		HIGH_panel = new JPanel(); 									// initialize HIGH_panel and set layout
		HIGH_panel.setLayout(new BoxLayout(HIGH_panel, BoxLayout.X_AXIS));
		
		HIGH_panel.add(Box.createHorizontalStrut(1));
		
		Playlist_Panel = new JPanel(); 								// initialize Playlist_Panel and set layout and set max dimensions
		Playlist_Panel.setLayout(new BoxLayout(Playlist_Panel, BoxLayout.Y_AXIS));
		Playlist_Panel.setMaximumSize(new Dimension(200,1000));
		
		PlaylistTitle = new JPanel(); 								// initialize PlaylistTitle and set max dimensions
		PlaylistTitle.setMaximumSize(new Dimension(100,15));
		
		playlist_ = new JLabel("My Playlists"); 					// initialize playlist_ label
		playlist_.setName("list of user's playlist");
		playlist_.setFont(new Font("Tahoma", Font.PLAIN, 15)); 		// set font for playlist_
		PlaylistTitle.add(playlist_);  								// add playlist_ to PlaylistTitle
					
		dm = new DefaultListModel(); 								// initialize dm and playlist_List (using dm)
		playlist_List = new JList(dm);
		playlist_List.setFont(new Font("Tahoma", Font.PLAIN, 14));		// set font for playlist_List
		getPlaylists(dm);												// update list gui with playlist information from json
				
		playlist_List.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				CardLayout c1 = (CardLayout)(CorePanel.getLayout());
				c1.show(CorePanel, SHIFT_PANEL);
				searchQuery_.requestFocusInWindow();
				
				JList list = (JList)e.getSource();				// this rectangle bounds between the first and last entries on playlist_List
				Rectangle r = list.getCellBounds(0, list.getLastVisibleIndex());	
				
				if (e.getButton() == MouseEvent.BUTTON1) {									// left mouse button double-click only
					if (e.getClickCount() == 2 && r != null && r.contains(e.getPoint())) {	// and only within the rectangle (that exists) 
						int index = list.locationToIndex(e.getPoint());
						
						System.out.println("Index identified: " + index);	// system: shows the index of the clicked item
						System.out.println("Index name: " + list.getSelectedValue().toString()); // system: shows the name of the clicked item
						
						System.out.println("Current panel in ShiftingPanel is '" + ShiftingPanel.getCurrentPanelName() + "'");
						
						// checks if the current panel is the same one as the one that just got clicked
						if (!list.getSelectedValue().toString().equals(ShiftingPanel.getCurrentPanelName())) {
							PlaylistPanel newPanel = new PlaylistPanel(userName, list.getSelectedValue().toString());
							playlist = list.getSelectedValue().toString();
							newPanel.setName(list.getSelectedValue().toString());	// initialize a new PlaylistPanel and set the name of the PlaylistPanel
														
							newPanel.getListener().setLabel(title_);		// set the labels from Description_Panel to follow the actions 
							newPanel.getListener().setLabel(artist_);		// of the buttons from PlaylistPanel 
							newPanel.getListener().setLabel(album_);					
							
//							newPanel.getListener().setButtonOn(previousSong_); // set buttons active when a song is selected 
//							newPanel.getListener().setButtonOn(playPause_);
//							newPanel.getListener().setButtonOn(nextSong_);

							ShiftingPanel.addComponent(newPanel);			// add the PlaylistPanel to ShiftingPanel
						}
						else 
							System.out.println("Current panel in ShiftingPanel remains '" + ShiftingPanel.getCurrentPanelName() + "'\n");
					}		//System announces that nothing has changed and that the current panel remained the same as before
				}
			}
		});
		
		UserSavedPanel = new JScrollPane(playlist_List); 				// initialize UserSavedPanel using playlist_List
		UserSavedPanel.setMaximumSize(new Dimension (300,700));
		
		PlaylistOptions = new JPanel(); 								// initialize PlaylistOptions and set layout
		PlaylistOptions.setLayout(new BoxLayout(PlaylistOptions, BoxLayout.X_AXIS));

		createPlaylist_ = new JButton("Create"); 						// initialize createPlaylist_ and assign action listener
		createPlaylist_.setName("create a new playlist for the user");
		createPlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Opening subwindow for playlist naming...");				
				playlistCreation.setLocationRelativeTo(frame); 			// show playlist creation dialog
				playlistCreation.setVisible(true); 						// set dialog visibility to true
			}	
		});
		
		removePlaylist_ = new JButton("Remove"); 						// initialize removePlaylist_ and assign action listener
		removePlaylist_.setName("remove a user's playlist");
		removePlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Displaying options for which playlist to delete...");	
				
				// the list of options would be based on the user's personal playlists				
				
				Object[] possibilities = dm.toArray(); 				// each playlist name would be listed here
				String removedPlaylist = (String) JOptionPane.showInputDialog(frame, "Which playlist are you removing?\n"
						+ "(There's no going back once you do.)", "Playlist Removal", 
						JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
				if (removedPlaylist == null) System.out.println("No playlist was removed in the end..."); 
				// the dialog was exited one way or another. Otherwise...
				else {
					removePlaylist(removedPlaylist, userName);		//removedPlaylist represents the playlist name to be deleted
					getPlaylists(dm); 								// update the model (and thus the gui) afterwards
					
					// if the current song is from this playlist, remove visibility of the description panel
					ShiftingPanel.removeFromHistory(removedPlaylist);
					System.out.println("The playlist --" + removedPlaylist + "-- was removed."); // system announcement
				}	
			}
		});
		
		PlaylistOptions.add(Box.createRigidArea(new Dimension(6,5))); 	// set the layout with rigid areas
		PlaylistOptions.add(createPlaylist_);							// and JButtons for PlaylistOptions
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,5)));
		PlaylistOptions.add(removePlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(6,5)));
		
		Playlist_Panel.add(Box.createVerticalStrut(5)); 				// set the layout with rigid areas
		Playlist_Panel.add(PlaylistTitle);								// and JPanels to Playlist_Panel
		Playlist_Panel.add(Box.createVerticalStrut(5));
		Playlist_Panel.add(UserSavedPanel);
		Playlist_Panel.add(Box.createVerticalStrut(5));
		Playlist_Panel.add(PlaylistOptions);
		Playlist_Panel.add(Box.createRigidArea(new Dimension(3,3)));
		
		Playlist_Panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); 	// set border type for Playlist_Panel
		
//		large song album cover (e_CoverPanel) can go here later
		
//		Explore_Panel
		Explore_Panel = new JPanel(); 									// initialize Explore_Panel and set layout
		Explore_Panel.setLayout(new BoxLayout(Explore_Panel, BoxLayout.Y_AXIS));
		
		TopPanel = new JPanel(); 										// initialize TopPanel and set layout
		TopPanel.setLayout(new BoxLayout(TopPanel, BoxLayout.X_AXIS));
		
		_SearchPanel = new JPanel(); 									// initialize _SearchPanel and set layout
		_SearchPanel.setLayout(new FlowLayout());
				
		searchField = new JComboBox();									// create searchField
		searchField.setEditable(true);									// allow for an editable text field
		searchField.setName("Search for...");							// set the name
		searchField.getEditor().getEditorComponent()
				.addFocusListener(new FocusListener() {			// set a focus listener to the text field itself
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				CardLayout cards = (CardLayout)(CorePanel.getLayout());	
				cards.show(CorePanel, SEARCH_PANEL);					// swap cards from ShiftingPanel to SearchMenuPanel
				System.out.println("searchField focus GAINED");			// system call
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				System.out.println("searchField focus LOST");			// system call
				searchQuery_.requestFocusInWindow();					// give focus back to searchQuery_ when focus is lost here
			}
		});
		
		searchQuery_ = new JButton("Search");							// creates searchQuery_
		searchQuery_.setName("search button");
		searchQuery_.addActionListener(new ActionListener() {			// add action listener that searches for text in searchField
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = (String)searchField.getSelectedItem();
				
				if (text.equals(""));
				else {searchField.insertItemAt(text, 0);}

				System.out.println("Searching for: " + text);
				if (!codeDenial(text)) // if special characters are used then this will go off
				{
					searchField.removeItem(text);	// error dialog
					JOptionPane.showMessageDialog(frame, "You can't do that. Stop it!", 
							"Inane warning - code injection rejection", JOptionPane.WARNING_MESSAGE);
				} else {
					playlist = "x";					
					SearchPanel.changeSearch(getSearchResults(text), text);	// otherwise, it will change the table of SearchMenuPanel
				}
				searchField.setSelectedItem("");
			}
		});

		_SearchPanel.add(searchField);								// add searchField and searchQuery_ to _SearchPanel
		_SearchPanel.add(searchQuery_);								// and set max dimensions
		_SearchPanel.setMaximumSize(new Dimension(200,40));
		
		_HistoryPanel = new JPanel();								// initialize _HistoryPanel and set layout
		_HistoryPanel.setLayout(new FlowLayout());					// and maximum size for the panel
		_HistoryPanel.setMaximumSize(new Dimension(200,40));		
		
		previousHistory_ = new JButton("\u276C \u276C");			// initialize previousHistory_ and nextHistory_ buttons
		previousHistory_.setEnabled(false);
		previousHistory_.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				searchQuery_.requestFocusInWindow();
			}
		});
		
		nextHistory_ = new JButton("\u276D \u276D");
		nextHistory_.setEnabled(false);
		nextHistory_.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				searchQuery_.requestFocusInWindow();
			}
		});
		
		previousHistory_.setName("previous panel");					// set names of buttons
		nextHistory_.setName("next panel");
				
		_HistoryPanel.add(previousHistory_);						// add the buttons to _HistoryPanel
		_HistoryPanel.add(nextHistory_);
		
		home_ = new JButton("\u2302");								// home button - returns to home screen
		home_.setName("go home");
		home_.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Current panel in ShiftingPanel is '" + ShiftingPanel.getCurrentPanelName() + "'");
				
				if (ShiftingPanel.getComponents()[0].getName().equals("Base Home") 
						|| ShiftingPanel.getComponents()[0].getName().equals("Home"))
					System.out.println("Current panel in ShiftingPanel remains '" + ShiftingPanel.getCurrentPanelName() + "'");
				else {
					JLabel title_ = new JLabel(homeCard, JLabel.CENTER);
					title_.setFont(Tahoma_Plain_FourZero);	
					
					JPanel newHome = new JPanel(new BorderLayout());	
					createNewHomePanel(newHome, title_, "Home");
				}
			}
		});
		
		_ProfilePanel = new JPanel();								// initialize _ProfilePanel and set layout
		_ProfilePanel.setLayout(new FlowLayout());
		username_ = new JLabel("User: " + userName + "  ");			// initialize username_ and logout_
		username_.setName("show user name");
		logout_ = new JButton("Logout");
		logout_.setName("log out of user's account");
		logout_.addActionListener(new ActionListener() { 			// assign action listener to logout_
			public void actionPerformed(ActionEvent e) {
				frame.dispose();									// disposes of current frame
				String [] arguments = {userName};					//send logout message to server
				requestReply.UDPRequestReply("loggedOut",arguments, aSocket, serverPort);
				System.out.println("Logging out..."); 							// system announcement
		        new Login(aSocket, serverPort, frame).setVisible(true); 		// creates new Login() object
		        System.out.println("User '" + userName + "' has logged out.");	// system announcement
			}	
		});
		
		_ProfilePanel.add(username_); 								// add username_ and logut_ to _Profile
		_ProfilePanel.add(logout_);	  								// and set max dimensions
		_ProfilePanel.setMaximumSize(new Dimension(100,40));
		
		TopPanel.add(_HistoryPanel); 								// _SearchPanel and _ProfilePanel
		TopPanel.add(Box.createRigidArea(new Dimension(5,5)));		// and _HistoryPanel are
		TopPanel.add(_SearchPanel);									// added to TopPanel and gets a border set to it
		TopPanel.add(Box.createHorizontalGlue());
		TopPanel.add(home_);
		TopPanel.add(Box.createRigidArea(new Dimension(1,1)));
		TopPanel.add(_ProfilePanel);
		TopPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
//		ShiftingPanel (the one that keeps changing)
		ShiftingPanel = new ShiftingPanel(this);					// initialize ShiftingPanel and set border
		ShiftingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		ShiftingPanel.setMinimumSize(new Dimension(500,500));
		ShiftingPanel.setName("Shift");
		
		ShiftingPanel.setHistorySwitch(previousHistory_);			// assign action listeners to previousHistory_ and nextHistory_
		ShiftingPanel.setHistorySwitch(nextHistory_);
		
		titleLabel = new JLabel(homeCard, JLabel.CENTER);
		titleLabel.setFont(Tahoma_Plain_FourZero);		// initialize titleLabel and set font
		
		HomePanel = new JPanel(new BorderLayout());	
		createNewHomePanel(HomePanel, titleLabel, "Base Home");		// loads HomePanel into ShiftingPanel
		
		String[] temp = {};
		SearchPanel = new SearchMenuPanel(userName, temp , "");
		SearchPanel.setName("Search");
		
		CorePanel = new JPanel();									// CorePanel initialized
		CorePanel.setLayout(new CardLayout());						// set layout to CardLayout
		
		CorePanel.add(ShiftingPanel, SHIFT_PANEL);					// add ShiftingPanel and SearchPanel to CorePanel
		CorePanel.add(SearchPanel, SEARCH_PANEL);
		CorePanel.setName("Core");
		
		Explore_Panel.add(TopPanel); 								// add TopPanel and CorePanel to Explore_Panel
		Explore_Panel.add(Box.createRigidArea(new Dimension(1,2)));
		Explore_Panel.add(CorePanel);

		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1))); 	// add rigid areas and panels
		HIGH_panel.add(Playlist_Panel);								// to HIGH_panel
		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1)));
		HIGH_panel.add(Explore_Panel);
		
		pane.add(HIGH_panel); 										// add HIGH_panel to pane (the content pane for frame)
	}
	
	/**
	 * This initialization method handles the lower half of the frame.
	 * @param pane: the frame's content pane
	 */
	private void addLowComponentsToHome(Container pane) {
		LOW_panel = new JPanel();									// initialize LOW_panel and set layout and max dimensions
		LOW_panel.setLayout(new BoxLayout(LOW_panel, BoxLayout.X_AXIS));
		LOW_panel.setMaximumSize(new Dimension(15000,100));
//		LOW_panel.add(Box.createHorizontalStrut(10));	 			// add horizontal strut to LOW_panel
		
		// small song album cover (m_CoverPanel) can go here later
		
		Description_Panel = new JPanel(); 							// initialize Description_Panel and set layout
		Description_Panel.setLayout(new BoxLayout(Description_Panel, BoxLayout.Y_AXIS));
		
		title_ = new JLabel("Title");								// initialize title_ and set max dimensions and text
		title_.setMaximumSize(new Dimension(150,20)); 
		title_.setName("title");
		title_.setVisible(false);
		
		artist_ = new JLabel("Artist(s)"); 							// initialize artist_ and set max dimensions and text
		artist_.setMaximumSize(new Dimension(150,20)); 
		artist_.setName("artist(s)");		
		artist_.setVisible(false);
		
		album_ = new JLabel("Album"); 								// initialize album_ and set max dimensions and text
		album_.setMaximumSize(new Dimension(150,20));		
		album_.setName("album");
		album_.setVisible(false);
		
		SearchPanel.setLabel(title_);		
		SearchPanel.setLabel(artist_);								// tie labels to SearchPanel
		SearchPanel.setLabel(album_);
		
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));	// add rigid areas and labels
		Description_Panel.add(title_);									// to Description_Panel
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		Description_Panel.add(artist_);
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		Description_Panel.add(album_);
		Description_Panel.add(Box.createRigidArea(new Dimension(1,1)));
		
		Description_Panel.setMinimumSize(new Dimension(175,200));		// set minimum, preferred, and
		Description_Panel.setPreferredSize(new Dimension(175,60));		// maximum sizes for
		Description_Panel.setMaximumSize(new Dimension(175,200));		// Description_Panel
		
		Song_Panel = new JPanel();										// initialize Song_Panel and set layout
		Song_Panel.setLayout(new BoxLayout(Song_Panel, BoxLayout.Y_AXIS));

		_SongBar = new JPanel(); 										// initialize _SongBar and set layout
		_SongBar.setLayout(new FlowLayout());
//		currentTime_ = new JLabel("current"); 							// initialize currentTime_, timedSlider, and songTime_
//		timedSlider = new JSlider(0,100,0);
//		songTime_ = new JLabel("total");
		// un-movable on initialization, movable when there is a song 'queued' (is that the word for it?)
		
//		currentTime_.setVisible(isThereASong); 						// set visibilities of currentTime_ and songTime_ to off
//		timedSlider.setEnabled(isThereASong);  						// timedSlider gets disabled
//		songTime_.setVisible(isThereASong);
		
//		_SongBar.add(currentTime_); 								// add labels and jslider to _SongBar
//		_SongBar.add(timedSlider);
//		_SongBar.add(songTime_);
							
		_SongButtons = new JPanel(); 								// initialize _SongButtons and set layout
		_SongButtons.setLayout(new FlowLayout());
		
//		addSong_ goes here
		
//		previousSong_ = new JButton("\u2758" + "\u2758"); 						// initialize previousSong_ and add an
//		previousSong_.addActionListener(new ActionListener()		// action listener to previousSong_
//		{ 		
//			@Override 
//			public void actionPerformed(ActionEvent e) 
//			{
//				System.out.println("Moving to previous song...");
//				// do things here
//				
//				playPause_.setText("\u2758" + "\u2758");
//				isSongPlaying = true;								// the song is playing when it's all over
//			}	
//		});
		
		playPause_ = new JButton("\u25B6");							// initialize playPause_ and add an 
		playPause_.addActionListener(new ActionListener() { 		// action listener to playPause_
			@Override 
			public void actionPerformed(ActionEvent arg0) {
				if (playlist.equals("Base Home") || playlist.equals("Home")) {}	// avoid it completely if playlist is set to a HomePanel 
				else {
					if (current!=null && current.isActive()) {	
						clipFrame = current.getFramePosition();
						current.stop();	
						isSongPlaying = false;
						pause = true;
						if (!playlist.equals("x")) {		// procs ONLY if it's a search menu panel
							if(songIndex == 0)	songIndex = songList.size()-1;
							else				songIndex--;
						}
						current.close();
						playPause_.setText("\u25B6"); 
						isSongPlaying = false;
					} 
					else if ((current == null) || (current!=null && !current.isActive())) {
						String song = title_.getText() + "_" + artist_.getText() + "_" + album_.getText()  + ".wav";
						if (pause && song.equals(currentSong)) {
							myInputStream = new ByteArrayInputStream((Arrays.copyOfRange(byteSong, 0 ,packet*64000)));
							try {
								current.close();
								AudioInputStream audioIn = AudioSystem.getAudioInputStream(myInputStream);
								current = AudioSystem.getClip();
								current.open(audioIn);
								myInputStream.close();
								current.setFramePosition(clipFrame);
								current.start();
								isSongPlaying = true;
								playPause_.setText("\u2758" + "\u2758");
							}catch(Exception e) {
								e.printStackTrace();
							} 
						} else {
							try {
								String [] arguments = {title_.getText() + "_" + artist_.getText() + "_" + album_.getText()  + ".wav","-1"};
								JSONObject obj = requestReply.UDPRequestReply("playSong", arguments, aSocket, serverPort);
								int size = obj.getInt("result");
								byteSong = new byte[size];
								isSongPlaying = true;
								packet = 0;
								pause = false;
								new Thread(new Runnable() 
								{
									@Override
									public void run() 
									{
										for(int i = 0; i<=size/64000; i++) {
											if(isSongPlaying) {
												try {	
													//ClipSyn still has problem if you play a song, stop it, then play it again
													//ClipSyn is used to make sure that only one clip linelistener is running at a time

													if(i>20 && i%10 == 0)
														if(clipSyn == true)
															clipSyn = false;
													
													InetAddress aHost = InetAddress.getByName("localhost");

													//Request
													arguments[1] = String.valueOf(i*64000);
													byte[] m = requestReply.JSONRequestObject("playSong",arguments).toString().getBytes("utf-8");
													DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
													aSocket.send(request);

													//Reply
													byte[] buffer = new byte[64000];

													DatagramPacket reply = new DatagramPacket(buffer, buffer.length);

													aSocket.receive(reply);

													if(size - i*64000 > 64000) {
														//System.out.println(i);
														System.arraycopy(reply.getData(), 0, byteSong, i*64000, 64000);
													}
													else {
														System.arraycopy(reply.getData(), 0, byteSong, i*64000, size - i*64000);
														//System.out.println("numb: "+i);
													}

												}
												catch (SocketException e) {
													System.out.println("Socket: " + e.getMessage());
												}
												catch (IOException e) {
													System.out.println("IO: " + e.getMessage());
												}
												packet++;
											}
										}
									}}).start();
								while (packet != 20);
								myInputStream = new ByteArrayInputStream(Arrays.copyOfRange(byteSong, 0, 20*64000));
								playMusic(myInputStream);
								myInputStream.close();
								try {
									// use line listener to take care of synchronous call
									current.addLineListener(new LineListener() {
										public void update(LineEvent event) {
											System.out.println("outhere");
											if (event.getType() == LineEvent.Type.STOP && !pause) {

												System.out.println("INHERE");
												clipFrame = current.getFramePosition();
												//current.stop();
												myInputStream = new ByteArrayInputStream((Arrays.copyOfRange(byteSong, 0 ,packet*64000)));
												try {
													AudioInputStream audioIn = AudioSystem.getAudioInputStream(myInputStream);
													current = AudioSystem.getClip();
													current.open(audioIn);
													myInputStream.close();
													current.setFramePosition(clipFrame);
													if(isSongPlaying)
														current.start();
												} catch(Exception e) {
													e.printStackTrace();
												}
												clipSyn=true;
											}
										}
									});

								} catch(Exception e) {
									e.printStackTrace();
								}
								playPause_.setText("\u2758" + "\u2758");
							} catch ( IOException e1) {
								e1.printStackTrace();
							}
							//isSongPlaying = true;
						}
					}
				}
			}
		});

//		nextSong_ = new JButton("\u2758" + "\u2758");			// initializes nextSong_ and add an
//		nextSong_.addActionListener(new ActionListener() 		// action listener to nextSong_
//		{ 			
//			@Override 
//			public void actionPerformed(ActionEvent e) 
//			{			
//				System.out.println("Moving to next song...");
//				//do things here
//				if(!(playlist.equals("x")))
//				{
//					if(!(songList.isEmpty()))
//					{
//						try{
//							current.stop();
//							System.out.println(songList.get(songIndex) + ".wav");
//							File file = new File(songList.get(songIndex) + ".wav");
//							AudioInputStream player = AudioSystem.getAudioInputStream(file);
//							current = AudioSystem.getClip();
//							current.open(player);
//							pos = 0;
//							current.setFramePosition(pos);
//							current.start();
//
//							if(songIndex == songList.size() -1)
//							{
//								songIndex = 0;
//							}
//							else
//							{
//								songIndex++;
//							}
//						}catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
//
//					playPause_.setText("\u2758" + "\u2758");
//					isSongPlaying = true;							// the song will begin playing when it's all over			
//				}	//
//			}});
	
//		add removeSong_ here
		
		// all these buttons initialize as un-clickables
		// addSong_
//		previousSong_.setEnabled(isThereASong);
//		playPause_.setEnabled(isThereASong);
//		nextSong_.setEnabled(isThereASong);
		// removeSong_
		// if there is a song, it will unlock and play automatically. but until then...
		
//		_SongButtons.add(previousSong_); 						// add buttons to _SongButtons
		_SongButtons.add(playPause_); 	
//		_SongButtons.add(nextSong_);
		
		Song_Panel.add(_SongBar); 								// add panels to Song_Panel
		Song_Panel.add(_SongButtons);
						
		LOW_panel.add(Box.createRigidArea(new Dimension(1,1))); // add rigid areas and panels
		LOW_panel.add(Description_Panel);						// to LOW_panel and set border
		LOW_panel.add(Box.createRigidArea(new Dimension(2,2)));
		LOW_panel.add(Song_Panel);
		LOW_panel.add(Box.createRigidArea(new Dimension(1,1)));
		LOW_panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));				
			
		pane.add(LOW_panel); // add LOW_panel to pane (the content pane of frame)
	}
	
	/**
	 * A static method that plays an audio input stream.
	 * @param music - the song that needs to be played
	 */
	public static void playMusic(InputStream music) {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(music);
			current = AudioSystem.getClip();
			current.open(audioIn);
			current.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A void method used to set the string variable 'playlist'. ShiftingPanel uses this.
	 * @param name: the name of the current playlist
	 */
	protected void setCurrentPlaylist(String name) {
		if (name.contains("Search for:"))
			playlist = "x";
		else playlist = name;
		System.out.println("currently looking at " + name);
	}
	
	// FOR USE WITH SEARCHQUERY_	
	/** ORIGIN: Login.java
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
	 * A void method that adds a souce JPanel (a HomePanel) to ShiftingPanel
	 * @param source - the panel being added
	 * @param title - the label on the panel
	 * @param name - the name of the panel being added
	 */
	private void createNewHomePanel(JPanel source, JLabel title, String name) {		
		source.setSize(804,455);						
		source.add(title, BorderLayout.CENTER);
		source.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED)); // and set border around it (debug testing only)
		
		source.setName(name);
				
		ShiftingPanel.addComponent(source);						// add HomePanel to ShiftingPanel
		ShiftingPanel.updateUI();
	}

	/**
	 * A void method that can make a frame visible.
	 * @param b boolean (true/false) that determines visibility of the frame (where true makes it visible, and false makes it not visible
	 */
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		frame.setVisible(b);
	}
	
	/** ORIGIN: Profile.java
	 * Read playlists array from json file and add to gui list
	 * @param dm defaultlistModel
	 */
	void getPlaylists(DefaultListModel<String> dm) {
		dm.clear(); //clear list 

		String [] arguments = {userName};
		JSONObject obj = requestReply.UDPRequestReply("getPlaylists",arguments, aSocket, serverPort);
		//read playlists
		String playlist = obj.get("result").toString();	
		
	    String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
	    //add playlist to default list
	    for(int i = 0; i < playlistArray.length; i++) 
	    	dm.addElement(playlistArray[i]);
	}
	
	/** ORIGIN: Profile.java
	 * Remove playlist from JSON FiLE
	 * @param playlist playlist to be removed
	 * @param username current login user
	 */
	void removePlaylist(String playlist, String username) {
		//Server side playlist removal
		String [] arguments = {username,playlist};
		requestReply.UDPRequestReply("removePlaylist",arguments, aSocket, serverPort);
	}

	/** ORIGIN: SearchMenuPanel.java
	 * Get search results 
	 * @param
	 */
	String[] getSearchResults(String search) {
		String [] arguments = {search};
		JSONObject obj = requestReply.UDPRequestReply("getSearch", arguments, aSocket, serverPort);
		String results  = obj.get("result").toString();
		
		if (results.length() < 3) return null;
		results = results.substring(2, results.length() - 2).replace("\",\"", "\n");
		System.out.println("These are the results:\n" + results);
		
		return results.split("\n");
	}
}

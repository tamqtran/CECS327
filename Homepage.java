import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import org.json.JSONObject;

/**
 * The Homepage class creates a frame that houses the main piece of this music streaming application.
 * @author Austin Tao
 * @since 9/20/2018
 */
public class Homepage {
	private volatile static Clip current;
	private int					songIndex, currentListIndex = -1;
	private ArrayList<String> 	songList = new ArrayList<String>();
	private List<String>		historyLog = new ArrayList<String>();
	
	private String 				userName, playlist, currentFilter;
	
	private final String[]		filterTypes = {"Song", "Artist", "Album"};

	private final String 		specials = "[!@#$%&*()+=|<>?{}\\[\\]~-]", 
								SHIFT_PANEL = "Shifting", SEARCH_PANEL = "Search",
								homeCard;
	
	private final Font			Tahoma_Plain_OneFour =  new Font("Tahoma", Font.PLAIN, 14),
								Tahoma_Plain_OneFive =  new Font("Tahoma", Font.PLAIN, 15),
								Tahoma_Plain_FourZero = new Font("Tahoma", Font.PLAIN, 40);
	
	private JFrame 				frame;
	
	private JPanel 				HIGH_panel, 
								 Playlist_Panel,	PlaylistTitle, PlaylistOptions,
								 Explore_Panel, 	TopPanel, _SearchPanel, _HistoryPanel, _ProfilePanel,
								 					HomePanel, CorePanel,
				  				LOW_panel, 
				   				 Description_Panel, 
				   				 Song_Panel, 	 	_SongBar, _SongButtons;
	
	private JButton 			createPlaylist_, removePlaylist_, home_,
								searchQuery_, logout_, previousHistory_, nextHistory_,
								previousSong_, playPause_, nextSong_
								//, addSong_, removeSong_		// add these to _SongButtons
								;	// addSong_ adds to a chosen playlist (or a new one), 
									// removeSong_ removes the song from that playlist ONLY
	
	private JLabel 				playlist_, username_, 
								title_, artist_, album_,

								currentTime_, songTime_, // song timers
								titleLabel;
	
	private JSlider				timedSlider; // song manipulator
	
	private JComboBox<String>	searchField, searchFilter;

	
	private JScrollPane			UserSavedPanel;
	
	private JList<?>			playlist_List;
	private DefaultListModel<String> 	dm;
	
	private ShiftingPanel       ShiftingPanel;
	private SearchMenuPanel		SearchPanel; 

	private CreatePlaylistDialog playlistCreation;
	
	// starts false; changes dependent on changes occurring in frame
	private boolean 			isSongPlaying = false, isHistory = false;
	
	DatagramSocket 				aSocket;		// For server connection
	int 						serverPort;
	static volatile int 		packet = 0;
	static int 					clipFrame = 0;
	static boolean 				clipSyn = true;
	static volatile byte[] 		byteSong;
	static InputStream 			myInputStream;
	Dimension 					shift;
	Thread 						play;
	boolean 					pause = false;
	String 						currentSong = "";
	
	/**
	 * Test driver for this class.
	 * @param args: a list of arguments. If none, then it will act as an empty array.
	 */
//	public static void main(String[] args) { // test
//		new Homepage("allan",null, 6733, new JFrame());
//	}
	
	/**
	 * Main constructor.
	 * @param user: the name of the user
	 * @param aSocket: a socket
	 * @param serverPort: the server port
	 */
	public Homepage(String user, DatagramSocket aSocket, int serverPort, Frame base) {
		userName = user; 								// take the username from input
		this.aSocket = aSocket;							// take the socket from input
		this.serverPort = serverPort;					// take the serverPort from input
		homeCard = "'MusicService' -  " + userName;		// initialize homeCard in relation to the username
		initialize(base);								// initializes the frame
	}
	
	/**
	 * The main initialization method. Initializes the frame and sets off everything else into motion.
	 */
	private void initialize(Frame base) {
		// initialize the frame and set minimum dimensions and default close operation for the frame
		frame = new JFrame("MusicService -- " + userName);

		frame.setMinimumSize(new Dimension(800,500));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// initializes and loads the upper half of frame components into frame's content pane
		addHighComponentsToHome(frame.getContentPane());
		// initializes and loads the lower half of frame components into frame's content pane
		addLowComponentsToHome(frame.getContentPane());
		// pack the frame
		frame.pack();
		
		// set the size of the frame and its location
		frame.setSize(new Dimension(1000,600)); 					
		frame.setLocationRelativeTo(base);
		
		// create and pack playlist creation dialog
		playlistCreation = new CreatePlaylistDialog(frame, userName, dm, aSocket, serverPort);
		playlistCreation.pack();
		
		// set the frame's root pane's default button to searchQuery_
		frame.getRootPane().setDefaultButton(searchQuery_);	

		// add a component listener between the frame and ShiftingPanel
		frame.addComponentListener(new ComponentAdapter() {	
			@Override public void componentResized(ComponentEvent arg0) {
				// create two Dimension objects - one for the size of the frame, the other as an offset
				Dimension f = frame.getSize(), shift = new Dimension(196,147);
				
				// change the size of ShiftingPanel to the size of the frame w/ the offset
				ShiftingPanel.setSize((int)(f.getSize().getWidth() - shift.getWidth()), 
										(int)(f.getSize().getHeight()- shift.getHeight()));
				
				// update the UI of ShiftingPanel after changing the size
				ShiftingPanel.updateUI();
				
				LOW_panel.setSize((int)f.getSize().getWidth(), (int)LOW_panel.getMinimumSize().getHeight());
				LOW_panel.updateUI();
			}
		});
		
		// make the frame visible
		frame.setVisible(true); 									
	}
	
	/**
	 * This initialization method handles the upper half of the frame.
	 * @param pane: the frame's content pane
	 */
	@SuppressWarnings("unchecked")
	private void addHighComponentsToHome(Container pane) {	
		// set up pane layout
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(Box.createRigidArea(new Dimension(1,1)));			
		
		// initialize HIGH_panel and set layout
		HIGH_panel = new JPanel(); 									
		HIGH_panel.setLayout(new BoxLayout(HIGH_panel, BoxLayout.X_AXIS));
		HIGH_panel.add(Box.createHorizontalStrut(1));
		
		// initialize Playlist_Panel and set layout and max dimensions
		Playlist_Panel = new JPanel(); 								
		Playlist_Panel.setLayout(new BoxLayout(Playlist_Panel, BoxLayout.Y_AXIS));
		
		// initialize PlaylistTitle and set max dimensions
		PlaylistTitle = new JPanel(); 								
		PlaylistTitle.setMaximumSize(new Dimension(90,25));
		
		// initialize playlist_ label, set font, and add to PlaylistTitle
		playlist_ = new JLabel("My Playlists"); 					
		playlist_.setName("list of user's playlist");
		playlist_.setFont(Tahoma_Plain_OneFive);
		PlaylistTitle.add(playlist_);
		
		// initialize dm and playlist_List (using dm)
		dm = new DefaultListModel<String>(); 								
		playlist_List = new JList<String>(dm);
		
		// set font for playlist_List
		playlist_List.setFont(Tahoma_Plain_OneFour);
		// update list gui with playlist information from json
		getPlaylists(dm);
		
		playlist_List.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Shift front card to ShiftingPanel and focus on searchQuery_
				((CardLayout)(CorePanel.getLayout())).show(CorePanel, SHIFT_PANEL);
				CorePanel.updateUI();
				
				JList<String> list = (JList<String>)e.getSource();				
				// this rectangle bounds between the first and last entries on playlist_List
				Rectangle r = list.getCellBounds(list.getFirstVisibleIndex(), list.getLastVisibleIndex());	

				// left mouse button double-click only
				if (e.getButton() == MouseEvent.BUTTON1) {	
					// and only within the rectangle (that exists)
					if (e.getClickCount() == 2 && r != null && r.contains(e.getPoint())) {	 						
						// system: shows the index of the clicked item
						// System.out.println("Index identified: " + list.locationToIndex(e.getPoint()););	
						
						// system: shows the name of the clicked item
						System.out.println("Index name: " + list.getSelectedValue().toString()); 
						
						// system: shows the current panel of ShiftingPanel
						System.out.println("Current panel in ShiftingPanel is '" 
												+ ShiftingPanel.getCurrentPanelName() + "'");
						
						// checks if the current panel is the same one as the one that just got clicked
						if (!list.getSelectedValue().toString().equals(ShiftingPanel.getCurrentPanelName())) {
							// initialize a new PlaylistPanel and set the name of the PlaylistPanel
							PlaylistPanel newPanel = new PlaylistPanel(userName, list.getSelectedValue().toString());
							playlist = list.getSelectedValue().toString();
							newPanel.setName(list.getSelectedValue().toString());	
							
							// set the labels from Description_Panel to follow the actions of the buttons from PlaylistPanel 
							newPanel.getListener().setLabel(title_);		
							newPanel.getListener().setLabel(artist_);		
							newPanel.getListener().setLabel(album_);					
							
							// set buttons active when a song is selected
//							newPanel.getListener().setButtonOn(previousSong_);  
//							newPanel.getListener().setButtonOn(playPause_);
//							newPanel.getListener().setButtonOn(nextSong_);
							
							// add the PlaylistPanel to ShiftingPanel
							ShiftingPanel.addComponent(newPanel);			
						} else 
							// System announcement: nothing has changed; the current panel remains the same as before
							System.out.println("Current panel in ShiftingPanel remains '" 
													+ ShiftingPanel.getCurrentPanelName() + "'\n");
					}		
				}
			}
		});
		
		// initialize UserSavedPanel using playlist_List
		UserSavedPanel = new JScrollPane(playlist_List); 				
		UserSavedPanel.setMaximumSize(new Dimension (300,700));
		
		// initialize PlaylistOptions and set layout
		PlaylistOptions = new JPanel(); 								
		PlaylistOptions.setLayout(new BoxLayout(PlaylistOptions, BoxLayout.X_AXIS));
		
		// initialize createPlaylist_ and removePlaylist_ and assign action listeners to each
		createPlaylist_ = new JButton("Create"); 						
		createPlaylist_.setName("create a new playlist for the user");
		createPlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				// system: the playlist creation dialog is being called forth
				System.out.println("Opening subwindow for playlist naming...");	
				
				// show playlist creation dialog and set dialog visibility to true
				playlistCreation.setLocationRelativeTo(frame); 			
				playlistCreation.setVisible(true); 						
			}	
		});
		removePlaylist_ = new JButton("Remove"); 						
		removePlaylist_.setName("remove a user's playlist");
		removePlaylist_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				System.out.println("Displaying options for which playlist to delete...");		
				
				// the list of options would be based on the user's personal playlists	
				// each playlist name would be listed here
				Object[] possibilities = dm.toArray();
				String removedPlaylist 
					= (String) JOptionPane.showInputDialog( frame,
						"Which playlist are you removing?\n(There's no going back once you do.)", 
						"Playlist Removal", 
						JOptionPane.PLAIN_MESSAGE, null, possibilities, null);
				
				// the dialog was exited one way or another
				if (removedPlaylist == null) 
					System.out.println("No playlist was removed in the end..."); 				
				else {
					// remove the playlist from this user's json file
					removePlaylist(removedPlaylist, userName);
					
					// update the model
					getPlaylists(dm);
					
					if (dm.isEmpty()) currentListIndex = -1;
					
					// if the current song is from this playlist, remove visibility of the description panel 
					// and remove all iterations of PlaylistPanels with this name from ShiftingPanel
					ShiftingPanel.removeFromHistory(removedPlaylist); 
					
					// system: the specified playlist has been scrubbed out of the user's profile
					System.out.println("The playlist --" + removedPlaylist + "-- was removed."); 
				}	
			}
		});
		
		// set the layout with rigid areas and JButtons for PlaylistOptions
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,0)));		PlaylistOptions.add(createPlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,0)));		PlaylistOptions.add(removePlaylist_);
		PlaylistOptions.add(Box.createRigidArea(new Dimension(5,0)));
		
		// set the layout with rigid areas and JPanels to Playlist_Panel
		Playlist_Panel.add(Box.createVerticalStrut(5));		Playlist_Panel.add(PlaylistTitle);
		Playlist_Panel.add(Box.createVerticalStrut(5));		Playlist_Panel.add(UserSavedPanel);
		Playlist_Panel.add(Box.createVerticalStrut(5));		Playlist_Panel.add(PlaylistOptions);
		Playlist_Panel.add(Box.createRigidArea(new Dimension(0,3)));
		
		// set border type for Playlist_Panel
		Playlist_Panel.setBorder(BorderFactory
				.createBevelBorder(BevelBorder.RAISED) 
//				.createLineBorder(Color.RED)
				);
//		Playlist_Panel.setPreferredSize(new Dimension(200,600));
		Playlist_Panel.setMaximumSize(new Dimension(200,500));
		
//		large song album cover (e_CoverPanel) can go here later
		
		// initialize Explore_Panel and set layout
		Explore_Panel = new JPanel(); 									
		Explore_Panel.setLayout(new BoxLayout(Explore_Panel, BoxLayout.Y_AXIS));
		
		// initialize TopPanel and set layout
		TopPanel = new JPanel(); 										
		TopPanel.setLayout(new BoxLayout(TopPanel, BoxLayout.X_AXIS));
		
		// initialize _SearchPanel and set layout
		_SearchPanel = new JPanel(); 									
		_SearchPanel.setLayout(new FlowLayout());
		
		//DefaultComboBoxModel<String> cbm = new DefaultComboBoxModel<String>();		
		
		// initialize searchField and set it as editable
		searchField = new JComboBox<String>();
		searchField.setEditable(true);
		searchField.setName("Search for...");

		searchField.setToolTipText("Double-click the text field to clear it.");

				
		isHistory = true; 
		//initialize isHistory (used for searchField focus)
		
		// add focus and mouse listeners to the text field component of searchField
		JTextField editorComponent = (JTextField)searchField.getEditor().getEditorComponent();
		editorComponent.setText("");
		editorComponent.addFocusListener(new FocusListener() {			
			@Override public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() { // the focus is run last
					@Override public void run() {
						System.out.println("searchField focus GAINED");	// system call

						System.out.println("Search");
						// swap cards from SearchMenuPanel to ShiftingPanel
						((CardLayout)(CorePanel.getLayout())).show(CorePanel, SEARCH_PANEL);	
						CorePanel.updateUI();

						System.out.println();
					}
				});
			}
			@Override public void focusLost(FocusEvent e) {
				SwingUtilities.invokeLater(new Runnable() { // the focus is run last
					@Override public void run() {	
						// Don't do it if the focus is moved to any of the components in SearchPanel
						boolean check = false;
						for(Component c : SearchPanel.getComponents()) {
							if (c.hasFocus()) {
								check = true;
								break;
							}
						}
						System.out.println("searchField focus LOST");	// system call
						// this will be skipped if focus was not passed to a component in SearchPanel. Otherwise...
						if (check == true) {

							System.out.println("Search");
							// swap cards from ShiftingPanel to SearchMenuPanel
							((CardLayout)(CorePanel.getLayout())).show(CorePanel, SEARCH_PANEL);	
							CorePanel.updateUI();
						} else {
							System.out.println("Shift");
							// swap cards from SearchMenuPanel to ShiftingPanel
							((CardLayout)(CorePanel.getLayout())).show(CorePanel, SHIFT_PANEL);	
							CorePanel.updateUI();
						}
						System.out.println();
					}
				});
			}
		});
		editorComponent.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
				System.out.println("item index: " + searchField.getSelectedIndex());
				if (searchField.getSelectedIndex() < 0);
				// iff double-clicked w/ a (left) mouseButton BUTTON1, then the entry in searchField is wiped
				else if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 1) {
						System.out.println("item clicked: " 
								+ ((searchField.getSelectedItem().equals("")) ? "<blank>" : searchField.getSelectedItem()));
					}
					if (e.getClickCount() == 2) {
						System.out.println("item double-clicked: " 
								+ ((searchField.getSelectedItem().equals("")) ? "<blank>" : searchField.getSelectedItem()));
						editorComponent.setText("");
						searchField.setSelectedIndex(-1);
					}
				}
				System.out.println();
			}
		});
		
		searchFilter = new JComboBox<String>(filterTypes);
		searchFilter.setEditable(false);
		searchFilter.setName("search by");
		searchFilter.setToolTipText("Choose one of these three filters. (Initial default on 'song')");
		searchFilter.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				currentFilter = (String) searchFilter.getSelectedItem();
			}
		});
		searchFilter.setSelectedIndex(0);
		
		// initialize searchQuery_ and add an action listener that searches for text in searchField
		searchQuery_ = new JButton("Search");							
		searchQuery_.setName("search button");
		searchQuery_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				try {
					String text = (String)searchField.getSelectedItem();
				
					if (text.equals(""));	// do nothing
					else { // add to the drop-down
						searchField.insertItemAt(text, 0);
					}

					System.out.println("Searching for: " + text);
					// if special characters are used then this will go off
					if (!codeDenial(text)) {
						searchField.removeItem(text);	// error dialog
						JOptionPane.showMessageDialog(frame, "You can't do that. Stop it!", 
								"Inane warning - code injection rejection", JOptionPane.WARNING_MESSAGE);
					} else {

						playlist = "x";						
						SearchPanel.changeSearch(getSearchResults(text), text);	
						// otherwise, it will change the table of SearchMenuPanel
						
						// swap cards from ShiftingPanel to SearchMenuPanel
						((CardLayout)(CorePanel.getLayout())).show(CorePanel, SEARCH_PANEL);	
						CorePanel.updateUI();

					}
				} catch (NullPointerException E) {
					E.getStackTrace();
					System.out.println("Nothing is here.");
				}
			}
		});
		
		// add searchField and searchQuery_ to _SearchPanel and set max dimensions

		_SearchPanel.add(searchQuery_);
		_SearchPanel.add(searchField);
		_SearchPanel.add(searchFilter);

		_SearchPanel.setMaximumSize(new Dimension(200,40));
		
		// initialize _HistoryPanel and set layout and maximum size for the panel
		_HistoryPanel = new JPanel();
		_HistoryPanel.setLayout(new FlowLayout());					
		_HistoryPanel.setMaximumSize(new Dimension(200,40));		
		
		// initialize previousHistory_ and nextHistory_ buttons
		previousHistory_ = new JButton("\u276C \u276C");			
		previousHistory_.setEnabled(false);
		nextHistory_ = new JButton("\u276D \u276D");
		nextHistory_.setEnabled(false);
		
		// set names of history buttons
		previousHistory_.setName("previous panel");					
		nextHistory_.setName("next panel");
		
		// add the buttons to _HistoryPanel
		_HistoryPanel.add(previousHistory_);						
		_HistoryPanel.add(nextHistory_);
		
		// initialize home_ and assign an action listener to home_
		home_ = new JButton("\u2302");
		home_.setName("go home");
		home_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				// system: tells the current panel of ShiftingPanel
				System.out.println("Current panel in ShiftingPanel is '" + ShiftingPanel.getCurrentPanelName() + "'");
				
				// stops the process if the current panel is already a HomePanel. Otherwise...
				if (ShiftingPanel.getComponents()[0].getName().equals("Base Home") 
						|| ShiftingPanel.getComponents()[0].getName().equals("Home"))
					System.out.println("Current panel in ShiftingPanel remains '" + ShiftingPanel.getCurrentPanelName() + "'");
				else {
					// create title banner and set font
					JLabel title_ = new JLabel(homeCard, JLabel.CENTER);
					title_.setFont(Tahoma_Plain_FourZero);	
					
					// initialize a new panel and load it into ShiftingPanel
					JPanel newHome = new JPanel(new BorderLayout());
					createNewHomePanel(newHome, title_, "Home");
				}
			}
		});
		
		// initialize _ProfilePanel and set layout
		_ProfilePanel = new JPanel();								
		_ProfilePanel.setLayout(new FlowLayout());
		
		// initialize username_ and logout_
		username_ = new JLabel("User: " + userName + "  ");			
		username_.setName("show user name");
		logout_ = new JButton("Logout");
		logout_.setName("log out of user's account");
		
		// assign action listener to logout_
		logout_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				//send logout message to server
				String [] arguments = {userName};					
				requestReply.UDPRequestReply("loggedOut",arguments, aSocket, serverPort);
				System.out.println("Logging out..."); // system: the user is logging out
				
				// disposes of current frame
				frame.dispose();				
				// creates new Login() object
		        new Login(aSocket, serverPort, frame).setVisible(true);
		        
		        // system: the user has logged out
		        System.out.println("User '" + userName + "' has logged out.");	
			}	
		});
		
		// add username_ and logut_ to _ProfilePanel and set max dimensions
		_ProfilePanel.add(username_); 								
		_ProfilePanel.add(logout_);	  								
		_ProfilePanel.setMaximumSize(new Dimension(100,40));
		
		// _SearchPanel and _ProfilePanel and _HistoryPanel are added to TopPanel and gets a border set to it
		TopPanel.add(_HistoryPanel); 	TopPanel.add(Box.createRigidArea(new Dimension(1,1)));		
		TopPanel.add(_SearchPanel);		TopPanel.add(Box.createHorizontalGlue());
		TopPanel.add(home_);			TopPanel.add(Box.createRigidArea(new Dimension(1,1)));
		TopPanel.add(_ProfilePanel);
		TopPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		// initialize CorePanel and set layout to CardLayout
		CorePanel = new JPanel();
		CorePanel.setLayout(new CardLayout());
		
		// initialize ShiftingPanel and set border
		ShiftingPanel = new ShiftingPanel(this);					
		ShiftingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		ShiftingPanel.setMinimumSize(new Dimension(500,500));
		ShiftingPanel.setName("Shift");
		
		// assign action listeners to previousHistory_ and nextHistory_
		ShiftingPanel.setHistorySwitch(previousHistory_);			
		ShiftingPanel.setHistorySwitch(nextHistory_);
		
		// initialize titleLabel and set font
		titleLabel = new JLabel(homeCard, JLabel.CENTER);
		titleLabel.setFont(Tahoma_Plain_FourZero);		
		
		// initialize HomePanel and load HomePanel into ShiftingPanel
		HomePanel = new JPanel(new BorderLayout());	
		createNewHomePanel(HomePanel, titleLabel, "Base Home");		
		
		//initialize SearchPanel with a blank search list and blank userSearch
		String[] temp = {};
		SearchPanel = new SearchMenuPanel(userName, temp , "");
		SearchPanel.setName("Search");
		
		// add ShiftingPanel and SearchPanel to CorePanel
		CorePanel.add(ShiftingPanel, SHIFT_PANEL);					
		CorePanel.add(SearchPanel, SEARCH_PANEL);
		CorePanel.setName("Core");
		
		// add TopPanel and CorePanel to Explore_Panel
		Explore_Panel.add(TopPanel); 								
		Explore_Panel.add(Box.createRigidArea(new Dimension(1,2)));
		Explore_Panel.add(CorePanel);
		
		// add rigid areas and panels to HIGH_panel
		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1))); 	
		HIGH_panel.add(Playlist_Panel);								
		HIGH_panel.add(Box.createRigidArea(new Dimension(1,1)));
		HIGH_panel.add(Explore_Panel);
		
		// add HIGH_panel to pane (the content pane for frame)
		pane.add(HIGH_panel);
	}
	
	/**
	 * This initialization method handles the lower half of the frame.
	 * @param pane: the frame's content pane
	 */
	private void addLowComponentsToHome(Container pane) {
		// initialize LOW_panel and set layout and max dimensions
		LOW_panel = new JPanel();									
		LOW_panel.setLayout(new BoxLayout(LOW_panel, BoxLayout.X_AXIS));
		LOW_panel.setMaximumSize(new Dimension(15000,100));
		
		// small song album cover (m_CoverPanel) can go here later
		
		// initialize Description_Panel and set layout
		Description_Panel = new JPanel(); 							
		Description_Panel.setLayout(new BoxLayout(Description_Panel, BoxLayout.Y_AXIS));
		
		// initialize title_ and set max dimensions and text
		title_ = new JLabel("Title");								
		title_.setMaximumSize(new Dimension(150,20)); 
		title_.setName("title");
		title_.setVisible(false);
		
		// initialize artist_ and set max dimensions and text
		artist_ = new JLabel("Artist(s)"); 							
		artist_.setMaximumSize(new Dimension(150,20)); 
		artist_.setName("artist(s)");		
		artist_.setVisible(false);
		
		// initialize album_ and set max dimensions and text
		album_ = new JLabel("Album"); 
		album_.setMaximumSize(new Dimension(150,20));		
		album_.setName("album");
		album_.setVisible(false);
		
		// tie labels to SearchPanel
		SearchPanel.setLabel(title_);		SearchPanel.setLabel(artist_);		SearchPanel.setLabel(album_);
		// add rigid areas and labels to Description_Panel
		Description_Panel.add(Box.createRigidArea(new Dimension(0,1)));			Description_Panel.add(title_);
		Description_Panel.add(Box.createRigidArea(new Dimension(0,1)));			Description_Panel.add(artist_);
		Description_Panel.add(Box.createRigidArea(new Dimension(0,1)));			Description_Panel.add(album_);
		Description_Panel.add(Box.createRigidArea(new Dimension(0,1)));
		
		// set minimum, preferred, and maximum sizes for Description_Panel
		Description_Panel.setMinimumSize(new Dimension(175,200));		
		Description_Panel.setPreferredSize(new Dimension(175,60));		
		Description_Panel.setMaximumSize(new Dimension(175,200));
		
		// initialize Song_Panel and set layout
		Song_Panel = new JPanel();										
		Song_Panel.setLayout(new BoxLayout(Song_Panel, BoxLayout.Y_AXIS));
		
		// initialize _SongBar and set layout
		_SongBar = new JPanel(); 
		_SongBar.setLayout(new FlowLayout());
		
		// initialize currentTime_, timedSlider, and songTime_
//		currentTime_ = new JLabel("current"); 							
//		timedSlider = new JSlider(0,100,0);
//		songTime_ = new JLabel("total");
		// un-movable on initialization, movable when there is a song 'queued' (is that the word for it?)
		
		// set visibilities of currentTime_ and songTime_ to off; timedSlider gets disabled
//		currentTime_.setVisible(isThereASong); 						
//		timedSlider.setEnabled(isThereASong);  						
//		songTime_.setVisible(isThereASong);
		
		// add labels and jslider to _SongBar
//		_SongBar.add(currentTime_); 								
//		_SongBar.add(timedSlider);
//		_SongBar.add(songTime_);
		
		// initialize _SongButtons and set layout
		_SongButtons = new JPanel(); 								
		_SongButtons.setLayout(new FlowLayout());
		
//		addSong_ goes here
		// initialize previousSong_ and add an action listener to previousSong_
//		previousSong_ = new JButton("\u2758" + "\u2758"); 						
//		previousSong_.addActionListener(new ActionListener() { 		
//			@Override public void actionPerformed(ActionEvent e) {
//				System.out.println("Moving to previous song...");
//				// do things here
//				
//				playPause_.setText("\u2758" + "\u2758");
				// the song is playing when it's all over
//				isSongPlaying = true;								
//			}	
//		});
		
		// initialize playPause_ and add an action listener to playPause_
		playPause_ = new JButton("\u25B6");							
		playPause_.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0) {
				// avoid it completely if playlist is a HomePanel 
				if (playlist.equals("Base Home") || playlist.equals("Home"));
				else {
					if (current!=null && current.isActive()) {	
						clipFrame = current.getFramePosition();
						current.stop();	
						isSongPlaying = false;
						
						pause = true;// procs ONLY if it's SearchPanel
						if (!playlist.equals("x")) {		
							if(songIndex == 0)	songIndex = songList.size()-1;
							else				songIndex--;
						}
						current.close();
						playPause_.setText("\u25B6"); 
						isSongPlaying = false;
					} else if ((current == null) || (current!=null && !current.isActive())) {
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
						}	} else {
							try {
								String [] arguments = {title_.getText() + "_" + artist_.getText() + "_" + album_.getText()  + ".wav","-1"};
								JSONObject obj = requestReply.UDPRequestReply("playSong", arguments, aSocket, serverPort);
								int size = obj.getInt("result");
								byteSong = new byte[size];
								isSongPlaying = true;
								packet = 0;
								pause = false;
								new Thread(new Runnable() {
									@Override public void run() {
										for(int i = 0; i<=size/64000; i++) {
											if(isSongPlaying) {
												try { // ClipSyn still has problem if you play a song, stop it, then play it again
													  // ClipSyn is used to make sure that only one clip linelistener is running at a time
													if(i>20 && i%10 == 0)
														if(clipSyn == true)	clipSyn = false;
													
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
													} else {
														System.arraycopy(reply.getData(), 0, byteSong, i*64000, size - i*64000);
														//System.out.println("numb: "+i);
												}	} catch (SocketException e) {
													System.out.println("Socket: " + e.getMessage());
												} catch (IOException e) {
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
												} clipSyn=true;
											}
										}
									});
								} catch(Exception e) {
									e.printStackTrace();
								}
								playPause_.setText("\u2758" + "\u2758");
							} catch ( IOException e1) {
								e1.printStackTrace();
							} //isSongPlaying = true;
						}
					}
				}
			}
		});
		
		// initializes nextSong_ and add an action listener to nextSong_
//		nextSong_ = new JButton("\u2758" + "\u2758");			
//		nextSong_.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent e) {
//				System.out.println("Moving to next song...");
//				//do things here
//				if(!(playlist.equals("x")))	{
//					if(!(songList.isEmpty())) {
//						try {
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
//							if(songIndex == songList.size() -1) songIndex = 0;
//							else								songIndex++;
//						} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
//							e1.printStackTrace();
//						}
//					}
//
//					playPause_.setText("\u2758" + "\u2758");
//					isSongPlaying = true;							
					// the song will begin playing when it's all over			
//				}
//			}});
	
//		add removeSong_ here
		
		// all these buttons initialize as un-clickables
		// addSong_
//		previousSong_.setEnabled(isThereASong);
//		playPause_.setEnabled(isThereASong);
//		nextSong_.setEnabled(isThereASong);
		// removeSong_
		// if there is a song, it will unlock and play automatically. until then...
		
		// add buttons to _SongButtons
//		_SongButtons.add(previousSong_); 						
		_SongButtons.add(playPause_); 	
//		_SongButtons.add(nextSong_);
		
		// add panels to Song_Panel
		Song_Panel.add(_SongBar); 								
		Song_Panel.add(_SongButtons);
		
		// add rigid areas and panels to LOW_panel and set border
		LOW_panel.add(Box.createRigidArea(new Dimension(1,0))); 	LOW_panel.add(Description_Panel);						
		LOW_panel.add(Box.createRigidArea(new Dimension(2,0)));		LOW_panel.add(Song_Panel);
		LOW_panel.add(Box.createRigidArea(new Dimension(1,0)));
		LOW_panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));				
		
		// add LOW_panel to pane (the content pane of frame)
		pane.add(LOW_panel); 
	}
	
	/**
	 * Get method. Returns the CorePanel
	 * @return CorePanel
	 */
	protected JPanel getCorePanel() {return CorePanel;}
	
	/**
	 * A static method that plays an audio input stream.
	 * @param music: the song that needs to be played
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
		if (name.contains("Search for:")) playlist = "x";
		else playlist = name;
		System.out.println("currently looking at " + name);
		isHistory = true;
		
		// ensure CorePanel front card is ShiftingPanel
//		if (isHistory == false) {
			((CardLayout)(CorePanel.getLayout())).show(CorePanel, SHIFT_PANEL);
			CorePanel.updateUI();
//		}
		isHistory = true;
	}
	
	protected void setHistory(boolean setting) {isHistory = setting;}
	
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
	 * @param source: the panel being added
	 * @param title: the label on the panel
	 * @param name: the name of the panel being added
	 */
	private void createNewHomePanel(JPanel source, JLabel title, String name) {
		// set specifics - size, layout, border, name - for source
		source.setSize(804,455);						
		source.add(title, BorderLayout.CENTER);
		source.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		source.setName(name);
		
		// add HomePanel to ShiftingPanel
		ShiftingPanel.addComponent(source);						
		ShiftingPanel.updateUI();
	}

	/**
	 * A void method that can make a frame visible.
	 * @param b boolean (true/false) that determines visibility of the frame (where true makes it visible, and false makes it not visible
	 */
	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
	/** ORIGIN: Profile.java
	 * Read playlists array from json file and add to gui list
	 * @param dm: defaultlistModel
	 */
	void getPlaylists(DefaultListModel<String> dm) {
		dm.clear(); //clear list 

		String [] arguments = {userName};
		JSONObject obj = requestReply.UDPRequestReply("getPlaylists",arguments, aSocket, serverPort);
		String playlist = obj.get("result").toString();	
		
		try {
			//add playlist to default list
			String [] playlistArray = playlist.substring(2, playlist.length() - 2).split("\",\"");
			for(int i = 0; i < playlistArray.length; i++) 
				dm.addElement(playlistArray[i]);
		} catch(StringIndexOutOfBoundsException e) {
			System.out.println("No available playlists for this user.");
		}
	}
	
	/** ORIGIN: Profile.java
	 * Remove playlist from JSON FILE
	 * @param playlist: playlist to be removed
	 * @param username: current login user
	 */
	void removePlaylist(String playlist, String username) {
		//Server side playlist removal
		String [] arguments = {username,playlist};
		requestReply.UDPRequestReply("removePlaylist",arguments, aSocket, serverPort);
	}

	/** ORIGIN: SearchMenuPanel.java
	 * Get search results 
	 * @param search: filter specification
	 */
	String[] getSearchResults(String search) {
		String [] arguments = {search, currentFilter};
		JSONObject obj = requestReply.UDPRequestReply("getSearch", arguments, aSocket, serverPort);
		String results  = obj.get("result").toString();
		
		if (results.length() < 3) return null;
		
		results = results.substring(2, results.length() - 2).replace("\",\"", "\n");
		System.out.println("These are the results:\n" + results);
		
		return results.split("\n");
	}
}
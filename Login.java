/* 
 * This file was created 8/30/2018 by Austin Tao.
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Login implements ActionListener {
	private JFrame frame;
	protected JTextField usernameField;
	protected JPasswordField passwordField;
	private JLabel nLabel, pLabel, eLabel;
	private JButton loginButton;
	private String message;
	private JPanel userPanel, passPanel, errorPanel, buttonPanel;
	private String specials = "[!@#$%&*()+=|<>?{}\\[\\]~-]";
	
	
	/*
	 * The driver. When run, the system calls the Login() constructor.
	 * NOTE: Can be commented out if need be.
	 */
	public static void main (String[] args) {
		new Login();
	}
	
	/*
	 * The main constructor. Constructs the frame for the login.
	 */
	public Login() {
		frame = new JFrame("'MusicService' Login");
		frame.setSize(300,175);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentsToPane(frame.getContentPane());
		frame.getRootPane().setDefaultButton(loginButton); //'ENTER' keystroke functionality set to loginButton
		frame.setVisible(true);
	}
	
	/*
	 * The extra panes constructor. Fills in the contents of the login frame.
	 */
	private void addComponentsToPane (Container pane){
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		
		userPanel = new JPanel();	userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));
		passPanel = new JPanel();	passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.LINE_AXIS));
		buttonPanel = new JPanel();	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		errorPanel = new JPanel();	errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.LINE_AXIS));
		
		nLabel = new JLabel("Username:");
		usernameField = new JTextField(); usernameField.setColumns(15);
		usernameField.setMaximumSize(usernameField.getPreferredSize());

		userPanel.add(nLabel);
		userPanel.add(Box.createRigidArea(new Dimension(5,0)));
		userPanel.add(usernameField);
		
		pLabel = new JLabel("Password:");
		passwordField = new JPasswordField(); passwordField.setColumns(15);
		passwordField.setMaximumSize(passwordField.getPreferredSize());
		
		passPanel.add(pLabel);
		passPanel.add(Box.createRigidArea(new Dimension(6,0)));
		passPanel.add(passwordField);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		
		loginButton = new JButton("Login"); loginButton.addActionListener(this);
		loginButton.setMaximumSize(loginButton.getPreferredSize());
		buttonPanel.add(loginButton);	
		
		eLabel = new JLabel("Error message"); 
		eLabel.setForeground(Color.RED); eLabel.setVisible(false);
		errorPanel.add(eLabel);
				
		pane.add(Box.createRigidArea(new Dimension(0,20)));		pane.add(userPanel);
		pane.add(Box.createRigidArea(new Dimension(0,7)));		pane.add(passPanel);
		pane.add(Box.createRigidArea(new Dimension(0,13)));		pane.add(buttonPanel);
		pane.add(Box.createRigidArea(new Dimension(0,10)));		pane.add(errorPanel);
	}
			
	/*
	 * The actionListener for the login button. When pressed, the system will test the typed-in username and password against
	 * current users in the system. If the username and password is found, then the login will let them through.
	 * Any mistake will be pointed out as an error message (in red).
	 */
	public void actionPerformed(ActionEvent e) {
		String user = usernameField.getText();
		String pass = String.valueOf(passwordField.getPassword());
		
		System.out.println("Typed User: " + user + "\nTyped Pass: " + pass); //debug
		
		if ((!user.isEmpty()) && (!pass.isEmpty())) {
			if (isUser(user) && codeDenial(user)) {
				if (codeDenial(pass) && confirmPassword(user, pass)) {
					System.out.println("Success. Redirecting..."); //debug
					frame.dispose(); 					//close the login
					//then redirect to homepage with their data
//					new Profile(user).setVisible(true); 	//version 1
					new Homepage(user).setVisible(true);	//version 2
				} else message = "Incorrect password; try again.";
			} else message = "No such user; try again.";
		} else message = "Fill out both boxes.";
		
		eLabel.setText(message);
		eLabel.setSize(eLabel.getPreferredSize());
		eLabel.setVisible(true);
	}
	
	/**
	 * A boolean method that tests if the system has a json file of the user 
	 * @param user: The name of the user
	 * @return a boolean determining if a user exists within the accounts currently logged in the service
	 */
	private boolean isUser (String user) {
		//find the existence of a user with this name from whatever json file they're stored in
		//if user == json.username_username then true
		try (InputStream input = new FileInputStream(user + ".json")) {
			JSONObject obj = new JSONObject(new JSONTokener(input));
		    String username = obj.get("username").toString();
		    return username.equals(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * A boolean method that checks if a given user exists in the list of accounts logged by the service, and is authentically
	 * the person they claim to be (using their password for proof).
	 * @param user: The name of the user
	 * @param pass: The password for the specified user
	 * @return a boolean determining if a password matches with the password logged in the given user's file
	 */
	private boolean confirmPassword (String user, String pass) {
		//find the user that has this username and this password from whatever json file they're stored in
		//if pass == json.username_pass then true
		try (InputStream input = new FileInputStream(user + ".json")) {
			JSONObject obj = new JSONObject(new JSONTokener(input));
		    String password = obj.get("password").toString();
		    return password.equals(pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

	/**
	 * A void method that can make a frame visible.
	 * @param b boolean (true/false) that determines visibility of the frame (where true makes it visible, and false makes it not visible
	 */
	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		frame.setVisible(b);
	}
}

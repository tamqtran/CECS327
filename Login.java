/* 
 * This file was created 8/30/2018 by Austin Tao.
*/
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login implements ActionListener {
	private JFrame frame;
	protected JTextField usernameField;
	protected JPasswordField passwordField;
	private JLabel n, p, E;
	protected JButton loginButton;
	private String message;
	private JPanel userPanel, passPanel, errorPanel, buttonPanel;
	private String specials = "[!@#$%&*()_+=|<>?{}\\[\\]~-]";
	
	public Login() {
		frame = new JFrame("'MusicService' Login");
		frame.setSize(300,175);
		
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentsToPane(frame.getContentPane());

		frame.setVisible(true);
	}
		
	public void addComponentsToPane (Container pane){
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
		
		userPanel = new JPanel();
		userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.LINE_AXIS));
		
		n = new JLabel("Username:");
		usernameField = new JTextField();	usernameField.setColumns(15);
		usernameField.setMaximumSize(usernameField.getPreferredSize());

		userPanel.add(n);
		userPanel.add(Box.createRigidArea(new Dimension(5,0)));
		userPanel.add(usernameField);
		
		passPanel = new JPanel();
		passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.LINE_AXIS));
		
		p = new JLabel("Password:");
		passwordField = new JPasswordField();	passwordField.setColumns(15);
		passwordField.setMaximumSize(passwordField.getPreferredSize());
		
		passPanel.add(p);
		passPanel.add(Box.createRigidArea(new Dimension(6,0)));
		passPanel.add(passwordField);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		loginButton = new JButton("Login"); 
		loginButton.setMaximumSize(loginButton.getPreferredSize());
		loginButton.addActionListener(this);
		buttonPanel.add(loginButton);
		
		errorPanel = new JPanel();
		errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.LINE_AXIS));
		E = new JLabel("Error message");	E.setForeground(Color.RED);
		E.setVisible(false);
		errorPanel.add(E);
				
		pane.add(Box.createRigidArea(new Dimension(0,20)));
		pane.add(userPanel);
		pane.add(Box.createRigidArea(new Dimension(0,7)));
		pane.add(passPanel);
		pane.add(Box.createRigidArea(new Dimension(0,13)));
		pane.add(buttonPanel);
		pane.add(Box.createRigidArea(new Dimension(0,10)));
		pane.add(errorPanel);
	}
	
	public static void main (String[] args) {
		new Login();
	}
	
	//If the user is in the system, then it will redirect to the main page with their data on it.
	//Otherwise, an error message will appear saying:
	//"Identify yourself on the text boxes above and click "Login"." (Neither box is filled out)
	//"Fill out both boxes to proceed." (Only one of the boxes is filled out)
	//"User not found. Please try again." (No such user found)
	//"Incorrect password. Please try again." (Incorrect password)
	//The error message will change based on the error made after clicking the login button.
	
	
	public void actionPerformed(ActionEvent e) {

		String user = usernameField.getText();
		char[] pass = passwordField.getPassword();
		System.out.println(user + " : " + String.valueOf(pass));
		
		if ((!user.isEmpty()) && (pass.length > 0)) {
			if (isUser(user) && codeDenialS(user)) {
				if (confirmPassword(user, pass) && codeDenialC(pass)) {
					System.out.println("Success. Redirecting...");
					//redirect to homepage with their data
					frame.dispose(); //then close the login
				} else message = "Incorrect password; try again.";
			} else message = "No such user; try again.";
		} else message = "Fill out both boxes.";
		
		E.setText(message);
		E.setSize(E.getPreferredSize());
		E.setVisible(true);
	}
	
	public boolean isUser (String user) {
		//find the existence of a user with this name from whatever .txt file they're stored in
		return false;
	}
	
	public boolean confirmPassword (String user, char[] pass) {
		//find the user that has this username and this password from whatever .txt file they're stored in
		return false;
	}
	
	public boolean codeDenialS (String input) {
		//denies if input has the following: "[!@#$%&*()_+=|<>?{}\\[\\]~-]" (code injection prevention)
		for (char c : input.toCharArray()) {
			for (char s : specials.toCharArray())
				if (s == c) return false;
		} return true;
	}
	
	public boolean codeDenialC (char[] input) {
		//denies if input has the following: "[!@#$%&*()_+=|<>?{}\\[\\]~-]" (code injection prevention)
		for (char c : input) {
			for (char s : specials.toCharArray())
				if (s == c) return false;
		} return true;
	}
}

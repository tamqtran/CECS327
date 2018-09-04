/* 
 * This file was created 8/30/2018 by Austin Tao.
*/
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login implements ActionListener {
	JFrame frame;
	JTextField usernameField;
	JPasswordField passwordField;
	JLabel n, p, E;
	JButton loginButton;
	String message, cmd;
	
	public Login() {
		frame = new JFrame("'MusicService' Login");
		frame.setSize(300,150);	frame.setLocationRelativeTo(null);
		frame.setLayout(null);	frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		n = new JLabel("Username:"); n.setLocation(20, 7);
		n.setSize(n.getPreferredSize());
		frame.add(n);
		
		usernameField = new JTextField();	usernameField.setColumns(15);
		usernameField.setSize(usernameField.getPreferredSize());
		usernameField.setLocation(105, 7);
		frame.add(usernameField);
		
		p = new JLabel("Password:");	p.setLocation(20, 35);
		p.setSize(p.getPreferredSize());
		frame.add(p);
		
		passwordField = new JPasswordField();	passwordField.setColumns(15);
		passwordField.setSize(passwordField.getPreferredSize());
		passwordField.setLocation(105, 35);
		frame.add(passwordField);
		
		E = new JLabel("Error message");
		E.setVisible(false);
		E.setSize(E.getPreferredSize());
		E.setLocation(10, 98);	
		E.setForeground(Color.RED);
		frame.add(E);
		
		loginButton = new JButton("Login");
		loginButton.setSize(loginButton.getPreferredSize());
		loginButton.setLocation(112,65);
		loginButton.addActionListener(this);
		frame.add(loginButton);
				
		frame.setVisible(true);
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
		if (isUser(user) && codeDenialS(user)) {
			char[] pass = passwordField.getPassword();
			if (confirmPassword(user, pass) && codeDenialC(pass)) {
				//redirect to homepage with their data
				//then close the login
			} else message = "Incorrect password; try again.";
		} else message = "No such user; try again.";
		E.setText(message);
		E.setSize(E.getPreferredSize());
		E.setVisible(true);
		
	}
	
	public boolean isUser (String user) {
		//find the existence of a user with this name from wherever they're stored
		return false;
	}
	
	public boolean confirmPassword (String user, char[] pass) {
		//find the user that has this username and this password
		return false;
	}
	
	public boolean codeDenialS (String input) {
		//denies if input has the following: ;://\!&=?, (code injection prevention)
		return false;
	}
	public boolean codeDenialC (char[] input) {
		//denies if input has the following: ;://\!&=?, (code injection prevention)
		return false;
	}
}

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
		
		loginButton = new JButton("Login");
		loginButton.setSize(loginButton.getPreferredSize());
		loginButton.setLocation(112,65);
		loginButton.addActionListener(this);
		frame.add(loginButton);
		
		E = new JLabel("Error message");
		E.setVisible(false);
		E.setSize(E.getPreferredSize());
		E.setLocation(105, 98);	E.setForeground(Color.RED);
		frame.add(E);
		
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
	
	
	public void actionPerformed(ActionEvent e ) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ok")) {
			String user = usernameField.getText();
			if (isUser(user)) {
				char[] pass = passwordField.getPassword();
				if (confirmPassword(user, pass)) {
					//redirect to homepage with their data
				}
			}
		}
	}
	
	public boolean isUser (String user) {
		return false;
	}
	
	public boolean confirmPassword (String user, char[] pass) {
		return false;
	}
	
	public boolean codeDenialS (String input) {
		return false;
	}
	public boolean codeDenialC (char[] input) {
		return false;
	}
}

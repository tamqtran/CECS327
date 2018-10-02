// This file has been created by Austin Tao on 10/1/2018.
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CreatePlaylistDialog extends JDialog implements ActionListener, PropertyChangeListener {

	private String typedText = null;
	private JTextField textField;
	private JOptionPane optionPane;
	
	private String specials = "[!@#$%&*()+=|<>?{}\\[\\]~-]";

	
	private String button1 = "Enter", button2 = "Cancel";
	
	public CreatePlaylistDialog(Frame homeFrame) {
		super(homeFrame, true); 
		textField = new JTextField(15);
		String msgString = "Name this new playlist:";
		
		Object[] array = {msgString, textField};
		Object[] options = {button1, button2};
		
		optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);
		
		setContentPane(optionPane);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
			}
		});
		
		textField.addActionListener(this);
		optionPane.addPropertyChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		// TODO Auto-generated method stub
		String prop = e.getPropertyName();
		if (isVisible() && (e.getSource() == optionPane) && 
				(JOptionPane.VALUE_PROPERTY.equals(prop) || 
				 JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();
			
			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				return;
			}
			
			if (button1.equals(value)) {
				typedText = textField.getText();
				if (!codeDenial(typedText)) {
					textField.selectAll();
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, 
						"This ("+ typedText + ") has unallowed special characters", "Try again", JOptionPane.ERROR_MESSAGE);
					typedText = null;
					textField.requestFocusInWindow();
				} else if (checkPlaylistNameUniqueness(typedText)) {
					clearAndHide();
					// playlist creation goes here
					
					System.out.println("The new playlist " + typedText + " has been created. Returning...");
				} else {
					textField.selectAll();
					JOptionPane.showMessageDialog(CreatePlaylistDialog.this, 
						"This ("+ typedText + ") is not unique", "Try again", JOptionPane.ERROR_MESSAGE);
					typedText = null;
					textField.requestFocusInWindow();
					System.out.println("This ("+ typedText + ") is not unique");
				}
			} else {
				typedText = null;
				clearAndHide();
				System.out.println("Subwindow has been hidden away...");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		optionPane.setValue(button1);
	}
	
	private boolean checkPlaylistNameUniqueness(String name) {
		// define name against all other playlist names in the user's account
		// true if name is unique, false otherwise
		
		return false;
	}
	
	public void clearAndHide() {
		textField.setText(null);
		setVisible(false);
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
}

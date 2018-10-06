// Created by Austin Tao on 10/5/2018

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * This class is a JLayeredPane with a few extra methods meant to have inner panel respond to size changes of this pane.
 * @author Austin Tao
 * @since 10/5/2018
 *
 */
public class ShiftingPanel extends JLayeredPane 
{
	
	private String username_;
	
	/**
	 * Main constructor. Creates a JLayeredPane called ShiftingPanel
	 * @param user the name of the user
	 */
	public ShiftingPanel(String user) 
	{
		username_ = user;														// assign user to username_
		this.setMinimumSize(new Dimension(500,500));							// set minimum size
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));	// set border
	}
	
	/**
	 * A method that assigns a component listener w/ a particular componentResized() method to the input panel
	 * @param panel a JPanel
	 */
	public void addResizeListenerTo(JPanel panel) 
	{
		this.addComponentListener(new ComponentAdapter() 		// assign component listener to panel
		{		
			public void componentResized(ComponentEvent e) 
			{			
				panel.setSize(getSize());  					//the size of the panel becomes the size of ShiftingPanel
			}
		});
	}
}

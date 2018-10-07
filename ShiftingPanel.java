// Created by Austin Tao on 10/5/2018

import java.awt.Component;
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
	private Component[] history;
	
	private Integer baseComponent = 0;
	private final Integer N_ZERO = new Integer(-1), ZERO = new Integer(0);
	
	/**
	 * Main constructor. Creates a JLayeredPane called ShiftingPanel
	 * @param user the name of the user
	 */
	public ShiftingPanel() 
	{
		this.setMinimumSize(new Dimension(501,501));							// set minimum size
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));	// set border
		
		
	}
	
	/**
	 * A method that assigns a component listener w/ a particular componentResized() method to the input panel
	 * @param panel a JPanel
	 */
	public void addResizeListenerTo(JPanel panel) 
	{
		this.addComponentListener(new ComponentAdapter() 		// assign component listener to ShiftingPanel
		{		
			public void componentResized(ComponentEvent e) 
			{			
				panel.setSize(getSize());  					//the size of the panel becomes the size of ShiftingPanel
			}
		});
	}
	
	/**
	 * This particular method will add the component to the zeroth layer of ShiftingPanel and adjust the history array
	 * and the components accordingly to this new inclusion
	 * @param c the component that is being added to ShiftingPanel
	 */
	public void addComponent(Component c) {
		
		//move current component to layer -1
		for (Component p : this.getComponentsInLayer(ZERO))
			this.setLayer(p, N_ZERO);
		// order components in layer -1 by order
		for (Component p : this.getComponentsInLayer(N_ZERO))
			this.moveToFront(p);
		
		this.add(c, JLayeredPane.DEFAULT_LAYER, ZERO);			// add component to layer 0
		history = this.getComponents();						// update history
		
		for (Component p : history)						//system: the layer the component is in
			System.out.println("Layer: " + this.getLayer(p) 	// the position of each component in the layer
			+ ", pos: " + this.getPosition(p) + ", name: " + p.getName());	// and the name of each component
		System.out.println("baseComponent is 0 of " + baseComponent++ + "\n");
	}
	
	// listeners for the previousHistory_ and nextHistory_ buttons will go here
	// what the buttons will do:
	// on initialization, neither should be clickable
	// once another panel has been added to ShiftingPanel, the previousHistory_ should become clickable
	// if the current panel is the oldest layer (this will be HomePanel) then only the nextHistory_ button will be clickable
	// if the current panel is the newest layer, then only the previousHistory_ button will be clickable
	// otherwise both buttons are clickable
	// clicking previousHistory_ once will change the panel to the previous layer before the current
	// clicking nextHistory_ once will change the panel to next layer after the current
}

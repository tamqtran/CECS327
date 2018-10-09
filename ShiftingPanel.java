// Created by Austin Tao on 10/5/2018

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
	private String username_, currentPanel, songName;
	protected Component[] history, previousPanels, nextPanels;
	
	private JButton prevButton, nextButton;
	
	private Integer baseComponent = -1;
	protected Integer currentComponent = 0;
	private final Integer N_TWO = new Integer(-2), N_ONE = new Integer(-1), ZERO = new Integer(0);
	
	/**
	 * Main constructor. Creates a JLayeredPane called ShiftingPanel
	 * @param user the name of the user
	 */
	public ShiftingPanel() 
	{
		this.setMinimumSize(new Dimension(501,501));							// set minimum size
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));	// set border
		
		previousPanels = this.getComponentsInLayer(N_ONE);
		nextPanels = this.getComponentsInLayer(N_TWO);
	}
	
	/**
	 * A method that assigns a component listener w/ a particular componentResized() method to the input panel.
	 * @param panel: a JPanel
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
	 * and the components accordingly to this new inclusion.
	 * @param c: the component that is being added to ShiftingPanel
	 */
	public void addComponent(Component c) 
	{
		//move current panel in layer 0 to layer -1 in pos 0
		for (Component p : this.getComponentsInLayer(ZERO))
			this.setLayer(p, N_ONE, 0);
		
		this.add(c, JLayeredPane.DEFAULT_LAYER, ZERO);			// add component to layer 0		
		
		addResizeListenerTo((JPanel)c);
		
		if (nextPanels.length > 0) 								// if the length of nextPanels is zero, then the following is skipped
		{
			for (Component p : this.getComponentsInLayer(N_TWO)) 
			{
				this.remove(p);
				currentMovedForward();			// for each item in nextPanels, remove that item from there
				baseComponent--;				// decrement baseComponent to compensate for each panel that is lost
			}
		}
		
		baseComponent++;						// add one to baseComponent location
		checkHistory();							// show current history parameters	
	}
	
	/**
	 * A void method that adds an action listener to one of the two JButtons it's used for. 
	 * Both buttons together function as the panel switcher for ShiftingPanel.
	 * @param button: a JButton, either previousHistory_ or nextHistory_
	 */
	public void setHistorySwitch(JButton button) {
		switch(button.getName()) {
		case "previous panel": 
			prevButton = button;	// assign previousHistory_ as prevButton and assign an action listener to it
			prevButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Moving to previous panel...");	// System announcement
					
					// the current panel would be moved into nextPanels in pos 0 (shift)
					
					for (Component p: getShiftingPanel().getComponentsInLayer(ZERO)) 
						getShiftingPanel().setLayer(p, N_TWO, 0);
					
					// the panel in pos 0 of previousPanels would become the current panel (shift)
					
					getShiftingPanel().setLayer(getShiftingPanel().getComponentsInLayer(N_ONE)[0], ZERO, 0);
					
					
					currentMovedBack();		//update history and compensate currentComponent
										
					checkHistory();
				}
			});
			break;
		case "next panel": 
			nextButton = button;	// assign nextHistory_ as nextButton and assign an action listener to it
			nextButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Moving to next panel...");	//System announcement
					
					// the current panel would be moved into previousPanels in pos 0 (shift)
					
					for (Component p: getShiftingPanel().getComponentsInLayer(ZERO)) 
						getShiftingPanel().setLayer(p, N_ONE, 0);
					
					
					// the panel in pos 0 for nextPanels would become the current panel (shift)
					
					getShiftingPanel().setLayer(getShiftingPanel().getComponentsInLayer(N_TWO)[0], ZERO, 0);
					
					currentMovedForward();		//compensate currentComponent
					
					checkHistory();
				}
			});
			break;
		}
	}
	
	/**
	 * A get method that returns currentComponent parameter.
	 * @return currentComponent, an Integer representing the index of the current panel.
	 */
	protected Integer getCurrentPanelIndex() {return currentComponent;}
	
	/**
	 * A get method that returns the currentPanel parameter.
	 * @return currentPanel, the string name of the current panel of ShiftingPanel.
	 */
	protected String getCurrentPanelName() {return currentPanel;}
	
	/**
	 * A get method that returns the history parameter.
	 * @return history, an array of Components for ShiftingPanel
	 */
	protected Component[] getHistory() {return history;}
	
	/**
	 * A get method that returns this ShiftingPanel.
	 * @return this ShiftingPanel class
	 */
	protected ShiftingPanel getShiftingPanel() {return this;}	
	
	/**
	 * A void method that increments currentComponent.
	 */
	protected void currentMovedBack() {currentComponent++;}
	
	/**
	 * A void method that decrements currentComponent.
	 */
	protected void currentMovedForward() {currentComponent--;}
	
	/**
	 * This void method removes all instances of a given playlist name from the history parameter.
	 * @param playlistName: the name of the playlist that is being removed
	 */
	public void removeFromHistory(String playlistName) {
		System.out.print("Before:");				// system: before the wipe
		checkHistory();
		
		int shift = 0, i = 0;
		for (Component p : history) 				// for each component in history...
		{
			if (p.getName().equals(playlistName)) 	// if that component has the same name as playlistName...
			{
				this.remove(i);						// remove that element at i
				++shift;							// pre-increment the shift
			}
			else i++;								// otherwise increment i
		}
		baseComponent -= shift;						// compensate baseComponent with shift
		
		System.out.println("Middle:");				// system: all instances of playlistName removed from history
		checkHistory();
		
		reduceFurther();	// redundancy checker
		
		System.out.println("After:");				// system: after clearing the redundancies.
		checkHistory();
	}
	
	/**
	 * A void method that removes redundancies within the history parameter.
	 * Ex: HOME, king, queen, king, jack -> remove queen -> HOME, king, king, jack -> reduceFurther() -> HOME, king, jack
	 */
	private void reduceFurther() {
		history = this.getComponents();
		int shift = 0;
		int i = 0;
		while(i < history.length-1) 
		{
			//check for adjacent elements that have the same name - if found, remove the second one
			if (history[i].getName().equals(history[i+1].getName())) 
			{
				this.remove(i+1);					// removes the second one
				++shift;							// add one to shift
				history = this.getComponents();		//update history
			} 
			else if ((history[i].getName().equals("Home") && history[i+1].getName().equals("Base Home")))
			{	// singular scenario: base home -> playlist -> home, delete playlist
				this.remove(i);	// removes item at index i from history
				this.setLayer(history[i+1], ZERO, 0);	// sends the item at index i+1 to layer 0
				++shift;								// proceed as normal
				history = this.getComponents();
			}
			else i++;
		}
		baseComponent -= shift;						//push compensation shift to baseComponent
	}
	
	/**
	 * A void method that updates, then lists out a bunch of relevant information about the user's settings in regards to ShiftingPanel.
	 */
	private void checkHistory() {
		updateHistory();		// update history parameters
		System.out.println("Layer 0 is the current panel; Layer -1 is previous panels; Layer -2 is future panels:");
		int i = 0;
		for (Component p : history)						//system: the layer the component is in
			System.out.println("Index: " + i++ + ", Layer: " + this.getLayer(p) 	// the position of each component in the layer
			+ ", pos: " + this.getPosition(p) + ", name: " + p.getName());	// and the name of each component
		System.out.println("baseComponent is at index " + baseComponent 
				+ "\ncurrentPanel is " + currentPanel + " at index " + currentComponent + "\n");
	}
	
	/**
	 * A void method that updates all history parameters and sets the history buttons enable/disable functions.
	 */
	private void updateHistory() {
		history = this.getComponents();						// assign history to the list of components of ShiftingPanel
		currentPanel = history[0].getName();				// assign currentPanel as the name of the first component in history
		previousPanels = this.getComponentsInLayer(N_ONE);	// assign previousPanels as the list of components in layer -1 of ShiftingPanel
		nextPanels = this.getComponentsInLayer(N_TWO);		// assign nextPanels as the list of components in layer -2 of ShiftingPanel
		
		// system: tell the total number of panels in history, total number of previous panels, and total number of future panels
		System.out.println(history.length + " total, " + previousPanels.length + " prev, " + nextPanels.length + " next");
		
		if (previousPanels.length > 0) prevButton.setEnabled(true); else prevButton.setEnabled(false);	// set history buttons enable/disable
		if (nextPanels.length > 0) nextButton.setEnabled(true); else nextButton.setEnabled(false);
	}
	
	public String getSong() {
		if (history[0].getClass() == PlaylistPanel.class) {
			songName = ((PlaylistPanel)history[0]).getListener().getSong();
		}
		else if (history[0].getClass() == SearchMenuPanel.class) {
			songName = ((SearchMenuPanel)history[0]).getSong();
		} else songName = null;
		
		System.out.println("ShiftingPanel sees " + songName);
		
		return songName;
	}
}

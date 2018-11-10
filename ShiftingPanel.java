// Created by Austin Tao on 10/5/2018

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
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
@SuppressWarnings("serial")
public class ShiftingPanel extends JLayeredPane 
{
	private Homepage homeFrame;
	
	private String currentPanel, songName;
	protected Component[] history, previousPanels, nextPanels;
	
	private JButton prevButton, nextButton;
	
	private Integer baseComponent = -1;
	protected Integer currentComponent = 0;
	private final Integer N_TWO = new Integer(-2), N_ONE = new Integer(-1), ZERO = new Integer(0);
	
	/**
	 * Main constructor. Creates a JLayeredPane called ShiftingPanel
	 * @param home: the home panel
	 */
	public ShiftingPanel(Homepage home) {
		homeFrame = home;
		this.setMinimumSize(new Dimension(501,501));							// set minimum size
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));	// set border
		
		previousPanels = this.getComponentsInLayer(N_ONE);
		nextPanels = this.getComponentsInLayer(N_TWO);
	}
	
	/**
	 * A method that assigns a component listener w/ a particular componentResized() method to the input panel.
	 * @param panel: a JPanel
	 */
	protected void addResizeListenerTo(JPanel panel) {
		this.addComponentListener(new ComponentAdapter() { 		// assign component listener to ShiftingPanel
			public void componentResized(ComponentEvent e) {			
				panel.setSize(getSize());  					//the size of the panel becomes the size of ShiftingPanel
				panel.updateUI();
			}
		});
	}
	
	/**
	 * This particular method will add the component to the zeroth layer of ShiftingPanel and adjust the history array
	 * and the components accordingly to this new inclusion.
	 * @param c: the component that is being added to ShiftingPanel
	 */
	protected void addComponent(Component c) {
		//move current panel in layer 0 to layer -1 in pos 0
		for (Component p : this.getComponentsInLayer(ZERO))
			this.setLayer(p, N_ONE, 0);
		
		this.add(c, JLayeredPane.DEFAULT_LAYER, ZERO);			// add component to layer 0		
		
		c.setSize(getSize());					// set the size of the new component to the size of ShiftingPanel
		addResizeListenerTo((JPanel)c);
				
		if (nextPanels.length > 0) {			// if the length of nextPanels is zero, then the following is skipped
			for (Component p : this.getComponentsInLayer(N_TWO)) {
				this.remove(p);					
				currentMovedForward();			// for each item in nextPanels, remove that item from there
				baseComponent--;				// decrement baseComponent to compensate for each panel that is lost
			}
		}
		
		baseComponent++;						// add one to baseComponent location
		reduceFurther();						// nothing should happen here

		updateAndCheck();
		
		setCurrentPlaylistInHomepage();
	}
	
	/**
	 * A void method that adds an action listener to one of the two JButtons it's used for. 
	 * Both buttons together function as the panel switcher for ShiftingPanel.
	 * @param button: a JButton, either previousHistory_ or nextHistory_
	 */
	protected void setHistorySwitch(JButton button) {
		switch(button.getName()) {
		case "previous panel": 
			prevButton = button;	// assign previousHistory_ as prevButton and assign an action listener to it
			prevButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Moving to previous panel...");	// System announcement

					// the current panel would be moved into nextPanels in pos 0 (shift)
					for (Component p: getShiftingPanel().getComponentsInLayer(ZERO)) 
						getShiftingPanel().setLayer(p, N_TWO);

					// the panel in pos 0 of previousPanels would become the current panel (shift)

					getShiftingPanel().setLayer(getShiftingPanel().getComponentsInLayer(N_ONE)[0], ZERO, 0);

					currentMovedBack();		//update history and compensate currentComponent

					updateAndCheck();
				}
			});
			break;
		case "next panel": 
			nextButton = button;	// assign nextHistory_ as nextButton and assign an action listener to it
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("Moving to next panel...");	//System announcement

					// the current panel would be moved into previousPanels in pos 0 (shift)		
					for (Component p: getShiftingPanel().getComponentsInLayer(ZERO)) 
						getShiftingPanel().setLayer(p, N_ONE, 0);

					// the panel in pos 0 for nextPanels would become the current panel (shift)

					getShiftingPanel().setLayer(getShiftingPanel().getComponentsInLayer(N_TWO)[nextPanels.length-1], ZERO, 0);

					currentMovedForward();		//compensate currentComponent

					updateAndCheck();
				}
			});
			break;
		default: 
			break;
		}
	}
	
	/**
	 * A void method that sets a variable in a related Homepage object to the name of the current panel in ShiftingPanel.
	 */
	private void setCurrentPlaylistInHomepage() {homeFrame.setCurrentPlaylist(currentPanel);}
	
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
	 * A get method that returns the current song selected in that panel. The code follows through on this get method regardless of class.
	 * @return: a string stating the title, artist(s), and album of the currently selected song
	 */
	protected String getSong() {
		if (history[0].getClass() == PlaylistPanel.class) {
			songName = ((PlaylistPanel)history[0]).getListener().getSong();
		} else if (history[0].getClass() == SearchMenuPanel.class) {
			songName = ((SearchMenuPanel)history[0]).getSong();
		} else songName = null;
		
		System.out.println("ShiftingPanel sees " + songName);
		
		return songName;
	}
	
	/**
	 * A void method that increments currentComponent.
	 */
	protected void currentMovedBack() {currentComponent++; baseComponent--;}
	
	/**
	 * A void method that decrements currentComponent.
	 */
	protected void currentMovedForward() {currentComponent--; baseComponent++;}
	
	/**
	 * This void method removes all instances of a given playlist name from the history parameter.
	 * @param playlistName: the name of the playlist that is being removed
	 */
	protected void removeFromHistory(String playlistName) {
		System.out.print("Before:");				// system: before the wipe
		checkHistory();
		
		int shift = 0, i = 0;
		for (Component p : history) {				// for each component in history...
			if (p.getName().equals(playlistName)) {	// if that component has the same name as playlistName...
				this.remove(i);						// remove that element at i
				++shift;							// pre-increment the shift
			} else i++;								// otherwise increment i
		}
		baseComponent -= shift;						// compensate baseComponent with shift
		
		System.out.println("Middle:");				// system: all instances of playlistName removed from history
		checkHistory();
		
		reduceFurther();							// redundancy checker
		
		System.out.println("After:");				// system: after clearing the redundancies.
		updateAndCheck();
	}
	
	/**
	 * A void method that removes redundancies within the history parameter.
	 * Ex: HOME, king, queen, king, jack -> remove queen -> HOME, king, king, jack -> reduceFurther() -> HOME, king, jack
	 */
	private void reduceFurther() {
		history = this.getComponents();
		int shift = 0, i = 0;
		while(i < history.length-1) {
			//check for adjacent elements that have the same name - if found, remove the second one
			if (history[i].getName().equals(history[i+1].getName())) {
				this.remove(i+1);					// removes the second one
				++shift;							// add one to shift
				history = this.getComponents();		//update history
			} else if ((history[i].getName().equals("Home") && history[i+1].getName().equals("Base Home")))	{	
				// singular scenario: 'base home -> playlist -> home', delete playlist, 'base home -> home' to 'base home'
				this.remove(i);	// removes item at index i from history
				this.setLayer(history[i+1], ZERO, 0);	// sends the item at index i+1 to layer 0
				++shift;								// proceed as normal
				history = this.getComponents();		//update history
			} else i++;
		}
		baseComponent -= shift;						//push compensation shift to baseComponent
	}
	
	private void updateAndCheck() {
		updateEverything();		// update every panel in ShiftingPanel, and ShiftingPanel
		checkHistory();			// check the state of ShiftingPanel
	}
	
	/**
	 * A void method that updates, then lists out a bunch of relevant information about the user's settings in regards to ShiftingPanel.
	 */
	private void checkHistory() {
		int i = 0;
		for (Component p : history)	{					//system: the layer the component is in
			System.out.print("Index: " + i + "\tLayer: " + this.getLayer(p) 	// the position of each component in the layer
			+ "\tpos: " + this.getPosition(p) + "\t  name: " + p.getName());			// and the name of each component

			System.out.println(((i++ == 0) && p.getName().equals(currentPanel)) ? "\t<CURRENT>" : "");

		} System.out.println("Indices of -- baseComponent: " + baseComponent + ", currentComponent: " + currentComponent + "\n");
	}
	
	/**
	 * A void method that updates all history parameters and sets the history buttons enable/disable functions.
	 */
	private void updateEverything() {
		history = this.getComponents();						// assign history to the list of components of ShiftingPanel
		currentPanel = history[0].getName();				// assign currentPanel as the name of the first component in history
		previousPanels = this.getComponentsInLayer(N_ONE);	// assign previousPanels as the list of components in layer -1 of ShiftingPanel
		nextPanels = this.getComponentsInLayer(N_TWO);		// assign nextPanels as the list of components in layer -2 of ShiftingPanel
		
		// this will:
		// - change visibility for all the panels in history s.t. only the current panel (at index 0 of history) is functional
		// - update the panels to any changes that may have occurred in one panel (playlist panels only)
		for (int n = 0; n < history.length; n++) {
			history[n].setVisible((n == 0) ? true : false);
			
			if (history[n].getClass() == PlaylistPanel.class)
				((PlaylistPanel)history[n]).updatePlaylist();
		}
		
		// system: tell the total number of panels in history, total number of previous panels, and total number of future panels
		System.out.println("\nShiftingPanel layers: " + history.length + " total, " 
		+ previousPanels.length + " prev, 1 current, " + nextPanels.length + " next");
		
		setCurrentPlaylistInHomepage();
		
		// enable or disable the history buttons depending on the emptiness of the previousPanels and nextPanels variables
//		if (previousPanels.length > 0) prevButton.setEnabled(true); else prevButton.setEnabled(false);	
		prevButton.setEnabled((previousPanels.length > 0) ? true : false);
//		if (nextPanels.length > 0) nextButton.setEnabled(true); else nextButton.setEnabled(false);
		nextButton.setEnabled((nextPanels.length > 0) ? true : false);
	}
}

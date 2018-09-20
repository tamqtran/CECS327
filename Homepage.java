/*
 * This file was created by Austin Tao on 9/20/2018.
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Homepage {
	private JFrame frame;
	protected JTextField searchField;
	private JPanel column, mainBox, highRow, lowRow, playlistOptions, songOptions, playOptions, songLine;

	public static void main(String[] args) {
		new Homepage("allan");
	}
	
	public Homepage(String user) {
		frame = new JFrame("MusicService -- " + user);
		frame.setMinimumSize(new Dimension(600,400));
		frame.setSize(new Dimension(1000,600));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentsToHome(frame.getContentPane());
		
		frame.setVisible(true);
	}
	
	public void addComponentsToHome(Container pane) {
		pane.setLayout(new BorderLayout());
		
		column = new JPanel(); column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
		highRow = new JPanel(); highRow.setLayout(new BoxLayout(highRow, BoxLayout.X_AXIS));
		mainBox = new JPanel(); // will be updated continuously based on user input and actions
		lowRow = new JPanel(); highRow.setLayout(new BoxLayout(lowRow, BoxLayout.Y_AXIS));
		
//		column: playlist list (box field - scrollable as necessary), playlistOptions(panel for add/remove buttons), 
//		     songOptions(for saveSong/favorite), playOptions(previous song, play/pause, next song)
//		highRow: search bar, profile section (name and logout button, FOR NOW)
//		mainBox: whatever panels happen to be (depending on user choices) - scrollable as necessary
//		lowRow: song line (current/total time of song, song progress bar(adjustable)), song description (title, artist, album)
		
	}

}

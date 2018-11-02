import java.awt.Dimension;

import javax.swing.*;

@SuppressWarnings("serial")
public class SearchMenuFrame extends JFrame {
	
	@SuppressWarnings("unused")
	private String username;

  public static void main(String[] args) {
	  String [] list = {};
	  new SearchMenuFrame("bill", list , "").setVisible(true);
  }
	
  public SearchMenuFrame(String username, String[] list, String searchFor) {
	this.username = username;
    setTitle("Song Search");
    setResizable(true);
    setSize(700,700);
    setMinimumSize(new Dimension(400,400));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new SearchMenuPanel(username, list, searchFor);
    this.add(panel);
  }
  
}
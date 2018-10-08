import java.awt.*;
import javax.swing.*;

public class SearchMenuFrame extends JFrame {
	
	private String username;

  public static void main(String[] args) {
	  new SearchMenuFrame("bill").setVisible(true);
  }
	
  public SearchMenuFrame(String username) {
	this.username = username;
    setTitle("Song Search");
    setResizable(false);
    setSize(700,700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    JPanel panel = new SearchMenuPanel(username);
//    this.add(panel);
  }
  
}
import javax.swing.*;

@SuppressWarnings("serial")
public class SearchMenuFrame extends JFrame {
	
	@SuppressWarnings("unused")
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
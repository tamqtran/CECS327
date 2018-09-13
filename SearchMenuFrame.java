import java.awt.*;
import javax.swing.*;

public class SearchMenuFrame extends JFrame {

  public SearchMenuFrame() {
    setTitle("Song Search");
    setResizable(false);
    setSize(500,400);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new SearchMenuPanel();
    this.add(panel);
  }
  
}
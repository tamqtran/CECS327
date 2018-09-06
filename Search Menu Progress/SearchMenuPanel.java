import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class SearchMenuPanel extends JPanel {
  private JTextField searchTextField;
  private JTable results;

  public SearchMenuPanel() {
    this.setLayout(new BorderLayout());

    searchTextField = new JTextField("Search for song title, album, or artist", 20);
    //Wipe textfield once you click it
    searchTextField.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        searchTextField.setText("");
      }});
    this.add(searchTextField, BorderLayout.NORTH);
    
    //JTable data
    String data[][] = { {"data1","data2","data3"}, {"data4","data5","data6"},{"data7","data8","data9"}};
    ArrayList<String> songSort = new ArrayList<String>();
    songSort.add("Song Title");
    songSort.add("Artist");
    songSort.add("Album");
    
    
    results = new JTable(data, songSort.toArray(new String[songSort.size()]));
    results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    //News JScrollPane else column names wont show up
    this.add(new JScrollPane(results), BorderLayout.CENTER);
  }
}
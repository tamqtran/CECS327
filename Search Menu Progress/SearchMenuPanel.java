import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.Document;
import javax.swing.table.*;

public class SearchMenuPanel extends JPanel implements DocumentListener{
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
    
    searchTextField.getDocument().addDocumentListener(this);
      
    this.add(searchTextField, BorderLayout.NORTH);
    
    String data[][] = { {"data1","data2","data3"}, {"data4","data5","data6"},{"data7","data8","data9"}};
    String[] columns = {"Song Title","Artist", "Album"};
    
    DefaultTableModel model = new DefaultTableModel(data, columns);
    
    results = new JTable(model);
    results.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    //News JScrollPane else column names wont show up
    this.add(new JScrollPane(results), BorderLayout.CENTER);
  }

  public void insertUpdate(DocumentEvent e) {
        String[] searchResults = search(searchTextField.getText());
        
        //Clears the Jtable
        DefaultTableModel model = (DefaultTableModel) results.getModel();
        model.setRowCount(0);
        
        for(int i = 0; i < searchResults.length; i++) {
          model.addRow(searchResults[i].split("_"));
        };
  }
  public void removeUpdate(DocumentEvent e) {
    String[] searchResults = search(searchTextField.getText());
        
        //Clears the Jtable
        DefaultTableModel model = (DefaultTableModel) results.getModel();
        model.setRowCount(0);
        
        for(int i = 0; i < searchResults.length; i++) {
          model.addRow(searchResults[i].split("_"));
        };
  }
  public void changedUpdate(DocumentEvent e) {
  }
  public String[] search(String text) {
    //Desired Music extension
    final String EXT = ".txt";
    
    //System command to grab current working directory
    String currentFolderPath = System.getProperty("user.dir");
    File currentFolder = new File(currentFolderPath);

    //list() vs listFiles()
    //List- string array
    //listFiles - files array
    //Doing list() reduces unnecessary code
    String[] FilesInFolder = currentFolder.list();
    
    //Storage for results
    ArrayList<String> searchResults = new ArrayList<String>();
    
    for (int i = 0; i < FilesInFolder.length; i++) {
      if (FilesInFolder[i].endsWith(EXT) && FilesInFolder[i].contains(text))
        searchResults.add(FilesInFolder[i].replace(EXT, ""));
    };
    
    return searchResults.toArray(new String[searchResults.size()]);
  }
  
}
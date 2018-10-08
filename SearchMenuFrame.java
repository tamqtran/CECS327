import java.awt.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.*;
import java.awt.event.*;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.swing.table.*;

/**
 * 
 * @author Vincent Vu
 * @since 09-06-2018
 *
 */
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
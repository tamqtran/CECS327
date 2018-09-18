import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayButton {
	static String uName = "";
	static String song = "";
	static String playlist = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// construct the frame
		JFrame frame = new PlayFrame("Nirvana_All Apologies_In Utero", "Cool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	static class PlayFrame extends JFrame{

		PlayFrame(String s, String u, String pL)
		{
			uName = u;
			playlist = pL;
			song = s;
			setTitle("Play");
			setSize(500, 200);
			JPanel p = new PlayPanel(u, pL);
			this.add(p);
		}
		PlayFrame(String s, String u)
		{
			uName = u;
			playlist = "";
			song = s;
			setTitle("Play");
			setSize(500, 200);
			JPanel p = new PlayPanel(u);
			this.add(p);
		}
	}
	static class PlayPanel extends JPanel{
		
		JButton play = new JButton("\u25B6");
		JButton back = new JButton("Back");
		JButton pause = new JButton("\u25F8");
		//JLabel title = new JLabel("Nirvana - All Apologies");
		PlayPanel(String u)
		{
			play.setFont(new Font("Dialog", Font.PLAIN, 30));
			ActionListener a = new YActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			//p1.add(title, BorderLayout.PAGE_START);
			p1.add(back, BorderLayout.WEST);
			p1.add(pause, BorderLayout.EAST);
			p1.add(play, BorderLayout.CENTER);
			pause.addActionListener(a);
			play.addActionListener(a);
			back.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.CENTER);
		}
		PlayPanel(String u, String p)
		{
			play.setFont(new Font("Dialog", Font.PLAIN, 30));
			ActionListener a = new XActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			//p1.add(title, BorderLayout.PAGE_START);
			p1.add(back, BorderLayout.WEST);
			p1.add(pause, BorderLayout.EAST);
			p1.add(play, BorderLayout.CENTER);
			play.addActionListener(a);
			pause.addActionListener(a);
			back.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.CENTER);
		}
		class YActionListener implements ActionListener{
			private PlayPanel panel;
			Clip current;
			int pos = 0;

			public YActionListener(PlayPanel b)
			{
				this.panel = b;
			}
			/**
			 * Implement ActionListener
			 * @param e ActionEvent
			 */
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Object o = e.getSource();
				if(o == panel.play)
				{
					try {
						System.out.println(song);
						File file = new File(song + ".wav");
						AudioInputStream player = AudioSystem.getAudioInputStream(file);
						current = AudioSystem.getClip();
						current.open(player);
						current.setFramePosition(pos);
						current.start();
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if(o == panel.back)
				{
					if(current != null)
					{
						if(current.isActive())
						{
							current.stop();
						}
						current.flush();
						current.close();
						
					}
					JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
					pFrame.dispose();
					new SearchMenuFrame(uName).setVisible(true);
				}
				else if(o == panel.pause)
				{
					if(current != null)
					{
						if(current.isActive())
						{
							pos = current.getFramePosition();
							current.stop();
						}
					}
				}
			}
		}
		class XActionListener implements ActionListener{
			private PlayPanel panel;
			Clip current;
			int pos = 0;

			public XActionListener(PlayPanel b)
			{
				this.panel = b;
			}
			/**
			 * Implement ActionListener
			 * @param e ActionEvent
			 */
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				Object o = e.getSource();
				if(o == panel.play )
				{
					if((current == null) || (current!=null && (current.isActive())))
					{
						try {
							System.out.println(song + ".wav");
							File file = new File(song + ".wav");
							AudioInputStream player = AudioSystem.getAudioInputStream(file);
							current = AudioSystem.getClip();
							current.open(player);
							current.setFramePosition(pos);
							current.start();
						} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else if(o == panel.back)
				{
					if(current != null)
					{
						if(current.isActive())
						{
							current.stop();
						}
						current.setFramePosition(0);
						current.flush();
						current.close();
					}
					JFrame pFrame = (JFrame) panel.getTopLevelAncestor();
					pFrame.dispose();
					if(playlist.isEmpty())
					{
						new SearchMenuFrame(uName).setVisible(true);
					}
					else 
					{
						new PlaylistFrame(uName, playlist).setVisible(true);
					}
				}
				else if(o == panel.pause)
				{
					if(current!= null)
					{
						if(current.isActive())
						{
							pos = current.getFramePosition();
							current.stop();
						}
					}
					
				}
			}
		}
	}

}

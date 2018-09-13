import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

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
		JFrame frame = new PlayFrame("Nirvana - All Apologies", "Cool");
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
			setSize(200, 200);
			JPanel p = new PlayPanel(u, pL);
			this.add(p);
		}
		PlayFrame(String s, String u)
		{
			uName = u;
			song = s;
			setTitle("Play");
			setSize(200, 200);
			JPanel p = new PlayPanel(u);
			this.add(p);
		}
	}
	static class PlayPanel extends JPanel{
		
		JButton play = new JButton("\u25B6");
		JLabel title = new JLabel("Nirvana - All Apologies");
		PlayPanel(String u)
		{
			ActionListener a = new YActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			p1.add(title, BorderLayout.PAGE_START);
			p1.add(play, BorderLayout.CENTER);
			play.addActionListener(a);
			this.setLayout(new BorderLayout(1, 1));
			this.add(p1, BorderLayout.CENTER);
		}
		PlayPanel(String u, String p)
		{
			ActionListener a = new YActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new BorderLayout(1, 1));
			p1.add(title, BorderLayout.PAGE_START);
			p1.add(play, BorderLayout.CENTER);
			play.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.CENTER);
		}
		class YActionListener implements ActionListener{
			private PlayPanel panel;

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
						PlaySong.play(song + ".wav");
					} catch (LineUnavailableException | IOException | UnsupportedAudioFileException
							| InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

}

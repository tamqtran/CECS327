import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayButton {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// construct the frame
		JFrame frame = new PlayFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	static class PlayFrame extends JFrame{

		PlayFrame()
		{
			setTitle("Chess");
			setSize(400, 400);
			JPanel p = new PlayPanel();
			this.add(p);
		}
	}
	static class PlayPanel extends JPanel{
		JButton play = new JButton("\u25B6");
		PlayPanel()
		{
			ActionListener a = new YActionListener(this);
			JPanel p1 = new JPanel();
			p1.setLayout(new GridLayout(6, 6));
			p1.add(play);
			play.addActionListener(a);
			this.setLayout(new BorderLayout());
			this.add(p1, BorderLayout.PAGE_END);
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
						PlaySong.play("Nirvana - All Apologies.wav");
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
package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objects.CustomizationTool;
import sounds.AudioPlayer;
import sounds.MusicPlayer;

/*
 * Author: Alan Sun
 * 
 * Class HighScoreGUI displays the frame for the high scores
 * Plays music and sound effects
 */
public class HighScoreGUI extends JFrame implements ActionListener {

	// required JComponents for this frame
	private JPanel scorePanel = new JPanel();
	private JButton returnButton = new JButton(new ImageIcon(
			new ImageIcon("images/backBtn.png").getImage().getScaledInstance(393 / 2 - 35, 170 / 2 - 20, 0)));
	private JLabel[] scoreLabels = new JLabel[10];;
	private JLabel sword1Label = new JLabel(
			new ImageIcon(new ImageIcon("images/sword.png").getImage().getScaledInstance(197, 768, 0)));
	private JLabel sword2Label = new JLabel(
			new ImageIcon(new ImageIcon("images/sword.png").getImage().getScaledInstance(197, 768, 0)));
	private JLabel screenLabel = new JLabel("Leaderboard");

	// menu variable for date reference
	private MenuGUI menu;

	// constructor of this class calls other methods and initializes the menu for reference
	public HighScoreGUI(MenuGUI menu) {

		this.menu = menu;

		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);
		playBackgroundMusic();

	}

	// adds all the required JComponents to the frame
	private void addJComponents() {

		// settings for the score panel and add it to the frame
		scorePanel.setLayout(null);
		scorePanel.setBounds(0, 0, 25 * 30, 25 * 28);
		scorePanel.setBackground(Color.black);
		scorePanel.setOpaque(true);
		add(scorePanel);

		// loop through each score label and set the required informations
		for (int i = 0; i < 10; i++) {
			scoreLabels[i] = new JLabel(10 - i + ".__" + menu.getTopScore()[10 - 1 - i] + "s");
			scoreLabels[i].setBounds(320, 550 - i * 50, 150, 50);
			scoreLabels[i].setForeground(Color.YELLOW);
			scoreLabels[i].setFont(new Font("TimesRoman", Font.ITALIC, 24));
			scorePanel.add(scoreLabels[i]);
		}

		// set the required informations for the other JComponents
		returnButton.setBounds(290, 600, returnButton.getIcon().getIconWidth(), returnButton.getIcon().getIconHeight());
		returnButton.addActionListener(this);
		screenLabel.setBounds(270, 10, 200, 100);
		screenLabel.setForeground(Color.YELLOW);
		screenLabel.setFont(new Font("TimesRoman", Font.ITALIC | Font.BOLD, 36));
		sword1Label.setBounds(260 - 150, -50, sword1Label.getIcon().getIconWidth(),
				sword1Label.getIcon().getIconHeight());
		sword2Label.setBounds(260 + 150, -50, sword2Label.getIcon().getIconWidth(),
				sword2Label.getIcon().getIconHeight());

		// places the JComponents to the panel
		scorePanel.add(screenLabel);
		scorePanel.add(returnButton);
		scorePanel.add(sword1Label);
		scorePanel.add(sword2Label);

	}

	// method Override from action listener that detect the button actions
	@Override
	public void actionPerformed(ActionEvent event) {

		// checks if return button is being pressed
		if (event.getSource().equals(returnButton)) {

			AudioPlayer.playAudio("sounds/select.wav");
			MusicPlayer.stopMusic();

			// returns to the menu
			menu.setVisible(true);
			MusicPlayer.playMusic("sounds/introMusic.wav");

			// destroys this frame
			this.dispose();

		}

	}

	// this method plays the background music for the report screen
	public void playBackgroundMusic() {

		// plays the selected music using the music player
		MusicPlayer.playMusic("sounds/chat.wav");

	}

}

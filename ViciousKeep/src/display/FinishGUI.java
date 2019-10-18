package display;

import java.awt.Color;
import java.awt.Font;
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
 * Class FinishGUI serves as the end screen of the program when player dies or beats the game
 * Contains the player results from playing the game, such as score
 * Plays background music and sound effects
 */
public class FinishGUI extends JFrame implements ActionListener {

	// all the JComponents required for this frame
	private JPanel finishPanel = new JPanel(null);
	private JLabel swordLabel = new JLabel(
			new ImageIcon(new ImageIcon("images/sword.png").getImage().getScaledInstance(197, 768, 0)));
	private JLabel scoreLabel = new JLabel();
	private JLabel winLabel = new JLabel(new ImageIcon("images/Success.png"));
	private JLabel failLabel = new JLabel(new ImageIcon("images/Failure.png"));
	private JButton returnButton = new JButton(new ImageIcon(
			new ImageIcon("images/backBtn.png").getImage().getScaledInstance(393 / 2 - 35, 170 / 2 - 20, 0)));

	// required variables for this frame
	private int points;
	private boolean condition;
	private MazeRaceGUI game;

	// constructor of the method calls other method and initializes variables
	public FinishGUI(MazeRaceGUI game, int points, boolean win) {

		this.points = points;
		this.game = game;
		condition = win;

		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);
		playBackgroundAudio();

	}

	// method that adds all the JComponets to this frame
	public void addJComponents() {

		// enables custom layout to the panel
		finishPanel.setLayout(null);
		// set the location and size
		finishPanel.setBounds(0, 0, 25 * 30, 25 * 28);
		
		// allow the background color black of the panel to appear
		finishPanel.setBackground(Color.black);
		finishPanel.setOpaque(true);
		
		// add the panel to the frame
		add(finishPanel);

		// checks if player wins and set the corresponding icon
		if (condition) {

			winLabel.setBounds(180, 100, winLabel.getIcon().getIconWidth(), winLabel.getIcon().getIconHeight());
			finishPanel.add(winLabel);

		} else {

			failLabel.setBounds(190, 100, failLabel.getIcon().getIconWidth(), failLabel.getIcon().getIconHeight());
			finishPanel.add(failLabel);

		}

		// set the information and location of all other labels and buttons
		returnButton.setBounds(290, 500, returnButton.getIcon().getIconWidth(), returnButton.getIcon().getIconHeight());
		returnButton.addActionListener(this);
		scoreLabel.setText("Time: " + points);
		scoreLabel.setForeground(Color.red);
		scoreLabel.setBackground(Color.BLACK);
		scoreLabel.setOpaque(true);
		scoreLabel.setFont(new Font("TimesRoman", Font.ITALIC | Font.BOLD, 36));
		scoreLabel.setBounds(290, 300, 400, 100);
		swordLabel.setBounds(260, -50, swordLabel.getIcon().getIconWidth(), swordLabel.getIcon().getIconHeight());

		// add the labels to this frame's panel
		finishPanel.add(scoreLabel);
		finishPanel.add(returnButton);
		finishPanel.add(swordLabel);

	}

	// method Override from action listener that detect the button actions
	@Override
	public void actionPerformed(ActionEvent event) {

		// checks if return button is being pressed
		if (event.getSource().equals(returnButton)) {
			
			AudioPlayer.playAudio("sounds/select.wav");
			
			MusicPlayer.stopMusic();
			MusicPlayer.playMusic("sounds/introMusic.wav");

			// returns to the menu
			game.getMenu().setVisible(true);
			destroyFrame();
		}

	}

	// method that closes the frame
	public void destroyFrame() {

		this.dispose();

	}

	// this method plays the background music for the report screen
	public void playBackgroundAudio() {

		// plays the selected music using the music player
		MusicPlayer.playMusic("sounds/finish.wav");

	}

}
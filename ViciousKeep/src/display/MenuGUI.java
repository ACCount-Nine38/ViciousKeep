package display;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

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
 * Class MenuGUI serves as the starting screen of the program
 * Contains JComponents that leads to other screens or exiting the program
 * Plays background music and sound effects
 */
public class MenuGUI extends JFrame implements ActionListener {

	// Required JComponents for the frame
	private JPanel menuPanel = new JPanel(null);
	private JLabel nameLabel = new JLabel(new ImageIcon(new ImageIcon
			("images/game title.png").getImage().getScaledInstance(491+35, 84+20, 0)));
	private JLabel swordLabel = new JLabel(new ImageIcon(new ImageIcon
			("images/sword.png").getImage().getScaledInstance(197, 768, 0)));
	private JButton startButton = new JButton(new ImageIcon(new ImageIcon
			("images/play btn.jpg").getImage().getScaledInstance(393/2, 170/2, 0)));
	private JButton scoreButton = new JButton(new ImageIcon(new ImageIcon
			("images/score btn.jpg").getImage().getScaledInstance(393/2, 170/2, 0)));
	private JButton quitButton = new JButton(new ImageIcon(new ImageIcon
			("images/quit btn.jpg").getImage().getScaledInstance(393/2, 170/2, 0)));

	// create a high score array size 11 to store the top scores
	public int[] topScore = new int[11];
	
	// constructor of the menu calls other methods
	public MenuGUI() {
		
		loadScore();
		addJComponents();
		CustomizationTool.frameSetup(this);
		CustomizationTool.customCursor(this);
		playBackgroundAudio();

	}

	public void addJComponents() {

		// settings for the score panel and add it to the frame
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 25 * 30, 25 * 28);
		menuPanel.setBackground(Color.black);
		menuPanel.setOpaque(true);
		add(menuPanel);

		// set the required informations for all the JComponents
		startButton.setBounds(270, 280, startButton.getIcon().getIconWidth(), startButton.getIcon().getIconHeight());
		startButton.addActionListener(this);
		scoreButton.setBounds(270, 280 + startButton.getIcon().getIconHeight() + 20, scoreButton.getIcon().getIconWidth(),
				scoreButton.getIcon().getIconHeight());
		scoreButton.addActionListener(this);
		quitButton.setBounds(270, 280 + startButton.getIcon().getIconHeight() + scoreButton.getIcon().getIconHeight() + 40, 
				quitButton.getIcon().getIconWidth(), quitButton.getIcon().getIconHeight());
		quitButton.addActionListener(this);
		nameLabel.setBounds(110, 100, nameLabel.getIcon().getIconWidth(), nameLabel.getIcon().getIconHeight());
		swordLabel.setBounds(260, -50, swordLabel.getIcon().getIconWidth(), swordLabel.getIcon().getIconHeight());
		
		// places the JComponents to the panel
		menuPanel.add(nameLabel);
		menuPanel.add(startButton);
		menuPanel.add(scoreButton);
		menuPanel.add(quitButton);
		menuPanel.add(swordLabel);
		
	}

	// method Override from action listener that detect the button actions
	@Override
	public void actionPerformed(ActionEvent event) {

		// check which source the action is coming from
		if (event.getSource().equals(startButton)) {
			
			AudioPlayer.playAudio("sounds/select.wav");
			MusicPlayer.stopMusic();
			
			// close this frame and enter the game frame
			new MazeRaceGUI(this);
			setVisible(false);
			
		}
		
		else if (event.getSource().equals(scoreButton)) {
			
			AudioPlayer.playAudio("sounds/select.wav");
			MusicPlayer.stopMusic();
			
			// close this frame and enter the high score frame
			new HighScoreGUI(this);
			setVisible(false);
			
		} 
		
		else if (event.getSource().equals(quitButton)) {
			
			AudioPlayer.playAudio("sounds/select.wav");
			
			// exits the program
			System.exit(1);
			
		}

	}
	
	// method that loads the score from a text file
	public void loadScore() {

		// try and catch to see if the file trying to load exist in the location
		try {

			// creating a scanner object that takes in a text file
			Scanner input = new Scanner(new File("mazes/highScore.txt"));

			// index for the top score array
			int scoreIndex = 0;
			
			// while the file have other informations, continue the iteration
			while (input.hasNext()) {

				// set the score at the current index as the value read from the file
				topScore[scoreIndex] = input.nextInt();
				scoreIndex++;
				
			}
			
			// close the scanner object
			input.close();

			// catch statement displays if the file is found
		} catch (FileNotFoundException error) {

			// if file is not found, display the error on the console
			System.out.println("File error:" + error);

		}

	}
	
	// method that updates the overall score to the most recent one
	public void updateScore() {
		
		// sorts the score array in ascending order
		Arrays.sort(topScore);
		
		// file for the high score
		String file = "mazes/highScore.txt";

		// try and catch to see if the file trying to load exist in the location
		try {
			
			// print writer is used to write onto the file
			PrintWriter pr = new PrintWriter(file);
			
			// loop through each index of the top score array and write it onto the file
			for (int i = 0; i < topScore.length; i++) {
				
				pr.println(topScore[i]);
				
			}
			
			// close the print writer and apply all the edits to the file
			pr.close();

		} catch (FileNotFoundException error) {
			
			// if file is not found, display the error on the console
			System.out.println("File error:" + error);
			
		}
	}


	// this method plays the background music for the report screen
	public void playBackgroundAudio() {

		// plays the selected music using the music player
		MusicPlayer.playMusic("sounds/intro-music.wav");

	}

	// getters and setters
	public int[] getTopScore() {
		return topScore;
	}

	public void setTopScore(int[] topScore) {
		this.topScore = topScore;
	}

}

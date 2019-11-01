package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import objects.CampFire;
import objects.Cell;
import objects.CustomizationTool;
import objects.Player;
import objects.SpikeBallTrap;
import objects.VaultDoor;
import sounds.AudioPlayer;
import sounds.MusicPlayer;

/*
 * Author: Alan Sun
 * 
 * Class MazeRackGUI displays the GUI/graphics of the game
 * Loads the map and displaying it
 * Creates the game objects
 * Update the objects with a timer
 */
public class MazeRaceGUI extends JFrame implements KeyListener, ActionListener {

	// variables for game objects that does not change
	private final int CELL_SIZE = 25;
	private final int NUM_MINERAL = 100;
	private final int NUM_CELLS_WIDTH = 30;
	private final int NUM_CELLS_HEIGHT = 30;
	private final int NUM_SPIKE_BALL_TRAP = 5;
	private final int NUM_SPIKE_TRAP = 15;
	private final int NUM_EXPLOSIVE_TRAP = 6;
	private final int REQUIRED_TREASURE = 5;

	// images for the game objects that does not change
	private final ImageIcon WALL = new ImageIcon("images/wall.png");
	private final ImageIcon LIGHT = new ImageIcon("images/clear square.png");
	private final ImageIcon DARK = new ImageIcon("images/black square.png");
	private final ImageIcon OUT_OF_BOUNDS = new ImageIcon("images/path.png");
	private final ImageIcon PATH = new ImageIcon("images/path.png");
	private final ImageIcon SPIKE_TRAP = new ImageIcon("images/spike trap.png");
	private final ImageIcon EXPLOSION_TRAP = new ImageIcon("images/explosion trap.png");
	private final ImageIcon HAMMER = new ImageIcon("images/hammer.png");
	private final ImageIcon SPIKE_BALL_TRAP = new ImageIcon("images/spikeball.png");
	private final ImageIcon ROCK = new ImageIcon("images/rock.png");
	private final ImageIcon TREASURE = new ImageIcon("images/treasure.png");
	private final ImageIcon VAULT_DOOR = new ImageIcon("images/vault door.png");

	// player images for the game objects that does not change
	private final ImageIcon playerLeft = new ImageIcon("images/player left.png");
	private final ImageIcon playerRight = new ImageIcon("images/player right.png");
	private final ImageIcon playerUp = new ImageIcon("images/player top.png");
	private final ImageIcon playerDown = new ImageIcon("images/player down.png");

	// JComponents for the game
	private JPanel scoreboardPanel = new JPanel(null);
	private JPanel mazePanel = new JPanel(new GridBagLayout());
	private JLabel resourceIcon = new JLabel(ROCK);
	private JLabel scoreLabel = new JLabel("Time:");
	private JLabel scoreAmountLabel = new JLabel("0");
	private JLabel resourceLabel = new JLabel("x0");
	private JLabel hammerLabel = new JLabel("x1, $5");
	private JLabel campFireLabel = new JLabel("$10");
	private JLabel visionLabel = new JLabel("$25");
	private JLabel treasureLabel = new JLabel("0/5");
	private JLabel treasureIcon = new JLabel(TREASURE);
	private JButton hammerButton = new JButton(HAMMER);
	private JButton campFireButton = new JButton(
			new ImageIcon(new ImageIcon("images/camp fire icon.png").getImage().getScaledInstance(25, 25, 0)));
	private JButton visionButton = new JButton(
			new ImageIcon(new ImageIcon("images/eye.png").getImage().getScaledInstance(25, 25, 0)));

	private GridBagConstraints constraints = new GridBagConstraints();

	// the maze array store cells of each individual map tile
	private Cell[][] maze = new Cell[NUM_CELLS_WIDTH][NUM_CELLS_HEIGHT];

	// the sight array store cells of each individual sight tile, either transparent
	// or opaque
	private Cell[][] sight = new Cell[NUM_CELLS_WIDTH][NUM_CELLS_HEIGHT];

	// game timer, used to update the game every second (500 millisecond)
	private Timer gameTimer = new Timer(500, this);

	// default variables for the number of resources and game time
	private int numMinerals = 30;
	private int numHammer = 1;
	private int treasureFound = 0;
	private int time = 0;

	// sound variables for music and audio
	private boolean musicPlaying = true;
	private boolean audioPlaying = true;

	// variable to detect if All Seeing Eye is active
	private boolean vision = false;

	// stores an uncertainty amount of camp fires
	private ArrayList<CampFire> campfires = new ArrayList<CampFire>();
	
	// objects that will be keep tracked of in the game
	private SpikeBallTrap[] spikeBallTrapList = new SpikeBallTrap[NUM_SPIKE_BALL_TRAP];
	private VaultDoor vaultDoor = new VaultDoor();
	private Player player = new Player("images/player down.png");

	// a menu object for reference
	private MenuGUI menu;

	// constructor calls the other methods and initializes variables
	public MazeRaceGUI(MenuGUI menu) {

		this.menu = menu;

		// other methods being called
		scoreboardPanelSetup(); 
		mazePanelSetup(); 
		playBackgroundMusic();
		CustomizationTool.frameSetup(this);
		gameFrameSetup();
		CustomizationTool.customCursor(this);
		updateSight(); 

	}
	
	// additional frame setups that apply only to this frame
	private void gameFrameSetup() {
		
		addKeyListener(this);
		gameTimer.start();
		
	}

	// method that sets up the score board panel
	private void scoreboardPanelSetup() {

		// enables custom layout
		scoreboardPanel.setLayout(null);
		// set the location and bounding box of the score board panel
		scoreboardPanel.setBounds(0, 0, 88, CELL_SIZE * NUM_CELLS_HEIGHT);
		// set the background color of the panel
		scoreboardPanel.setBackground(new Color(65, 65, 65));
		// add the score board panel and maze panel to the frame
		add(scoreboardPanel);

		// sets the required option and information for all the buttons
		resourceIcon.setBounds(20, 300, 25, 25);
		hammerButton.setBounds(20, 400, 25, 25);
		hammerButton.addActionListener(this);
		hammerButton.setFocusable(false);
		campFireButton.setBounds(20, 450, 25, 25);
		campFireButton.addActionListener(this);
		campFireButton.setFocusable(false);
		visionButton.setBounds(20, 500, 25, 25);
		visionButton.addActionListener(this);
		visionButton.setFocusable(false);
		treasureIcon.setBounds(20, 600, 25, 25);

		// sets the required option and information for all the labels
		resourceLabel.setText("x" + numMinerals);
		resourceLabel.setBounds(50, 300 + 3, 100, 25);
		resourceLabel.setForeground(Color.white);
		resourceLabel.setFont(new Font("TimesRoman", Font.ITALIC, 12));
		hammerLabel.setBounds(50, 400 + 3, 100, 25);
		hammerLabel.setForeground(Color.white);
		hammerLabel.setFont(new Font("TimesRoman", Font.ITALIC, 12));
		campFireLabel.setBounds(50, 450 + 3, 100, 25);
		campFireLabel.setForeground(Color.white);
		campFireLabel.setFont(new Font("TimesRoman", Font.ITALIC, 12));
		visionLabel.setBounds(50, 500 + 3, 100, 25);
		visionLabel.setForeground(Color.white);
		visionLabel.setFont(new Font("TimesRoman", Font.ITALIC, 12));
		treasureLabel.setBounds(50, 600 + 3, 100, 25);
		treasureLabel.setForeground(Color.white);
		treasureLabel.setFont(new Font("TimesRoman", Font.ITALIC, 12));
		scoreAmountLabel.setBounds(10, 100, 100, 25);
		scoreAmountLabel.setForeground(Color.red);
		scoreAmountLabel.setFont(new Font("TimesRoman", Font.ITALIC | Font.BOLD, 24));
		scoreLabel.setBounds(10, 75, 100, 25);
		scoreLabel.setForeground(Color.red);
		scoreLabel.setFont(new Font("TimesRoman", Font.ITALIC | Font.BOLD, 24));

		// add the score label and timer label to score board panel
		scoreboardPanel.add(resourceLabel);
		scoreboardPanel.add(resourceIcon);
		scoreboardPanel.add(scoreAmountLabel);
		scoreboardPanel.add(scoreLabel);
		scoreboardPanel.add(hammerLabel);
		scoreboardPanel.add(hammerButton);
		scoreboardPanel.add(campFireButton);
		scoreboardPanel.add(visionButton);
		scoreboardPanel.add(campFireLabel);
		scoreboardPanel.add(visionLabel);
		scoreboardPanel.add(treasureLabel);
		scoreboardPanel.add(treasureIcon);
	
	}

	// method that sets up the maze panel
	private void mazePanelSetup() {
		
		// set the location and bounding box of the maze panel
		mazePanel.setBounds(50, -50, CELL_SIZE * NUM_CELLS_WIDTH, CELL_SIZE * NUM_CELLS_HEIGHT);
		mazePanel.setBackground(Color.BLACK);
		mazePanel.setOpaque(true);
		add(mazePanel);

		// load the maze 
		loadMaze();
		
		// places the objects in the maze to its corresponding position
		placeMineral();
		placePlayer();
		placeOutOfBoundsObject(TREASURE, REQUIRED_TREASURE);
		placeOutOfBoundsObject(SPIKE_TRAP, NUM_SPIKE_TRAP);
		placeOutOfBoundsObject(EXPLOSION_TRAP, NUM_EXPLOSIVE_TRAP);
		placeVaultDoor();
		spawnSpikeBallTrap();
		addMenuBar();

	}

	// method that adds the items relating to the menu bar
	private void addMenuBar() {

		// create a new JMenuBar item that stores different menus
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// create a new menu called control and add it to the menu bar
		JMenu controlMenu = new JMenu("Control");
		menuBar.add(controlMenu);

		// creating the exit option under the control menu
		JMenuItem restartOption = new JMenuItem("Return to Menu");

		// add an action listener for button actions when clicked
		restartOption.addActionListener(new ActionListener() {

			// method handles the current button's actions
			@Override
			public void actionPerformed(ActionEvent e) {

				// play sound effects for the button and musics for the intro screen
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/select.wav");

				MusicPlayer.stopMusic();
				MusicPlayer.playMusic("sounds/intro-music.wav");

				// go to the menu screen
				menu.setVisible(true);
				exitFrame(); 

			}
		});

		controlMenu.add(restartOption);

		// exit button closes the program
		JMenuItem exitOption = new JMenuItem("Exit Program");

		exitOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// exits the program
				System.exit(1);

			}
		});

		controlMenu.add(exitOption);

		// the help menu will include all the help related menu items
		JMenu helpMenu = new JMenu("Help");

		menuBar.add(helpMenu);

		// the description menu item will specify the screen descriptions and controls
		JMenuItem descriptionOption = new JMenuItem("Description");
		descriptionOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (audioPlaying)
					AudioPlayer.playAudio("sounds/select.wav");

				// shows screen description and controls upon clicking
				JOptionPane.showMessageDialog(null, "-----TRAPS-----\n"
						+ "- beware of spike ball traps that move around in the map, do not get crushed\n"
						+ "- beware of spike traps located inside a hidden room, it looks like a path with spikes\n"
						+ "- beware of explosive traps located inside a hidden room, it looks like a red wire\n\n"
						+ "- -----SHOP-----\n" + "- purchase hammer to break the walls\n"
						+ "- purchase camp fire to grant an area of light\n"
						+ "- purchase purchase vision to grant the sight of all tiles in the map until you move\n\n"
						+ "-----Goal-----\n"
						+ "- collect all 5 treasures found in hidden rooms by breaking the walls with a hammer\n"
						+ "- find the vault door to escape\n"
						+ "- you will only successfully escape if you obtain 5 treasures and step on the vault door\n"
						+ "- good luck!\n\n" + "click 'ok' to continue...", "Description",
						JOptionPane.INFORMATION_MESSAGE);

			}

		});

		helpMenu.add(descriptionOption);

		// the description menu item will specify the screen descriptions and controls
		JMenuItem controlOption = new JMenuItem("Controls");
		controlOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (audioPlaying)
					AudioPlayer.playAudio("sounds/select.wav");

				// shows control description and controls upon clicking
				JOptionPane.showMessageDialog(null,
						"- Use the W-A-S-D keys to move the player\n"
								+ "- use the arrow keys to break the wall, if a hammer is avaliable\n"
								+ "- the score is based on how fast you escape the keep ***WITHOUT DYING***\n"
								+ "- you may purchase items by clicking the 3 item buttons on the side\n\n"
								+ "click 'ok' to continue...",
						"Controls", JOptionPane.INFORMATION_MESSAGE);

			}

		});

		helpMenu.add(controlOption);

		// add audio menu is used to control sound effects
		JMenu audioMenu = new JMenu("Audio");

		menuBar.add(audioMenu);

		// this menu item allows the user to disable music
		JMenuItem disableMusicOption = new JMenuItem("Disable Background Music");
		disableMusicOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// stops the music from playing
				MusicPlayer.stopMusic();

			}

		});

		audioMenu.add(disableMusicOption);

		// this menu item allows the user to play a random Music music
		JMenuItem enableMusicOption = new JMenuItem("Enable Background Music");
		enableMusicOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// stops the music, if there are any
				if (musicPlaying)
					MusicPlayer.stopMusic();

				// play a random music and music playing is set to true
				playBackgroundMusic();
				musicPlaying = true;

			}

		});

		audioMenu.add(enableMusicOption);

		// this menu item allows the user to play a random Music music
		JMenuItem disableSFXOption = new JMenuItem("Disable Sound Effect");
		disableSFXOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// disabling all sounds by turning sound playing into false
				audioPlaying = false;

			}

		});

		audioMenu.add(disableSFXOption);

		// this menu item allows the user to play a random Music music
		JMenuItem enableSFXOption = new JMenuItem("Enable Sound Effect");
		enableSFXOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// enable sound effects to play for this screen
				audioPlaying = true;

			}

		});

		audioMenu.add(enableSFXOption);

	}

	// method that exits the current frame
	private void exitFrame() {

		this.dispose();

	}

	// method that loads the maze
	private void loadMaze() {

		// set the default variable for row as 0
		int row = 0;
		// char array for reading the line of the input to be broken down into individual components
		char[] line;

		// try and catch to see if the file trying to load exist in the location
		try {

			// creating a scanner object that takes in a text file
			Scanner input = new Scanner(new File("mazes/maze.txt"));

			// continue the iteration while file have other informations
			while (input.hasNext()) {

				// break down the line read into a char array
				line = input.nextLine().toCharArray();

				// for loop filling the cells of each line by calling the fillCell method
				for (int col = 0; col < line.length; col++) {
					fillSight(row, col);
					fillCell(line[col], row, col);
				}

				row++;

			}

			// closes the underlying stream on scanner
			input.close();

		} catch (FileNotFoundException error) {

			System.out.println("File error:" + error);

		}

	}

	// method that fills each cell of the map
	private void fillCell(char shape, int row, int col) {

		// maze array used for creating the actual map of the game
		maze[row][col] = new Cell(row, col);

		// detect each character taken in to generate the tiles
		if (shape == 'W')
			maze[row][col].setIcon(WALL);
		else if (shape == 'X')
			maze[row][col].setIcon(OUT_OF_BOUNDS);
		else if (shape == '.')
			maze[row][col].setIcon(PATH);
		else if (shape == 'C')
			maze[row][col].setIcon(ROCK);
		else if (shape == 'S')
			maze[row][col].setIcon(SPIKE_TRAP);
		else if (shape == 'E')
			maze[row][col].setIcon(EXPLOSION_TRAP);

		// allow positioning of components relative to one another using constraints
		constraints.gridx = col;
		constraints.gridy = row;
		
		// adds the maze array to the maze panel
		mazePanel.add(maze[row][col], constraints);

	}

	// method that fills the player sight box given the player location
	public void fillSight(int row, int col) {
		
		// maze array used for creating the actual map of the game
		sight[row][col] = new Cell(row, col);

		// fill the given row and column as a dark square so it is not visible
		sight[row][col].setIcon(DARK);

		// allow positioning of components relative to one another using constraints
		constraints.gridx = col;
		constraints.gridy = row;
		
		// adds the sight array to the maze panel
		mazePanel.add(sight[row][col], constraints);

	}

	// method that places the player object
	private void placePlayer() {

		// find an empty cell to spawn the player
		Cell cell = findEmptyCell(PATH);

		// set the spawn position at the cell location
		player.setRow(cell.getRow());
		player.setCol(cell.getCol());

		// change the spawn position icon as the player icon
		maze[player.getRow()][player.getCol()].setIcon(player.getIcon());

	}
		
	// method that places all the rocks at the start of the game
	private void placeMineral() {

		// loop through the mineral amount and place it
		for (int mineral = 0; mineral <= NUM_MINERAL; mineral++) {

			Cell cell = findEmptyCell(PATH);

			// update the maze icon at the current position as the rock mineral
			maze[cell.getRow()][cell.getCol()].setIcon(ROCK);

		}

	}

	// method that places a rock mineral on the maze after the timer starts
	private void placeRock() {

		Cell cell = findEmptyCell(PATH);

		maze[cell.getRow()][cell.getCol()].setIcon(ROCK);

	}

	// method that places the camp fire
	private void placeCampFire() {

		// creates a new camp fire object
		CampFire campFire = new CampFire();

		campFire.setRow(player.getRow());
		campFire.setCol(player.getCol());

		maze[player.getRow()][player.getCol()].setIcon(campFire.getIcon());

		// add the camp fire to the list for it to be updated
		campfires.add(campFire);

	}

	// method that spawns all the spike ball traps by placing each into the field
	private void spawnSpikeBallTrap() {

		// spawns a trap for the given number
		for (int i = 0; i < NUM_SPIKE_BALL_TRAP; i++) {
			
			// creating each of the trap objects and place it on the map
			spikeBallTrapList[i] = new SpikeBallTrap("images/spikeBall.png");
			placespikeBallTrap(spikeBallTrapList[i]);

		}

	}

	// method that places the given spike ball trap
	private void placespikeBallTrap(SpikeBallTrap spikeBallTrap) {

		Cell cell = findEmptyCell(PATH);
		
		spikeBallTrap.setRow(cell.getRow());
		spikeBallTrap.setCol(cell.getCol());

		maze[spikeBallTrap.getRow()][spikeBallTrap.getCol()].setIcon(spikeBallTrap.getIcon());

	}
	
	// method that places the vault door object
	private void placeVaultDoor() {

		Cell cell = findEmptyCell(PATH);

		vaultDoor.setRow(cell.getRow());
		vaultDoor.setCol(cell.getCol());

		maze[cell.getRow()][cell.getCol()].setIcon(VAULT_DOOR);

	}

	// method that places an object in an area player is not allowed to spawn
	private void placeOutOfBoundsObject(ImageIcon icon, int amount) {

		// place the object until the amount is reached
		for (int i = 0; i < amount; i++) {

			Cell cell = findEmptyCell(OUT_OF_BOUNDS);
			maze[cell.getRow()][cell.getCol()].setIcon(icon);

		}

	}

	// method that finds an empty cell on the maze given the image  icon
	private Cell findEmptyCell(ImageIcon type) {

		// create a new cell variable to be returned
		Cell cell = new Cell(0, 0);

		// loop through the map until the correct position is found
		do {
			
			// the position is selected at random
			cell.setRow((int) (Math.random() * (NUM_CELLS_WIDTH - 6)) + 2);
			cell.setCol((int) (Math.random() * (NUM_CELLS_HEIGHT - 6)) + 2);

		} while (maze[cell.getRow()][cell.getCol()].getIcon() != type);

		return cell;

	}

	// the action performed method applies applies the aftereffect of button detection
	@Override
	public void actionPerformed(ActionEvent event) {

		// detect which source the action is coming from
		if (event.getSource() == gameTimer) {
			// update the time variable or score of the player
			time++;
			scoreAmountLabel.setText(Integer.toString(time));

			// moves the spike ball trap by checking each trap's availability to move
			for (SpikeBallTrap spikeBallTrap : spikeBallTrapList)
				spikeBallTrapDetection(spikeBallTrap);

			// random variable that gives a change for an extra rock to drop 
			int rockDrop = (int) (Math.random() * 8);
			if (rockDrop == 0) 
				placeRock();

			// constantly updating the sight effect for each camp fire the player placed
			for (int i = 0; i < campfires.size(); i++) {
				
				maze[campfires.get(i).getRow()][campfires.get(i).getCol()].setIcon(CampFire.CAMPFIRE);
				
				// traversing through a 2D loop to apply light effect to the surroundings
				for (int j = campfires.get(i).getRow() - 1; j < campfires.get(i).getRow() + 2; j++) 
					for (int k = campfires.get(i).getCol() - 1; k < campfires.get(i).getCol() + 2; k++) 
						sight[j][k].setIcon(LIGHT);
				
			}
				
			// ensure that the vault door icon does not get updated into something else
			maze[vaultDoor.getRow()][vaultDoor.getCol()].setIcon(VAULT_DOOR);

		}
		
		else if (event.getSource() == hammerButton) {
			
			// see if player have enough resource
			if (numMinerals >= 5) {

				// increment the number of hammer and decrease the amount of material by its cost
				numHammer++;
				numMinerals -= 5;
				
				// update labels
				resourceLabel.setText("x " + numMinerals);
				hammerLabel.setText("x " + numHammer + ",$5");
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/gear-up.wav");

			} else {

				if (audioPlaying)
					AudioPlayer.playAudio("sounds/error.wav");

			}
		}
		
		else if (event.getSource() == campFireButton) {
			
			if (numMinerals >= 10) {
				
				placeCampFire();

				numMinerals -= 10;
				resourceLabel.setText("x " + numMinerals);
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/fire.wav");

			} else {

				if (audioPlaying)
					AudioPlayer.playAudio("sounds/error.wav");

			}
		}
		
		else if (event.getSource() == visionButton) {

			if (numMinerals >= 25) {

				allSeeingEye();
				numMinerals -= 25;
				resourceLabel.setText("x " + numMinerals);
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/eagle.wav");

			} else {

				if (audioPlaying)
					AudioPlayer.playAudio("sounds/error.wav");
			}
		}

	}

	//  method that applies the effect of the All Seeing Eye
	private void allSeeingEye() {

		// traversing though a nested for loop to update each sight box to transparent/light
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				
				// allow the user to see the tile
				sight[j][i].setIcon(LIGHT);
				
			}
		}

		// all seeing eye is not active
		vision = true;

	}

	// method that checks if the given spike ball trap is able to move to the next tile
	private void spikeBallTrapDetection(SpikeBallTrap spikeBallTrap) {

		// if the spike ball trap does not hits a wall, change its direction else move it
		if (spikeBallTrap.getDirection() == 0
				&& maze[spikeBallTrap.getRow() - 1][spikeBallTrap.getCol()].getIcon() != WALL)
			movespikeBallTrap(spikeBallTrap, -1, 0);
		
		else if (spikeBallTrap.getDirection() == 1
				&& maze[spikeBallTrap.getRow() + 1][spikeBallTrap.getCol()].getIcon() != WALL)
			movespikeBallTrap(spikeBallTrap, 1, 0);
		
		else if (spikeBallTrap.getDirection() == 2
				&& maze[spikeBallTrap.getRow()][spikeBallTrap.getCol() - 1].getIcon() != WALL)
			movespikeBallTrap(spikeBallTrap, 0, -1);
		
		else if (spikeBallTrap.getDirection() == 3
				&& maze[spikeBallTrap.getRow()][spikeBallTrap.getCol() + 1].getIcon() != WALL)
			movespikeBallTrap(spikeBallTrap, 0, 1);
		
		else
			spikeBallTrap.setDirection((int) (Math.random() * 4));

	}

	// @Override methods from key listener
	@Override
	public void keyReleased(KeyEvent key) {

	}

	@Override
	public void keyTyped(KeyEvent key) {

	}

	// method that performs all the key actions upon pressing a key
	@Override
	public void keyPressed(KeyEvent key) {

		// W-A-S-D controls are used to move the player up-down-left-right
		if (key.getKeyCode() == KeyEvent.VK_W) {

			// changes the player image to its corresponding direction
			player.setIcon(playerUp);

			// removes the previous sight box
			removeSight();

			// if wall does not exist at the direction player is heading, move the player
			if (maze[player.getRow() - 1][player.getCol() + 0].getIcon() != WALL)
				movePlayer(-1, 0);

			// updates a new sight box
			updateSight();

		} else if (key.getKeyCode() == KeyEvent.VK_D) {

			player.setIcon(playerRight);

			removeSight();

			if (maze[player.getRow() + 0][player.getCol() + 1].getIcon() != WALL)
				movePlayer(0, 1);

			updateSight();

		} else if (key.getKeyCode() == KeyEvent.VK_S) {

			player.setIcon(playerDown);

			removeSight();

			if (maze[player.getRow() + 1][player.getCol() + 0].getIcon() != WALL)
				movePlayer(1, 0);

			updateSight();

		} else if (key.getKeyCode() == KeyEvent.VK_A) {

			player.setIcon(playerLeft);

			removeSight();

			if (maze[player.getRow() + 0][player.getCol() - 1].getIcon() != WALL)
				movePlayer(0, -1);

			updateSight();

		}

		// if all seeing eye is active, set it to false once player moves
		if (key.getKeyCode() == KeyEvent.VK_W || key.getKeyCode() == KeyEvent.VK_A || key.getKeyCode() == KeyEvent.VK_S
				|| key.getKeyCode() == KeyEvent.VK_D) {

			if (vision) {

				for (int i = 0; i < 26; i++)
					for (int j = 0; j < 26; j++)
						// disable all seeing eye effect
						sight[i][j].setIcon(DARK);

				// disable all seeing eye variable
				vision = false;

			}

		}

		// if player contains a hammer, then player can break walls
		if (numHammer > 0) {

			// player can break walls using the arrow keys, representing the attack directions
			if (key.getKeyCode() == KeyEvent.VK_UP
					&& maze[player.getRow() - 1][player.getCol() - 0].getIcon() == WALL) {

				// set the icon to a path, player is now able to walk through it
				maze[player.getRow() - 1][player.getCol() - 0].setIcon(PATH);

				// one attack cost a hammer
				numHammer--;
				hammerLabel.setText("x " + Integer.toString(numHammer) + ",$5");
				
				// play the sound effect of breaking the wall
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/wall-smash.wav");

			} else if (key.getKeyCode() == KeyEvent.VK_DOWN
					&& maze[player.getRow() + 1][player.getCol() - 0].getIcon() == WALL) {

				maze[player.getRow() + 1][player.getCol() - 0].setIcon(PATH);
				numHammer--;
				
				hammerLabel.setText("x " + Integer.toString(numHammer) + ",$5");
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/wall-smash.wav");

			} else if (key.getKeyCode() == KeyEvent.VK_LEFT
					&& maze[player.getRow() + 0][player.getCol() - 1].getIcon() == WALL) {

				maze[player.getRow() + 0][player.getCol() - 1].setIcon(PATH);
				numHammer--;
				
				hammerLabel.setText("x " + Integer.toString(numHammer) + ",$5");
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/wall-smash.wav");

			} else if (key.getKeyCode() == KeyEvent.VK_RIGHT
					&& maze[player.getRow() + 0][player.getCol() + 1].getIcon() == WALL) {

				maze[player.getRow() + 0][player.getCol() + 1].setIcon(PATH);
				numHammer--;
				
				hammerLabel.setText("x " + Integer.toString(numHammer) + ",$5");
				
				if (audioPlaying)
					AudioPlayer.playAudio("sounds/wall-smash.wav");

			}

		}

	}

	// method that fills the new sight rectangle once player moves
	private void updateSight() {

		// making a 9x9 sight square around the player using nested for loop
		for (int i = player.getRow() - 1; i < player.getRow() - 1 + 3; i++) {
			for (int j = player.getCol() - 1; j < player.getCol() - 1 + 3; j++) {

				// player can now see this square
				sight[i][j].setIcon(LIGHT);

			}
		}

	}
		
	// method that removes the old sight rectangle once player moves
	private void removeSight() {

		// updating the 9x9 sight square around the player using nested for loop
		for (int i = player.getRow() - 1; i < player.getRow() - 1 + 3; i++) {
			for (int j = player.getCol() - 1; j < player.getCol() - 1 + 3; j++) {

				// player cannot see this square
				sight[i][j].setIcon(DARK);

			}
		}

	}

	// method that moves the player, checks for collision and apply collision effects
	public void movePlayer(int dRow, int dCol) {

		// set the path stepped on into a path
		maze[player.getRow()][player.getCol()].setIcon(PATH);

		// detect different collision and apply effects
		if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == ROCK) {

			// if it is a rock, then player found a resource he could use to make stuff
			numMinerals+=1;
			resourceLabel.setText("x " + Integer.toString(numMinerals));

		} else if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == SPIKE_TRAP) {

			// play sound effect for this collision
			if (audioPlaying)
				AudioPlayer.playAudio("sounds/spike-trap.wav");

			// ends the game
			gameOver("Spike", player.getRow(), player.getCol(), false);

		} else if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == EXPLOSION_TRAP) {

			if (audioPlaying)
				AudioPlayer.playAudio("sounds/explosion.wav");

			gameOver("Explosion", player.getRow(), player.getCol(), false);

		} else if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == SPIKE_BALL_TRAP) {

			if (audioPlaying)
				AudioPlayer.playAudio("sounds/crushed.wav");

			gameOver("Crushed", player.getRow(), player.getCol(), false);

		} else if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == TREASURE) {

			treasureFound++;
			treasureLabel.setText(treasureFound + "/5");

		} else if (maze[player.getRow() + dRow][player.getCol() + dCol].getIcon() == VAULT_DOOR) {

			// if all the treasures are found and player reached the escape door, player wins
			if (treasureFound == 5)
				gameOver("Win", player.getRow(), player.getCol(), true);

		}

		// move player to its new location and set the tile icon as the player
		player.move(dRow, dCol);
		maze[player.getRow()][player.getCol()].setIcon(player.getIcon());

	}

	// method that moves the spike ball
	private void movespikeBallTrap(SpikeBallTrap spikeBallTrap, int dRow, int dCol) {

		// set the previous location as path
		maze[spikeBallTrap.getRow()][spikeBallTrap.getCol()].setIcon(PATH);

		// if player hits the trap
		if (maze[spikeBallTrap.getRow() + dRow][spikeBallTrap.getCol()
				+ dCol] == maze[player.getRow() + dRow][player.getCol() + dCol]) {

			// plays the sound effect of being crushed
			if (audioPlaying)
				AudioPlayer.playAudio("sounds/crushed.wav");

			// player loses
			gameOver("Explosion", player.getRow(), player.getCol(), false);

		}

		// move the trap to its new location and set it as the trap icon
		spikeBallTrap.move(dRow, dCol);
		maze[spikeBallTrap.getRow()][spikeBallTrap.getCol()].setIcon(spikeBallTrap.getIcon());
	}

	// method that creates effects to enter the game over screen
	private void gameOver(String type, int playerX, int playerY, boolean win) {

		// once game ends, stops the timer to stop all actions
		gameTimer.stop();

		// if the player wins, then update then check if it is his high score
		if (win) {
			menu.topScore[10] = time;
			menu.updateScore();
		}

		// if player died by an explosion, draw the effect
		if (type.equals("Explosion")) {

			player.setIcon(new ImageIcon("images/explosion.gif"));

		}

		// 500 millisecond wait until current screen exits to display effects above
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {

				openFinishFrame(win);

			}
		}, 800);

	}

	// method that opens the frame to display results when game ends
	private void openFinishFrame(boolean win) {

		MusicPlayer.stopMusic();
		new FinishGUI(this, time, win);

		// delete the current screen
		this.dispose();

	}

	// this method plays the background music for the report screen
	public void playBackgroundMusic() {

		// plays the selected music using the music player
		MusicPlayer.playMusic("sounds/quake.wav");

	}

	// getter and setters
	public MenuGUI getMenu() {
		return menu;
	}

	public void setMenu(MenuGUI menu) {
		this.menu = menu;
	}

}

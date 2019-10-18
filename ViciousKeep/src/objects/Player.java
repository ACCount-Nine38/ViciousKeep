package objects;

import javax.swing.ImageIcon;

/*
 * Author: Alan Sun
 * 
 * Object class of player
 * Extending cell to access location methods
 */
public class Player extends Cell {

	// constructor of the player class sets the player icon
	public Player(String fileName) {

		setIcon(new ImageIcon(fileName));
		
	}
	
	// method that moves the player at the given location
	public void move(int dRow, int dCol) {
		
		setRow(getRow() + dRow);
		setCol(getCol() + dCol);
		
	}

}

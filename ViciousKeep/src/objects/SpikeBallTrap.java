package objects;

import javax.swing.ImageIcon;

/*
 * Author: Alan Sun
 * 
 * Object class of the spike trap class
 * Extending cell to access location methods
 */
public class SpikeBallTrap extends Cell{

	// direction variable to detect which way this object is going
	private int direction;
	
	// constructor of the player class sets the player icon
	public SpikeBallTrap(String fileName) {
		
		// set the icon of this object
		setIcon(new ImageIcon(fileName));
		// set a random direction
		direction = (int)(Math.random()*4);
		
	}
	
	// method that moves the player at the given location
	public void move(int dRow, int dCol) {
		
		setRow(getRow() + dRow);
		setCol(getCol() + dCol);
		
	}

	// getters and setters
	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

}

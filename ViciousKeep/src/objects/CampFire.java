package objects;

import javax.swing.ImageIcon;

/*
 * Author: Alan Sun
 * 
 * Object class of the camp fire
 * Extending cell to access location methods
 */
public class CampFire extends Cell {

	// static image icon that can be used for all classes
	public static final ImageIcon CAMPFIRE = new ImageIcon("images/camp fire.gif");
	
	// constructor of the camp fire class sets the camp fire icon
	public CampFire() {

		setIcon(CAMPFIRE);
		
	}
	
}

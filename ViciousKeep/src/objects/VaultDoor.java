package objects;

import javax.swing.ImageIcon;

/*
 * Author: Alan Sun
 * 
 * Object class of the vault door
 * Extending cell to access location methods
 */
public class VaultDoor extends Cell {
	
	// constructor of the vault door class sets the vault door icon
	public VaultDoor() {

		setIcon(new ImageIcon("images/vaultDoor.png"));
		
	}

}

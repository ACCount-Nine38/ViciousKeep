package objects;

import javax.swing.JLabel;

/*
 * Author: Alan Sun
 * 
 * Class cell act as a parent class for all the cell objects extended from cell
 * Act as the tiles for the map
 */
public class Cell extends JLabel {

	// row and column variables to keep track of the tile position
	private int row;
	private int col;
	
	// empty constructor for initialization
	public Cell() {
		
	}
	
	// Overloaded constructor for cell placement
	public Cell(int row, int col) {

		this.row = row;
		this.col = col;

	}

	// getters and setters
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	// method that prints all the variables for this class
	@Override
	public String toString() {
		return "Cell [row=" + row + ", col=" + col + "]";
	}

}

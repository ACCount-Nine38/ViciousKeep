package objects;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;

/*
 * Author: Alan Sun
 * 
 * This class serves as a tool that will be used in most of the classes
 * Contains methods that are used in all frames
 * Able to setup a frame and change the cursor icon
 */
public class CustomizationTool {

	// method that sets up a frame
	public static void frameSetup(JFrame frame) {

		// set the name and size of the frame, and now allowing user to resize
		frame.setTitle("Vicious Keep");
		frame.setSize(25 * 30, 25 * 28);
		frame.setResizable(false);

		// disables auto layout, center program, exit frame when programcloses
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set frame to appear on screen
		frame.setVisible(true);

	}

	// method that changes the cursor icon
	public static void customCursor(JFrame frame) {

		// using the java TookKit to change cursors
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// load an image using ToolKit
		Image mouse = toolkit.getImage("cursor.png");

		// set the cursor icon giving a new image, point, and name
		frame.setCursor(toolkit.createCustomCursor(mouse, new Point(0, 0), "Custom Cursor"));

	}

}

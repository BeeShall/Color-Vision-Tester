 package view;

import lejos.hardware.lcd.TextLCD;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Abstract class for all the Menus
 */
public abstract  class Menu {
	protected Menu previous;
	protected TextLCD lcd;
	/**
	 * Constructor for the class
	 * @param previous the class that it needs to return to.
	 */
	public Menu(Menu previous){
		this.previous = previous;
	}
	
	/** 
	 * Description: Method to invoke the class. Once this method is called, the menu is drawn on the screen and action is taken based on the user's menu selection.
	 * @return Menu - returns the Menu to go after depending on user's selection 
	 */
	public abstract Menu invokeMenu();
	
	/**
	 * Description: Method used to display cursor on a specific point in the LCD.
	 * @param x: x coordinates of the point to display coordinates
	 * @param y: x coordinates of the point to display coordinates 
	 * @param select: boolean value to determine whether the menu option is selected or not
	 */
	protected void displayCursor(int x, int y, boolean select){
		String selected;
		boolean inverted = false;
		if(select) {selected = "_"; inverted = true;}
		else selected = " ";
		lcd.drawString(selected, x, y, inverted);
	}

}

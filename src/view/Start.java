package view;

import lejos.hardware.Button;
import model.Dictionary;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/
/**
 * @author Bishal
 * class that denotes the start Menu. This is a cild of the Menu Interface.
 */
public class Start extends Menu {
	protected Robot robot;
	protected BoardAttributes boardAttr;
	protected Dictionary dictionary;

	
	/**
	 * Description: Constructor for the Start menu
	 * @param previous: The menu it needs to return after exiting
	 */
	public Start(Menu previous) {			
			super(previous);		
			robot = new Robot();
			robot.initLCDDisplay();	
			this.lcd = robot.getLCD();
			
			boardAttr = new BoardAttributes();
			
			dictionary = new Dictionary();
			// TODO Auto-generated constructor stub
		}
	
	public static void main(String[] args){
		Menu menu = new Start(null);
		while(menu != null){
			menu = menu.invokeMenu();
		}
	}
	
	
	

	/** 
	 * Description: Inherited method to invoke the class. Once this method is called, the menu is drawn on the screen and action is taken based on the user's menu selection.
	 * @return Menu - returns the Menu to go after depending on user's selection 
	 */
	@Override
	public Menu invokeMenu() {
		// TODO Auto-generated method stub	
		drawMenu();
		int xCoord=0;
		int yCoord =2;
		displayCursor(xCoord,yCoord,true);
		while (true){	
			int buttonId = Button.waitForAnyPress();
			displayCursor(xCoord,yCoord,false);
			if(buttonId == Button.ID_UP || buttonId == Button.ID_DOWN){
				if(yCoord == 2) yCoord++;
				else if(yCoord == 3) yCoord--;
			}
			else if(buttonId == Button.ID_ENTER){
				if(yCoord == 2) return new CalibrationMenu(this,robot,boardAttr);
				if(yCoord == 3) return new ScanMenu(this,robot,boardAttr, dictionary);;
			}
			else if(buttonId == Button.ID_ESCAPE){
				System.out.println("Goodbye");
				System.exit(0);
				return null;
			}
			displayCursor(xCoord,yCoord,true);
		}
	}
	
	/** 
	 * Description: Method to write the strings on the screen 
	 */
	private void drawMenu(){
		lcd.clear();
		lcd.drawString("Main Menu:",0,0);
		lcd.drawString("-----------------",0 ,1);
		lcd.drawString(" 1. Calibration", 0, 2);
		lcd.drawString(" 2. Scan", 0, 3);
	}
}

package view;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import lejos.utility.TextMenu;
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
		LCD.drawString("Scanning the pixels in 5 seconds", 0, 0);
		for(int i=0; i<10;i++){
			for(int j=0; j<10;j++){
				robot.travelPilot(0.67);
				float colorValue = robot.getFloorColorValue();
				dictionary.addPixelToTable(i, j, colorValue);
			}
			if(i!=9){
				rotateRobot(0.67);
			}
		}
		LCD.clear();
		LCD.drawString("Board succesfully scanned!", 0, 0);
		Delay.msDelay(2000);	
		
		
		String[] kValues = {"6","7","8"};
		int index = -1;
		Delay.msDelay(300);		
		while (!Button.ENTER.isDown()) {
			TextMenu menu = new TextMenu(kValues, 0, "Pick a value for k");
			index = menu.select();
			if(Button.ESCAPE.isDown()){
				return null;
			}	
			
		}
		
		dictionary.invokeClustering(Integer.parseInt(kValues[index]));		
		return null;
	}
	
	/** 
	 * Description: Method to rotate the robot after scanning each row.
	 * @param travelDistance : the pixelSize i.e. the distance robot needs to travel every single time
	 */
	private void rotateRobot(double travelDistance) {
		//bring back the robot using the distance it has traveled
		robot.travelPilot(-10 * travelDistance);
		//rotate right
		robot.rotatePilot(-90);
		//travel 1 pixel
		robot.travelPilot(travelDistance);
		//rotate left
		robot.rotatePilot(90);
	}
	
}

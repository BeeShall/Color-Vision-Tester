package view;

import lejos.hardware.Button;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Class that handles the Calibration menu and the calibration issues. This is a child of the Menu Interface.
 */

public class CalibrationMenu extends Menu {
	private Robot robot;
	private BoardAttributes boardAttr;

	/**
	 * Description : Constructor for the Calibration Menu.
	 * @param previous: Menu to return to after finishing
	 * @param robot: The robot object that we have been working on
	 * @param boardAttr: the attributes of the we are working on
	 * @param dictionary: the dictionary that holds the rules and searches for this session.
	 */
	public CalibrationMenu(Menu previous, Robot robot, BoardAttributes boardAttr) {
		super(previous);
		// TODO Auto-generated constructor stub
		this.robot = robot;
		//if the robot isn't calibrated yet, initialize the light sensor
		if(!robot.isCalibrated()){
			this.robot.initColorSensor();
		}
		this.lcd = robot.getLCD();
		
		this.boardAttr = boardAttr;
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
			switch(buttonId){
			case Button.ID_UP:
				if(yCoord == 2) yCoord=4;
				else yCoord--;
				break;
			case Button.ID_DOWN:
				if(yCoord == 4) yCoord=2;
				else yCoord++;
				break;
			case Button.ID_ENTER:
				if(yCoord == 2) readOnColor();
				if(yCoord == 3) readOffColor();
				if(yCoord == 4) readPixelSize();
				break;
			case Button.ID_ESCAPE:
				return previous;
			}
			displayCursor(xCoord,yCoord,true);
		}
	}
	
	/** 
	 * Description: Method to write the strings on the screen 
	 */
	private void drawMenu(){
		lcd.clear();
		lcd.drawString("Calibration Menu", 0, 0);
		lcd.drawString("-----------------",0 ,1);
		lcd.drawString(" 1. Read ON color", 0, 2);
		lcd.drawString(" 2. Read OFF color", 0, 3);
		lcd.drawString(" 3. Read pixel size", 0, 4);
	}
	
	/** 
	 * Description: Method to read the on color on the board.
	 */
	private void readOnColor(){
		lcd.clear();
		lcd.drawString("Place the sensor", 0, 1);
		lcd.drawString("within 2 seconds", 0, 2);
		Delay.msDelay(2000);
		lcd.clear();
		lcd.drawString("Scanning ON color", 0, 1);
		boardAttr.setOnColor(robot.getFloorColorValue());
		lcd.drawString("ON color saved as:", 0, 2);
		lcd.drawString(""+boardAttr.getOnColor(), 0, 3);
		Delay.msDelay(5000);
		drawMenu();		
	}
	
	/** 
	 * Description: Method to read the on color on the board.
	 */
	private void readOffColor(){
		lcd.clear();
		lcd.drawString("Place the sensor", 0, 1);
		lcd.drawString("within 2 seconds", 0, 2);
		Delay.msDelay(2000);
		lcd.clear();
		lcd.drawString("Scanning OFF color", 0, 1);
		boardAttr.setOffColor(robot.getFloorColorValue());;
		lcd.drawString("OFF color saved as:", 0, 2);
		lcd.drawString(""+boardAttr.getOffColor(), 0, 3);
		Delay.msDelay(5000);
		drawMenu();	
	}
	
	/**
	 * Description: Method used to read the pixel size.
	 */
	private void readPixelSize(){
		lcd.clear();
		lcd.drawString("Place the sensor", 0, 1);
		lcd.drawString("within 2 seconds", 0, 2);
		Delay.msDelay(2000);
		lcd.clear();
		lcd.drawString("Scanning the pixelSize", 0, 1);
		DifferentialPilot pilot = robot.getPilot();
		boardAttr.setMargin();
		float margin = boardAttr.getMargin();
		pilot.forward();
		double time1 =0.00;
		double time2 = 0.00;
		boolean onColorRead = false;
		while(true){
			float colorRead = robot.getFloorColorValue();	
			lcd.clear(); 
			System.out.println();
			//if the onColor is detected for the first time, take a time stamp
			if(!onColorRead && (colorRead>margin)){
				onColorRead = true;
				time1 = System.currentTimeMillis()/1000.00;
			}	
			//if on color is already read and the selected color is off color, it mean the pixel scannning has completed. So, take a time stamp
			else if(onColorRead && (colorRead<margin)){
				time2 = System.currentTimeMillis()/1000.00;
				break;
			}
		}
		pilot.stop();
		lcd.clear();
		//calculate the pixel size by multiplying the speed with the amount of time robot took to scan the pixel
		double pixelSize = robot.getRobotSpeed()*(time2-time1);	
		boardAttr.setPixelSize(pixelSize);
		lcd.drawString("Pixel Size read as:", 0, 3);
		lcd.drawString(""+pixelSize, 0, 4);
		Delay.msDelay(5000);
		drawMenu();
	}

}

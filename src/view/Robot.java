package view;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

public class Robot {
	private Port sensorPort;
	private EV3ColorSensor colorSensor;
	private SampleProvider sampleProvider;
	private TextLCD lcd;
	private DifferentialPilot pilot;
	private double robotSpeed;
	private boolean calibrated;
	
	/**
	 * Description: Constructor for the Robot class. Initializes the pilot and the sets the travel and rotate speed.
	 */
	public Robot(){
		pilot = new DifferentialPilot(5.5f,9.5f,Motor.C,Motor.B,false);
		robotSpeed = 4;
		calibrated = false;
		pilot.setTravelSpeed(robotSpeed);
		pilot.setRotateSpeed(10);
	}
	
	/**
	 * Description: Method to get the speed of the Robot.
	 * @return speed of robot
	 */
	public double getRobotSpeed(){
		return robotSpeed;
	}
	
	/**
	 * Description: method to initialize the color sensor
	 */
	public void initColorSensor(){
		//fetching the EV3 Color sensor and initializing it
		sensorPort = LocalEV3.get().getPort("S1");   
		colorSensor = new EV3ColorSensor(sensorPort);
		//initializing the color sample provider with Red Mode.
		sampleProvider = colorSensor.getRedMode(); 
		calibrated = true;
	}
	
	
	/**
	 * Description: Method to check if the calibration has been conducted yet or not. To prevent from null pointer eexcetion while going to scan menu
	 * @return boolean value based on whether calibrated or not
	 */
	public boolean isCalibrated(){
		return calibrated;
	}
	
	/**
	 * Description: Method to initialize the LCD display of the robot
	 */
	public void initLCDDisplay(){
		lcd = LocalEV3.get().getTextLCD();
		lcd.clear();
	}
	
	/**
	 * Description: Method to get the LCD display.
	 * @return LCD display
	 */
	public TextLCD getLCD(){
		return lcd;
	}
	
	/**
	 * Description: Method to move the pilot by a certain distance.
	 * @param travelDistance: distance in centimetres
	 */
	public void travelPilot(double travelDistance){
		pilot.travel(travelDistance);
	}
	
	/**
	 * Description: Method to stop the robot.
	 */
	public void stopRobot(){
		pilot.stop();
	}
	
	/**
	 * Description: Method to get the pilot of the robot.
	 * @return Differential Pilot for the robot
	 */
	public DifferentialPilot getPilot(){
		return pilot;
	}
	
	/**
	 * Description: Method used to get the current color scanned by the light sensor.
	 * @return float value of the color scanned
	 */
	public float getFloorColorValue(){
		int sampleSize = sampleProvider.sampleSize();   
		float[] sample = new float[sampleSize];
		sampleProvider.fetchSample(sample, 0);
		return sample[0];
	}
}
	
	
	
	

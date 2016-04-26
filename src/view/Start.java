package view;

import java.util.Collection;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.utility.Delay;
import lejos.utility.TextMenu;
import model.ClusterLimits;
import model.Dictionary;
import model.FullPattern;

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
	private DifferentialPilot pilot;

	
	/**
	 * Description: Constructor for the Start menu
	 * @param previous: The menu it needs to return after exiting
	 */
	public Start(Menu previous) {			
			super(previous);		
			robot = new Robot();
			robot.initLCDDisplay();	
			robot.initColorSensor();
			Delay.msDelay(5000);
			this.lcd = robot.getLCD();
			
			boardAttr = new BoardAttributes();
			
			dictionary = new Dictionary();
			pilot = robot.getPilot();
			
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
		long time =  (long) ((1.693/7)*1000);
		boolean rotateRight = true;
		for(int i =0;i<6;i++){
			pilot.forward();
			for(int j=0;j<6;j++){
				Delay.msDelay(time);
				int col = j;
				if(i%2==1) col = 5-j;
				dictionary.addPixelToTable(i, col, robot.getFloorColorValue());
				System.out.println(i+" "+col+" "+robot.getFloorColorValue());
			}
			
			pilot.stop();
			if(i!=5){
				rotateRobot(1.693, rotateRight);
				rotateRight = !rotateRight;
				Delay.msDelay(5000);
			}
		}
		LCD.clear();
		LCD.drawString("Board succesfully scanned!", 0, 0);
		Delay.msDelay(2000);	
		
		while(true){
			runClustering();
			LCD.drawString("Run clustering again?", 0, 0);
			Delay.msDelay(300);		
			while (!Button.ENTER.isDown()) {
				if(Button.ESCAPE.isDown()){
					return null;
				}
			}			
		}
	}
	
	private void runClustering(){
		String[] kValues = {"6","7","8"};
		int index = -1;
		Delay.msDelay(300);	
		for(int i=0; i<LCD.SCREEN_HEIGHT;i++){
			System.out.println("                                           ");
		}
		while (!Button.ENTER.isDown()) {
			TextMenu menu = new TextMenu(kValues, 1, "Pick a value for k");
			index = menu.select();
			if(Button.ESCAPE.isDown()){
				return;
			}				
		}
		LCD.clear();
		for(int i=0; i<LCD.SCREEN_HEIGHT;i++){
			System.out.println("                                           ");
		}
		
		Collection<ClusterLimits> colorLimits= dictionary.invokeClusteringOnColor(Integer.parseInt(kValues[index]));
		//print all the clusters;
		System.out.println("Number of clusters " + colorLimits.size());
		for (ClusterLimits c : colorLimits) {			
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: " + (int) c.getTopLeft().getRow() + " " + (int) c.getTopLeft().getCol());
			System.out.println(
					"Bottom Right: " + (int) c.getBottomRight().getRow() + " " + (int) c.getBottomRight().getCol());
			pauseLCD();
		}
		System.out.println("Iterations: "+dictionary.getNoOfIterations());
		pauseLCD();
		LCD.clear();
		Collection<ClusterLimits> distanceLimits = dictionary.invokeClusteringOnDistance(2);
		//print all the clusters
		System.out.println("Number of clusters " + distanceLimits.size());
		for (ClusterLimits c : distanceLimits) {			
			System.out.println(c.getColorVlaue());
			System.out.println("Top Left: " + (int) c.getTopLeft().getRow() + " " + (int) c.getTopLeft().getCol());
			System.out.println(
					"Bottom Right: " + (int) c.getBottomRight().getRow() + " " + (int) c.getBottomRight().getCol());
			pauseLCD();
		}
		System.out.println("Iterations: "+dictionary.getNoOfIterations());
		pauseLCD();
		LCD.clear();
		Collection<FullPattern> fullPatterns = dictionary.getPatterns();
		//print all the full patterns
		for (FullPattern f : fullPatterns) {
			System.out.print(f.getColorValue() + " ");
			System.out.println(f.getLetter());
		}
		pauseLCD();
		LCD.clear();
	}
	
	private void pauseLCD(){
		Delay.msDelay(300);		
		while (!Button.ENTER.isDown()) {	
		}
	}
	
	/** 
	 * Description: Method to rotate the robot after scanning each row.
	 * @param travelDistance : the pixelSize i.e. the distance robot needs to travel every single time
	 */
	private void rotateRobot(double travelDistance, boolean rotateRight) {
		//bring back the robot using the distance it has traveled
		int rotateAngle;
		if(rotateRight) rotateAngle = -90;
		else rotateAngle = 90;
		robot.travelPilot(3*travelDistance+1);
		//rotate right
		robot.rotatePilot(rotateAngle);
		//travel 1 pixel
		robot.travelPilot(travelDistance);
		//rotate left
		robot.rotatePilot(rotateAngle);
	}
	
}

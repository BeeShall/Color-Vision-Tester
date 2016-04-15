package model;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Class that denotes each rule. Hold a 8X6 boolean array to denote each pixel in the board and a name for the rule. 
 */

public class Letter {
	private boolean[][] gridCoords = new boolean[8][6];
	private String name;
	
	public Letter(){
		name = "";
	}
	
	/**
	 * Method to check if a certain pixel is set in this rule
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 * @return boolean value denoting whether the pixel is set or not
	 */
	public boolean isPixelSetAt(int x, int y){
		return gridCoords[x][y];
	}
	
	/**
	 * Method to set a certain pixel in the rule
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 */
	public void setPixelAt(int x, int y){
		gridCoords[x][y] = true;
	}
	
	/**
	 * Method to set the pixel to false at a specific coordinate
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 */
	public void clearPixelAt(int x, int y){
		gridCoords[x][y]=false;
	}
	
	/**
	 * Method to set the name for the rule
	 * @param name: name for the rule
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Getter class for the name of the string
	 * @return name of the string
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Method to copy into current letter from another letter
	 * @param l: letter to copy from
	 */
	public void copyFrom(Letter l){
		for(int i=0;i<gridCoords.length;i++){
			for(int j=0; j<gridCoords[i].length;j++){
				//if the pixel is On on the othe letter, set it on on current letter.
				if(l.isPixelSetAt(i, j)){
					gridCoords[i][j] = true;
				}
			}
		}		
	}
	
	/**
	 * Method to compare the current letter with the given letter
	 * @param l: letter to compare with
	 * @return boolean value based on where the comparison is true or false.
	 */
	public boolean compareLetter(Letter l){
		for(int i=0;i<gridCoords.length;i++){
			for(int j=0; j<gridCoords[i].length;j++){
				if(l.isPixelSetAt(i, j) != this.isPixelSetAt(i, j)){
					return false;
				}
			}
		}
		return true;
	}
	

}

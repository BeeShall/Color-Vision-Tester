package model;

import java.util.ArrayList;
import java.util.List;


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
	public List<String> pixels;
	public List<Letter> intermediates;
	private String name;
	private int dimRow;
	private int dimCol;
	
	public Letter(){
		pixels = new ArrayList<String>();
		intermediates = new ArrayList<Letter>();
		name = "";
	}
	
	/**
	 * Method to check if a certain pixel is set in this rule
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 * @return boolean value denoting whether the pixel is set or not
	 */
	public boolean isPixelSetAt(int x, int y){
		return checkIntermediate(x+" "+y, this);
	}
	
	private boolean checkIntermediate(String coords, Letter l){
		//System.out.println(this.name);
		if(pixels.contains(coords)) return true;
		else{
			if(pixels.isEmpty()) return false;
			else{
				for(Letter letter: intermediates){
					return letter.isPixelSetAt(coords);
				}
			}
		}
		return false;
	}
	
	private boolean isPixelSetAt(String coords){
		return checkIntermediate(coords, this);
	}
	public void addIntermediate(Letter l){
		intermediates.add(l);
		if(l.getDimRow()>this.dimRow) this.dimRow = l.getDimRow();
		if(l.getDimCol()>this.dimCol) this.dimCol = l.getDimCol();
	}
	
	
	public int getDimRow() {
		return dimRow;
	}

	public void setDimRow(int dimRow) {
		this.dimRow = dimRow;
	}

	public int getDimCol() {
		return dimCol;
	}

	public void setDimCol(int dimCol) {
		this.dimCol = dimCol;
	}

	/**
	 * Method to set a certain pixel in the rule
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 */
	public void setPixelAt(int x, int y){
		if(x>dimRow) dimRow = x;
		if(y>dimCol) dimCol = y; 
		pixels.add(x+" "+y);
	}
	
	public void setPixelAt(String coords){
		pixels.add(coords);
	}
	
	/**
	 * Method to set the pixel to false at a specific coordinate
	 * @param x: x coordinate of pixel
	 * @param y: y coordinate of pixel
	 */
	public void clearPixelAt(int x, int y){
		pixels.remove(pixels.indexOf(x+" "+y));
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
	 * Method to compare the current letter with the given letter
	 * @param l: letter to compare with
	 * @return boolean value based on where the comparison is true or false.
	 */
	public boolean compareLetter(Letter l){
		for(String pixel: pixels){			
			if(!l.isPixelSetAt(pixel)) return false;
		}
		return true;
	}
	

}

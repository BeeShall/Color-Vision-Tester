package model;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 5 - Color Vision Tester                *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/27/2016                                          *
************************************************************/

/**
 * @author Bishal
 *Class to hold the full pattern detected after clustering
 */
public class FullPattern {
	//color value the letter was detectedd from
	private float colorValue;
	//String value of the pattern
	private String letter;
	public FullPattern(float colorValue, String letter) {
		super();
		this.colorValue = colorValue;
		this.letter = letter;
	}
	public float getColorValue() {
		return colorValue;
	}
	public void setColorValue(float colorValue) {
		this.colorValue = colorValue;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}

}

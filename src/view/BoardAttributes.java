package view;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 4 - Scanner                            *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/9/2016                                          *
************************************************************/

/**
 * @author Bishal
 * Class that hold the attributes of the board.
 */

public class BoardAttributes {
	private float onColor;
	private float offColor;
	private double pixelSize;
	private static int row = 8;
	private static int col = 6;
	private float margin = 0;
	
	
	public float getOnColor() {
		return onColor;
	}
	
	public void setOnColor(float onColor) {
		this.onColor = onColor;		
	}
	
	
	/**
	 * Description: set the margin as the midpoint between on color and off color
	 */
	public void setMargin() {
		margin = (float) ((onColor+offColor)/2.0);		
	}
	
	public float getMargin() {
		return margin;
	}	
	
	public float getOffColor() {
		return offColor;
	}
	
	public void setOffColor(float offColor) {
		this.offColor = offColor;
	}
	
	public double getPixelSize() {
		return pixelSize;
	}
	
	public void setPixelSize(double d) {
		this.pixelSize = d;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
}

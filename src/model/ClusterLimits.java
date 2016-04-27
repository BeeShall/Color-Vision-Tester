package model;

/************************************************************
* Name:  Bishal Regmi                                      *
* Project:  Project 5 - Color Vision Tester                *
* Class:  CMPS 331 - Artificial Intelligence               *
* Date:  4/27/2016                                          *
************************************************************/

/**
 * @author Bishal
 *Class to hold the bounding box represented by a cluster
 */
public class ClusterLimits {
	//top left pixel of the cluster
	Pixel topLeft;
	//bottom right pixel of the cluster
	Pixel bottomRight;
	//color value represented by the cluster
	float colorVlaue;
	String letter;
	public float getColorVlaue() {
		return colorVlaue;
	}
	public void setColorVlaue(float colorVlaue) {
		this.colorVlaue = colorVlaue;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public ClusterLimits(Pixel topLeft, Pixel bottomRight) {
		super();
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	public Pixel getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Pixel topLeft) {
		this.topLeft = topLeft;
	}
	public Pixel getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Pixel bottomRight) {
		this.bottomRight = bottomRight;
	}
	
	

}

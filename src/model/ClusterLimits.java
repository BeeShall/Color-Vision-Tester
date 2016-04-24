package model;

public class ClusterLimits {
	Pixel topLeft;
	Pixel bottomRight;
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

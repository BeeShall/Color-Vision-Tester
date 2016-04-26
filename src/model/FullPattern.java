package model;

public class FullPattern {
	private float colorValue;
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

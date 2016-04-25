package model;

public class Pixel {
	private float row;
	private float col;	
	private float colorValue;
	
	public Pixel(float row, float col){
		super();
		this.row = row;
		this.col = col;
		this.colorValue = -1;
	}

	public Pixel(float row, float col, float colorValue) {
		super();
		this.row = row;
		this.col = col;
		this.colorValue = colorValue;
	}
	public float getRow() {
		return row;
	}
	public void setRow(float row) {
		this.row = row;
	}
	public float getCol() {
		return col;
	}
	public void setCol(float col) {
		this.col = col;
	}
	public float getColorValue() {
		return colorValue;
	}
	public void setColorValue(float colorValue) {
		this.colorValue = colorValue;
	}
	public void adjustRow(float row){
		this.row+=row;
	}
	public void adjustCol(float col){
		this.col+=col;
	}

}

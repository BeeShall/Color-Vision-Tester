package model;

public class Pixel {
	private int row;
	private int col;	
	private float colorValue;

	public Pixel(int row, int col, float colorValue) {
		super();
		this.row = row;
		this.col = col;
		this.colorValue = colorValue;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public float getColorValue() {
		return colorValue;
	}
	public void setColorValue(float colorValue) {
		this.colorValue = colorValue;
	}
	public void adjustRow(int row){
		this.row+=row;
	}
	public void adjustCol(int col){
		this.col+=col;
	}

}
